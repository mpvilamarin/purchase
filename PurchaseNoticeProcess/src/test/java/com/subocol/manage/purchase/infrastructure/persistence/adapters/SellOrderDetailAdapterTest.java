package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.domain.models.Order;
import com.subocol.manage.purchase.domain.models.SellOrder;
import com.subocol.manage.purchase.domain.models.SellOrderDetail;
import com.subocol.manage.purchase.infrastructure.persistence.dtos.SellOrderValuesDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SellOrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.SellOrderDetailRepository;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.SellOrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SellOrderDetailAdapterTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private SellOrderDetailRepository sellOrderDetailRepository;
    @Autowired
    private SellOrderRepository sellOrderRepository;

    private SellOrderDetailAdapter sellOrderDetailAdapter;

    private OrderModel orderModel;
    private SellOrderModel sellOrderModel;



    @BeforeEach
    public void setUp() {
        orderModel = OrderModel.builder().id(5593L).date(new Timestamp(1664579257)).status("assigned").workshop("SUBARU DEL CANAL")
                .time(null).priority(0).reference("Order-123").comment("null").workshopDeliveryDate(null)
                .documentUrl("https://example.com/document.pdf").orderIdDms(789).orderPurchaseDms(101112).orderPurchaseChile(131415L).orderIdSubocol("SUB123")
                .billingServiceId(1617).unforeseen(true).repairOrder(BigDecimal.valueOf(1000)).purchaseTypeId(1819).build();

//        sellOrder = SellOrder.builder().id(1L).order(order).creationDate(new Timestamp(1664579257))
//                .lastUpdateDate(new Timestamp(1664579257)).subtotal(100.0).iva(10.0).total(110.0)
//                .pdfUrl("https://example.com/sellorder.pdf").details(new HashSet<>()).build();

        sellOrderModel = SellOrderModel.builder().id(1L).order(orderModel).creationDate(new Timestamp(1664579257))
                .lastUpdateDate(new Timestamp(1664579257)).subtotal(100.0).iva(10.0).total(110.0)
                .pdfUrl("https://example.com/sellorder.pdf").details(new HashSet<>()).build();
        sellOrderDetailAdapter=new SellOrderDetailAdapter(entityManager, sellOrderDetailRepository);
    }

    @Test
    void testSaveAllNative() {
        // Create a sample list of SellOrderDetail objects
        List<SellOrderDetail> sellOrderDetails = new ArrayList<>();
        SellOrderModel sellOrderModelSaved=sellOrderRepository.save(sellOrderModel);

        SellOrderDetail sellOrderDetail1=SellOrderDetail.builder().sellOrder(new SellOrder().setId(sellOrderModelSaved.getId()))
                .total(50D).amount(1).description("this ia test!").comment("this is a test!")
                .discount(10D).promiseDelivery(Timestamp.valueOf(LocalDateTime.now()))
                .grossPrice(60D).positionPiece(1).reference("this is a test!").unitPrice(60D)
                .build();

        SellOrderDetail sellOrderDetail2=SellOrderDetail.builder().sellOrder(new SellOrder().setId(sellOrderModelSaved.getId()))
                .total(50D).amount(1).description("this ia test!").comment("this is a test!")
                .discount(10D).promiseDelivery(Timestamp.valueOf(LocalDateTime.now()))
                .grossPrice(60D).positionPiece(2).reference("this is a test!").unitPrice(60D)
                .build();

        sellOrderDetails.add(sellOrderDetail1);
        sellOrderDetails.add(sellOrderDetail2);
        // Mock the behavior of EntityManager
//        Query mockQuery = mock(Query.class);
//        int expectedSavedItems = 5;
//        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
//        when(entityManager.createNativeQuery(anyString()).executeUpdate()).thenReturn(expectedSavedItems);

//        Query mockQuery = mock(Query.class);
        int expectedSavedItems = 2;
//        when(entityManager.createNativeQuery(anyString())).thenReturn(mockQuery);
//        when(mockQuery.executeUpdate()).thenReturn(expectedSavedItems);
        // Call the method under test
        int savedItems = sellOrderDetailAdapter.saveAllNative(sellOrderDetails);


        // Verify interactions and assertions
        assertEquals(expectedSavedItems, savedItems);
    }
}
