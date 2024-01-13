package com.subocol.manage.purchase.domain.services;

import com.subocol.manage.purchase.common.annotations.ServiceDeclaration;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.models.*;

import java.util.List;
import java.util.Set;

@ServiceDeclaration
public interface CreateOrder {

    Set<Order> createOrderQuotation(Notice notice, List<ProductQuotation> productQuotations, Integer typePurchaseId) throws ExceptionUtil;

    Order createOrderWithProducts(Notice notice, Integer priority, Long subsidiaryId, List<ProductQuotation> productQuotations, Integer typePurchaseId) throws ExceptionUtil;

    Order createOrder(Notice notice, Integer priority, Long subsidiaryId, Integer typePurchaseId) throws ExceptionUtil;

    ProductOrder createProductOrderQuotation(Order order, ProductQuotation productQuotation, Integer typePurchaseId) throws ExceptionUtil;

    boolean createPurchaseOrder(Order order) throws ExceptionUtil;

    boolean saveSellOrder(Order order, List<ProductOrder> productOrderList) throws ExceptionUtil;

    boolean createStatusReplacement(Order order, String externalEvent) throws ExceptionUtil;

    String purchaseOrderId(SellOrder sellOrder) throws ExceptionUtil;

    StatusParts createStatusParts(ProductOrder productOrder, StatusReplacement statusReplacement, Order order) throws ExceptionUtil;

    Order createOrderQuotationConcessionarie(List<ProductQuotation> productQuotations, Notice notice, Quotation quotation) throws ExceptionUtil;

}