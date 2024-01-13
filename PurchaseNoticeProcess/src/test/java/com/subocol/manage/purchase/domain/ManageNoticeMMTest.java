package com.subocol.manage.purchase.domain;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.persistence.InsurerRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.ManualPurchaseRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.NoticeRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.ProductQuotationRepositoryPort;
import com.subocol.manage.purchase.domain.services.CreateOrder;
import com.subocol.manage.purchase.domain.services.ValidateProductFinishTimer;
import com.subocol.manage.purchase.domain.servicesimpl.ManageNoticeMM;
import com.subocol.manage.purchase.domain.servicesimpl.SendManualPurchaseImpl;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.Integrations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.*;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_MANAGE_NOTICE;
import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.INSURER_NOT_FOUND;
import static com.subocol.manage.purchase.domain.constant.ManageNoticeConstant.TYPE_AUTOMATIC;
import static com.subocol.manage.purchase.domain.constant.ManageNoticeConstant.TYPE_MANUAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageNoticeMMTest {
    @Mock
    private ValidateProductFinishTimer validateProductFinishTimer;
    @Mock
    private CreateOrder createOrder;
    @Mock
    private NoticeRepositoryPort noticeRepositoryPort;
    @Mock
    private ProductQuotationRepositoryPort productQuotationRepositoryPort;
    @Mock
    private ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;
    @Mock
    private InsurerRepositoryPort insurerRepositoryPort;
    @Mock
    private SendManualPurchaseImpl sendManualPurchase;
    @Mock
    private Integrations integrations;

    @InjectMocks
    private ManageNoticeMM manageNoticeMM;
    Notice notice;
    Long noticeId=454L;
    Insurer insurer;

    ProductQuotation product1;
    ProductQuotation product2;
    ProductQuotation product3;
    ProductOrder productOrder1;
    ProductOrder productOrder2;
    Order order1;
    Order order2;

    @BeforeEach
    void setup(){
        notice= Notice.builder().id(noticeId).externalEvent(123).build();
        insurer= Insurer.builder().totManual(true).newSuggestedReferenceParameter(true).cantRefToShow(2).build();

        product1 = ProductQuotation.builder().id(514L).auth(true).maxCostPiece(false).maxDeliveryDays(false).userName("duvan").winner(true).build();
        product2 = ProductQuotation.builder().id(9643L).auth(false).maxCostPiece(false).maxDeliveryDays(false).userName("duvan").winner(true).build();
        product3 = ProductQuotation.builder().id(6423L).auth(true).maxCostPiece(false).maxDeliveryDays(false).userName("duvan").winner(true).build();

        productOrder1 = ProductOrder.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(TimeZoneUtil.getTimestampByDefaultZone())
                .comment("ProductComment").positionPiece(1).status("Accepted").build();

        order1 = Order.builder().id(456L).date(TimeZoneUtil.getTimestampByDefaultZone()).reference("oiuyiuytuy").products(new HashSet<>(Set.of(productOrder1))).build();
        productOrder2 = ProductOrder.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(TimeZoneUtil.getTimestampByDefaultZone())
                .comment("ProductComment").positionPiece(1).status("Accepted").build();
        order2 = Order.builder().id(457L).date(TimeZoneUtil.getTimestampByDefaultZone()).reference("asdasda").products(new HashSet<>(Set.of(productOrder2))).build();

    }

    @Test
    void testManageNoticeByNoticeId_Auth() throws ExceptionUtil {
        // Given-preconditions

        notice.setAuth(true);

        when(noticeRepositoryPort.findById(noticeId)).thenReturn(Optional.of(notice));

        when(validateProductFinishTimer.findProductsQuotationByPriorityOne(anyLong(), anyString())).thenReturn(List.of(product1, product2));

        when(createOrder.createOrderQuotation(any(Notice.class), anyList(), anyInt())).thenReturn(new HashSet<>(Set.of(order1))).thenReturn(new HashSet<>(Set.of(order2)));
        when(productQuotationRepositoryPort.findAllWinnersByNotice(anyLong())).thenReturn(List.of(product3));
        when(productQuotationRepositoryPort.findAllWinnersByNotice(anyLong())).thenReturn(List.of(product3));
        when(insurerRepositoryPort.findByInsurerId(notice.getInsuranceNumber())).thenReturn(Optional.of(insurer));

        // When-Action to do
        boolean result = manageNoticeMM.manageNoticeByNoticeId(noticeId, true);

        // Then-validations
        assertTrue(result);
        verify(noticeRepositoryPort).findById(anyLong());
        verify(validateProductFinishTimer).updateActiveProductQuotation(notice.getId());
        verify(validateProductFinishTimer).findProductsQuotationByPriorityOne(anyLong(), anyString());
        verify(validateProductFinishTimer).manageStatusProductQuotation(anyLong(), anyString(), anyList(), anyBoolean());
        verify(validateProductFinishTimer).manageStatusQuotation(anyLong());
        verify(createOrder).createOrderQuotation(any(Notice.class), anyList(), eq(TYPE_AUTOMATIC));
        verify(createOrder).createOrderQuotation(any(Notice.class), anyList(), eq(TYPE_MANUAL));
        verify(productQuotationRepositoryPort).updatePurchaseProductQuotation(anyList(), eq(true));
        verify(productQuotationRepositoryPort).findAllWinnersByNotice(notice.getId());
        verify(manualPurchaseRepositoryPort).updatePurchaseSubsidiary(eq(notice.getExternalEvent().toString()), anyList(), eq(true));
    }
    @Test
    void testManageNoticeByNoticeId_NotAuth() throws ExceptionUtil {
        // Given-preconditions

        notice.setAuth(false);

        when(noticeRepositoryPort.findById(noticeId)).thenReturn(Optional.of(notice));

        when(insurerRepositoryPort.findByInsurerId(notice.getInsuranceNumber())).thenReturn(Optional.of(insurer));

        // When-Action to do
        boolean result = manageNoticeMM.manageNoticeByNoticeId(noticeId, true);

        // Then-validations
        assertTrue(result);
        verify(noticeRepositoryPort).findById(anyLong());
        verify(validateProductFinishTimer).updateActiveProductQuotation(notice.getId());
        verify(validateProductFinishTimer).manageStatusProductQuotation(anyLong(), anyString(), isNull(), eq(false));
        verify(validateProductFinishTimer).manageStatusQuotation(anyLong());

        verify(createOrder, never()).createOrderQuotation(any(Notice.class), anyList(), anyInt());
        verify(productQuotationRepositoryPort, never()).updatePurchaseProductQuotation(anyList(), anyBoolean());
        verify(manualPurchaseRepositoryPort, never()).updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean());
    }

    @Test
    void testManageNoticeByNoticeId_CouldNotFoundNotice() throws ExceptionUtil {
        // Given-preconditions
        when(noticeRepositoryPort.findById(noticeId)).thenReturn(Optional.empty());

        // When-Action to do
        ExceptionUtil exceptionUtil = assertThrows(ExceptionUtil.class, () -> manageNoticeMM.manageNoticeByNoticeId(noticeId, true));

        // Then-validations
        assertEquals(HttpStatus.BAD_REQUEST.value(), exceptionUtil.getCode());
        assertEquals(ERROR_MANAGE_NOTICE + noticeId, exceptionUtil.getMessage());
        verify(noticeRepositoryPort).findById(anyLong());
        verify(validateProductFinishTimer, never()).updateActiveProductQuotation(anyLong());
    }

    @Test
    void testManageNoticeByNoticeId_InsurerNotFound() throws ExceptionUtil {
        // Given-preconditions

        notice.setAuth(false);
        long insurerNumber=1234L;
        notice.setInsuranceNumber(insurerNumber);
        when(noticeRepositoryPort.findById(noticeId)).thenReturn(Optional.of(notice));

        when(insurerRepositoryPort.findByInsurerId(notice.getInsuranceNumber())).thenReturn(Optional.empty());

        // When-Action to do
        ExceptionUtil result = assertThrows(ExceptionUtil.class, ()-> manageNoticeMM.manageNoticeByNoticeId(noticeId, true));

        // Then-validations
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getCode());
        assertEquals(INSURER_NOT_FOUND + notice.getInsuranceNumber(), result.getEx());
        assertEquals(ERROR_MANAGE_NOTICE + notice.getId(), result.getMessage());
    }
}
