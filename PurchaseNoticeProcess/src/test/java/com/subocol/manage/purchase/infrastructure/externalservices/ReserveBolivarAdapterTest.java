package com.subocol.manage.purchase.infrastructure.externalservices;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationBolivarDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalBolivarDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SendSparesToFollowUPDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.FollowUpAdapter;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.ReserveBolivarAdapter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ReserveBolivarAdapterTest {

    @Mock
    private RestTemplate restTemplate;
    @Mock
    private SingletonProperties propertiesBean;
    @InjectMocks
    private ReserveBolivarAdapter reserveBolivarAdapter;

    ReserveCalculationBolivarDTO reserveCalculationBolivarDTO;
    ReserveCalculationTotalBolivarDTO reserveCalculationTotalBolivarDTO;
    ReserveCalculationTotalBolivarDTO reserveCalculationTotalBolivarUnforeseenDTO;

    @BeforeEach
    void setup() {

        reserveCalculationBolivarDTO= ReserveCalculationBolivarDTO.builder().numeroAviso(451641).build();

        reserveCalculationTotalBolivarUnforeseenDTO= ReserveCalculationTotalBolivarDTO.builder().totalRepuNeto(62.0).totalTotNeto(95.0).build();

        reserveCalculationTotalBolivarDTO= ReserveCalculationTotalBolivarDTO.builder().totalRepuNeto(67.0).totalTotNeto(64.0).build();

        reserveCalculationBolivarDTO.setImprevistos(List.of(reserveCalculationTotalBolivarUnforeseenDTO));

        reserveCalculationBolivarDTO.setImprevistos(List.of(reserveCalculationTotalBolivarDTO));
    }



    @Test
    void testSendReserveCalculationAdmin() {
        String resultRequest = "The Process is successfully";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(resultRequest, HttpStatus.OK);


        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.ADMIN_RESERVE_BOLIVAR))
                .thenReturn("https://liferaydev.subocol.com/o/avisos/putTotalesCompras");


        Mockito.doNothing().when(restTemplate).put(anyString(), any(HttpEntity.class), eq(String.class));

        reserveBolivarAdapter.sendReserveCalculationAdmin(reserveCalculationBolivarDTO);


        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.ADMIN_RESERVE_BOLIVAR);
        verify(restTemplate, times(1)).put(anyString(), any(HttpEntity.class), eq(String.class));

    }

    @Test
    void testSendReserveCalculationAdmin_withException() {

        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.ADMIN_RESERVE_BOLIVAR))
                .thenThrow(new RuntimeException());

        String result=
            reserveBolivarAdapter.sendReserveCalculationAdmin(reserveCalculationBolivarDTO);

        assertNull(result);
//        assertEquals("An error occurred in the external service sending reserve bolivar with notice: "+reserveCalculationBolivarDTO.getNumeroAviso() , exception.getMessage());

        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.ADMIN_RESERVE_BOLIVAR);

        verify(restTemplate, never()).put(anyString(), any(HttpEntity.class), eq(String.class));
    }





}
