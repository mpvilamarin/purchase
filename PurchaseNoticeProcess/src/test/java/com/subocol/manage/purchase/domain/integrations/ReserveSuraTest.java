package com.subocol.manage.purchase.domain.integrations;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.Insurer;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.models.SendReserveManage;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.ReserveSura;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.ReserveSuraAdapter;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReserveSuraTest {

    @Mock
    private ProductOrdersPiecesNoticeRepositoryPort productOrdersPiecesNoticeRepositoryPort;
    @Mock
    private PieceRepositoryPort pieceRepositoryPort;
    @Mock
    private TimeZoneUtil timeZoneUtil;
    @Mock
    private ProductOrderRepositoryPort productOrderRepositoryPort;
    @Mock
    private SendReserveManageRepositoryPort sendReserveManageRepositoryPort;
    @Mock
    private NoticeRepositoryPort noticeRepositoryPort;
    @Mock
    private InsurerRepositoryPort insurerRepositoryPort;
    @Mock
    private ReserveSuraAdapter reserveSuraAdapter;
    @InjectMocks
    private ReserveSura reserveSura;

    Notice notice;
    Insurer insurer;
    Insurer insurer2;
    SendReserveManage sendReserveManage;
    ReserveCalculationTotalSuraDTO reserveCalculationTotalSuraDTO;
    List<ReserveRepuestosSuraDTO> reserveRepuestosSuraDTOS=new ArrayList<>();
    @BeforeEach
    void setup() {

        notice = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(123)
                .id(789L).eventId(456L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000001L).build();

        insurer = Insurer.builder().totManual(true).newSuggestedReferenceParameter(true).cantRefToShow(2)
                .flowReserveBolivar(false).flowReserveSura(true).build();

        insurer2 = Insurer.builder().totManual(true).newSuggestedReferenceParameter(true).cantRefToShow(2)
                .flowReserveBolivar(false).build();

        sendReserveManage = SendReserveManage.builder().id(5415L).externalEvent(97345).initCarSended(true).date(new Timestamp(1664579257)).build();
        reserveCalculationTotalSuraDTO = new ReserveCalculationTotalSuraDTO(100.0, 110.0, 10.0);

        reserveRepuestosSuraDTOS.add(new ReserveRepuestosSuraDTO(
                "123", 1, "Pieza1", "REF1", 10, 50.0, 5.0, 10.0, "ORIGEN1", true, 15.0, "PROV1", 10D
        ));
        reserveRepuestosSuraDTOS.add(new ReserveRepuestosSuraDTO(
                "456", 2, "Pieza2", "REF2", 20, 30.0, 3.0, 5.0, "ORIGEN2", false, 10.0, "PROV2", 10D
        ));
    }

    @Test
    void testSendPiecesReserveSura_sendReserveManageIsPresent() {

        Long rowUpdate = 4L;
        List<Integer> positions = Arrays.asList(1, 2, 3);
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(rowUpdate);
        when(pieceRepositoryPort.findInitialPiecesByExternalEventSuraConditionFalse(anyInt(), anyBoolean())).thenReturn(positions);
        when(sendReserveManageRepositoryPort.findByExternalEvent(anyInt())).thenReturn(Optional.of(sendReserveManage));
        when(productOrdersPiecesNoticeRepositoryPort.totalGrossPriceOrdersByExternalEventAndEventId(
                anyInt(),  anyList(), anyBoolean())).thenReturn(reserveCalculationTotalSuraDTO);
        when(productOrdersPiecesNoticeRepositoryPort.findPiecesOrdersByExternalEvent(
                anyInt(), anyList(), anyList())).thenReturn(reserveRepuestosSuraDTOS);

        boolean result = reserveSura.sendPiecesReserveSura(insurer, notice);

        Assertions.assertTrue(result);
        verify(noticeRepositoryPort, times(2))
                .countAllByExternalEventAndUnforeseen(anyInt(),anyBoolean());
        verify(pieceRepositoryPort, times(2))
                .findInitialPiecesByExternalEventSuraConditionFalse(anyInt(),anyBoolean());
        verify(sendReserveManageRepositoryPort, times(1))
                .findByExternalEvent(anyInt());
        verify(productOrdersPiecesNoticeRepositoryPort, times(2))
                .totalGrossPriceOrdersByExternalEventAndEventId(anyInt(),anyList(),anyBoolean());
        verify(productOrdersPiecesNoticeRepositoryPort, times(1))
                .findPiecesOrdersByExternalEvent(anyInt(), anyList(), anyList());
    }

    @Test
    void testSendPurchaseTotalReserve_sendReserveManageNotPresent() {

        Long rowUpdate = 4L;
        List<Integer> positions = Arrays.asList(1, 2, 3);

        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(rowUpdate);
        when(pieceRepositoryPort.findInitialPiecesByExternalEventSuraConditionFalse(anyInt(), anyBoolean())).thenReturn(positions);
        when(sendReserveManageRepositoryPort.findByExternalEvent(anyInt())).thenReturn(Optional.empty());

        when(productOrderRepositoryPort.countProductOrderByExternalEventAndPosition(anyInt(), anyList())).thenReturn(rowUpdate);
        boolean result = reserveSura.sendPiecesReserveSura(insurer, notice);

        Assertions.assertFalse(result);

        verify(noticeRepositoryPort, times(3))
                .countAllByExternalEventAndUnforeseen(anyInt(),anyBoolean());
        verify(pieceRepositoryPort, times(3))
                .findInitialPiecesByExternalEventSuraConditionFalse(anyInt(),anyBoolean());
        verify(sendReserveManageRepositoryPort, times(1))
                .findByExternalEvent(anyInt());
        verify(productOrderRepositoryPort, times(1))
                .countProductOrderByExternalEventAndPosition(anyInt(),anyList());

    }

@Test
void testSendPiecesReserveSura_withException() throws Exception{
    // Given-preconditions
    Long rowUpdate = 4L;
    List<Integer> positions = Arrays.asList(1, 2, 3);
    String messageException="this is a exception!";
    when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(rowUpdate);
    when(pieceRepositoryPort.findInitialPiecesByExternalEventSuraConditionFalse(anyInt(), anyBoolean())).thenReturn(positions);
    when(sendReserveManageRepositoryPort.findByExternalEvent(anyInt())).thenThrow(new RuntimeException(messageException));

    // When-Action to do
    Boolean result= reserveSura.sendPiecesReserveSura(insurer, notice);

    // then-validations
    assertFalse(result);
//    assertEquals(messageException, exceptionUtil.getEx());
//    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
//    assertEquals(ErrorMessageHandler.ERROR_SEND_RESERVE_SURA+ notice.getId(), exceptionUtil.getMessage());
}
    @Test
    void testValidateInitPositionsSura_withException() throws Exception{
        // Given-preconditions
        Long rowUpdate = 1L;
        List<Integer> positions = Arrays.asList(1, 2, 3);
        String messageException="this is a exception!";
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(rowUpdate);
        when(pieceRepositoryPort.findInitialPiecesByExternalEventSuraConditionTrue(anyInt(), anyBoolean())).thenThrow(new RuntimeException(messageException));

        // When-Action to do
        List<Integer> result=
                reserveSura.validateInitPositionsSura(notice.getExternalEvent(), true);

        // then-validations
        assertNull(result);
//        assertEquals(messageException, exceptionUtil.getEx());
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
//        assertEquals(ErrorMessageHandler.ERROR_VALIDATE_POSITION_SURA+ notice.getExternalEvent(), exceptionUtil.getMessage());
    }
    @Test
    void testCalculateValuesReserveSura_withException() throws Exception{
        // Given-preconditions

        String messageException="this is a exception!";
        when(productOrdersPiecesNoticeRepositoryPort.findPiecesOrdersByExternalEvent(anyInt(), anyList(), anyList())).thenThrow(new RuntimeException(messageException));

        // When-Action to do
        ReserveCalculationSuraDTO result=
                reserveSura.calculateValuesReserveSura(notice, List.of(), List.of());

        // then-validations
        assertNull(result);
//        assertEquals(messageException, exceptionUtil.getEx());
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
//        assertEquals(ErrorMessageHandler.ERROR_CALCULATING_VALUES_SURA+ notice.getId(), exceptionUtil.getMessage());
    }
    @Test
    void testValidateInitPositionsWithoutDesistSura_withException() throws Exception{
        // Given-preconditions
        Long rowUpdate = 1L;
        String messageException="this is a exception!";
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(rowUpdate);
        when(pieceRepositoryPort.findInitialPiecesByExternalEventSuraConditionTrue(anyInt(), anyBoolean())).thenThrow(new RuntimeException(messageException));

        // When-Action to do
        List<Integer> result=
                reserveSura.validateInitPositionsWithoutDesistSura(notice.getExternalEvent(), true);

        // then-validations
        assertNull(result);
//        assertEquals(messageException, exceptionUtil.getEx());
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
//        assertEquals(ErrorMessageHandler.ERROR_VALIDATE_POSITION_WITH_DESIST_SURA+ notice.getExternalEvent(), exceptionUtil.getMessage());
    }
    @Test
    void testCreateSendReserveManage_withException() throws Exception{
        // Given-preconditions

        String messageException="this is a exception!";
        when(sendReserveManageRepositoryPort.save(any(SendReserveManage.class))).thenThrow(new RuntimeException(messageException));

        // When-Action to do
        SendReserveManage result =reserveSura.createSendReserveManage(notice.getExternalEvent());

        // then-validations
        assertNull(result);
//        assertEquals(messageException, exceptionUtil.getEx());
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
//        assertEquals(ErrorMessageHandler.ERROR_CREATE_SEND_RESERVE_MANAGE+ notice.getExternalEvent(), exceptionUtil.getMessage());
    }

}
