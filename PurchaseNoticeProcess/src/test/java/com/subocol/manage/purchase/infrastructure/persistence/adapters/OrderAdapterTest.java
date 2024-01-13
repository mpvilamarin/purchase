package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.models.Order;
import com.subocol.manage.purchase.domain.models.ProductOrder;
import com.subocol.manage.purchase.domain.models.Subsidiary;
import com.subocol.manage.purchase.infrastructure.persistence.entities.NoticeModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SubsidiaryModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderAdapterTest {
    @Mock
    private OrderRepository repository;
    @InjectMocks
    private OrderAdapter orderAdapter;
    OrderModel orderModel;
    Order order;
    NoticeModel noticeModel;
    Notice notice;
    Long noticeId = 19867L;
    SubsidiaryModel subsidiaryModel;
    Subsidiary subsidiary;
    ProductOrderModel productOrder1;
    ProductOrder productOrder2;

    @BeforeEach
    void setup() {
        subsidiaryModel = SubsidiaryModel.builder().id(10L).locationExternalId(10L).build();

        subsidiary = Subsidiary.builder().id(10L).locationExternalId(10L).build();

        noticeModel = NoticeModel.builder().auth(true).brand("FORD").line("FUSION").externalEvent(123)
                .id(789L).eventId(456L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000001L).build();

        notice = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(123)
                .id(789L).eventId(456L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000001L).build();

        productOrder1 = ProductOrderModel.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(TimeZoneUtil.getTimestampByDefaultZone())
                .comment("ProductComment").positionPiece(1).status("Accepted").build();

        productOrder2 = ProductOrder.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(TimeZoneUtil.getTimestampByDefaultZone())
                .comment("ProductComment").positionPiece(1).status("Accepted").build();

        orderModel = OrderModel.builder().id(5593L).date(new Timestamp(1664579257)).subsidiary(subsidiaryModel).status("assigned").workshop("SUBARU DEL CANAL")
                .time(null).notice(noticeModel).priority(0).products(Set.of(productOrder1)).reference("Order-123").comment("null").workshopDeliveryDate(null)
                .documentUrl("https://example.com/document.pdf").orderIdDms(789).orderPurchaseDms(101112).orderPurchaseChile(131415L).orderIdSubocol("SUB123")
                .billingServiceId(1617).unforeseen(true).repairOrder(BigDecimal.valueOf(1000)).purchaseTypeId(1819).build();

        order = Order.builder().id(5593L).date(new Timestamp(1664579257)).subsidiary(subsidiary).status("assigned").workshop("SUBARU DEL CANAL")
                .time(null).notice(notice).priority(0).products(Set.of(productOrder2)).reference("Order-123").comment("null").workshopDeliveryDate(null)
                .documentUrl("https://example.com/document.pdf").orderIdDms(789).orderPurchaseDms(101112).orderPurchaseChile(131415L).orderIdSubocol("SUB123")
                .billingServiceId(1617).unforeseen(true).repairOrder(BigDecimal.valueOf(1000)).purchaseTypeId(1819).build();

    }

    @Test
    void orderAdapter_findById_ReturnOrderEntity() {

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(orderModel));

        Optional<Order> resultOrder = orderAdapter.findById(noticeId);

        assertThat(resultOrder).isPresent();
        assertThat(resultOrder.get()).isNotNull();
        assertThat(resultOrder.get()).isExactlyInstanceOf(Order.class);

        Order domainOrder = resultOrder.get();

        assertThat(domainOrder).isNotNull();
        assertEquals(domainOrder.getId(), orderModel.getId());
        assertEquals(domainOrder.getDate(), orderModel.getDate());
        assertEquals(domainOrder.getSubsidiary().getId(), orderModel.getSubsidiary().getId());
        assertEquals(domainOrder.getStatus(), orderModel.getStatus());
        assertEquals(domainOrder.getWorkshop(), orderModel.getWorkshop());
        assertEquals(domainOrder.getTime(), orderModel.getTime());
        assertEquals(domainOrder.getNotice().getId(), orderModel.getNotice().getId());
        assertEquals(domainOrder.getPriority(), orderModel.getPriority());
        assertEquals(domainOrder.getProducts().size(), orderModel.getProducts().size());
        assertEquals(domainOrder.getReference(), orderModel.getReference());
        assertEquals(domainOrder.getComment(), orderModel.getComment());
        assertEquals(domainOrder.getWorkshopDeliveryDate(), orderModel.getWorkshopDeliveryDate());
        assertEquals(domainOrder.getDocumentUrl(), orderModel.getDocumentUrl());
        assertEquals(domainOrder.getOrderIdDms(), orderModel.getOrderIdDms());
        assertEquals(domainOrder.getOrderPurchaseDms(), orderModel.getOrderPurchaseDms());
        assertEquals(domainOrder.getOrderPurchaseChile(), orderModel.getOrderPurchaseChile());
        assertEquals(domainOrder.getOrderIdSubocol(), orderModel.getOrderIdSubocol());
        assertEquals(domainOrder.getBillingServiceId(), orderModel.getBillingServiceId());
        assertEquals(domainOrder.getUnforeseen(), orderModel.getUnforeseen());
        assertEquals(domainOrder.getRepairOrder(), orderModel.getRepairOrder());
        assertEquals(domainOrder.getPurchaseTypeId(), orderModel.getPurchaseTypeId());

    }

    @Test
    void orderAdapter_findById_ReturnEmptyOptional() {

        Long noticeId = 1L;

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Order> resultNotice = orderAdapter.findById(noticeId);

        assertThat(resultNotice).isNotPresent();

    }

    @Test
    void testSave() {

        when(repository.save(any(OrderModel.class))).thenReturn(orderModel);

        Order savedOrder = orderAdapter.save(order);

        assertEquals(order.getId(), savedOrder.getId());
        assertEquals(order.getDate(), savedOrder.getDate());
        assertEquals(order.getSubsidiary().getId(), savedOrder.getSubsidiary().getId());
        assertEquals(order.getStatus(), savedOrder.getStatus());
        assertEquals(order.getWorkshop(), savedOrder.getWorkshop());
        assertEquals(order.getTime(), savedOrder.getTime());
        assertEquals(order.getNotice().getId(), savedOrder.getNotice().getId());
        assertEquals(order.getPriority(), savedOrder.getPriority());
        assertEquals(order.getProducts().size(), savedOrder.getProducts().size());
        assertEquals(order.getReference(), savedOrder.getReference());
        assertEquals(order.getComment(), savedOrder.getComment());
        assertEquals(order.getWorkshopDeliveryDate(), savedOrder.getWorkshopDeliveryDate());
        assertEquals(order.getDocumentUrl(), savedOrder.getDocumentUrl());
        assertEquals(order.getQuotation(), savedOrder.getQuotation());
        assertEquals(order.getOrderIdDms(), savedOrder.getOrderIdDms());
        assertEquals(order.getOrderPurchaseDms(), savedOrder.getOrderPurchaseDms());
        assertEquals(order.getOrderPurchaseChile(), savedOrder.getOrderPurchaseChile());
        assertEquals(order.getOrderIdSubocol(), savedOrder.getOrderIdSubocol());
        assertEquals(order.getBillingServiceId(), savedOrder.getBillingServiceId());
        assertEquals(order.getUnforeseen(), savedOrder.getUnforeseen());
        assertEquals(order.getRepairOrder(), savedOrder.getRepairOrder());
        assertEquals(order.getPurchaseTypeId(), savedOrder.getPurchaseTypeId());

    }
}
