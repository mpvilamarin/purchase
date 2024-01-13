package com.subocol.manage.purchase.domain.servicesimpl.integrations;

import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.externalservices.BillingOrdersPort;
import com.subocol.manage.purchase.domain.ports.persistence.CurrencyRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.SellOrderRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.TaxRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.OrderPurchaseInvoiceDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.OrderPurchaseInvoiceReplacementDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingOrders {

    private final BillingOrdersPort billingOrdersPort;
    private final SellOrderRepositoryPort sellOrderRepositoryPort;
    private final TaxRepositoryPort taxRepositoryPort;
    private final CurrencyRepositoryPort currencyRepositoryPort;

    public boolean sendBillingOrder(Order order, Notice notice) {

        try {

            Pair<Double, Double> valuesSellOrder = sellOrderRepositoryPort.findValuesSellOrderByOrderId(order.getId());
            Double subtotalSellOrder = valuesSellOrder.getFirst();
            Double totalSellOrder = valuesSellOrder.getSecond();
            log.info("Start process buying order to billing");
            OrderPurchaseInvoiceDTO orderPurchaseInvoiceDTO = new OrderPurchaseInvoiceDTO();
            orderPurchaseInvoiceDTO.setGrossValue(subtotalSellOrder);


            //Se consulta la información del proveedor
            Provider provider = order.getSubsidiary().getProvider();
            orderPurchaseInvoiceDTO.setProviderNit(provider.getNit());
            orderPurchaseInvoiceDTO.setCompanyNameProvider(provider.getName());

            //Se consulta la información de los impuestos
            Optional<Tax> tax = taxRepositoryPort.findTaxByCountry(notice.getIdCountry());

            tax.ifPresent(value -> orderPurchaseInvoiceDTO.setTaxValue(value.getPercentage()));

            orderPurchaseInvoiceDTO.setTotalValue(totalSellOrder);


            //Se consulta la información de la moneda
            Optional<Currency> currency = currencyRepositoryPort.findByCountryId(notice.getIdCountry());
            currency.ifPresent(value -> orderPurchaseInvoiceDTO.setCurrency(value.getPrefix()));

            orderPurchaseInvoiceDTO.setOrderNumber(order.getId());


            //Se consulta la información del aviso, placa, siniestro y aseguradora
            orderPurchaseInvoiceDTO.setExternalEvent(Long.valueOf(notice.getExternalEvent()));
            orderPurchaseInvoiceDTO.setPlate(notice.getPlate());
            orderPurchaseInvoiceDTO.setClaimNumber(notice.getClaimNumber());
            orderPurchaseInvoiceDTO.setInsuranceNumber(notice.getInsuranceNumber());


            //Se llena la información de la lista de los repuestos
            List<OrderPurchaseInvoiceReplacementDTO> listReplacement = new ArrayList<>();

            for (ProductOrder replacement : order.getProducts()) {
                if (!replacement.getStatus().equalsIgnoreCase(ManageNoticeConstant.DESIST)) {
                    OrderPurchaseInvoiceReplacementDTO orderPurchaseInvoiceReplacementDTO = new OrderPurchaseInvoiceReplacementDTO();
                    orderPurchaseInvoiceReplacementDTO.setReplacementRef(replacement.getReference());
                    orderPurchaseInvoiceReplacementDTO.setReplacementName(replacement.getDescription());
                    orderPurchaseInvoiceReplacementDTO.setOrderedQuantity(replacement.getAmount());
                    orderPurchaseInvoiceReplacementDTO.setReplacementValue(replacement.getPrice());
                    orderPurchaseInvoiceReplacementDTO.setReplacementId(replacement.getId());
                    listReplacement.add(orderPurchaseInvoiceReplacementDTO);
                }

            }

            orderPurchaseInvoiceDTO.setReplacement(listReplacement);

            //Se envía la orden de compra a gestión de la facturación
            billingOrdersPort.sendBillingOrders(orderPurchaseInvoiceDTO);
            log.info("Finish buying order to billing");
            return true;
        } catch (Exception e) {
            log.info(e.toString());
            e.printStackTrace();
            return false;
        }
    }

}
