package com.subocol.manage.purchase.domain.servicesimpl;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.enums.CauseManualPurchase;
import com.subocol.manage.purchase.domain.models.Insurer;
import com.subocol.manage.purchase.domain.models.ManualPurchase;
import com.subocol.manage.purchase.domain.models.ManualPurchaseAdi;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.ports.persistence.ManualPurchaseAdiRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.ManualPurchaseRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.ProductQuotationRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.SuggestedReferenceRepositoryPort;
import com.subocol.manage.purchase.domain.services.SendManualPurchase;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterProductQuotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.*;

@Slf4j
@Service
public class SendManualPurchaseImpl implements SendManualPurchase {


    private final ProductQuotationRepositoryPort productQuotationRepositoryPort;
    private final ManualPurchaseAdiRepositoryPort manualPurchaseAdiRepositoryPort;
    private final SuggestedReferenceRepositoryPort suggestedReferenceRepositoryPort;
    private final ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;

    public SendManualPurchaseImpl(ProductQuotationRepositoryPort productQuotationRepositoryPort,
                                  ManualPurchaseAdiRepositoryPort manualPurchaseAdiRepositoryPort, ManualPurchaseRepositoryPort manualPurchaseRepositoryPort,
                                  SuggestedReferenceRepositoryPort suggestedReferenceRepositoryPort) {

        this.productQuotationRepositoryPort = productQuotationRepositoryPort;
        this.manualPurchaseAdiRepositoryPort = manualPurchaseAdiRepositoryPort;
        this.manualPurchaseRepositoryPort = manualPurchaseRepositoryPort;
        this.suggestedReferenceRepositoryPort = suggestedReferenceRepositoryPort;
    }


    @Override
    public void setManualPurchasePieces(Notice notice, Insurer insurer) throws ExceptionUtil {
        try {
            log.info("Start setManualPurchasePieces for noticeId: " + notice.getId());
            List<ManualPurchaseAdi> manualPurchaseAdiList = findManualPurchaseADIToManualProcess(notice.isAuth(), notice.getExternalEvent().longValue(), notice.getEventId(), notice.getId(), insurer.getTotManual());
            List<CounterProductQuotation> listDataProduct = findCounterProductQuotationToManualProcess(notice.getId(), manualPurchaseAdiList);
            List<ManualPurchase> mpSaved = new ArrayList<>();

            for (ManualPurchaseAdi productInAdiSchema : manualPurchaseAdiList) {

                Optional<CounterProductQuotation> dataProductOptional = listDataProduct.stream().filter(data -> data.getPosition().intValue() == productInAdiSchema.getPosition()).findFirst();

                dataProductOptional.ifPresent(counterProductQuotation -> {

                    ManualPurchase manualPurchase = settingManualPurchase(productInAdiSchema, counterProductQuotation, notice, insurer);

                    if (manualPurchase != null) {
                        mpSaved.add(manualPurchaseRepositoryPort.save(manualPurchase));
                    }
                });

            }

            log.info(mpSaved.stream()
                    .map(mp -> "Saved manual purchase with id: " + mp.getId() + " description: " + mp.getDescription() + " cause: " + mp.getCause())
                    .collect(Collectors.joining("; ")));

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ErrorMessageHandler.concatenateStringAndObject(ERROR_SET_MANUAL_PURCHASE, notice.getId()), e.getMessage());
        }

    }


    @Override
    public List<ManualPurchaseAdi> findManualPurchaseADIToManualProcess(boolean auth, Long externalEvent, Long eventId, Long noticeId, boolean totParameter) {
        try {
            List<ManualPurchaseAdi> listProductsInAdiSchema = null;

            if (auth) {
                listProductsInAdiSchema = manualPurchaseAdiRepositoryPort.findManualPurchaseAdiByEventFilterManualPurchaseForAuth(
                        externalEvent, eventId, noticeId, totParameter);

            } else {
                listProductsInAdiSchema = manualPurchaseAdiRepositoryPort.findManualPurchaseAdiByEventFilterManualPurchase(
                        externalEvent, eventId, totParameter);
            }
            return listProductsInAdiSchema;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ErrorMessageHandler.concatenateStringAndObject(ERROR_FIND_MANUAL_PURCHASE_ADI, noticeId), e.getMessage());
        }
    }

    @Override
    public List<CounterProductQuotation> findCounterProductQuotationToManualProcess(Long noticeId, List<ManualPurchaseAdi> listProductsInAdiSchema) {
        try {
            return productQuotationRepositoryPort.findCounterStatusProductQuotationDTOForSubmissionManualPurchase(noticeId,
                    listProductsInAdiSchema.stream().map(ManualPurchaseAdi::getPosition).toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ErrorMessageHandler.concatenateStringAndObject(ERROR_COUNTERS_MANUAL_PURCHASE_PROCESS, noticeId), e.getMessage());
        }

    }

    @Override
    public ManualPurchase settingManualPurchase(ManualPurchaseAdi productInAdiSchema, CounterProductQuotation dataProduct, Notice notice, Insurer insurer) {
        try {
            boolean isManualPurchaseProduct = true;
            boolean isManualPurchaseProductOmitted = true;

            if (!notice.isAuth()) {
                // verifica si la ganadora tiene alertamiento
                isManualPurchaseProduct = dataProduct.getAlertAndWinnerProducts() >= 1;
                // verifica si la ganadora ha sido omitida
                isManualPurchaseProductOmitted = (dataProduct.getOmittedProducts()
                        + dataProduct.getRejectedQuotedProducts()) == dataProduct.getTotalProducts();
            }

            if (isManualPurchaseProduct || isManualPurchaseProductOmitted) {

                ManualPurchase manualPiece = new ManualPurchase();

                if (dataProduct.getMaxCostPiece() > 0)
                    manualPiece.setCause(CauseManualPurchase.MAX_COST_PIECE.toString());
                else if (dataProduct.getExtraCost() > 0)
                    manualPiece.setCause(CauseManualPurchase.EXTRA_COST.toString());
                else if (dataProduct.getOverTime() > 0)
                    manualPiece.setCause(CauseManualPurchase.MAX_DELIVERY_DAYS.toString());
                else if (dataProduct.getRejectedQuotedProducts() >= dataProduct.getOmittedProducts())
                    manualPiece.setCause(CauseManualPurchase.REJECTED_QUOTED.toString());
                else
                    manualPiece.setCause(CauseManualPurchase.OMITTED.toString());

                if ((manualPiece.getCause().equalsIgnoreCase(CauseManualPurchase.MAX_COST_PIECE.toString()) ||
                        manualPiece.getCause().equalsIgnoreCase(CauseManualPurchase.EXTRA_COST.toString()) ||
                        manualPiece.getCause().equalsIgnoreCase(CauseManualPurchase.MAX_DELIVERY_DAYS.toString())) && !notice.isAuth()) {
                    return null;
                }

                if (Boolean.TRUE.equals(insurer.getNewSuggestedReferenceParameter())) {

                    List<String> sugesstedReferenceForProduct = suggestedReferenceRepositoryPort
                            .findListSuggestedReferenceByEventAndPosition(notice.getEventId(),
                                    productInAdiSchema.getPosition(), insurer.getCantRefToShow());
                    Collections.reverse(sugesstedReferenceForProduct);
                    if (!sugesstedReferenceForProduct.isEmpty() && !(sugesstedReferenceForProduct.size() == 1
                            && sugesstedReferenceForProduct.contains(""))) {
                        manualPiece.setSuggestedReference(sugesstedReferenceForProduct.get(0));
                    } else {
                        manualPiece.setSuggestedReference("");
                    }
                } else {
                    manualPiece.setSuggestedReference("");
                }


                manualPiece.setBrand(notice.getBrand()).setLine(notice.getLine()).setPlate(notice.getPlate())
                        .setAuth(dataProduct.getAuth())
                        .setExternalEvent(notice.getExternalEvent().toString())
                        .setDescription(productInAdiSchema.getPieces())
                        .setQuantity(productInAdiSchema.getQuantity())
                        .setDate(TimeZoneUtil.getTimestampByDefaultZone())
                        .setPosition(productInAdiSchema.getPosition()).setEventId(productInAdiSchema.getEventId());

                return manualPiece;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ErrorMessageHandler.concatenateStringAndObject(ERROR_SAVE_MANUAL_PURCHASE, notice.getId()), e.getMessage());

        }
    }

}



