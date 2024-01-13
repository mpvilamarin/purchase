package com.subocol.manage.purchase.domain.servicesimpl;

import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.externalservices.LocationExternalServicesPort;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.DesistDTO;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.FollowUp;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.ReserveBolivar;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.ReserveSura;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.*;
import static com.subocol.manage.purchase.domain.constant.ManageNoticeConstant.COLOMBIA;
import static com.subocol.manage.purchase.domain.models.enums.ProductOrderStatus.DESIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class DesistProduct {

    private final ProductOrderRepositoryPort productOrderRepositoryPort;
    private final ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;
    private final SellOrderRepositoryPort sellOrderRepositoryPort;
    private final SellOrderDetailRepositoryPort sellOrderDetailRepositoryPort;
    private final LocationExternalServicesPort locationExternalServicesPort;
    private final TaxRepositoryPort taxRepositoryPort;
    private final DesistRepositoryPort desistRepositoryPort;
    private final StatusPartsRepositoryPort statusPartsRepositoryPort;
    private final ProductQuotationRepositoryPort productQuotationRepositoryPort;
    private final OrderRepositoryPort orderRepositoryPort;
    private final ReserveBolivar reserveBolivar;
    private final ReserveSura reserveSura;
    private final FollowUp followUp;
    private final InsurerRepositoryPort insurerRepositoryPort;

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO desistProduct(DesistDTO desistDTO) throws ExceptionUtil {
        try {
            ResponseDTO response = new ResponseDTO();
            List<Integer> positions = new ArrayList<>();
            List<Long> productOrderIds = new ArrayList<>();

            List<ProductOrder> productOrders = productOrderRepositoryPort.findAllById(desistDTO.getIds());

            Order order = productOrders.get(0).getOrder();
            Notice notice = productOrders.get(0).getOrder().getNotice();

            Timestamp timestamp = TimeZoneUtil.getTimestampByDefaultZone();

            productOrderRepositoryPort.updateDesistProductOrder(desistDTO.getIds(), desistDTO.getUserName(), timestamp);

            productOrders.forEach(product -> {
                positions.add(product.getPositionPiece());
                productOrderIds.add(product.getId());
            });

            createManualPurchaseDesist(productOrders);
            updateSellOrderDetail(order, positions);
            saveDesist(desistDTO, order.getId(), productOrderIds);

            statusPartsRepositoryPort.updateStatusByProductOrderId(DESIST.toString(), productOrderIds);
            productQuotationRepositoryPort.updateStatusByPositionPieceAndProductOrderId(productOrderIds);

            if (!productOrders.isEmpty()) {

                Map<String, Long> counts = productOrderRepositoryPort.getCountsDesistAndTotal(productOrders.get(0).getOrder().getId());
                Long countDesist = counts.get("countDesisted");
                Long countAll = counts.get("totalCount");

                if (countAll != null && countAll.equals(countDesist)) {
                    orderRepositoryPort.updateStatusByOrderId(DESIST.toString(), order.getId());
                }

                Insurer insurer = insurerRepositoryPort.findByInsurerId(notice.getInsuranceNumber())
                        .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.concatenateStringAndObject(INSURER_NOT_FOUND, notice.getInsuranceNumber())));

                if (Boolean.TRUE.equals(insurer.getFlowReserveBolivar()) && insurer.getName().equalsIgnoreCase(ManageNoticeConstant.BOLIVAR)) {
                    reserveBolivar.sendPurchaseTotalReserveDesistBolivar(notice);
                }
                if (Boolean.TRUE.equals(insurer.getFlowReserveSura()) && insurer.getName().equalsIgnoreCase(ManageNoticeConstant.SURA)) {
                    reserveSura.sendPurchaseTotalReserveDesistSura(notice);
                }
                productQuotationRepositoryPort.updatePurchaseSubsidiary(notice.getExternalEvent().toString(), positions, false);
                manualPurchaseRepositoryPort.updatePurchaseSubsidiary(notice.getExternalEvent().toString(), positions, false);

                followUp.sendPurchasesToFollowUpDesist(notice.getExternalEvent().longValue(), desistDTO.getIds());

            }
            response.setMessage("Se desistio correctamente los productos con ids : " + desistDTO.getIds());
            response.setStatus(200);
            response.setSuccess(true);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_DESIST_PRODUCT, e.getMessage());
        }
    }


    public void createManualPurchaseDesist(List<ProductOrder> productOrders) throws ExceptionUtil {
        try {
            Notice notice = productOrders.get(0).getOrder().getNotice();
            for (ProductOrder pieceDesistProductOrders : productOrders) {
                Optional<ManualPurchase> manualPurchaseOptional = manualPurchaseRepositoryPort.findByPositionAndExternalEvent(pieceDesistProductOrders.getPositionPiece(),
                        productOrders.get(0).getOrder().getNotice().getExternalEvent().toString());

                if (manualPurchaseOptional.isEmpty()) {
                    ManualPurchase manualPiece = new ManualPurchase();

                    manualPiece.setBrand(notice.getBrand());
                    manualPiece.setLine(notice.getLine());
                    manualPiece.setPlate(notice.getPlate());
                    manualPiece.setExternalEvent(notice.getExternalEvent().toString());
                    manualPiece.setDescription(pieceDesistProductOrders.getDescription());
                    manualPiece.setQuantity(pieceDesistProductOrders.getAmount());
                    manualPiece.setReference(pieceDesistProductOrders.getReference());
                    manualPiece.setSuggestedReference("");
                    manualPiece.setCause(DESIST.toString().toUpperCase());
                    manualPiece.setEventId(notice.getEventId());
                    manualPiece.setPosition(pieceDesistProductOrders.getPositionPiece());
                    manualPiece.setDate(TimeZoneUtil.getTimestampByDefaultZone());
                    manualPiece.setAuth(true);

                    manualPurchaseRepositoryPort.save(manualPiece);
                } else {
                    ManualPurchase manualPurchase;
                    manualPurchase = manualPurchaseOptional.get();

                    manualPurchaseRepositoryPort.updateDesistCauseAndStatusById(manualPurchase.getId());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_CREATE_MANUAL_PURCHASE_DESIST, e.getMessage());
        }

    }


    public boolean updateSellOrderDetail(Order order, List<Integer> positions) {

        List<Long> sellOrderDetailIds = sellOrderDetailRepositoryPort.findAllByOrderIdAndPosition(order.getId(), positions);

        sellOrderDetailRepositoryPort.deleteAllById(sellOrderDetailIds);

        SellOrder sellOrder = sellOrderRepositoryPort.findByOrderId(order.getId())
                .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.concatenateStringAndObject(SUBSIDIARY_NOT_FOUND, order.getId())));

        Double subtotal = sellOrder.getDetails().stream()
                .filter(detail -> !sellOrderDetailIds.contains(detail.getId()))
                .mapToDouble(SellOrderDetail::getTotal)
                .sum();

        Long countryId = locationExternalServicesPort.findLocation(order.getSubsidiary().getLocationExternalId()).getCountryId();

        if (subtotal != null) {
            Optional<Tax> taxOptional = countryId == null ?
                    taxRepositoryPort.findTaxByCountry((long) COLOMBIA) : taxRepositoryPort.findTaxByCountry(countryId);

            if (taxOptional.isEmpty()) return false;

            double iva = (subtotal * taxOptional.get().getPercentage()) / 100;
            double ivaRound = Math.round(iva * 100.0) / 100.0;
            sellOrder.setIva(ivaRound);

            double total = subtotal + ivaRound;
            sellOrderRepositoryPort.updateSubtotalTotalIva(sellOrder.getId(), subtotal, total, ivaRound);
        }

        return true;
    }

    public void saveDesist(DesistDTO desistDTO, Long idOrder, List<Long> productOrderIds) {

        List<Desist> lsDesist = new ArrayList<>();
        for (Long productOrderId : productOrderIds) {

            Desist desist = new Desist();
            desist.setCausal(desistDTO.getCausal());
            desist.setObservation(desistDTO.getObservation());
            desist.setIdOrder(idOrder);
            desist.setIdProductOrder(productOrderId);

            lsDesist.add(desist);
        }

        desistRepositoryPort.saveAllNative(lsDesist);
    }

}
