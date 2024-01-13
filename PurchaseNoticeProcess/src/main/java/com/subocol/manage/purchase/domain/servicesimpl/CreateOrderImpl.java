package com.subocol.manage.purchase.domain.servicesimpl;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.ThrowingConsumer;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.externalservices.LocationExternalServicesPort;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.services.CreateOrder;
import com.subocol.manage.purchase.domain.utils.OrderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.*;
import static com.subocol.manage.purchase.domain.constant.ManageNoticeConstant.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class CreateOrderImpl implements CreateOrder {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductOrderRepositoryPort productOrderRepositoryPort;
    private final LocationExternalServicesPort locationExternalServicesPort;
    private final SellOrderRepositoryPort sellOrderRepositoryPort;
    private final SubsidiaryRepositoryPort subsidiaryRepositoryPort;
    private final TaxRepositoryPort taxRepositoryPort;
    private final NoticeRepositoryPort noticeRepositoryPort;
    private final InsuranceCarrierRepositoryPort insuranceCarrierRepositoryPort;
    private final StatusReplacementRepositoryPort statusReplacementRepositoryPort;
    private final ProductQuotationRepositoryPort productQuotationRepositoryPort;
    private final ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;
    private final SellOrderDetailRepositoryPort sellOrderDetailRepositoryPort;
    private final StatusPartsRepositoryPort statusPartsRepositoryPort;

    @Override
    public Set<Order> createOrderQuotation(Notice notice, List<ProductQuotation> productQuotations, Integer typePurchaseId) throws ExceptionUtil {
        log.info("START CREATING ORDERS FOR EXTERNAL_EVENT: " + notice.getExternalEvent());
        List<Order> orders = new CopyOnWriteArrayList<>();
        try {
            List<Long> distinctSubsidiaryIds = productQuotations.stream()
                    .map(pq -> pq.getQuotation().getSubsidiary().getId())
                    .distinct()
                    .toList();

            for (Long subsidiaryId : distinctSubsidiaryIds) {
                orders.add(createOrderWithProducts(notice, 1, subsidiaryId,
                        productQuotations.stream().filter(
                                        product -> Objects.equals(product.getQuotation().getSubsidiary().getId(), subsidiaryId))
                                .toList(), typePurchaseId));
            }

            for (Order order : orders)
                saveOrderAndProductOrder(order);

            if (Objects.equals(typePurchaseId, TYPE_MANUAL)) {
                productQuotationRepositoryPort.updateProductQuotationStatusTrueMP(
                        productQuotations.stream().map(ProductQuotation::getId).toList(),
                        DEFAULT_MANUAL_PURCHASE_STATUS, true);

                manualPurchaseRepositoryPort.updateManualPurchaseByPosition(DEFAULT_MANUAL_PURCHASE_STATUS,
                        notice.getExternalEvent().toString(),
                        productQuotations.stream().map(ProductQuotation::getPosition).toList());
            }

            orders.forEach(
                    ThrowingConsumer.wrapExceptions(this::createPurchaseOrder));

            orders.forEach(
                    ThrowingConsumer.wrapExceptions(order -> createStatusReplacement(order, notice.getExternalEvent().toString())));

            log.info("NOTICE UPDATED WITH ORDERS: " + notice.getExternalEvent() + " ORDERS AMOUNT: " + orders.size());
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = ErrorMessageHandler.concatenateStringAndObject(ERROR_CREATE_ORDER, notice.getId());
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage, e.getMessage());
        }
        return new HashSet<>(orders);
    }

    @Override
    public Order createOrderWithProducts(Notice notice, Integer priority, Long subsidiaryId, List<ProductQuotation> productQuotations, Integer typePurchaseId) throws ExceptionUtil {
        Set<ProductOrder> finalProductOrders = new CopyOnWriteArraySet<>();
        try {

            Order order = createOrder(notice, priority, subsidiaryId, typePurchaseId);

            productQuotations.parallelStream().forEach(ThrowingConsumer.wrapExceptions(
                    pq -> finalProductOrders.add(createProductOrderQuotation(order, pq, typePurchaseId))));

            order.setProducts(finalProductOrders);

            return order;
        } catch (Exception e) {
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorMessageHandler.concatenateStringAndObject(ERROR_CREATE_ORDER_AND_PRODUCTS, notice.getId()), e.getMessage());
        }
    }

    @Override
    public Order createOrder(Notice notice, Integer priority, Long subsidiaryId, Integer typePurchaseId) throws ExceptionUtil {
        try {
            Subsidiary subsidiary = subsidiaryRepositoryPort.findById(subsidiaryId)
                    .orElseThrow(() -> new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ErrorMessageHandler.concatenateStringAndObject(SUBSIDIARY_NOT_FOUND, subsidiaryId)));


            return new Order().setPriority(priority)
                    .setSubsidiary(subsidiary)
                    .setWorkshop(notice.getWorkshop())
                    .setStatus(ASSIGNED.toLowerCase())
                    .setDate(TimeZoneUtil.getTimestampByDefaultZone())
                    .setNotice(notice)
                    .setUnforeseen(notice.isUnforeseen())
                    .setRepairOrder(notice.getRepairOrder())
                    .setPurchaseTypeId(typePurchaseId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ErrorMessageHandler.concatenateStringAndObject(ERROR_CREATE_ORDER, notice.getId()), e.getMessage());
        }
    }

    @Override
    public ProductOrder createProductOrderQuotation(Order order, ProductQuotation productQuotation, Integer typePurchaseId) throws ExceptionUtil {
        try {
            String userName = "";

            if (TYPE_AUTOMATIC.equals(typePurchaseId)) {
                userName = AUTOMATIC_PURCHASE;
            } else if (TYPE_MANUAL.equals(typePurchaseId)) {
                userName = productQuotation.getUserName();
            }
            return new ProductOrder()
                    .setStatus(ManageNoticeConstant.ACCEPTED.toLowerCase())
                    .setReference(productQuotation.getReference())
                    .setPrice(productQuotation.getNetPrice())
                    .setDescription(productQuotation.getDescription())
                    .setOrder(order)
                    .setQuality(productQuotation.getQuality())
                    .setAmount(productQuotation.getAmount())
                    .setGrossPrice(productQuotation.getGrossPrice())
                    .setImporter(productQuotation.getImporter())
                    .setPromisedDeliveryDays(productQuotation.getDeliveryTime())
                    .setPromiseDelivery(new Timestamp(OrderUtil
                            .plusBusinessDays(order.getDate(), productQuotation.getDeliveryTime() == null ? 0 : productQuotation.getDeliveryTime())
                            .getTime()))
                    .setAcceptDate(TimeZoneUtil.getTimestampByDefaultZone())
                    .setIsDelayed(false)
                    .setDesist(false)
                    .setPositionPiece(productQuotation.getPosition())
                    .setComment(productQuotation.getComment())
                    .setUserName(userName)
                    .setTotalDiscount(
                            (productQuotation.getDiscountAdditional() == null ? 0 : productQuotation.getDiscountAdditional()) +
                                    (productQuotation.getDiscountBrand() == null ? 0 : productQuotation.getDiscountBrand()) +
                                    (productQuotation.getDiscountCampaigns() == null ? 0 : productQuotation.getDiscountCampaigns()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_CREATE_PRODUCT_ORDER, e.getMessage());
        }
    }

    @Override
    public boolean createPurchaseOrder(Order order) throws ExceptionUtil {
        try {
            if (!order.getProducts().isEmpty())
                saveSellOrder(order, order.getProducts().stream().toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_CREATE_PURCHASE_ORDER, e.getMessage());
        }
        return true;
    }

    @Override
    public boolean saveSellOrder(Order order, List<ProductOrder> productOrderList) throws ExceptionUtil {
        try {
            SellOrder sellOrder = SellOrder.builder()
                    .order(new Order().setId(order.getId()))
                    .subtotal(0D)
                    .iva(0.0)
                    .total(0.0)
                    .details(new HashSet<>())
                    .creationDate(TimeZoneUtil.getTimestampByDefaultZone())
                    .lastUpdateDate(TimeZoneUtil.getTimestampByDefaultZone())
                    .build();

            sellOrder = sellOrderRepositoryPort.save(sellOrder);
            sellOrder.setOrder(order);

            Set<SellOrderDetail> orderDetailSet = new HashSet<>();

            for (ProductOrder productOrder : productOrderList)
                orderDetailSet.add(addNewDetails(sellOrder, productOrder));

            Double subtotal = orderDetailSet.stream()
                    .map(SellOrderDetail::getTotal)
                    .reduce(Double::sum)
                    .orElse(null);

            Long countryId = locationExternalServicesPort.findLocation(sellOrder.getOrder().getSubsidiary().getLocationExternalId()).getCountryId();

            if (subtotal != null) {
                Optional<Tax> taxOptional = countryId != null ?
                        taxRepositoryPort.findTaxByCountry(countryId) : taxRepositoryPort.findTaxByCountry((long) COLOMBIA);

                if (taxOptional.isEmpty())
                    return false;

                double iva = (subtotal * taxOptional.get().getPercentage()) / 100;
                double ivaRound = Math.round(iva * 100.0) / 100.0;

                sellOrder.setIva(ivaRound);
                double total = subtotal + sellOrder.getIva();

                sellOrder.setSubtotal(Math.round(subtotal * 100.0) / 100.0)
                        .setTotal(Math.round(total * 100.0) / 100.0)
                        .setOrder(new Order().setId(order.getId()));
                sellOrder = sellOrderRepositoryPort.save(sellOrder);
                sellOrder.setOrder(order);

            }

            order.setOrderIdSubocol(purchaseOrderId(sellOrder));

            sellOrderDetailRepositoryPort.saveAllNative(orderDetailSet.stream().toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_SAVE_SELL_ORDER, e.getMessage());
        }
        return true;
    }

    public SellOrderDetail addNewDetails(SellOrder sellOrder, ProductOrder productOrder) throws ExceptionUtil {
        try {
            return SellOrderDetail.builder()
                    .sellOrder(new SellOrder().setId(sellOrder.getId()))
                    .unitPrice(productOrder.getPrice())
                    .amount(productOrder.getAmount())
                    .total(productOrder.getPrice() * productOrder.getAmount())
                    .reference(productOrder.getReference() != null ? productOrder.getReference() : "")
                    .description(productOrder.getDescription())
                    .grossPrice(productOrder.getGrossPrice())
                    .discount(productOrder.getTotalDiscount())
                    .promiseDelivery(productOrder.getPromiseDelivery())
                    .comment(productOrder.getComment())
                    .positionPiece(productOrder.getPositionPiece())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_CREATE_SELL_ORDER_DETAIL, e.getMessage());

        }
    }

    @Override
    public boolean createStatusReplacement(Order order, String externalEvent) throws ExceptionUtil {
        try {
            Provider provider = order.getSubsidiary().getProvider();

            StatusReplacement statusReplacement = StatusReplacement.builder()
                    .externalEvent(externalEvent)
                    .seller("Orbika")
                    .dateOrder(order.getDate())
                    .provider(provider.getName())
                    .email(provider.getEmail())
                    .phone(provider.getPhone())
                    .approvedDate(order.getDate())
                    .providerObservation(order.getComment())
                    .quantityParts(order.getProducts().size())
                    .emailSubsidiary(order.getSubsidiary().getEmail())
                    .subsidiary(order.getSubsidiary().getName())
                    .phoneSubsidiary(order.getSubsidiary().getPhone())
                    .build();

            statusReplacement = statusReplacementRepositoryPort.save(statusReplacement);

            Set<StatusParts> finalStatusParts = new CopyOnWriteArraySet<>();
            StatusReplacement finalStatusReplacement = statusReplacement;

            order.getProducts().parallelStream().forEach(
                    ThrowingConsumer.wrapExceptions(productOrder ->
                            finalStatusParts.add(createStatusParts(productOrder, finalStatusReplacement, order))));

            statusReplacement.setStatusParts(finalStatusParts);

            statusPartsRepositoryPort.saveAllNative(finalStatusParts.stream().toList());
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_CREATE_STATUS_REPLACEMENT, e.getMessage());

        }
    }

    @Override
    public String purchaseOrderId(SellOrder sellOrder) throws ExceptionUtil {
        String purchaseOrderId;
        try {
            Optional<Notice> noticeOptional = noticeRepositoryPort.findById(sellOrder.getOrder().getNotice().getId());

            Notice notice;

            if (noticeOptional.isPresent())
                notice = noticeOptional.get();
            else
                return ErrorMessageHandler.concatenateStringAndObject(NOTICE_NOT_FOUND, sellOrder.getOrder().getNotice().getId());

            InsuranceCarrier insuranceCarrier;

            Optional<InsuranceCarrier> insuranceCarrierOptional = insuranceCarrierRepositoryPort.findById(notice.getInsuranceNumber());

            if (insuranceCarrierOptional.isPresent())
                insuranceCarrier = insuranceCarrierOptional.get();
            else
                return ErrorMessageHandler.concatenateStringAndObject(INSURER_CARRIER_NOT_FOUND, notice.getInsuranceNumber());

            String providerType = sellOrder.getOrder().getSubsidiary().getProvider().getProviderClassification();

            if ("AUTOSUMINISTRO".toLowerCase().equalsIgnoreCase(providerType))
                providerType = TYPE_CONCESSIONAIRE;
            else if ("MOSTRADOR".toLowerCase().equalsIgnoreCase(providerType))
                providerType = TYPE_MULTI_BRAND;
            else if ("MIXTO".toLowerCase().equalsIgnoreCase(providerType))
                providerType = TYPE_MIXED;
            else providerType = EXTERNAL;

            if (notice.getIdCountry() == ManageNoticeConstant.COLOMBIA)
                purchaseOrderId = (PREFIX_COLOMBIA);
            else if (notice.getIdCountry() == ManageNoticeConstant.CHILE)
                purchaseOrderId = (PREFIX_CHILE);
            else
                purchaseOrderId = (PREFIX_PANAMA);

            purchaseOrderId = (purchaseOrderId + HYPHEN + insuranceCarrier.getPrefix() + providerType + HYPHEN + sellOrder.getOrder().getId());

            return purchaseOrderId;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_CREATE_PURCHASE_ORDER_ID, e.getMessage());
        }
    }

    @Override
    public StatusParts createStatusParts(ProductOrder productOrder, StatusReplacement statusReplacement, Order order) throws ExceptionUtil {
        try {
            return StatusParts.builder()
                    .statusReplacement(new StatusReplacement().setId(statusReplacement.getId()))
                    .idProductOrder(productOrder.getId())
                    .namePart(productOrder.getDescription())
                    .reference(productOrder.getReference())
                    .importPart(productOrder.getImporter())
                    .idOrder(order.getId())
                    .totalParts(productOrder.getAmount())
                    .status(productOrder.getStatus())
                    .approvedOrderDate(order.getDate())
                    .estimateDeliveryDate(productOrder.getPromiseDelivery())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_CREATE_STATUS_PARTS, e.getMessage());
        }
    }

    public Order createOrderQuotationConcessionarie(List<ProductQuotation> productQuotations, Notice notice, Quotation quotation) throws ExceptionUtil {
        log.info("START CREATING ORDERS FOR EXTERNAL_EVENT: " + notice.getExternalEvent());

        try {

            Order order = createOrderWithProducts(notice, 0, quotation.getSubsidiary().getId(), productQuotations, TYPE_AUTOMATIC);

            saveOrderAndProductOrder(order);

            createPurchaseOrder(order);

            createStatusReplacement(order, notice.getExternalEvent().toString());

            log.info("NOTICE UPDATED WITH ORDERS: " + notice.getExternalEvent());
            return order;
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = ErrorMessageHandler.concatenateStringAndObject(ERROR_CREATE_ORDER, notice.getId());
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage, e.getMessage());
        }
    }

    public void saveOrderAndProductOrder(Order order) {
        try {
            Set<ProductOrder> productOrderList = new HashSet<>(order.getProducts());
            order.getProducts().clear();

            order.setId(orderRepositoryPort.save(order).getId());
            long orderId = order.getId();
            log.info("Order saved successfully id: " + orderId);

            productOrderRepositoryPort.saveAllNative(productOrderList.stream().map(product -> product.setOrder(new Order().setId(orderId))).toList());

            order.setProducts(new HashSet<>(productOrderRepositoryPort.findAllByIdOrder(order.getId())));
        } catch (Exception e) {
            throw new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), ERROR_SAVING_ORDERS_AND_PRODUCT_ORDERS, e.getMessage());
        }
    }

}
