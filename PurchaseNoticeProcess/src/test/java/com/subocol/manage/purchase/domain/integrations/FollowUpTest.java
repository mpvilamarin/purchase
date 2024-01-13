package com.subocol.manage.purchase.domain.integrations;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.externalservices.BillingOrdersPort;
import com.subocol.manage.purchase.domain.ports.externalservices.FollowUpPort;
import com.subocol.manage.purchase.domain.ports.persistence.CurrencyRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.NoticeRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.SellOrderRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.TaxRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SendSparesToFollowUPDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.BillingOrders;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.FollowUp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowUpTest {

    @Mock
    private FollowUpPort followUpPort;
    @Mock
    private NoticeRepositoryPort noticeRepositoryPort;
    @InjectMocks
    private FollowUp followUp;

    Order order;
    Tax tax;
    Currency currency;
    Subsidiary subsidiary;
    Provider provider;
    Notice notice;
    Long eventId = 456L;
    Long externalEvent = 123L;
    ProductOrder productOrder1;
    ProductOrder productOrder2;
    ProductOrder productOrder3;

    SpareDetailToFollowUpDTO spareDetailToFollowUpDTO1;
    SpareDetailToFollowUpDTO spareDetailToFollowUpDTO2;
    SpareDetailToFollowUpDTO spareDetailToFollowUpDTO3;

    @BeforeEach
    void setup() {

        notice = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(externalEvent.intValue())
                .id(454L).eventId(eventId).build();

        subsidiary=Subsidiary.builder().id(100L).email("test@test.com").phone("3131311313")
                .locationExternalId(1L).alias("Taller").classification("Multimarca").build();

        provider=Provider.builder().id(200L).providerClassification("Multimarca").name("provider Test").phone("898989977")
                .nit("897456123").build();

        order = Order.builder().id(456L).date(TimeZoneUtil.getTimestampByDefaultZone()).notice(notice)
                .build();

        productOrder1 = ProductOrder.builder().id(22323L)
                .amount(1).grossPrice(100D).guide("this is a guide")
                .importer(false).comment("this is a commet")
                .price(40D).status(ManageNoticeConstant.SENT)
                .description("bomber delantero").positionPiece(1)
                .quality("ORIGINAL")
                .build();
        productOrder2 = ProductOrder.builder().id(22324L)
                .amount(2).grossPrice(100D).guide("this is a guide")
                .importer(false).comment("this is a commet")
                .price(20D).status(ManageNoticeConstant.SENT)
                .description("espejo retrovisor").positionPiece(2)
                .quality("ORIGINAL")
                .build();
        productOrder3 = ProductOrder.builder().id(22324L)
                .amount(2).grossPrice(100D).guide("this is a guide")
                .importer(false).comment("this is a commet")
                .price(20D).status(ManageNoticeConstant.DESIST)
                .description("espejo retrovisor").positionPiece(3)
                .quality("ORIGINAL")
                .build();

        spareDetailToFollowUpDTO1=SpareDetailToFollowUpDTO.builder()
                .cantidad(productOrder1.getAmount()).esCotizado(true)
                .posicion(productOrder1.getPositionPiece()).deleted(false)
                .build();

        spareDetailToFollowUpDTO2=SpareDetailToFollowUpDTO.builder()
                .cantidad(productOrder2.getAmount()).esCotizado(true)
                .posicion(productOrder2.getPositionPiece()).deleted(false)
                .build();

        spareDetailToFollowUpDTO3=SpareDetailToFollowUpDTO.builder()
                .cantidad(productOrder3.getAmount()).esCotizado(true)
                .posicion(productOrder3.getPositionPiece()).deleted(false)
                .build();

    }

    @Test
    void testSendPurchasesToFollowUp() {
        // Given-preconditions
        order.setSubsidiary(subsidiary.setProvider(provider))
                .setProducts(new HashSet<>(List.of(productOrder1, productOrder2, productOrder3)));

        given(noticeRepositoryPort.findSpareToFollowUpByExternalEventAndPositions(anyLong(), anyList(), anyBoolean(), anyBoolean()))
                .willReturn(List.of(spareDetailToFollowUpDTO1, spareDetailToFollowUpDTO2));

        given(noticeRepositoryPort.findSpareToFollowUpByExternalEventAndPositionsNoAuth(anyLong()))
                .willReturn(List.of(spareDetailToFollowUpDTO3));

        // When-Action to do
        boolean result = followUp.sendPurchasesToFollowUp(externalEvent, new HashSet<>(List.of(order)));

        // then-validations
        Assertions.assertTrue(result);
        verify(noticeRepositoryPort, times(1))
                .findSpareToFollowUpByExternalEventAndPositions(anyLong(), anyList(), anyBoolean(), anyBoolean());
        verify(noticeRepositoryPort, times(1))
                .findSpareToFollowUpByExternalEventAndPositionsNoAuth(anyLong());
    }

    @Test
    void testSendPurchasesToFollowUp_withoutSpareNotAuth() {
        // Given-preconditions
        order.setSubsidiary(subsidiary.setProvider(provider))
                .setProducts(new HashSet<>(List.of(productOrder1, productOrder2, productOrder3)));

        given(noticeRepositoryPort.findSpareToFollowUpByExternalEventAndPositions(anyLong(), anyList(), anyBoolean(), anyBoolean()))
                .willReturn(List.of(spareDetailToFollowUpDTO1, spareDetailToFollowUpDTO2));

        given(noticeRepositoryPort.findSpareToFollowUpByExternalEventAndPositionsNoAuth(anyLong()))
                .willReturn(List.of());

        // When-Action to do
        boolean result = followUp.sendPurchasesToFollowUp(externalEvent, new HashSet<>(List.of(order)));

        // then-validations
        Assertions.assertTrue(result);
        verify(noticeRepositoryPort, times(1))
                .findSpareToFollowUpByExternalEventAndPositions(anyLong(), anyList(), anyBoolean(), anyBoolean());
        verify(noticeRepositoryPort, times(1))
                .findSpareToFollowUpByExternalEventAndPositionsNoAuth(anyLong());
    }
    @Test
    void testSendBillingOrder_withException() throws Exception{
        // Given-preconditions
        order.setSubsidiary(subsidiary.setProvider(provider))
                .setProducts(new HashSet<>(List.of(productOrder1, productOrder2, productOrder3)));

        String messageException="this is a exception!";
        given(noticeRepositoryPort.findSpareToFollowUpByExternalEventAndPositions(anyLong(), anyList(), anyBoolean(), anyBoolean()))
                .willThrow(new RuntimeException(messageException));

        // When-Action to do
        boolean result=
                followUp.sendPurchasesToFollowUp( externalEvent, new HashSet<>(List.of(order)));

        // then-validations
        assertFalse(result);
//        assertEquals(messageException, exceptionUtil.getEx());
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
//        assertEquals(ErrorMessageHandler.ERROR_SPARE_FOLLOWUP, exceptionUtil.getMessage());
    }

    @Test
    void testRequestToFollowUp_listDTOEmpty() throws Exception{
        // Given-preconditions

        // When-Action to do
        Boolean result = followUp.requestToFollowUp( externalEvent, List.of());

        // then-validations
        assertFalse(result);
        verify(followUpPort, never())
                .sendDataToSFollowUp(any(SendSparesToFollowUPDTO.class));
    }
}
