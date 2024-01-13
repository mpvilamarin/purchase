package com.subocol.manage.purchase.domain.ports.externalservices;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.OrderPurchaseInvoiceDTO;

@Port
public interface BillingOrdersPort {

    boolean sendBillingOrders(OrderPurchaseInvoiceDTO orderPurchaseInvoiceDTO);

}
