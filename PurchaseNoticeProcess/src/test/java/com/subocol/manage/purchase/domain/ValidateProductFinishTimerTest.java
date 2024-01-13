package com.subocol.manage.purchase.domain;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterStatusProductQuotation;
import com.subocol.manage.purchase.domain.models.ProductQuotation;
import com.subocol.manage.purchase.domain.ports.persistence.ProductQuotationRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.QuotationRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.ValidateProductFinishTimerImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_CHANGE_STATUS;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateProductFinishTimerTest{
    @Mock
    private ProductQuotationRepositoryPort productQuotationRepositoryPort;
    @Mock
    private QuotationRepositoryPort quotationRepositoryPort;
    @InjectMocks
    private ValidateProductFinishTimerImpl validateProductFinishTimerImpl;



    @Test
    @DisplayName("Test updateActiveProductQuotationTest")
    void updateActiveProductQuotationTest(){
        //Given-preconditions
        given(productQuotationRepositoryPort.updateActiveProductQuotation(anyLong(), anyBoolean())).willReturn(1);
        //when-action to do
        int test = validateProductFinishTimerImpl.updateActiveProductQuotation(45415L);
        //then-verify result
        assertThat(test).isPositive();

        verify(productQuotationRepositoryPort,times(1)).updateActiveProductQuotation(anyLong(),anyBoolean());
    }

    @Test
    @DisplayName("Test findProducsQuotationByPriorityOneTest")
    void findProducsQuotationByPriorityOneTest(){
        //Given-preconditions
        ProductQuotation product1 = ProductQuotation.builder().id(514L).auth(true).maxCostPiece(false).maxDeliveryDays(false).userName("duvan").winner(true).build();
        ProductQuotation product2 = ProductQuotation.builder().id(9643L).auth(false).maxCostPiece(false).maxDeliveryDays(false).userName("duvan").winner(true).build();
        ProductQuotation product3 = ProductQuotation.builder().id(6423L).auth(true).maxCostPiece(false).maxDeliveryDays(false).userName("duvan").winner(true).build();
        given(productQuotationRepositoryPort.findByEventAndPriorityQuotation(anyLong(),anyString())).willReturn(List.of(product1,product2,product3));
        //when-action to do
        List<ProductQuotation> productQuotation = validateProductFinishTimerImpl.findProductsQuotationByPriorityOne(41564L,"6245441");
        //then-verify result
        verify(productQuotationRepositoryPort,times(1)).findByEventAndPriorityQuotation(anyLong(),anyString());
        assertThat(productQuotation).hasSize(3).hasOnlyElementsOfType(ProductQuotation.class)
        .asList().containsOnlyOnceElementsOf(List.of(product1,product2,product3));

    }

    @Test
    @DisplayName("Test manageStatusProductQuotationTest")
    void manageStatusProductQuotationTest() throws ExceptionUtil {
        Long noticeId = 1L;
        String externalEvent = "event";
        List<Long> productQuotationIds = Collections.singletonList(1L);

        when(productQuotationRepositoryPort.updateStatusManageQuotationPieces(noticeId, externalEvent,
                ManageNoticeConstant.AUTOMATIC, ManageNoticeConstant.SENT,
                ManageNoticeConstant.OMITTED)).thenReturn(1);

        when(productQuotationRepositoryPort.updateStatusManageQuotationPiecesOptionQuote(productQuotationIds, ManageNoticeConstant.ACCEPTED))
                .thenReturn(1);

        validateProductFinishTimerImpl.manageStatusProductQuotation(noticeId, externalEvent, productQuotationIds, true);

        verify(productQuotationRepositoryPort).updateStatusManageQuotationPieces(noticeId, externalEvent,
                ManageNoticeConstant.AUTOMATIC, ManageNoticeConstant.SENT,
                ManageNoticeConstant.OMITTED);
        verify(productQuotationRepositoryPort).updateStatusManageQuotationPiecesOptionQuote(productQuotationIds,
                ManageNoticeConstant.ACCEPTED);
    }

    @Test
    void manageStatusProductQuotationTestWithException() throws ExceptionUtil {
        Long noticeId = 1L;
        String externalEvent = "event";
        List<Long> productQuotationIds = Collections.singletonList(1L);
        String messageException="this is a exception!";

        when(productQuotationRepositoryPort.updateStatusManageQuotationPieces(noticeId, externalEvent,
                ManageNoticeConstant.AUTOMATIC, ManageNoticeConstant.SENT,
                ManageNoticeConstant.OMITTED)).thenThrow(new RuntimeException(messageException));

        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()-> validateProductFinishTimerImpl.manageStatusProductQuotation(noticeId, externalEvent, productQuotationIds, true));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals(messageException, result.getEx());
        assertEquals(ERROR_CHANGE_STATUS+ "ProductQuotation", result.getMessage());
    }

    @Test
    void manageStatusQuotation() throws ExceptionUtil {
        Long noticeId = 1L;

        List<CounterStatusProductQuotation> quotationCountersList = Arrays.asList(
                new CounterStatusProductQuotation(1L, 3L, 2L, 1L, 1L, 2L, 4L),
                new CounterStatusProductQuotation(2L, 3L, 2L, 0L, 1L, 0L, 4L),
                new CounterStatusProductQuotation(3L, 5L, 0L, 0L, 5L, 0L, 0L),
                new CounterStatusProductQuotation(4L, 5L, 1L, 0L, 4L, 0L, 0L)
        );

        when(quotationRepositoryPort.countStatusProductQuotationMM(noticeId)).thenReturn(quotationCountersList);

        validateProductFinishTimerImpl.manageStatusQuotation(noticeId);

        verify(quotationRepositoryPort).updateStatusQuotation(1L, ManageNoticeConstant.QUOTED);
        verify(quotationRepositoryPort, never()).updateStatusQuotation(2L, ManageNoticeConstant.QUOTED);
        verify(quotationRepositoryPort).updateStatusQuotation(3L, ManageNoticeConstant.REJECTED_QUOTED);
        verify(quotationRepositoryPort).updateStatusQuotation(2L, ManageNoticeConstant.OMITTED);
        verify(quotationRepositoryPort).updateStatusQuotation(4L, ManageNoticeConstant.REJECTED_QUOTED);
    }

    @Test
    void manageStatusQuotationWithException() throws ExceptionUtil {
        Long noticeId = 1L;

        String messageException="this is a exception!";

        when(quotationRepositoryPort.countStatusProductQuotationMM(noticeId))
                .thenThrow(new RuntimeException(messageException));

        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()->validateProductFinishTimerImpl.manageStatusQuotation(noticeId));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals(messageException, result.getEx());
        assertEquals(ERROR_CHANGE_STATUS+ "quotation", result.getMessage());
    }

}
