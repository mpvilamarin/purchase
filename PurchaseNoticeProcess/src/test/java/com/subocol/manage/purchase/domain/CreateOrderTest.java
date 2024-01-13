package com.subocol.manage.purchase.domain;

import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.externalservices.LocationExternalServicesPort;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.servicesimpl.CreateOrderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.*;
import static com.subocol.manage.purchase.domain.constant.ManageNoticeConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderTest {
    @Mock
    private ProductQuotationRepositoryPort productQuotationRepositoryPort;
    @Mock
    private ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;
    @Mock
    private NoticeRepositoryPort noticeRepositoryPort;
    @Mock
    private OrderRepositoryPort orderRepositoryPort;
    @Mock
    private ProductOrderRepositoryPort productOrderRepositoryPort;
    @Mock
    private SubsidiaryRepositoryPort subsidiaryRepositoryPort;
    @Mock
    private InsuranceCarrierRepositoryPort insuranceCarrierRepositoryPort;
    @Mock
    private SellOrderRepositoryPort sellOrderRepositoryPort;
    @Mock
    private TaxRepositoryPort taxRepositoryPort;
    @Mock
    private StatusReplacementRepositoryPort statusReplacementRepositoryPort;
    @Mock
    private SellOrderDetailRepositoryPort sellOrderDetailRepositoryPort;
    @Mock
    private LocationExternalServicesPort locationExternalServicesPort;
    @Mock
    private StatusPartsRepositoryPort statusPartsRepositoryPort;
    @InjectMocks
    private CreateOrderImpl createOrderImpl;
    ProductQuotation productQuotation1;
    ProductOrder productOrder1;
    SellOrder sellOrder;
    SellOrder sellOrder2;
    SellOrderDetail sellOrderDetail;
    Quotation quotation;
    Provider provider;
    Provider provider2;
    Subsidiary subsidiary;
    Subsidiary subsidiary2;
    StatusReplacement statusReplacement;
    Notice notice;
    Notice notice2;
    Order order;
    Order order2;
    Tax tax;
    Location location;

    @BeforeEach
    void setup() {
        productQuotation1 = ProductQuotation.builder()
                .position(1).reference("ProductReference").netPrice(10.0).description("ProductDescription")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2)
                .discountCampaigns(1.0).discountAdditional(2.0)
                .discountManual(3.0).comment("ProductComment").build();

        notice = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(123)
                .id(789L).eventId(456L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000001L).build();

        notice2 = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(123)
                .id(789L).eventId(456L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) CHILE).insuranceNumber(152000002L).build();

        order = Order.builder().id(456L).date(TimeZoneUtil.getTimestampByDefaultZone()).notice(notice).build();

        order2 = Order.builder().id(456L).date(TimeZoneUtil.getTimestampByDefaultZone()).notice(notice2).build();

        productOrder1 = ProductOrder.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(TimeZoneUtil.getTimestampByDefaultZone())
                .comment("ProductComment").positionPiece(1).status("Accepted").build();

        provider = Provider.builder().name("providerTest").email("providerTest@prueba.com").phone("12345678").providerClassification("AUTOSUMINISTRO").build();

        provider2 = Provider.builder().name("providerTest").email("providerTest@prueba.com").phone("12345678").providerClassification("MOSTRADOR").build();

        subsidiary = Subsidiary.builder().id(10L).locationExternalId(10L).build();
        subsidiary2 = Subsidiary.builder().id(10L).locationExternalId(10L).build();
        sellOrderDetail = SellOrderDetail.builder().total(10.0).build();
        quotation = Quotation.builder().id(1L).build();
        sellOrder = SellOrder.builder().id(1L).build();
        sellOrder2 = SellOrder.builder().id(1L).build();
        tax = Tax.builder().id(1L).percentage(10).build();
        location = Location.builder().id(1L).countryId(1L).build();
        productQuotation1.setQuotation(quotation);
        quotation.setSubsidiary(subsidiary);
        order.setSubsidiary(subsidiary);
        order2.setSubsidiary(subsidiary2);
        subsidiary.setProvider(provider);
        subsidiary2.setProvider(provider2);
        sellOrder.setOrder(order);
        sellOrder2.setOrder(order2);
        sellOrder.addDetail(sellOrderDetail);
        sellOrder2.addDetail(sellOrderDetail);

    }

    @Test
    void createPurchaseOrder() throws ExceptionUtil {
        Long orderId = 1L;
        Set<ProductOrder> products=new HashSet<>();
        products.add(productOrder1);
        order.setProducts(products);

        given(sellOrderRepositoryPort.save(any(SellOrder.class))).willReturn(sellOrder);
        given(locationExternalServicesPort.findLocation(sellOrder.getOrder().getSubsidiary().getLocationExternalId())).willReturn(location);
       boolean result = createOrderImpl.createPurchaseOrder(order);

        assertThat(result).isTrue();
        verify(sellOrderRepositoryPort, times(1)).save(any(SellOrder.class));

    }

    @Test
    void createPurchaseOrderWithoutProductOrder() throws ExceptionUtil {
        Long orderId = 1L;
        Set<ProductOrder> products=new HashSet<>();
        order.setProducts(products);
        boolean result = createOrderImpl.createPurchaseOrder(order);

        assertThat(result).isTrue();
        verify(sellOrderRepositoryPort, never()).save(any(SellOrder.class));

    }


    @Test
    void createOrder() throws ExceptionUtil {

        Mockito.when(subsidiaryRepositoryPort.findById(subsidiary.getId())).thenReturn(Optional.of(subsidiary));

        Order result = createOrderImpl.createOrder(notice, 1, subsidiary.getId(), TYPE_MANUAL);

        assertNotNull(result);
        Assertions.assertEquals(1, result.getPriority());
        Assertions.assertEquals(subsidiary, result.getSubsidiary());
        Assertions.assertEquals(notice.getWorkshop(), result.getWorkshop());
        Assertions.assertEquals(ASSIGNED.toLowerCase(), result.getStatus());
        assertNotNull(result.getDate());
        Assertions.assertFalse(result.getUnforeseen());
        Assertions.assertEquals(BigDecimal.valueOf(52.56), result.getRepairOrder());
        Assertions.assertEquals(TYPE_MANUAL, result.getPurchaseTypeId());
    }


    @Test
    void testCreateProductOrderQuotation() throws Exception {

        ProductOrder result = createOrderImpl.createProductOrderQuotation(order, productQuotation1, TYPE_MANUAL);

        assertNotNull(result);
        Assertions.assertEquals(ManageNoticeConstant.ACCEPTED.toLowerCase(), result.getStatus());
        Assertions.assertEquals(productQuotation1.getReference(), result.getReference());
        Assertions.assertEquals(productQuotation1.getNetPrice(), result.getPrice());
        Assertions.assertEquals(productQuotation1.getDescription(), result.getDescription());
        Assertions.assertEquals(order.getId(), result.getOrder().getId());
        Assertions.assertEquals(productQuotation1.getQuality(), result.getQuality());
        Assertions.assertEquals(productQuotation1.getAmount(), result.getAmount());
        Assertions.assertEquals(productQuotation1.getGrossPrice(), result.getGrossPrice());
        Assertions.assertEquals(productQuotation1.getImporter(), result.getImporter());
        Assertions.assertEquals(productQuotation1.getDeliveryTime() == null ? 0 : productQuotation1.getDeliveryTime().toString(), String.valueOf(result.getPromisedDeliveryDays()));

    }

    @Test
    void testAddNewDetails() throws Exception {

        SellOrderDetail result = createOrderImpl.addNewDetails(sellOrder, productOrder1);

        assertNotNull(result);
        Assertions.assertEquals(sellOrder.getId(), result.getSellOrder().getId());
        Assertions.assertEquals(productOrder1.getPrice(), result.getUnitPrice());
        Assertions.assertEquals(productOrder1.getAmount(), result.getAmount());
        Assertions.assertEquals(productOrder1.getPrice() * productOrder1.getAmount(), result.getTotal());
        Assertions.assertEquals(productOrder1.getReference(), result.getReference());
        Assertions.assertEquals(productOrder1.getDescription(), result.getDescription());
        Assertions.assertEquals(productOrder1.getGrossPrice(), result.getGrossPrice());
        Assertions.assertEquals(productOrder1.getTotalDiscount(), result.getDiscount());
        Assertions.assertEquals(productOrder1.getPromiseDelivery(), result.getPromiseDelivery());
        Assertions.assertEquals(productOrder1.getComment(), result.getComment());
        Assertions.assertEquals(productOrder1.getPositionPiece(), result.getPositionPiece());
    }

    @Test
    void testAddNewDetails_Exception() throws ExceptionUtil {

        ExceptionUtil exceptionUtil = assertThrows(ExceptionUtil.class, () -> createOrderImpl.addNewDetails(null, null));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
        assertEquals(ERROR_CREATE_SELL_ORDER_DETAIL, exceptionUtil.getMessage());
    }


    @Test
    void testCreateStatusParts() throws Exception {

        StatusReplacement statusReplacement = Mockito.mock(StatusReplacement.class);

        StatusParts result = createOrderImpl.createStatusParts(productOrder1, statusReplacement, order);

        assertNotNull(result);
        Assertions.assertEquals(statusReplacement.getId(), result.getStatusReplacement().getId());
        Assertions.assertEquals(productOrder1.getId(), result.getIdProductOrder());
        Assertions.assertEquals(productOrder1.getDescription(), result.getNamePart());
        Assertions.assertEquals(productOrder1.getReference(), result.getReference());
        Assertions.assertEquals(productOrder1.getImporter(), result.getImportPart());
        Assertions.assertEquals(order.getId(), result.getIdOrder());
        Assertions.assertEquals(productOrder1.getAmount(), result.getTotalParts());
        Assertions.assertEquals(productOrder1.getStatus(), result.getStatus());
        Assertions.assertEquals(order.getDate(), result.getApprovedOrderDate());
        Assertions.assertEquals(productOrder1.getPromiseDelivery(), result.getEstimateDeliveryDate());
    }

    @Test
    void testCreateStatusParts_Exception() throws ExceptionUtil {

        ExceptionUtil exceptionUtil = assertThrows(ExceptionUtil.class, () -> createOrderImpl.createStatusParts(null, statusReplacement, order));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
        assertEquals(ERROR_CREATE_STATUS_PARTS, exceptionUtil.getMessage());
    }

    @Test
    void testCreateStatusReplacement_Exception() throws ExceptionUtil {
        String externalEvent = notice.getExternalEvent().toString();
        ExceptionUtil exceptionUtil = assertThrows(ExceptionUtil.class, () ->
                createOrderImpl.createStatusReplacement(null, externalEvent));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
        assertEquals(ERROR_CREATE_STATUS_REPLACEMENT, exceptionUtil.getMessage());
    }

    @Test
    void testPurchaseOrderIdChile() throws ExceptionUtil {

        String expectedPurchaseOrderId = "CL-CLM-456";

        given(noticeRepositoryPort.findById(anyLong())).willReturn(Optional.of(notice2));

        InsuranceCarrier insuranceCarrier = Mockito.mock(InsuranceCarrier.class);
        Mockito.when(insuranceCarrierRepositoryPort.findById(notice2.getInsuranceNumber())).thenReturn(Optional.of(insuranceCarrier));
        Mockito.when(insuranceCarrier.getPrefix()).thenReturn("CL");

        String purchaseOrderId = createOrderImpl.purchaseOrderId(sellOrder2);

        verify(noticeRepositoryPort, times(1)).findById(anyLong());
        verify(insuranceCarrierRepositoryPort, times(1)).findById(notice2.getInsuranceNumber());
        Assertions.assertEquals(expectedPurchaseOrderId, purchaseOrderId);
    }

    @Test
    void testPurchaseOrderIdColombia() throws ExceptionUtil {

        String expectedPurchaseOrderId = "CO-COPA-456";

        given(noticeRepositoryPort.findById(anyLong())).willReturn(Optional.of(notice));

        InsuranceCarrier insuranceCarrier = Mockito.mock(InsuranceCarrier.class);
        Mockito.when(insuranceCarrierRepositoryPort.findById(notice.getInsuranceNumber())).thenReturn(Optional.of(insuranceCarrier));
        Mockito.when(insuranceCarrier.getPrefix()).thenReturn("COP");

        String purchaseOrderId = createOrderImpl.purchaseOrderId(sellOrder);

        verify(noticeRepositoryPort, times(1)).findById(anyLong());
        verify(insuranceCarrierRepositoryPort, times(1)).findById(notice.getInsuranceNumber());
        Assertions.assertEquals(expectedPurchaseOrderId, purchaseOrderId);
    }

    @Test
    void testPurchaseOrderId_withException() throws ExceptionUtil {

        String expectedPurchaseOrderId = "CO-COPA-456";
        String messageException="this is a exception!";
        given(noticeRepositoryPort.findById(anyLong())).willThrow(new RuntimeException(messageException));

        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()->  createOrderImpl.purchaseOrderId(sellOrder));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        Assertions.assertEquals(messageException, result.getEx());
        Assertions.assertEquals( ERROR_CREATE_PURCHASE_ORDER_ID, result.getMessage());
    }

    @Test
    void testSaveSellOrder() throws ExceptionUtil {
        List<ProductOrder> productOrderList = List.of(productOrder1);
        sellOrder.setIva(10D);
        given(locationExternalServicesPort.findLocation(sellOrder.getOrder().getSubsidiary().getLocationExternalId())).willReturn(location);
        given(taxRepositoryPort.findTaxByCountry(location.getCountryId())).willReturn(Optional.of(tax));
        when(sellOrderRepositoryPort.save(any(SellOrder.class))).thenReturn(sellOrder.setId(100L));
        boolean result = createOrderImpl.saveSellOrder(order, productOrderList);

        verify(locationExternalServicesPort, times(1)).findLocation(sellOrder.getOrder().getSubsidiary().getLocationExternalId());
        verify(taxRepositoryPort, times(1)).findTaxByCountry(location.getCountryId());

        verify(sellOrderRepositoryPort, times(2)).save(any(SellOrder.class));

//        verify(sellOrderRepositoryPort, times(1)).updateSubtotalTotalIva(anyLong(), anyDouble(), anyDouble(), anyDouble());
        assertTrue(result);

    }

    @Test
    void testSaveSellOrder_withException() throws ExceptionUtil {
        String messageException="this is a exception!";
        List<ProductOrder> productOrderList = List.of(productOrder1);
        sellOrder.setIva(10D);
        given(locationExternalServicesPort.findLocation(sellOrder.getOrder().getSubsidiary().getLocationExternalId())).willReturn(location);
        given(taxRepositoryPort.findTaxByCountry(location.getCountryId())).willThrow(new RuntimeException(messageException));
        when(sellOrderRepositoryPort.save(any(SellOrder.class))).thenReturn(sellOrder.setId(100L));

        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()->  createOrderImpl.saveSellOrder(order, productOrderList));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        Assertions.assertEquals(messageException, result.getEx());
        Assertions.assertEquals( ERROR_SAVE_SELL_ORDER, result.getMessage());

    }

    @Test
    void testCreateOrderWithProductsManual() throws ExceptionUtil {

        List<ProductQuotation> productOrderList = List.of(productQuotation1);

        given(subsidiaryRepositoryPort.findById(anyLong())).willReturn(Optional.ofNullable(subsidiary));

        Order result = createOrderImpl.createOrderWithProducts(notice, 1, subsidiary.getId(), productOrderList, 2);

        assertNotNull(result);
        Assertions.assertEquals(productOrderList.size(), result.getProducts().size());
    }
    @Test
    void testCreateOrderWithProducts_withException() throws ExceptionUtil {

        List<ProductQuotation> productOrderList = List.of(productQuotation1);

        given(subsidiaryRepositoryPort.findById(anyLong())).willReturn(Optional.ofNullable(subsidiary));

        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()->   createOrderImpl.createOrderWithProducts(notice, 1, null, productOrderList, 2));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        Assertions.assertEquals( ERROR_CREATE_ORDER_AND_PRODUCTS+notice.getId(), result.getMessage());

    }

    @Test
    void createOrderQuotationTest() throws ExceptionUtil {

        List<ProductQuotation> productOrderList = List.of(productQuotation1);
        Set<ProductOrder> productOrders=new HashSet<>();
        productOrders.add(productOrder1);
        order.setProducts(productOrders);

        given(subsidiaryRepositoryPort.findById(anyLong())).willReturn(Optional.ofNullable(subsidiary));

        when(orderRepositoryPort.save(any(Order.class))).thenReturn(order);
        when(statusReplacementRepositoryPort.save(any(StatusReplacement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Set<Order> actualOrders = createOrderImpl.createOrderQuotation(notice, productOrderList, TYPE_AUTOMATIC);

        Assertions.assertEquals(1, actualOrders.size());
        verify(orderRepositoryPort, times(1)).save(any(Order.class));
        verify(statusReplacementRepositoryPort, times(1)).save(any(StatusReplacement.class));

    }

    @Test
    void createOrderQuotationTestManual() throws ExceptionUtil {

        List<ProductQuotation> productOrderList = List.of(productQuotation1);
        Integer typePurchaseId = 2;

        Set<ProductOrder> productOrders=new HashSet<>();
        productOrders.add(productOrder1);
        order.setProducts(productOrders);

        given(subsidiaryRepositoryPort.findById(anyLong())).willReturn(Optional.ofNullable(subsidiary));
        given(productQuotationRepositoryPort.updateProductQuotationStatusTrueMP(anyList(), anyString(), anyBoolean())).willReturn(1);
        given(manualPurchaseRepositoryPort.updateManualPurchaseByPosition(anyString(), anyString(), anyList())).willReturn(1);


        when(orderRepositoryPort.save(any(Order.class))).thenReturn(order);
        when(statusReplacementRepositoryPort.save(any(StatusReplacement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Set<Order> actualOrders = createOrderImpl.createOrderQuotation(notice, productOrderList, typePurchaseId);

        Assertions.assertEquals(1, actualOrders.size());
        verify(orderRepositoryPort, times(1)).save(any(Order.class));
        verify(statusReplacementRepositoryPort, times(1)).save(any(StatusReplacement.class));
        verify(productQuotationRepositoryPort, times(1)).updateProductQuotationStatusTrueMP(anyList(), anyString(), anyBoolean());
        verify(manualPurchaseRepositoryPort, times(1)).updateManualPurchaseByPosition(anyString(), anyString(), anyList());

    }

    @Test
    void createOrderQuotation_withException() throws ExceptionUtil {

        List<ProductQuotation> productOrderList = List.of(productQuotation1);
        Integer typePurchaseId = 2;

        Set<ProductOrder> productOrders=new HashSet<>();
        productOrders.add(productOrder1);
        order.setProducts(productOrders);

        given(subsidiaryRepositoryPort.findById(anyLong())).willThrow(new RuntimeException());

        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()->  createOrderImpl.createOrderQuotation(notice, productOrderList, typePurchaseId));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        Assertions.assertEquals( ERROR_CREATE_ORDER+notice.getId(), result.getMessage());
    }

    @Test
    void testCreateProductOrderQuotation_withException() throws ExceptionUtil {

        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()->   createOrderImpl.createProductOrderQuotation(order, null, null));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        Assertions.assertEquals( ERROR_CREATE_PRODUCT_ORDER, result.getMessage());

    }

    @Test
    void testCreatePurchaseOrder_withException() throws ExceptionUtil {

        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()->   createOrderImpl.createPurchaseOrder(null));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        Assertions.assertEquals( ERROR_CREATE_PURCHASE_ORDER, result.getMessage());

    }

    @Test
    void testSaveOrderAndProductOrder_withException() throws ExceptionUtil {

        ExceptionUtil result=assertThrows(ExceptionUtil.class, ()->   createOrderImpl.saveOrderAndProductOrder(null));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getCode());
        Assertions.assertEquals( ERROR_SAVING_ORDERS_AND_PRODUCT_ORDERS, result.getMessage());

    }

    @Test
    void createOrderQuotationConcessionarieTest() throws ExceptionUtil {

        List<ProductQuotation> productOrderList = List.of(productQuotation1);
        Set<ProductOrder> productOrders=new HashSet<>();
        productOrders.add(productOrder1);
        order.setProducts(productOrders);

        given(subsidiaryRepositoryPort.findById(anyLong())).willReturn(Optional.ofNullable(subsidiary));

        when(orderRepositoryPort.save(any(Order.class))).thenReturn(order);
        when(statusReplacementRepositoryPort.save(any(StatusReplacement.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productOrderRepositoryPort.findAllByIdOrder(order.getId())).thenReturn(new ArrayList<>(productOrders));
        when(sellOrderRepositoryPort.save(any(SellOrder.class))).thenReturn(sellOrder);
        when(locationExternalServicesPort.findLocation(sellOrder.getOrder().getSubsidiary().getLocationExternalId())).thenReturn(location);

        Order actualOrders = createOrderImpl.createOrderQuotationConcessionarie( productOrderList, notice, quotation);

        assertNotNull(actualOrders);
        verify(orderRepositoryPort, times(1)).save(any(Order.class));
        verify(statusReplacementRepositoryPort, times(1)).save(any(StatusReplacement.class));
        Assertions.assertEquals(productOrderList.size(), actualOrders.getProducts().size());
        Assertions.assertEquals(subsidiary, actualOrders.getSubsidiary());

    }

    @Test
    void createOrderQuotationConcessionarie_CatchBlock() {
        List<ProductQuotation> productQuotationList = List.of(productQuotation1);

        given(subsidiaryRepositoryPort.findById(anyLong())).willReturn(Optional.ofNullable(subsidiary));

        when(orderRepositoryPort.save(any(Order.class))).thenThrow(new RuntimeException("Mocked exception"));

        assertThrows(ExceptionUtil.class, () -> {
            createOrderImpl.createOrderQuotationConcessionarie(productQuotationList, notice, quotation);
        });

        verify(orderRepositoryPort, times(1)).save(any(Order.class));
        verify(statusReplacementRepositoryPort, never()).save(any(StatusReplacement.class));
        verify(productOrderRepositoryPort, never()).findAllByIdOrder(anyLong());
        verify(sellOrderRepositoryPort, never()).save(any(SellOrder.class));
        verify(locationExternalServicesPort, never()).findLocation(anyLong());
    }
}
