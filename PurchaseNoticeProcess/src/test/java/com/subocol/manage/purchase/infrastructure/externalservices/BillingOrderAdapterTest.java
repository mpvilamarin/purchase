package com.subocol.manage.purchase.infrastructure.externalservices;

import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.models.Location;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.OrderPurchaseInvoiceDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.OrderPurchaseInvoiceReplacementDTO;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.BillingOrdersAdapter;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.LocationExternalServicesAdapter;
import com.subocol.manage.purchase.infrastructure.externalservices.dtos.CityDTO;
import com.subocol.manage.purchase.infrastructure.externalservices.dtos.CountryDTO;
import com.subocol.manage.purchase.infrastructure.externalservices.dtos.LocationDTO;
import com.subocol.manage.purchase.infrastructure.externalservices.dtos.RegionDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BillingOrderAdapterTest {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BillingOrdersAdapter billingOrdersAdapter;

    @Mock
    private SingletonProperties propertiesBean;

    OrderPurchaseInvoiceDTO orderPurchaseInvoiceDTO;
    OrderPurchaseInvoiceReplacementDTO orderPurchaseInvoiceReplacementDTO1;
    OrderPurchaseInvoiceReplacementDTO orderPurchaseInvoiceReplacementDTO2;


    @BeforeEach
    void setup() {

        orderPurchaseInvoiceReplacementDTO1=OrderPurchaseInvoiceReplacementDTO.builder()
                .orderedQuantity(1).replacementId(564556L).replacementRef("TU456789")
                .replacementValue(120D).replacementName("volante").build();

        orderPurchaseInvoiceReplacementDTO2=OrderPurchaseInvoiceReplacementDTO.builder()
                .orderedQuantity(2).replacementId(564557L).replacementRef("WER4567796")
                .replacementValue(40D).replacementName("parabrisas").build();

        orderPurchaseInvoiceDTO=OrderPurchaseInvoiceDTO.builder()
                .claimNumber("2323232").orderNumber(123456L).plate("IOP789")
                .currency("$").grossValue(200D).taxValue(20).externalEvent(123456789L)
                .providerNit("34455667").totalValue(220D).insuranceNumber(200000002L)
                .replacement(List.of(orderPurchaseInvoiceReplacementDTO1, orderPurchaseInvoiceReplacementDTO2))
                .build();

    }

    @Test
    void sendBillingOrders() {

        String resultRequest = "The Process is sucessfully";
        ResponseEntity<String> responseEntity=new ResponseEntity<>(resultRequest, HttpStatus.OK);
        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.ORDERS_BILLING_MANAGEMENT))
                .thenReturn("https://liferaydev.subocol.com/o/gestionfacturacion-seguimiento");

        Mockito.when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        boolean result = billingOrdersAdapter.sendBillingOrders(orderPurchaseInvoiceDTO);

        assertTrue(result);
        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.ORDERS_BILLING_MANAGEMENT);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));

    }

    @Test
    void sendBillingOrders_withException() {

        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.ORDERS_BILLING_MANAGEMENT))
                .thenThrow(new RuntimeException());


        boolean result = billingOrdersAdapter.sendBillingOrders(null);

        assertFalse(result);
        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.ORDERS_BILLING_MANAGEMENT);
        verify(restTemplate, never()).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));

    }

}

