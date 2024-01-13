package com.subocol.manage.purchase.domain;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.enums.CauseManualPurchase;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.persistence.ManualPurchaseAdiRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.ManualPurchaseRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.ProductQuotationRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.SuggestedReferenceRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.SendManualPurchaseImpl;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterProductQuotation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_CHANGE_STATUS;
import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_SET_MANUAL_PURCHASE;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendManualPurchaseTest {

    @Mock
    private ManualPurchaseAdiRepositoryPort manualPurchaseAdiRepositoryPort;
    @Mock
    private ProductQuotationRepositoryPort productQuotationRepositoryPort;
    @Mock
    private ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;

    @Mock
    SuggestedReferenceRepositoryPort suggestedReferenceRepositoryPort;
    @InjectMocks
    private SendManualPurchaseImpl sendManualPurchase;

    ManualPurchaseAdi manualPurchaseAdi1;
    ManualPurchaseAdi manualPurchaseAdi2;
    ManualPurchaseAdi manualPurchaseAdi3;
    ManualPurchaseAdi manualPurchaseAdi4;
    CounterProductQuotation counterProductQuotation1;
    CounterProductQuotation counterProductQuotation2;
    CounterProductQuotation counterProductQuotation3;
    CounterProductQuotation counterProductQuotation4;
    List<String> suggestedReference;
    Notice notice;
    Notice noticeNotAuth;
    Insurer insurer;
    Long eventId = 456L;
    Long externalEvent = 123L;
    ManualPurchase manualPurchase;
    @BeforeEach
    void setup() {
        manualPurchaseAdi1 = ManualPurchaseAdi.builder().eventId(eventId)
                .position(1).brand("FORD").homologatedName("parabrisas delantero").quality("original")
                .manualPurchase(false).pieces("parabrisas delantero").build();

        manualPurchaseAdi2 = ManualPurchaseAdi.builder().eventId(eventId)
                .position(2).brand("FORD").homologatedName("bujia").quality("original")
                .manualPurchase(false).pieces("bujia").build();

        manualPurchaseAdi3 = ManualPurchaseAdi.builder().eventId(eventId)
                .position(3).brand("FORD").homologatedName("farola delantera izquierda").quality("original")
                .manualPurchase(false).pieces("farola delantera izquierda").build();

        manualPurchaseAdi4 = ManualPurchaseAdi.builder().eventId(eventId)
                .position(4).brand("FORD").homologatedName("puerta izquierda").quality("original")
                .manualPurchase(false).pieces("puerta izquierda").build();

        counterProductQuotation1 = CounterProductQuotation.builder().position(1L).totalProducts(5L)
                .omittedProducts(5L).alertAndWinnerProducts(0L).extraCost(0L).overTime(0L)
                .rejectedQuotedProducts(0L).maxCostPiece(0L).auth(true).build();
        counterProductQuotation2 = CounterProductQuotation.builder().position(2L).totalProducts(5L)
                .omittedProducts(2L).alertAndWinnerProducts(0L).extraCost(2L).overTime(1L)
                .rejectedQuotedProducts(0L).maxCostPiece(0L).auth(true).build();
        counterProductQuotation3 = CounterProductQuotation.builder().position(3L).totalProducts(5L)
                .omittedProducts(2L).alertAndWinnerProducts(0L).extraCost(0L).overTime(0L)
                .rejectedQuotedProducts(3L).maxCostPiece(0L).auth(true).build();
        counterProductQuotation4 = CounterProductQuotation.builder().position(4L).totalProducts(5L)
                .omittedProducts(2L).alertAndWinnerProducts(1L).extraCost(0L).overTime(0L)
                .rejectedQuotedProducts(1L).maxCostPiece(1L).auth(true).build();

        notice = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(externalEvent.intValue())
                .id(454L).eventId(eventId).build();
        noticeNotAuth = Notice.builder().auth(false).brand("FORD").line("FUSION").externalEvent(externalEvent.intValue())
                .id(454L).eventId(eventId).build();
        insurer = Insurer.builder().totManual(true).newSuggestedReferenceParameter(true).cantRefToShow(2).build();
        suggestedReference =new ArrayList<>(List.of("MMCCMMCC", "1234MMCC"));
        manualPurchase = ManualPurchase.builder().eventId(eventId)
                .position(1).brand("FORD").id(1L).auth(true).description("paragolpes delantero")
                .build();


    }

    @Test
    void testFindManualPurchaseADIToManualProcess_WithAuth() {
        // Given-preconditions
        boolean auth = true;
        Long externalEvent = 123L;
        Long noticeId = 789L;
        boolean totParameter = true;

        List<ManualPurchaseAdi> expectedList = List.of(manualPurchaseAdi1, manualPurchaseAdi2, manualPurchaseAdi3);

        given(manualPurchaseAdiRepositoryPort.findManualPurchaseAdiByEventFilterManualPurchaseForAuth(
                externalEvent, eventId, noticeId, totParameter))
                .willReturn(expectedList);

        // When-Action to do
        List<ManualPurchaseAdi> actualList = sendManualPurchase.findManualPurchaseADIToManualProcess(auth, externalEvent, eventId, noticeId, totParameter);

        // then-validations
        Assertions.assertEquals(expectedList, actualList);
        verify(manualPurchaseAdiRepositoryPort, times(1))
                .findManualPurchaseAdiByEventFilterManualPurchaseForAuth(externalEvent, eventId, noticeId, totParameter);
        verify(manualPurchaseAdiRepositoryPort, never())
                .findManualPurchaseAdiByEventFilterManualPurchase(externalEvent, eventId, totParameter);
    }

    @Test
    void testFindManualPurchaseADIToManualProcess_WithoutAuth() {
        // Given-preconditions
        boolean auth = false;

        boolean totParameter = true;

        List<ManualPurchaseAdi> expectedList = List.of(manualPurchaseAdi1, manualPurchaseAdi2, manualPurchaseAdi3);


        given(manualPurchaseAdiRepositoryPort.findManualPurchaseAdiByEventFilterManualPurchase(
                externalEvent, eventId, totParameter))
                .willReturn(expectedList);

        // When-Action to do
        List<ManualPurchaseAdi> actualList = sendManualPurchase.findManualPurchaseADIToManualProcess(auth, externalEvent, eventId, null, totParameter);

        // then-validations
        Assertions.assertEquals(expectedList, actualList);
        verify(manualPurchaseAdiRepositoryPort, times(1))
                .findManualPurchaseAdiByEventFilterManualPurchase(externalEvent, eventId, totParameter);
        verify(manualPurchaseAdiRepositoryPort, never())
                .findManualPurchaseAdiByEventFilterManualPurchaseForAuth(externalEvent, eventId, null, totParameter);
    }

    @Test
    void testFindManualPurchaseADIToManualProcess_WithException() {
        // Given-preconditions
        String messageException="this is a exception!";
        boolean auth = false;
        boolean totParameter = true;
        Long noticeId=1234L;
        given(manualPurchaseAdiRepositoryPort.findManualPurchaseAdiByEventFilterManualPurchase(
                externalEvent, eventId, totParameter))
                .willThrow(new RuntimeException(messageException));

        // When-Action to do
        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()-> sendManualPurchase.findManualPurchaseADIToManualProcess(auth, externalEvent, eventId, noticeId, totParameter));

        // then-validations
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        Assertions.assertEquals(messageException, result.getEx());
        Assertions.assertEquals( ErrorMessageHandler.ERROR_FIND_MANUAL_PURCHASE_ADI+noticeId, result.getMessage());
    }
    @Test
    void testFindCounterProductQuotationToManualProcess() {

        Long noticeId = 123L;

        List<ManualPurchaseAdi> listProductsInAdiSchema = List.of(manualPurchaseAdi1, manualPurchaseAdi2, manualPurchaseAdi3);

        List<Integer> positions = List.of(1, 2, 3);

        List<CounterProductQuotation> expectedList = List.of(counterProductQuotation1, counterProductQuotation2, counterProductQuotation3);

        given(productQuotationRepositoryPort.findCounterStatusProductQuotationDTOForSubmissionManualPurchase(
                noticeId, positions))
                .willReturn(expectedList);

        // When-Action to do
        List<CounterProductQuotation> actualList = sendManualPurchase.findCounterProductQuotationToManualProcess(noticeId, listProductsInAdiSchema);

        // then-validations
        Assertions.assertEquals(expectedList, actualList);
        verify(productQuotationRepositoryPort, times(1))
                .findCounterStatusProductQuotationDTOForSubmissionManualPurchase(noticeId, positions);
    }

    @Test
    void testFindCounterProductQuotationToManualProcessWithException() {

        String messageException="this is a exception!";
        Long noticeId = 123L;

        List<ManualPurchaseAdi> listProductsInAdiSchema = List.of(manualPurchaseAdi1, manualPurchaseAdi2, manualPurchaseAdi3);

        List<Integer> positions = List.of(1, 2, 3);
        given(productQuotationRepositoryPort.findCounterStatusProductQuotationDTOForSubmissionManualPurchase(
                noticeId, positions))
                .willThrow(new RuntimeException(messageException));

        // When-Action to do
        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()-> sendManualPurchase.findCounterProductQuotationToManualProcess(noticeId, listProductsInAdiSchema));

        // then-validations
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        Assertions.assertEquals(messageException, result.getEx());
        Assertions.assertEquals( ErrorMessageHandler.ERROR_COUNTERS_MANUAL_PURCHASE_PROCESS+noticeId, result.getMessage());
    }

    @Test
    void testSettingManualPurchase_WithCauseOmitted() {
        // Given-preconditions
        given(suggestedReferenceRepositoryPort.findListSuggestedReferenceByEventAndPosition(anyLong(),
                anyInt(), anyInt())).willReturn(suggestedReference);
//        given(manualPurchaseRepositoryPort.save(any(ManualPurchase.class))).willReturn(manualPurchase);
        // When-Action to do
        ManualPurchase manualPurchaseActual= sendManualPurchase.settingManualPurchase(manualPurchaseAdi1, counterProductQuotation1, notice, insurer);

        // Assert
//        verify(manualPurchaseRepositoryPort, times(1)).save(any(ManualPurchase.class));
        verify(suggestedReferenceRepositoryPort, times(1)).findListSuggestedReferenceByEventAndPosition(anyLong(),
                anyInt(), anyInt());

        Assertions.assertEquals(CauseManualPurchase.OMITTED.toString(), manualPurchaseActual.getCause());
        Assertions.assertEquals(suggestedReference.get(0), manualPurchaseActual.getSuggestedReference());
    }

    @Test
    void testSettingManualPurchase_WithCauseExtraCost() {
        // Given-preconditions
        given(suggestedReferenceRepositoryPort.findListSuggestedReferenceByEventAndPosition(anyLong(),
                anyInt(), anyInt())).willReturn(suggestedReference);
//        given(manualPurchaseRepositoryPort.save(any(ManualPurchase.class))).willReturn(manualPurchase);
        // When-Action to do
        ManualPurchase manualPurchaseActual= sendManualPurchase.settingManualPurchase(manualPurchaseAdi2, counterProductQuotation2, notice, insurer);

        // Assert
//        verify(manualPurchaseRepositoryPort, times(1)).save(any(ManualPurchase.class));
        verify(suggestedReferenceRepositoryPort, times(1)).findListSuggestedReferenceByEventAndPosition(anyLong(),
                anyInt(), anyInt());

        Assertions.assertEquals(ManageNoticeConstant.EXTRA_COST, manualPurchaseActual.getCause());
        Assertions.assertEquals(suggestedReference.get(0), manualPurchaseActual.getSuggestedReference());
    }


    @Test
    void testSettingManualPurchase_WithCauseRejectQuoted() {
        // Given-preconditions
        given(suggestedReferenceRepositoryPort.findListSuggestedReferenceByEventAndPosition(anyLong(),
                anyInt(), anyInt())).willReturn(suggestedReference);
//        given(manualPurchaseRepositoryPort.save(any(ManualPurchase.class))).willReturn(manualPurchase);
        // When-Action to do
        ManualPurchase manualPurchaseActual= sendManualPurchase.settingManualPurchase(manualPurchaseAdi3, counterProductQuotation3, notice, insurer);

        // Assert
//        verify(manualPurchaseRepositoryPort, times(1)).save(any(ManualPurchase.class));
        verify(suggestedReferenceRepositoryPort, times(1)).findListSuggestedReferenceByEventAndPosition(anyLong(),
                anyInt(), anyInt());

        Assertions.assertEquals(CauseManualPurchase.REJECTED_QUOTED.toString(), manualPurchaseActual.getCause());
        Assertions.assertEquals(suggestedReference.get(0), manualPurchaseActual.getSuggestedReference());
    }

    @Test
    void testSettingManualPurchase_WithMaxCostPieceAndNotAuth() {
        // Given-preconditions
        // When-Action to do
        ManualPurchase manualPurchaseActual= sendManualPurchase.settingManualPurchase(manualPurchaseAdi4, counterProductQuotation4, noticeNotAuth, insurer);

        // Assert
        verify(manualPurchaseRepositoryPort, never()).save(any(ManualPurchase.class));
        verify(suggestedReferenceRepositoryPort, never()).findListSuggestedReferenceByEventAndPosition(anyLong(),
                anyInt(), anyInt());

        Assertions.assertNull(manualPurchaseActual);

    }

    @Test
    void testSettingManualPurchase_WithException() {
        // Given-preconditions
        String messageException="this is a exception!";
        given(suggestedReferenceRepositoryPort.findListSuggestedReferenceByEventAndPosition(anyLong(),
                anyInt(), anyInt())).willThrow(new RuntimeException(messageException));

        // When-Action to do
        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()->  sendManualPurchase.settingManualPurchase(manualPurchaseAdi1, counterProductQuotation1, notice, insurer));

        // Assert
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        Assertions.assertEquals(messageException, result.getEx());
        Assertions.assertEquals( ErrorMessageHandler.ERROR_SAVE_MANUAL_PURCHASE+notice.getId(), result.getMessage());
    }

    @Test
    void testSetManualPurchasePieces() {

        //Given-Preconditions
        given(manualPurchaseAdiRepositoryPort.findManualPurchaseAdiByEventFilterManualPurchaseForAuth(
                externalEvent, eventId, notice.getId(), insurer.getTotManual()))
                .willReturn(List.of(manualPurchaseAdi1, manualPurchaseAdi2, manualPurchaseAdi3));

        given(productQuotationRepositoryPort.findCounterStatusProductQuotationDTOForSubmissionManualPurchase(
                anyLong(), anyList()))
                .willReturn(List.of(counterProductQuotation1, counterProductQuotation2, counterProductQuotation3));

        given(suggestedReferenceRepositoryPort.findListSuggestedReferenceByEventAndPosition(anyLong(),
                anyInt(), anyInt())).willReturn(suggestedReference);

        given(manualPurchaseRepositoryPort.save(any(ManualPurchase.class))).willReturn(manualPurchase);

        //When-Action to do
        sendManualPurchase.setManualPurchasePieces(notice, insurer);

        //then-validations

        verify(productQuotationRepositoryPort, times(1)).findCounterStatusProductQuotationDTOForSubmissionManualPurchase(anyLong(), anyList());
        verify(suggestedReferenceRepositoryPort, times(3)).findListSuggestedReferenceByEventAndPosition(anyLong(),
                anyInt(), anyInt());
        verify(manualPurchaseRepositoryPort, times(3)).save(any(ManualPurchase.class));

    }

    @Test
    void testSetManualPurchasePieces_withException() {

        //Given-Preconditions
        String messageException="this is a exception!";
        given(manualPurchaseAdiRepositoryPort.findManualPurchaseAdiByEventFilterManualPurchaseForAuth(
                externalEvent, eventId, notice.getId(), insurer.getTotManual()))
                .willThrow(new RuntimeException(messageException));

        //When-Action to do
        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()->  sendManualPurchase.setManualPurchasePieces(notice, insurer));

        //then-validations

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        Assertions.assertEquals(ErrorMessageHandler.ERROR_FIND_MANUAL_PURCHASE_ADI+notice.getId(), result.getEx());
        Assertions.assertEquals( ERROR_SET_MANUAL_PURCHASE+notice.getId(), result.getMessage());

    }
}
