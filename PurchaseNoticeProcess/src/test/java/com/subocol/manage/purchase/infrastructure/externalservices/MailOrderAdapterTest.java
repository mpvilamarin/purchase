package com.subocol.manage.purchase.infrastructure.externalservices;

import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.MailOrderCreateDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SendSparesToFollowUPDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.MailOrderAdapter;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MailOrderAdapterTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private SingletonProperties propertiesBean;
    @InjectMocks
    private MailOrderAdapter mailOrderAdapter;

    @Test
    void sendMailOrder() {

        List<MailOrderCreateDTO> mailOrderCreateDTOS = new ArrayList<>();
        mailOrderCreateDTOS.add(MailOrderCreateDTO.builder().orderId(999999999L).aviso("999999999L").build());

        String testUrl = "https://liferaydev.subocol.com/o/api/notification/ordercreated";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("true", HttpStatus.OK);

        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.NOTIFICATION_BASE_PATH))
                .thenReturn(testUrl);

        Mockito.when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        boolean result = mailOrderAdapter.sendOrderCreateNotification(mailOrderCreateDTOS);

        assertTrue(result);
        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.NOTIFICATION_BASE_PATH);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));

    }


}

