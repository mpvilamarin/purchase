package com.subocol.manage.purchase.domain.integrations;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.Insurer;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.models.SendReserveManage;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationBolivarDTO;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.ReserveBolivar;
import com.subocol.manage.purchase.infrastructure.externalservices.adapters.ReserveBolivarAdapter;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReserveBolivarTest {

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
    private ReserveBolivarAdapter reserveBolivarAdapter;
    @InjectMocks
    private ReserveBolivar reserveBolivar;

    Notice notice;
    Insurer insurer;
    Insurer insurer2;
    SendReserveManage sendReserveManage;


    @BeforeEach
    void setup() {

        notice = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(123)
                .id(789L).eventId(456L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000001L).build();

        insurer = Insurer.builder().totManual(true).newSuggestedReferenceParameter(true).cantRefToShow(2)
                .flowReserveBolivar(true).build();

        insurer2 = Insurer.builder().totManual(true).newSuggestedReferenceParameter(true).cantRefToShow(2)
                .flowReserveBolivar(false).build();

        sendReserveManage = SendReserveManage.builder().id(5415L).externalEvent(97345).initCarSended(true).date(new Timestamp(1664579257)).build();
    }

    @Test
    void testSendPurchaseTotalReserve_sendReserveManageIsPresent() {

        Long rowUpdate = 4L;
        List<Integer> positions = Arrays.asList(1, 2, 3);
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(rowUpdate);
        when(pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(), anyBoolean())).thenReturn(positions);
        when(sendReserveManageRepositoryPort.findByExternalEvent(anyInt())).thenReturn(Optional.of(sendReserveManage));
        when(productOrdersPiecesNoticeRepositoryPort.totalPriceOrdersByExternalEventAndEventId(anyInt(),anyBoolean(),anyList(),anyBoolean())).thenReturn(100.0);

        boolean result = reserveBolivar.sendPurchaseTotalReserve(notice);

        Assertions.assertTrue(result);

        verify(insurerRepositoryPort, times(1))
                .findByInsurerId(anyLong());
        verify(noticeRepositoryPort, times(2))
                .countAllByExternalEventAndUnforeseen(anyInt(),anyBoolean());
        verify(pieceRepositoryPort, times(2))
                .findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(),anyBoolean());
        verify(pieceRepositoryPort, times(2))
                .findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(),anyBoolean());
        verify(sendReserveManageRepositoryPort, times(1))
                .findByExternalEvent(anyInt());
        verify(productOrdersPiecesNoticeRepositoryPort, times(4))
                .totalPriceOrdersByExternalEventAndEventId(anyInt(),anyBoolean(),anyList(),anyBoolean());

    }

    @Test
    void testSendPurchaseTotalReserve_sendReserveManageNotPresent() {

        Long rowUpdate = 4L;
        List<Integer> positions = Arrays.asList(1, 2, 3);
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(rowUpdate);
        when(pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(), anyBoolean())).thenReturn(positions);
        when(sendReserveManageRepositoryPort.findByExternalEvent(anyInt())).thenReturn(Optional.empty());
        when(productOrderRepositoryPort.countProductOrderByExternalEventAndPosition(anyInt(),anyList())).thenReturn(2L);

        boolean result = reserveBolivar.sendPurchaseTotalReserve(notice);

        Assertions.assertFalse(result);

        verify(insurerRepositoryPort, times(1))
                .findByInsurerId(anyLong());
        verify(noticeRepositoryPort, times(3))
                .countAllByExternalEventAndUnforeseen(anyInt(),anyBoolean());
        verify(pieceRepositoryPort, times(3))
                .findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(),anyBoolean());
        verify(sendReserveManageRepositoryPort, times(1))
                .findByExternalEvent(anyInt());
        verify(productOrderRepositoryPort, times(1))
                .countProductOrderByExternalEventAndPosition(anyInt(),anyList());

    }

    @Test
    void testSendPurchaseTotalReserve_sendReserveManageNotPresentAndCreateSendReserveManage() {

        Long rowUpdate = 4L;
        List<Integer> positions = Arrays.asList(1, 2, 3);
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(rowUpdate);
        when(sendReserveManageRepositoryPort.findByExternalEvent(anyInt())).thenReturn(Optional.empty());
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(),anyBoolean())).thenReturn(1L);


        boolean result = reserveBolivar.sendPurchaseTotalReserve(notice);

        Assertions.assertTrue(result);

        verify(insurerRepositoryPort, times(1))
                .findByInsurerId(anyLong());
        verify(noticeRepositoryPort, times(3))
                .countAllByExternalEventAndUnforeseen(anyInt(),anyBoolean());
        verify(pieceRepositoryPort, times(3))
                .findInitialPiecesByExternalEventBolivarConditionTrue(anyInt(),anyBoolean());
        verify(sendReserveManageRepositoryPort, times(1))
                .findByExternalEvent(anyInt());
        verify(sendReserveManageRepositoryPort, times(1))
                .save(any(SendReserveManage.class));

    }

    @Test
    void testSendPurchaseTotalReserve_ValidateInitPositionsEmptyList() {

        Long rowUpdate = 4L;
        List<Integer> positions = Arrays.asList(1, 2, 3);
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(rowUpdate);
        when(pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(), anyBoolean())).thenReturn(Collections.emptyList());
        when(sendReserveManageRepositoryPort.findByExternalEvent(anyInt())).thenReturn(Optional.empty());
//        when(pieceRepositoryPort.findInitialPiecesByExternalWithOrders(anyInt(),anyBoolean())).thenReturn(List.of(1));


        boolean result = reserveBolivar.sendPurchaseTotalReserve(notice);

        Assertions.assertTrue(result);

        verify(insurerRepositoryPort, times(1))
                .findByInsurerId(anyLong());
        verify(noticeRepositoryPort, times(3))
                .countAllByExternalEventAndUnforeseen(anyInt(),anyBoolean());
        verify(pieceRepositoryPort, times(3))
                .findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(),anyBoolean());
        verify(sendReserveManageRepositoryPort, times(1))
                .findByExternalEvent(anyInt());


    }

    @Test
    void testSendPurchaseTotalReserve_ExceptionInTryBlock() {
        when(insurerRepositoryPort.findByInsurerId(anyLong()))
                .thenThrow(new RuntimeException("Simulated exception"));

        boolean result=
            reserveBolivar.sendPurchaseTotalReserve(notice);

        assertFalse(result);

//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getCode());
//        assertEquals(ERROR_SEND_RESERVE_BOLIVAR + notice.getId(), exception.getMessage());

        verify(insurerRepositoryPort, times(1)).findByInsurerId(anyLong());
        verify(noticeRepositoryPort, never())
                .countAllByExternalEventAndUnforeseen(anyInt(),anyBoolean());
        verify(pieceRepositoryPort, never())
                .findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(),anyBoolean());
        verify(sendReserveManageRepositoryPort, never())
                .findByExternalEvent(anyInt());
    }

    @Test
    void testValidateInitPositions_ExceptionInTryBlock() {

        Integer externalEvent = 515412;

        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean()))
                .thenThrow(new RuntimeException("Simulated exception"));

       List<Integer> result=
            reserveBolivar.validateInitPositions(externalEvent, true);

       assertEquals(List.of(), result);

//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getCode());
//        assertEquals(ERROR_VALIDATE_POSITION_BOLIVAR + externalEvent, exception.getMessage());

        verify(noticeRepositoryPort, times(1)).countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean());
        verify(sendReserveManageRepositoryPort, never()).findByExternalEvent(anyInt());
        verify(productOrdersPiecesNoticeRepositoryPort, never()).totalPriceOrdersByExternalEventAndEventId(anyInt(),anyBoolean(),anyList(),anyBoolean());
    }

    @Test
    void testCalculateValuesReserve_ExceptionInTryBlock() {

        List<Integer> positionInit = Arrays.asList(1, 2, 3);
        List<Integer> positionsUnforeseen = Arrays.asList(4, 5, 6);

        when(productOrdersPiecesNoticeRepositoryPort.totalPriceOrdersByExternalEventAndEventId(anyInt(), anyBoolean(), anyList(), anyBoolean()))
                .thenThrow(new RuntimeException("Simulated exception"));


        ReserveCalculationBolivarDTO result=
            reserveBolivar.calculateValuesReserve(notice, positionInit, positionsUnforeseen);

        assertNull(result);
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getCode());
//        assertEquals(ERROR_CALCULATING_VALUES_BOLIVAR + notice.getId(), exception.getMessage());

        verify(productOrdersPiecesNoticeRepositoryPort, times(1))
                .totalPriceOrdersByExternalEventAndEventId(anyInt(), anyBoolean(), anyList(), anyBoolean());
        verify(pieceRepositoryPort, never())
                .findInitialPiecesByExternalWithOrders(anyInt(), anyBoolean());
        verify(sendReserveManageRepositoryPort, never())
                .save(any(SendReserveManage.class));

    }

    @Test
    void testValidateInitPositionsWithoutDesist_ExceptionInTryBlock() {
        Integer externalEvent = 515412;

        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean()))
                .thenThrow(new RuntimeException("Simulated exception"));

        List<Integer> result=
            reserveBolivar.validateInitPositionsWithoutDesist(externalEvent, true);

        assertEquals(List.of(), result);
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getCode());
//        assertEquals(ERROR_VALIDATE_POSITION_BOLIVAR_WITH_DESIST + externalEvent, exception.getMessage());

        verify(noticeRepositoryPort, times(1)).countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean());
        verify(sendReserveManageRepositoryPort, never())
                .save(any(SendReserveManage.class));

    }


    @Test
    void testCreateSendReserveManage_ExceptionInTryBlock() {
        Integer externalEvent = 515412;

        when(sendReserveManageRepositoryPort.save(any(SendReserveManage.class)))
                .thenAnswer(invocation -> {
                    throw new RuntimeException("Simulated exception");
                });

        SendReserveManage result =
            reserveBolivar.createSendReserveManage(externalEvent);

        assertNull(result);

//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getCode());
//        assertEquals(ERROR_CREATE_SEND_RESERVE_MANAGE , exception.getMessage());

        verify(sendReserveManageRepositoryPort, times(1)).save(any(SendReserveManage.class));

    }

    @Test
    void testSendPurchaseTotalReserve_insurerSura() {

        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer2));

        boolean result = reserveBolivar.sendPurchaseTotalReserve(notice);

        Assertions.assertFalse(result);

        verify(insurerRepositoryPort, times(1))
                .findByInsurerId(anyLong());
        verify(noticeRepositoryPort, never())
                .countAllByExternalEventAndUnforeseen(anyInt(),anyBoolean());
        verify(pieceRepositoryPort, never())
                .findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(),anyBoolean());
        verify(sendReserveManageRepositoryPort, never())
                .findByExternalEvent(anyInt());
        verify(productOrdersPiecesNoticeRepositoryPort, never())
                .totalPriceOrdersByExternalEventAndEventId(anyInt(),anyBoolean(),anyList(),anyBoolean());

    }

    @Test
    void testSendPurchaseTotalReserve_sendReserveManageIsPresent2() {

        Long rowUpdate = 4L;
        List<Integer> positions = Arrays.asList(1, 2, 3);
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(rowUpdate);
        when(pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(), anyBoolean())).thenReturn(positions);
        when(sendReserveManageRepositoryPort.findByExternalEvent(anyInt())).thenReturn(Optional.of(sendReserveManage));
        when(productOrdersPiecesNoticeRepositoryPort.totalPriceOrdersByExternalEventAndEventId(anyInt(),anyBoolean(),anyList(),anyBoolean())).thenReturn(100.0);

        boolean result = reserveBolivar.sendPurchaseTotalReserve(notice);

        Assertions.assertTrue(result);

        verify(insurerRepositoryPort, times(1))
                .findByInsurerId(anyLong());
        verify(noticeRepositoryPort, times(2))
                .countAllByExternalEventAndUnforeseen(anyInt(),anyBoolean());
        verify(pieceRepositoryPort, times(2))
                .findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(),anyBoolean());
        verify(sendReserveManageRepositoryPort, times(1))
                .findByExternalEvent(anyInt());
        verify(productOrdersPiecesNoticeRepositoryPort, times(4))
                .totalPriceOrdersByExternalEventAndEventId(anyInt(),anyBoolean(),anyList(),anyBoolean());

    }

}
