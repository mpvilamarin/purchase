package com.subocol.manage.purchase.domain.servicesimpl.integrations;

import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.ports.externalservices.AlertPiecesValuationPort;
import com.subocol.manage.purchase.domain.ports.persistence.OptionQuotePort;
import com.subocol.manage.purchase.domain.ports.persistence.ProductQuotationRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeValuationDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertPiecesValuationImpl {

    private final ProductQuotationRepositoryPort productQuotationRepositoryPort;

    private final OptionQuotePort optionQuotePort;

    private final AlertPiecesValuationPort alertPiecesValuationPort;

    public Boolean alertPiecesValuation(Integer externalEvent, boolean auth, String workshopType) {
        List<PiecesValuationDTO> pieces = new ArrayList<>();
        List<Long> lsIdsProductQuotation = new ArrayList<>();
        List<Long> lsIdsProductPurchase = new ArrayList<>();

        try {
            NoticeValuationDTO noticeQuote = new NoticeValuationDTO();
            noticeQuote.setNumeroAviso(externalEvent);
            if (workshopType.contentEquals(ManageNoticeConstant.SELF_SUPPLY)) {
                if (auth) {
                    pieces = productQuotationRepositoryPort.findPiecesValuationConcessionaireAccepted(externalEvent);
                }
                pieces.addAll(productQuotationRepositoryPort.findPiecesValuationConcessionaireQuoted(externalEvent));
            } else {
                if (auth) {
                    pieces.addAll(optionQuotePort.findPiecesValuationMultibrandAccepted(externalEvent));
                }
                pieces.addAll(optionQuotePort.findPiecesValuationMultibrandQuoted(externalEvent));
                pieces.addAll(productQuotationRepositoryPort.findPiecesValuationMultibrandBought(externalEvent));

            }
            if (pieces.isEmpty())
                return false;

            pieces.forEach(piece -> {
                lsIdsProductQuotation.add(piece.getId());
                if (piece.getComprada().equals("S")) {
                    lsIdsProductPurchase.add(piece.getId());
                }
            });

            productQuotationRepositoryPort.updateSendValuationQuotation(lsIdsProductQuotation, true);

            if (!lsIdsProductPurchase.isEmpty()) {
                productQuotationRepositoryPort.updateSendValuationPurchase(lsIdsProductPurchase, true);
            }
            noticeQuote.setPiezas(pieces);

            alertPiecesValuationPort.serviceAlertPiecesValuation(noticeQuote);

        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }

        return true;
    }
}
