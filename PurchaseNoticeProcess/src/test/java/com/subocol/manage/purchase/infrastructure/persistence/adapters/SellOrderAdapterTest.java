package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.domain.models.Order;
import com.subocol.manage.purchase.domain.models.SellOrder;
import com.subocol.manage.purchase.infrastructure.persistence.dtos.SellOrderValuesDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SellOrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.SellOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellOrderAdapterTest {

    @Mock
    private SellOrderRepository repository;

    @InjectMocks
    private SellOrderAdapter sellOrderAdapter;

    SellOrder sellOrder;
    SellOrderModel sellOrderModel;
    Order order;
    OrderModel orderModel;

    Long sellOrderId = 1L;

    @BeforeEach
    void setup() {

        order = Order.builder().id(5593L).date(new Timestamp(1664579257)).status("assigned").workshop("SUBARU DEL CANAL")
                .time(null).priority(0).reference("Order-123").comment("null").workshopDeliveryDate(null)
                .documentUrl("https://example.com/document.pdf").orderIdDms(789).orderPurchaseDms(101112).orderPurchaseChile(131415L).orderIdSubocol("SUB123")
                .billingServiceId(1617).unforeseen(true).repairOrder(BigDecimal.valueOf(1000)).purchaseTypeId(1819).build();

        orderModel = OrderModel.builder().id(5593L).date(new Timestamp(1664579257)).status("assigned").workshop("SUBARU DEL CANAL")
                .time(null).priority(0).reference("Order-123").comment("null").workshopDeliveryDate(null)
                .documentUrl("https://example.com/document.pdf").orderIdDms(789).orderPurchaseDms(101112).orderPurchaseChile(131415L).orderIdSubocol("SUB123")
                .billingServiceId(1617).unforeseen(true).repairOrder(BigDecimal.valueOf(1000)).purchaseTypeId(1819).build();

        sellOrder = SellOrder.builder().id(1L).order(order).creationDate(new Timestamp(1664579257))
                .lastUpdateDate(new Timestamp(1664579257)).subtotal(100.0).iva(10.0).total(110.0)
                .pdfUrl("https://example.com/sellorder.pdf").details(new HashSet<>()).build();

        sellOrderModel = SellOrderModel.builder().id(1L).order(orderModel).creationDate(new Timestamp(1664579257))
                .lastUpdateDate(new Timestamp(1664579257)).subtotal(100.0).iva(10.0).total(110.0)
                .pdfUrl("https://example.com/sellorder.pdf").details(new HashSet<>()).build();

    }

    @Test
    void sellOrderAdapter_findByOrderId_ReturnInsurerEntity() {

        when(repository.findByOrder_Id(anyLong())).thenReturn(Optional.of(sellOrderModel));

        Optional<SellOrder> resultSellOrder = sellOrderAdapter.findByOrderId(sellOrderId);

        verify(repository, times(1)).findByOrder_Id(sellOrderId);

        assertThat(resultSellOrder).isPresent();
        assertThat(resultSellOrder.get()).isNotNull();
        assertThat(resultSellOrder.get()).isExactlyInstanceOf(SellOrder.class);

        SellOrder domainSellOrder = resultSellOrder.get();

        assertThat(domainSellOrder.getId()).isEqualTo(sellOrderId);
        assertThat(domainSellOrder).isNotNull();

        try {
            //Assertions.assertEquals for all entity attributes
            AttributeAssertions.assertAttributesEqual(sellOrder, domainSellOrder);
            AttributeAssertions.assertAttributesEqual(sellOrder.getOrder(), domainSellOrder.getOrder());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }

    @Test
    void sellOrderAdapter_findByOrderId_ReturnsEmptyOptional() {

        when(repository.findByOrder_Id(anyLong())).thenReturn(Optional.empty());

        Optional<SellOrder> resultSellOrder = sellOrderAdapter.findByOrderId(order.getId());

        verify(repository, times(1)).findByOrder_Id(order.getId());

        assertThat(resultSellOrder).isEmpty();
    }

    @Test
    void sellOrderAdapter_testSave() {

        when(repository.save(any(SellOrderModel.class))).thenReturn(sellOrderModel);

        SellOrder savedSellOrder = sellOrderAdapter.save(sellOrder);

        verify(repository, times(1)).save(any());

        assertEquals(sellOrder.getId(), savedSellOrder.getId());
        assertEquals(sellOrder.getOrder().getId(), savedSellOrder.getOrder().getId());
        assertEquals(sellOrder.getCreationDate(), savedSellOrder.getCreationDate());
        assertEquals(sellOrder.getLastUpdateDate(), savedSellOrder.getLastUpdateDate());
        assertEquals(sellOrder.getSubtotal(), savedSellOrder.getSubtotal());
        assertEquals(sellOrder.getIva(), savedSellOrder.getIva());
        assertEquals(sellOrder.getTotal(), savedSellOrder.getTotal());
        assertEquals(sellOrder.getPdfUrl(), savedSellOrder.getPdfUrl());
        assertEquals(sellOrder.getDetails(), savedSellOrder.getDetails());

    }

    @Test
    void testFindSellOrderValuesDTOByOrderId() {

        Double subTotal = 80D;
        Double total = 100D;
        when(repository.findSellOrderValuesDTOByOrderId(anyLong()))
                .thenReturn(new SellOrderValuesDTO(subTotal, total));

        Pair<Double, Double> result = sellOrderAdapter.findValuesSellOrderByOrderId(order.getId());

        verify(repository, times(1)).findSellOrderValuesDTOByOrderId(anyLong());

        assertEquals(subTotal, result.getFirst());
        assertEquals(total, result.getSecond());

    }

    @Test
    void testUpdateSubtotalAndTotalAndIvaById() {

        Double subTotal = 80D;
        Double total = 100D;
        Double iva = 20D;
        when(repository.updateSubtotalAndTotalAndIvaById(anyLong(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(1);

        int result = sellOrderAdapter.updateSubtotalTotalIva(sellOrderId, subTotal, total, iva);

        verify(repository, times(1)).updateSubtotalAndTotalAndIvaById(anyLong(), anyDouble(), anyDouble(), anyDouble());

        assertThat(result).isPositive();

    }
}
