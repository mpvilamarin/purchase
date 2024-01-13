package com.subocol.manage.purchase.domain;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.ports.externalservices.AlertPiecesValuationPort;
import com.subocol.manage.purchase.domain.ports.persistence.OptionQuotePort;
import com.subocol.manage.purchase.domain.ports.persistence.ProductQuotationRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeValuationDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.AlertPiecesValuationImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertPiecesValuationTest {

    @Mock
    private ProductQuotationRepositoryPort productQuotationRepositoryPort;
    @Mock
    private AlertPiecesValuationPort alertPiecesValuationPort;
    @Mock
    private OptionQuotePort optionQuotePort;
    @InjectMocks
    private AlertPiecesValuationImpl alertPiecesValuation;
    Notice notice1;
    Notice notice2;
    PiecesValuationDTO piecesQuoted1;
    PiecesValuationDTO piecesQuoted2;
    PiecesValuationDTO piecesAccepted1;
    PiecesValuationDTO piecesAccepted2;
    List<PiecesValuationDTO> piecesAccepted = new ArrayList<>();
    List<PiecesValuationDTO> piecesQuoted = new ArrayList<>();

    @BeforeEach
    void setup() {
        notice1 = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(123)
                .id(789L).eventId(456L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000001L).workshopType("Autosuministro").build();

        notice2 = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(124)
                .id(790L).eventId(457L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000001L).workshopType("Multimarca").build();

        piecesAccepted1 = PiecesValuationDTO.builder().posicion(1).codigo("02100043nndn").referencia("Sin Referencia").cantidad(1)
                .valorUnitario(10.0).valorUnitarioConDescuento(2.3).comprada("S").tiempoEstimadoEntrega(1).calidadRepuesto("ORIGINAL")
                .descuento(77.0).nombreSucursalGanadora("PROVEEDOR AUTOSUM DEV").build();

        piecesAccepted2 = PiecesValuationDTO.builder().posicion(2).codigo("02100043nndn").referencia("Sin Referencia").cantidad(1)
                .valorUnitario(10.0).valorUnitarioConDescuento(2.3).comprada("S").tiempoEstimadoEntrega(1).calidadRepuesto("ORIGINAL")
                .descuento(77.0).nombreSucursalGanadora("PROVEEDOR AUTOSUM DEV").build();

        piecesQuoted1 = PiecesValuationDTO.builder().posicion(3).codigo("02100043nndn").referencia("Sin Referencia").cantidad(1)
                .valorUnitario(10.0).valorUnitarioConDescuento(2.3).comprada("N").tiempoEstimadoEntrega(1).calidadRepuesto("ORIGINAL")
                .descuento(77.0).nombreSucursalGanadora("PROVEEDOR AUTOSUM DEV").build();

        piecesQuoted2 = PiecesValuationDTO.builder().posicion(4).codigo("02100043nndn").referencia("Sin Referencia").cantidad(1)
                .valorUnitario(10.0).valorUnitarioConDescuento(2.3).comprada("N").tiempoEstimadoEntrega(1).calidadRepuesto("ORIGINAL")
                .descuento(77.0).nombreSucursalGanadora("PROVEEDOR AUTOSUM DEV").build();

        piecesAccepted = new ArrayList<>();
        piecesAccepted.add(piecesAccepted1);
        piecesAccepted.add(piecesAccepted2);

        piecesQuoted = new ArrayList<>();
        piecesQuoted.add(piecesQuoted1);
        piecesQuoted.add(piecesQuoted2);

    }

    @Test
    void testAlertPiecesValuationWorkshopTypeSelfSupplyAuthTrue() throws ExceptionUtil {

        Mockito.when(productQuotationRepositoryPort.findPiecesValuationConcessionaireAccepted(notice1.getExternalEvent())).thenReturn(piecesAccepted);
        Mockito.when(productQuotationRepositoryPort.findPiecesValuationConcessionaireQuoted(notice1.getExternalEvent())).thenReturn(piecesQuoted);
        Mockito.when(productQuotationRepositoryPort.updateSendValuationQuotation(anyList(), anyBoolean())).thenReturn(4);
        Mockito.when(productQuotationRepositoryPort.updateSendValuationPurchase(anyList(), anyBoolean())).thenReturn(2);

        alertPiecesValuation.alertPiecesValuation(notice1.getExternalEvent(),true, notice1.getWorkshopType());

        verify(productQuotationRepositoryPort).updateSendValuationQuotation(anyList(), eq(true));
        verify(productQuotationRepositoryPort).updateSendValuationPurchase(anyList(), eq(true));
        verify(alertPiecesValuationPort).serviceAlertPiecesValuation(any(NoticeValuationDTO.class));
        verify(productQuotationRepositoryPort, times(1)).findPiecesValuationConcessionaireAccepted(anyInt());
        verify(productQuotationRepositoryPort, times(1)).findPiecesValuationConcessionaireQuoted(anyInt());

    }

    @Test
    void testAlertPiecesValuationWorkshopTypeSelfSupplyAuthFalse() throws ExceptionUtil {

        Mockito.when(productQuotationRepositoryPort.findPiecesValuationConcessionaireQuoted(notice1.getExternalEvent())).thenReturn(piecesQuoted);
        Mockito.when(productQuotationRepositoryPort.updateSendValuationQuotation(anyList(), anyBoolean())).thenReturn(4);

        alertPiecesValuation.alertPiecesValuation(notice1.getExternalEvent(),false, notice1.getWorkshopType());

        verify(productQuotationRepositoryPort).updateSendValuationQuotation(anyList(), eq(true));
        verify(productQuotationRepositoryPort, never()).updateSendValuationPurchase(anyList(), eq(true));
        verify(productQuotationRepositoryPort, never()).findPiecesValuationConcessionaireAccepted(anyInt());
        verify(alertPiecesValuationPort).serviceAlertPiecesValuation(any(NoticeValuationDTO.class));
        verify(productQuotationRepositoryPort, times(1)).findPiecesValuationConcessionaireQuoted(anyInt());

    }


    @Test
    void testAlertPiecesValuation_Exception() throws ExceptionUtil {
        Mockito.when(productQuotationRepositoryPort.findPiecesValuationConcessionaireQuoted(Mockito.anyInt())).thenThrow(new RuntimeException("Error en la consulta"));

        boolean result= alertPiecesValuation.alertPiecesValuation(notice1.getExternalEvent(), false, notice1.getWorkshopType());
        Assertions.assertNotNull(result);
        Mockito.verify(productQuotationRepositoryPort, Mockito.never()).updateSendValuationQuotation(Mockito.anyList(), Mockito.eq(true));
        Mockito.verify(productQuotationRepositoryPort, Mockito.never()).updateSendValuationPurchase(Mockito.anyList(), Mockito.eq(true));
        Mockito.verify(productQuotationRepositoryPort, Mockito.never()).findPiecesValuationConcessionaireAccepted(Mockito.anyInt());
        Mockito.verify(productQuotationRepositoryPort).findPiecesValuationConcessionaireQuoted(Mockito.anyInt());
    }

    @Test
    void testAlertPiecesValuationWorkshopTypeMultibrandAuthTrue() throws ExceptionUtil {

        Mockito.when(optionQuotePort.findPiecesValuationMultibrandAccepted(notice2.getExternalEvent())).thenReturn(piecesAccepted);
        Mockito.when(optionQuotePort.findPiecesValuationMultibrandQuoted(notice2.getExternalEvent())).thenReturn(piecesQuoted);
        Mockito.when(productQuotationRepositoryPort.findPiecesValuationMultibrandBought(notice2.getExternalEvent())).thenReturn(piecesAccepted);
        Mockito.when(productQuotationRepositoryPort.updateSendValuationQuotation(anyList(), anyBoolean())).thenReturn(4);
        Mockito.when(productQuotationRepositoryPort.updateSendValuationPurchase(anyList(), anyBoolean())).thenReturn(2);

        alertPiecesValuation.alertPiecesValuation(notice2.getExternalEvent(),true, notice2.getWorkshopType());

        verify(productQuotationRepositoryPort).updateSendValuationQuotation(anyList(), eq(true));
        verify(productQuotationRepositoryPort).updateSendValuationPurchase(anyList(), eq(true));
        verify(alertPiecesValuationPort).serviceAlertPiecesValuation(any(NoticeValuationDTO.class));
        verify(optionQuotePort, times(1)).findPiecesValuationMultibrandAccepted(anyInt());
        verify(productQuotationRepositoryPort, times(1)).findPiecesValuationMultibrandBought(anyInt());
        verify(optionQuotePort, times(1)).findPiecesValuationMultibrandQuoted(anyInt());

    }

    @Test
    void testAlertPiecesValuationWorkshopTypeMultibrandAuthFalse() throws ExceptionUtil {

        Mockito.when(optionQuotePort.findPiecesValuationMultibrandQuoted(notice2.getExternalEvent())).thenReturn(piecesQuoted);
        Mockito.when(productQuotationRepositoryPort.updateSendValuationQuotation(anyList(), anyBoolean())).thenReturn(2);

        alertPiecesValuation.alertPiecesValuation(notice2.getExternalEvent(),false, notice2.getWorkshopType());

        verify(productQuotationRepositoryPort).updateSendValuationQuotation(anyList(), eq(true));
        verify(productQuotationRepositoryPort, never()).updateSendValuationPurchase(anyList(), eq(true));
        verify(alertPiecesValuationPort).serviceAlertPiecesValuation(any(NoticeValuationDTO.class));
        verify(optionQuotePort, never()).findPiecesValuationMultibrandAccepted(anyInt());
        verify(productQuotationRepositoryPort).findPiecesValuationMultibrandBought(anyInt());
        verify(optionQuotePort, times(1)).findPiecesValuationMultibrandQuoted(anyInt());

    }

    @Test
    void testAlertPiecesValuationWorkshopTypeMultibrandAuthFalse_isEmpty() throws ExceptionUtil {
        List<PiecesValuationDTO> piecesValuationEmpty = Collections.emptyList();

        Mockito.when(optionQuotePort.findPiecesValuationMultibrandQuoted(notice2.getExternalEvent())).thenReturn(piecesValuationEmpty);

        alertPiecesValuation.alertPiecesValuation(notice2.getExternalEvent(),false, notice2.getWorkshopType());

        verify(productQuotationRepositoryPort, never()).updateSendValuationQuotation(anyList(), eq(true));
        verify(productQuotationRepositoryPort, never()).updateSendValuationPurchase(anyList(), eq(true));
        verify(alertPiecesValuationPort, never()).serviceAlertPiecesValuation(any(NoticeValuationDTO.class));
        verify(optionQuotePort, never()).findPiecesValuationMultibrandAccepted(anyInt());
        verify(productQuotationRepositoryPort).findPiecesValuationMultibrandBought(anyInt());
        verify(optionQuotePort, times(1)).findPiecesValuationMultibrandQuoted(anyInt());

    }
}
