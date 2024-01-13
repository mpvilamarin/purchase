package com.subocol.manage.purchase.domain.integrations;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.enums.CauseManualPurchase;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.externalservices.BillingOrdersPort;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.servicesimpl.SendManualPurchaseImpl;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterProductQuotation;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.BillingOrders;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOrderModel;
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

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_CREATE_STATUS_REPLACEMENT;
import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_SET_MANUAL_PURCHASE;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BillingOrderTest {

    @Mock
    private BillingOrdersPort billingOrdersPort;
    @Mock
    private SellOrderRepositoryPort sellOrderRepositoryPort;
    @Mock
    private TaxRepositoryPort taxRepositoryPort;
    @Mock
    private CurrencyRepositoryPort currencyRepositoryPort;
    @InjectMocks
    private BillingOrders billingOrders;

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
    @BeforeEach
    void setup() {

        notice = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(externalEvent.intValue())
                .id(454L).eventId(eventId).build();

        subsidiary=Subsidiary.builder().id(100L).email("test@test.com").phone("3131311313")
                .locationExternalId(1L).alias("Taller").classification("Multimarca").build();

        provider=Provider.builder().id(200L).providerClassification("Multimarca").name("provider Test").phone("898989977")
                .nit("897456123").build();

        tax=Tax.builder().id(300L).taxName("iva").countryId(1L).percentage(19).build();

        currency=Currency.builder().id(1L).currencyId(1).countryId(1L).prefix("$").build();

        order = Order.builder().id(456L).date(TimeZoneUtil.getTimestampByDefaultZone()).notice(notice)
                .build();

        productOrder1 = ProductOrder.builder().id(22323L)
                .amount(1).grossPrice(100D).guide("this is a guide")
                .importer(false).comment("this is a commet")
                .price(40D).status(ManageNoticeConstant.SENT)
                .description("bomber delantero")
                .quality("ORIGINAL")
                .build();
        productOrder2 = ProductOrder.builder().id(22324L)
                .amount(2).grossPrice(100D).guide("this is a guide")
                .importer(false).comment("this is a commet")
                .price(20D).status(ManageNoticeConstant.SENT)
                .description("espejo retrovisor")
                .quality("ORIGINAL")
                .build();
        productOrder3 = ProductOrder.builder().id(22324L)
                .amount(2).grossPrice(100D).guide("this is a guide")
                .importer(false).comment("this is a commet")
                .price(20D).status(ManageNoticeConstant.DESIST)
                .description("espejo retrovisor")
                .quality("ORIGINAL")
                .build();

    }

    @Test
    void testSendBillingOrder() {
        // Given-preconditions
        order.setSubsidiary(subsidiary.setProvider(provider))
                .setProducts(new HashSet<>(List.of(productOrder1, productOrder2, productOrder3)));

        given(sellOrderRepositoryPort.findValuesSellOrderByOrderId(order.getId()))
                .willReturn(Pair.of(80D, 100D));

        given(taxRepositoryPort.findTaxByCountry(notice.getIdCountry()))
                .willReturn(Optional.of(tax));

        given(currencyRepositoryPort.findByCountryId(notice.getIdCountry()))
                .willReturn(Optional.of(currency));

        // When-Action to do
        boolean result = billingOrders.sendBillingOrder( order, notice);

        // then-validations
        Assertions.assertTrue(result);
        verify(sellOrderRepositoryPort, times(1))
                .findValuesSellOrderByOrderId(order.getId());
        verify(taxRepositoryPort, times(1))
                .findTaxByCountry(notice.getIdCountry());
    }

    @Test
    void testSendBillingOrder_withException() {
        // Given-preconditions
        String messageException="this is a exception!";
        given(sellOrderRepositoryPort.findValuesSellOrderByOrderId(order.getId()))
                .willThrow(new RuntimeException(messageException));

        // When-Action to do
        boolean result=billingOrders.sendBillingOrder( order, notice);

        // then-validations
        assertFalse(result);
//        assertEquals(messageException, exceptionUtil.getEx());
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
//        assertEquals(ErrorMessageHandler.ERROR_SEND_BILLING_ORDERS, exceptionUtil.getMessage());
    }

}
