package com.subocol.manage.purchase.infrastructure.externalservices;

import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.OrderPurchaseInvoiceDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.OrderPurchaseInvoiceReplacementDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SendSparesToFollowUPDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.BillingOrdersAdapter;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.FollowUpAdapter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class FollowUpAdapterTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private SingletonProperties propertiesBean;
    @InjectMocks
    private FollowUpAdapter followUpAdapter;

    SendSparesToFollowUPDTO sendSparesToFollowUPDTO;
    SpareDetailToFollowUpDTO spareDetailToFollowUpDTO1;
    SpareDetailToFollowUpDTO spareDetailToFollowUpDTO2;

    @BeforeEach
    void setup() {

        spareDetailToFollowUpDTO1=SpareDetailToFollowUpDTO.builder()
                .posicion(1).esCotizado(true).cantidad(1).deleted(false)
                .idStatusParts(123L).build();

        spareDetailToFollowUpDTO2=SpareDetailToFollowUpDTO.builder()
                .posicion(2).esCotizado(true).cantidad(1).deleted(false)
                .idStatusParts(124L).build();

        sendSparesToFollowUPDTO=SendSparesToFollowUPDTO.builder()
                .idAviso(1234L).repuestos(List.of(spareDetailToFollowUpDTO1, spareDetailToFollowUpDTO2)).build();

    }

    @Test
    void sendBillingOrders() {

        String resultRequest = "The Process is sucessfully";
        ResponseEntity<String> responseEntity=new ResponseEntity<>(resultRequest, HttpStatus.OK);
        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.FOLLOWUP_SEND_INFORMATION))
                .thenReturn("https://liferaydev.subocol.com/o/aviso-seguimiento/cotizados/crear-actualizar");

        Mockito.when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        boolean result = followUpAdapter.sendDataToSFollowUp(sendSparesToFollowUPDTO);

        assertTrue(result);
        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.FOLLOWUP_SEND_INFORMATION);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));

    }

    @Test
    void sendBillingOrders_withException() {

        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.FOLLOWUP_SEND_INFORMATION))
               .thenThrow(new RuntimeException());


        boolean result = followUpAdapter.sendDataToSFollowUp(sendSparesToFollowUPDTO);

        assertFalse(result);
        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.FOLLOWUP_SEND_INFORMATION);
        verify(restTemplate, never()).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));

    }

}

