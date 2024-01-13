package com.subocol.manage.purchase.infrastructure.externalservices.adapters;

import com.google.gson.Gson;
import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.ports.externalservices.BillingOrdersPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.OrderPurchaseInvoiceDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Adapter
@RequiredArgsConstructor
public class BillingOrdersAdapter implements BillingOrdersPort {

    public static final String POST_ADD_BILLING_ORDER = "/api/agregarpedidofacturacion";

    private final RestTemplate restTemplate;

    private final SingletonProperties propertiesBean;


    @Override
    public boolean sendBillingOrders(OrderPurchaseInvoiceDTO orderPurchaseInvoiceDTO) {
        try {
            log.info("Start request billing service buying order");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<OrderPurchaseInvoiceDTO> request = new HttpEntity<>(orderPurchaseInvoiceDTO, headers);
            String url = propertiesBean.getCurrentPropertyByKey(PropertiesConstants.ORDERS_BILLING_MANAGEMENT) +
                    POST_ADD_BILLING_ORDER;

            log.info("Request send: \n" + new Gson().toJson(request));

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            String result = response.getBody();
            log.info("Request response: " + result);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
