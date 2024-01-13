package com.subocol.manage.purchase.domain.servicesimpl;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.services.CreateOrder;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterStatusProductQuotation;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.Integrations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManageNoticeCC {

    private final CreateOrder createOrder;
    private final NoticeRepositoryPort noticeRepositoryPort;
    private final ProductQuotationRepositoryPort productQuotationRepositoryPort;
    private final ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;
    private final InsurerRepositoryPort insurerRepositoryPort;
    private final ProductOverrunCostRepositoryPort productOverrunCostRepositoryPort;
    private final QuotationRepositoryPort quotationRepositoryPort;
    private final Integrations integrations;


    public boolean manageNoticeCC(Long noticeId, Long quotationId, List<Long> listProductQuotationIds, Boolean serviceIntegration) {
        try {
            long startTime = System.currentTimeMillis();
            Notice notice = noticeRepositoryPort.findById(noticeId)
                    .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.concatenateStringAndObject(NOTICE_NOT_FOUND, noticeId)));
            Insurer insurer = insurerRepositoryPort.findByInsurerId(notice.getInsuranceNumber())
                    .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.concatenateStringAndObject(INSURER_NOT_FOUND, notice.getInsuranceNumber())));

            if (notice.isAuth()) {
                List<ProductQuotation> productOverTimeOverCost = new ArrayList<>();
                List<ProductQuotation> productCreateOrder = new ArrayList<>();

                Order order = new Order();
                Set<Order> orders = new HashSet<>();
                if (!listProductQuotationIds.isEmpty()) {
                    productOverTimeOverCost = productQuotationRepositoryPort
                            .findPiecesByIdAndOverTimeOverCost(listProductQuotationIds);
                    productCreateOrder = productQuotationRepositoryPort.findPiecesByIdAndOverTime(listProductQuotationIds);
                }
                if (!productOverTimeOverCost.isEmpty()) {
                    setProductOverruncost(notice, productOverTimeOverCost);
                }

                if (!productCreateOrder.isEmpty()) {

                    Quotation quotation = quotationRepositoryPort.findById(quotationId)
                            .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.concatenateStringAndObject(QUOTATION_NOT_FOUND, quotationId)));

                    order = createOrder.createOrderQuotationConcessionarie(productCreateOrder, notice, quotation);

                    List<Long> listProductCreateOrder = productCreateOrder.stream().map(ProductQuotation::getId)
                            .toList();

                    productQuotationRepositoryPort.updateStatusAndPurchaseById(listProductCreateOrder);

                    productOverrunCostRepositoryPort.updatePurchaseByStatusAndIdIn(listProductCreateOrder);
                    orders.add(order);
                }

                changeStatusQuotationsVoid(quotationId);
                List<Long> quotationIds = quotationRepositoryPort.findQuotationWithAllProductManage(quotationId);
                quotationRepositoryPort.updateQuotationManaged(quotationIds);

                if (order.getProducts() != null && !order.getProducts().isEmpty()) {
                    List<Integer> positions = new ArrayList<>();
                    order.getProducts().forEach(productOrder -> positions.add(productOrder.getPositionPiece()));
                    productQuotationRepositoryPort.updatePurchaseSubsidiary(notice.getExternalEvent().toString(), positions, true);
                    manualPurchaseRepositoryPort.updatePurchaseSubsidiary(notice.getExternalEvent().toString(), positions, true);
                }
                if (Boolean.TRUE.equals(serviceIntegration))
                    integrations.integrationByNotice(notice, insurer, orders);
            }
            log.info("finish manage notice: " + noticeId + " in: " + (System.currentTimeMillis() - startTime) + " ms");
            return true;
        } catch (ExceptionUtil ex) {
            ex.printStackTrace();
            throw new ExceptionUtil(ex.getStatusCode(),
                    ErrorMessageHandler.concatenateStringAndObject(ERROR_MANAGE_NOTICE_CONCESSIONAIRE, noticeId), ex.getMessage());
        }
    }

    public boolean setProductOverruncost(Notice notice, List<ProductQuotation> productQuotation) {
        try {
            for (ProductQuotation pieceQuotedProductQuotation : productQuotation) {
                ProductOverrunCost productOverruncost;
                Optional<ProductOverrunCost> optionalProductOverrunCost = productOverrunCostRepositoryPort.findAllByPieceId(pieceQuotedProductQuotation.getId());

                productOverruncost = optionalProductOverrunCost.orElseGet(ProductOverrunCost::new);

                productOverruncost.setExternalEvent(notice.getExternalEvent().toString())
                        .setBrand(notice.getBrand())
                        .setLine(notice.getLine())
                        .setPlate(notice.getPlate())
                        .setDescription(pieceQuotedProductQuotation.getDescription())
                        .setQuantity(pieceQuotedProductQuotation.getAmount())
                        .setReference(pieceQuotedProductQuotation.getReference())
                        .setSuggestedReference(pieceQuotedProductQuotation.getSuggestedReference())
                        .setTimeDelivery(pieceQuotedProductQuotation.getDeliveryTime())
                        .setImporter(pieceQuotedProductQuotation.getImporter())
                        .setQuotation(pieceQuotedProductQuotation.getQuotation())
                        .setValueExtraCost(pieceQuotedProductQuotation.getValueExtraCost())
                        .setExtraCost(pieceQuotedProductQuotation.getExtraCost())
                        .setNetPrice(pieceQuotedProductQuotation.getNetPrice())
                        .setGrossPrice(pieceQuotedProductQuotation.getGrossPrice())
                        .setDiscountAdditional(pieceQuotedProductQuotation.getDiscountAdditional())
                        .setDiscountBrand(pieceQuotedProductQuotation.getDiscountBrand())
                        .setDiscountCampaigns(pieceQuotedProductQuotation.getDiscountCampaigns())
                        .setStatus(pieceQuotedProductQuotation.getStatus())
                        .setDate(TimeZoneUtil.getTimestampByDefaultZone())
                        .setMaxDeliveryDays(pieceQuotedProductQuotation.getMaxDeliveryDays())
                        .setQuality(pieceQuotedProductQuotation.getQuality())
                        .setComment(pieceQuotedProductQuotation.getComment())
                        .setPieceId(pieceQuotedProductQuotation.getId())
                        .setDiscountManual(pieceQuotedProductQuotation.getDiscountManual())
                        .setPosition(pieceQuotedProductQuotation.getPosition());

                productOverrunCostRepositoryPort.save(productOverruncost);
            }
            return true;
        } catch (ExceptionUtil ex) {
            ex.printStackTrace();
            throw new ExceptionUtil(ex.getStatusCode(),
                    ErrorMessageHandler.concatenateStringAndObject(ERROR_SET_PRODUCT_OVERRUN_COST, notice.getId()), ex.getMessage());
        }
    }

    public void changeStatusQuotationsVoid(Long quotationId) throws ExceptionUtil {
        try {
            CounterStatusProductQuotation quotationCounters = quotationRepositoryPort.countStatusProductQuotation(quotationId);

            changeStatusQuotation(quotationCounters, quotationRepositoryPort);

        } catch (ExceptionUtil ex) {
            ex.printStackTrace();
            throw new ExceptionUtil(ex.getStatusCode(),
                    ErrorMessageHandler.concatenateStringAndObject(ERROR_CHANGE_STATUS, "quotation"), ex.getMessage());
        }

    }

    public static void changeStatusQuotation(CounterStatusProductQuotation quotationCounters, QuotationRepositoryPort quotationRepositoryPort) {
        Long totalProducts = quotationCounters.getTotalProducts();
        Long omittedProducts = quotationCounters.getOmittedProducts();
        Long rejectedQuotedProducts = quotationCounters.getRejectedQuotedProducts();

        if (quotationCounters.getQuotedProducts() > 0 || quotationCounters.getAcceptedProducts() > 0) {
            quotationRepositoryPort.updateStatusQuotation(quotationCounters.getId(), ManageNoticeConstant.QUOTED);
        } else if (omittedProducts.longValue() == totalProducts.longValue() || (omittedProducts > 0
                && (omittedProducts.doubleValue() / totalProducts.doubleValue()) >= 0.5)) {
            quotationRepositoryPort.updateStatusQuotation(quotationCounters.getId(),
                    ManageNoticeConstant.OMITTED);
        } else if (rejectedQuotedProducts.longValue() == totalProducts.longValue()
                || (rejectedQuotedProducts > 0
                && (rejectedQuotedProducts.doubleValue() / totalProducts.doubleValue()) >= 0.5)) {
            quotationRepositoryPort.updateStatusQuotation(quotationCounters.getId(),
                    ManageNoticeConstant.REJECTED_QUOTED);
        }
    }
}



