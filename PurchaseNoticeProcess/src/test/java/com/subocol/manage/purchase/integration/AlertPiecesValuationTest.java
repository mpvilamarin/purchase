package com.subocol.manage.purchase.integration;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.AlertPiecesValuationAdapter;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeValuationDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_EXTERNAL_SERVICE_ALERT_PIECES_VALUATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AlertPiecesValuationTest {

    @Spy
    private RestTemplate restTemplate;

    @InjectMocks
    private AlertPiecesValuationAdapter alertPiecesValuationAdapter;

    @Mock
    private SingletonProperties propertiesBean;

    NoticeValuationDTO noticeValuationDTO = new NoticeValuationDTO();



    @BeforeEach
    void setup() {
        noticeValuationDTO = NoticeValuationDTO.builder().numeroAviso(484620).build();

        PiecesValuationDTO piecesValuationDTO = PiecesValuationDTO.builder().calidadRepuesto("Original")
                .cantidad(1).codigo("").comprada("N").descuento(10.0).id(1413776L).nombreSucursalGanadora("EXCLUSIVE MOTORS SA")
                .posicion(3).valorUnitario(10.0).valorUnitarioConDescuento(9.0).tiempoEstimadoEntrega(3).build();

        List<PiecesValuationDTO> piecesList = new ArrayList<>();
        piecesList.add(piecesValuationDTO);

        noticeValuationDTO.setPiezas(piecesList);
    }

    @Test
    void testServiceAlertPiecesValuation() throws ExceptionUtil {

        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.AUTH_VALUATION_SERVICES))
                .thenReturn("cmljaGFyZHphcmFtYUBzdWJvY29sLmNvbTpyaWNoYXJkMjAyMw==");

        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.VALUATION_QUOTE_PIECES))
                .thenReturn("https://liferaydev.subocol.com/o/valoracion/estadoPiezasCompras");


        String result = alertPiecesValuationAdapter.serviceAlertPiecesValuation(noticeValuationDTO);


        assertNotNull(result);
        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.AUTH_VALUATION_SERVICES);
        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.VALUATION_QUOTE_PIECES);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }


    @Test
    void testServiceAlertPiecesValuation_() {

        Mockito.lenient().when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.AUTH_VALUATION_SERVICES))
                .thenThrow(new RuntimeException("Simulated exception when getting property"));

        Mockito.lenient().when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.VALUATION_QUOTE_PIECES))
                .thenReturn("https://liferaydev.subocol.com/o/valoracion/estadoPiezasCompras");

        String result=alertPiecesValuationAdapter.serviceAlertPiecesValuation(noticeValuationDTO);

//        assertEquals(ERROR_EXTERNAL_SERVICE_ALERT_PIECES_VALUATION, exception.getMessage());
        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.AUTH_VALUATION_SERVICES);
        verify(restTemplate, never()).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }


    @Test
    void testServiceAlertPiecesValuation_withRestClientException() {

        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.AUTH_VALUATION_SERVICES))
                .thenReturn("cmljaGFyZHphcmFtYUBzdWJvY29sLmNvbTpyaWNoYXJkMjAyMw==");

        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.VALUATION_QUOTE_PIECES))
                .thenReturn("https://liferaydev.subocol.com/o/valoracion/estadoPiezasCompras");


        String result = alertPiecesValuationAdapter.serviceAlertPiecesValuation(null);


        assertNull(result);

        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.AUTH_VALUATION_SERVICES);
        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.VALUATION_QUOTE_PIECES);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));
    }



}
