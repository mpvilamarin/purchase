package com.subocol.manage.purchase.domain.servicesimpl;

import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.ThrowingConsumer;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.services.CreateOrder;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.AuthNoticePiecesDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeClaimNumberDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesQuoteAuthDTO;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.Integrations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.subocol.manage.purchase.domain.constant.ManageNoticeConstant.TYPE_MANUAL;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthNoticeWithPieces {

    private final NoticeRepositoryPort noticeRepositoryPort;

    private final ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;

    private final PieceRepositoryPort pieceRepositoryPort;

    private final ProductQuotationRepositoryPort productQuotationRepositoryPort;

    private final DataEventRepositoryPort dataEventRepositoryPort;

    private final InsurerRepositoryPort insurerRepositoryPort;

    private final QuotationRepositoryPort quotationRepositoryPort;

    private final ManageNoticeCC manageNoticeCC;

    private final ManageNoticeMM manageNoticeMM;

    private final CreateOrder createOrder;

    private final Integrations integrations;

    private final OrderRepositoryPort orderRepositoryPort;

    private final ProductOrderRepositoryPort productOrderRepositoryPort;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO auth(AuthNoticePiecesDTO authNoticePieces) {

        ResponseDTO response = new ResponseDTO();
        HashMap<String, Object> dataResponse = new HashMap<>();

        String externalEvent = authNoticePieces.getExternalEvent();
        String claimNumber = authNoticePieces.getClaimNumber();

        try {

            List<Notice> notices = noticeRepositoryPort.findAllByExternalEventAndAuth(externalEvent, true);

            if (!notices.isEmpty())
                throw new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.ERROR_PREVIOUSLY_AUTHORIZED_NOTICE);

            List<Integer> positions = authNoticePieces.getSpareParts()
                    .stream()
                    .map(PiecesQuoteAuthDTO::getPosition)
                    .filter(position -> position > 0)
                    .toList();

            if (positions.isEmpty())
                throw new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.ERROR_EMPTY_SPARE_PARTS);

            if (positions.size() == manualPurchaseRepositoryPort.countDeletedPiecesByPositions(externalEvent, positions))
                throw new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.ERROR_SHIPPED_PARTS_DELETED);

            if (positions.size() != pieceRepositoryPort.countPiecesByExternalEventAndPositions(externalEvent, positions))
                throw new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.ERROR_AUTHORIZING_NOTICE_PIECE_NOT_FOUND);

            productQuotationRepositoryPort.updateAllAuthByIdInAndEventId(true, positions, externalEvent);
            manualPurchaseRepositoryPort.updateAuthByExternalEventAndPosition(true, externalEvent, positions);

            updateClaimNumber(externalEvent, claimNumber);

            noticeRepositoryPort.updateAuthByExternalEvent(externalEvent, true);
            List<Notice> noticeList = noticeRepositoryPort.findAllByExternalEvent(externalEvent);

            Insurer insurer = insurerRepositoryPort.findByInsurerId(noticeList.get(0).getInsuranceNumber())
                    .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.INSURER_NOT_FOUND));

            for (Notice notice : noticeList) {
                notice.setAuth(true);
                HashMap<String, String> dataResponseNotice = new HashMap<>();

                if (notice.getWorkshopType().equalsIgnoreCase(ManageNoticeConstant.SELF_SUPPLY)) {

                    Quotation quotation = quotationRepositoryPort.findQuotationByNoticeIdAndFlowType(notice.getId()).orElse(null);

                    if (quotation != null) {
                        List<Long> listIds = productQuotationRepositoryPort.findIdsByQuotationIdAndAuthTrue(quotation.getId());
                        manageNoticeCC.manageNoticeCC(notice.getId(), quotation.getId(), listIds, false);
                    }

                    List<ProductQuotation> lsProductQuotationManual = productQuotationRepositoryPort.findAllWinnersByNotice(notice.getId());
                    if (!lsProductQuotationManual.isEmpty()) {
                        createOrder.createOrderQuotation(notice, lsProductQuotationManual, TYPE_MANUAL);
                        List<Integer> positionsWinner = new ArrayList<>();
                        lsProductQuotationManual.forEach(productQuotation -> positionsWinner.add(productQuotation.getPosition()));
                        productQuotationRepositoryPort.updatePurchaseSubsidiary(notice.getExternalEvent().toString(), positionsWinner, true);
                        manualPurchaseRepositoryPort.updatePurchaseSubsidiary(notice.getExternalEvent().toString(), positionsWinner, true);

                    }
                } else if (TimeZoneUtil.getTimestampByDefaultZone().getTime() > notice.getQuotationEstimatedDate().getTime()) {
                    manageNoticeMM.manageNoticeByNoticeId(notice.getId(), Boolean.FALSE);
                }

                response.setMessage("Se autorizo aviso " + notice.getExternalEvent() + " NoticeId " + notice.getId());
                response.setStatus(200);
                response.setSuccess(true);
                dataResponseNotice.put("status", "200");
                dataResponseNotice.put("message", "Se autorizo aviso " + notice.getExternalEvent() + " NoticeId " + notice.getId());
                log.info(response.getMessage());
                dataResponse.put(notice.getId().toString(), dataResponseNotice);

            }

            dataEventRepositoryPort.updateAuthByExternalEvent(Integer.parseInt(externalEvent));
            List<Long> quotationIds = quotationRepositoryPort.findQuotationManagedByExternalEvent(externalEvent);
            quotationRepositoryPort.updateQuotationManaged(quotationIds);

            noticeList.forEach(ThrowingConsumer.wrapExceptions(notice -> {
                        List<Order> orders = orderRepositoryPort.findAllByNoticeId(notice.getId());

                        for (Order orderProduct : orders) {
                            List<ProductOrder> productOrders = productOrderRepositoryPort.findAllByIdOrder(orderProduct.getId());
                            orderProduct.setProducts(new HashSet<>(productOrders));
                        }

                        integrations.integrationByNotice(notice, insurer, new HashSet<>(orders));
                    }
            ));

            response.setData(dataResponse);

            return response;

        } catch (ExceptionUtil e) {
            e.printStackTrace();
            throw new ExceptionUtil(e.getStatusCode(), e.getMessage(), e.getMessage());
        }
    }

    public boolean updateClaimNumber(String externalEvent, String claimNumber) {
        try {
            NoticeClaimNumberDTO result = noticeRepositoryPort.findDistinctClaimNumberByExternalEvent(externalEvent, claimNumber);
            if (result != null && !(result.getCountPackages().equals(result.getCountClaimEquals()))) {
                dataEventRepositoryPort.updateClaimNumberByExternalEvent(externalEvent, claimNumber);
                noticeRepositoryPort.updateClaimNumberByExternalEvent(externalEvent, claimNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
