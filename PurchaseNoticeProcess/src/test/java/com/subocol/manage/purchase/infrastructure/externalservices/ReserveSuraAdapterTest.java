package com.subocol.manage.purchase.infrastructure.externalservices;

import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.*;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.ReserveSuraAdapter;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ReserveSuraAdapterTest {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private SingletonProperties propertiesBean;
    @InjectMocks
    private ReserveSuraAdapter reserveSuraAdapter;

    private ReserveCalculationSuraDTO reserveCalculationSuraDTO=new ReserveCalculationSuraDTO();
    private ReserveCalculationTotalSuraDTO pedidoInicial;
    private ReserveCalculationTotalSuraDTO imprevistos;
    private List<ReserveRepuestosSuraDTO> repuesto=new ArrayList<>();

    @BeforeEach
    void setup() {

       pedidoInicial=ReserveCalculationTotalSuraDTO.builder().totalRepuestos(100D).precioTotalIva(10D).valorTotalDescuento(0D).build();
       imprevistos=ReserveCalculationTotalSuraDTO.builder().totalRepuestos(100D).precioTotalIva(10D).valorTotalDescuento(0D).build();

       repuesto.add(new ReserveRepuestosSuraDTO(
                "123", 1, "Pieza1", "REF1", 10, 50.0, 5.0, 10.0, "ORIGEN1", true, 15.0, "PROV1", 10D
        ));
       repuesto.add(new ReserveRepuestosSuraDTO(
                "456", 2, "Pieza2", "REF2", 20, 30.0, 3.0, 5.0, "ORIGEN2", false, 10.0, "PROV2", 10D
        ));

        reserveCalculationSuraDTO.setNumeroAviso(123);
        reserveCalculationSuraDTO.setPedidoInicial(pedidoInicial);
        reserveCalculationSuraDTO.setImprevistos(imprevistos);
        reserveCalculationSuraDTO.setRepuesto(repuesto);
    }

    @Test
    void sendPiecesReserveAdminSura_sucessfully() {

        String resultRequest = "The Process is sucessfully";
        ResponseEntity<String> responseEntity=new ResponseEntity<>(resultRequest, HttpStatus.OK);
        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.ADMIN_RESERVE_SURA))
                .thenReturn("https://liferaydev.subocol.com/o/avisos/putTotalesCompras");

        Mockito.when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        String result = reserveSuraAdapter.sendPiecesReserveAdminSura(reserveCalculationSuraDTO);

        assertEquals(HttpStatus.OK.toString(), result);
        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.ADMIN_RESERVE_SURA);
        verify(restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));

    }

    @Test
    void sendPiecesReserveAdminSura_withException() {

        Mockito.when(propertiesBean.getCurrentPropertyByKey(PropertiesConstants.ADMIN_RESERVE_SURA))
               .thenThrow(new RuntimeException());


        String result = reserveSuraAdapter.sendPiecesReserveAdminSura(reserveCalculationSuraDTO);

        assertEquals("", result);
        verify(propertiesBean, times(1)).getCurrentPropertyByKey(PropertiesConstants.ADMIN_RESERVE_SURA);
        verify(restTemplate, never()).postForEntity(anyString(), any(HttpEntity.class), eq(String.class));

    }

}

