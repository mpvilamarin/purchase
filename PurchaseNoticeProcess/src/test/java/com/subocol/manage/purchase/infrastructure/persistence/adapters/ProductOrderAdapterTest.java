package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.models.Order;
import com.subocol.manage.purchase.domain.models.ProductOrder;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ProductOrderRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductOrderAdapterTest {

    @Mock
    private ProductOrderRepository repository;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ProductOrderAdapter productOrderAdapter;
    ProductOrderModel productOrder1;
    ProductOrderModel productOrder2;
    ProductOrder productOrder3;
    SpareDetailToFollowUpDTO spareDetailToFollowUpDTO;
    ReserveCalculationTotalSuraDTO reserveCalculationTotalSuraDTO;
    List<ReserveRepuestosSuraDTO> reserveRepuestosSuraDTOS =new ArrayList<>();

    @BeforeEach
    void setup() {

        productOrder1 = ProductOrderModel.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(new Timestamp(1664579257))
                .comment("ProductComment").positionPiece(1).status("Accepted").build();

        productOrder2 = ProductOrderModel.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(new Timestamp(1664579257))
                .comment("ProductComment").positionPiece(1).status("Accepted").build();

        productOrder3 = ProductOrder.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(new Timestamp(1664579257))
                .comment("ProductComment").positionPiece(1).status("Accepted").build();

        spareDetailToFollowUpDTO=SpareDetailToFollowUpDTO.builder()
                .cantidad(productOrder1.getAmount()).esCotizado(true)
                .posicion(productOrder1.getPositionPiece()).deleted(false)
                .build();

        reserveCalculationTotalSuraDTO = new ReserveCalculationTotalSuraDTO(100.0, 110.0, 10.0);

        reserveRepuestosSuraDTOS.add(new ReserveRepuestosSuraDTO(
                "123", 1, "Pieza1", "REF1", 10, 50.0, 5.0, 10.0, "ORIGEN1", true, 15.0, "PROV1", 10D
        ));
        reserveRepuestosSuraDTOS.add(new ReserveRepuestosSuraDTO(
                "456", 2, "Pieza2", "REF2", 20, 30.0, 3.0, 5.0, "ORIGEN2", false, 10.0, "PROV2", 10D
        ));

    }

    @Test
    void productOrderAdapter_findAllByIdOrder_ReturnsProductOrderEntity() {

        List<ProductOrderModel> productOrderList = List.of(productOrder1, productOrder2);

        when(repository.findAllByOrder_Id(anyLong())).thenReturn(productOrderList);

        List<ProductOrder> resultProductOrder = productOrderAdapter.findAllByIdOrder(45521L);

        assertNotNull(resultProductOrder);
        assertFalse(resultProductOrder.isEmpty());
        assertEquals(productOrderList.size(), resultProductOrder.size());

        for (int i = 0; i < productOrderList.size(); i++) {
            ProductOrderModel productOrderModel = productOrderList.get(i);
            ProductOrder productOrder = resultProductOrder.get(i);

            assertEquals(productOrderModel.getId(), productOrder.getId());

        }

    }

    @Test
    void productOrderAdapter_findAllByIdOrder_ReturnsEmptyList() {

        List<ProductOrderModel> productOrderList = Collections.emptyList();

        when(repository.findAllByOrder_Id(anyLong())).thenReturn(productOrderList);

        List<ProductOrder> resultProductOrder = productOrderAdapter.findAllByIdOrder(45521L);

        assertNotNull(resultProductOrder);
        assertTrue(resultProductOrder.isEmpty());

    }

    @Test
    void testSave() {

        when(repository.save(any(ProductOrderModel.class))).thenReturn(productOrder1);

        ProductOrder savedProductOrder = productOrderAdapter.save(productOrder3);

        try {
            //Assertions.assertEquals for all entity attributes
            AttributeAssertions.assertAttributesEqual(productOrder3, savedProductOrder);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }

    @Test
    void saveAllNative() {

//        when(entityManager.createNativeQuery(any(String.class))).thenReturn();

        ProductOrder product1 = ProductOrder.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(new Timestamp(1664579257))
                .comment("ProductComment").positionPiece(1).status("Accepted").order(new Order().setId(100L)).build();

        ProductOrder product2 = ProductOrder.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(new Timestamp(1664579257))
                .comment("ProductComment").positionPiece(1).status("Accepted").order(new Order().setId(100L)).build();

        ProductOrder product3 = ProductOrder.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(new Timestamp(1664579257))
                .comment("ProductComment").positionPiece(1).status("Accepted").order(new Order().setId(100L)).build();


        List<ProductOrder> productOrderList = List.of(product1, product2, product3);

//        productOrderAdapter.saveAllNative(productOrderList);

    }

    @Test
    void testCountProductOrderByExternalEventAndPosition_ReturnCount() {

        Integer externalEvent = 11198;
        List<Integer> positions = Arrays.asList(1, 2, 3, 4, 5, 6);
        int expectedCount = 8;

        when(repository.countProductOrderByExternalEventAndPosition(anyInt(), anyList())).thenReturn(8L);

        Long result = productOrderAdapter.countProductOrderByExternalEventAndPosition(externalEvent,positions);

        verify(repository, times(1)).countProductOrderByExternalEventAndPosition(externalEvent, positions);
        assertEquals(expectedCount, result);

    }


    @Test
    void testFindAllById_ReturnsEntity() {

        List<ProductOrderModel> productOrderList = List.of(productOrder1, productOrder2);

        when(repository.findAllById(anyList())).thenReturn(productOrderList);

        List<ProductOrder> resultProductOrder = productOrderAdapter.findAllById(List.of(5454L,5455L));

        assertNotNull(resultProductOrder);
        assertFalse(resultProductOrder.isEmpty());
        assertEquals(productOrderList.size(), resultProductOrder.size());

        for (int i = 0; i < productOrderList.size(); i++) {
            ProductOrderModel productOrderModel = productOrderList.get(i);
            ProductOrder productOrder = resultProductOrder.get(i);

            assertEquals(productOrderModel.getId(), productOrder.getId());

        }

    }

    @Test
    void testFindAllById_ReturnsEmptyList() {

        List<ProductOrderModel> productOrderList = Collections.emptyList();

        when(repository.findAllById(anyList())).thenReturn(productOrderList);

        List<ProductOrder> resultProductOrder = productOrderAdapter.findAllById(List.of(5454L,5455L));

        assertNotNull(resultProductOrder);
        assertTrue(resultProductOrder.isEmpty());

    }

    @Test
    void testUpdateDesistProductOrder_ReturnCount() {

        List<Long> positions = Arrays.asList(1L, 2L, 3L);
        int expectedCount = 8;
        String UserName = "user";
        Timestamp timestamp = TimeZoneUtil.getTimestampByDefaultZone();

        when(repository.updateDesistProductOrder(anyList(), anyString(), any(Timestamp.class))).thenReturn(8);

        int result = productOrderAdapter.updateDesistProductOrder(positions ,UserName, timestamp);

        verify(repository, times(1)).updateDesistProductOrder(positions ,UserName, timestamp);
        assertEquals(expectedCount, result);

    }

    @Test
    void testGetCountsDesistAndTotal_ReturnCount() {

        Map<String, Long> countsMap = new HashMap<>();
        countsMap.put("countDesisted", 1L);
        countsMap.put("totalCount", 1L);
        Long orderId = 581L;

        when(repository.getCountsDesistAndTotal(anyLong())).thenReturn(countsMap);

        Map<String, Long> result = productOrderAdapter.getCountsDesistAndTotal(orderId);

        assertNotNull(result);
        assertEquals(1, result.get("countDesisted"));
        assertEquals(1, result.get("totalCount"));

    }

    @Test
    void testFindSpareToFollowUpByExternalEventAndPositionsDesist_ReturnCount() {

        List<Long> orderIds = List.of(6455L);

        when(repository.findSpareToFollowUpByExternalEventAndPositionsDesist(anyList())).thenReturn(List.of(spareDetailToFollowUpDTO));

        List<SpareDetailToFollowUpDTO> result = productOrderAdapter.findSpareToFollowUpByExternalEventAndPositionsDesist(orderIds);

        assertNotNull(result);
        verify(repository, times(1)).findSpareToFollowUpByExternalEventAndPositionsDesist(orderIds);
        assertEquals(List.of(spareDetailToFollowUpDTO), result);

    }

    @Test
    void testTotalPriceOrdersByExternalEventAndEventId_ReturnDouble() {

        Double expectedCount = 152.0;
        Integer externalEvent = 514451;
        boolean type = true;
        boolean unforeseen = false;
        List<Integer> positionPiece = List.of(1, 2, 3);

        when(repository.totalPriceOrdersByExternalEventAndEventId(anyInt(),anyList(),anyList(),anyBoolean())).thenReturn(152.0);

        Double result = productOrderAdapter.totalPriceOrdersByExternalEventAndEventId(externalEvent,type,positionPiece,unforeseen);

        assertNotNull(result);
        assertEquals(expectedCount, result);

    }

    @Test
    void testTotalGrossPriceOrdersByExternalEventAndEventId_ReturnValues() {

        Integer externalEvent = 514451;
        boolean type = false;
        boolean unforeseen = false;
        List<Integer> positionPiece = List.of(1, 2, 3);

        when(repository.totalGrossPriceOrdersByExternalEventAndEventId(anyInt(),anyList(),anyList(),anyBoolean())).thenReturn(reserveCalculationTotalSuraDTO);

        ReserveCalculationTotalSuraDTO result = productOrderAdapter.totalGrossPriceOrdersByExternalEventAndEventId(externalEvent,type,positionPiece,unforeseen);

        assertNotNull(result);
        assertEquals(reserveCalculationTotalSuraDTO, result);

    }

    @Test
    void testFindPiecesOrdersByExternalEvent_ReturnValues() {

        Integer externalEvent = 514451;
        boolean type = true;
        List<Integer> positionPiece = List.of(1, 2, 3);

        when(repository.findPiecesOrdersByExternalEvent(anyInt(),anyList(),anyList())).thenReturn(reserveRepuestosSuraDTOS);

        List<ReserveRepuestosSuraDTO> result = productOrderAdapter.findPiecesOrdersByExternalEvent(externalEvent,type,positionPiece);

        assertNotNull(result);
        assertEquals(reserveRepuestosSuraDTOS, result);

    }
}
