package com.subocol.manage.purchase.domain;

import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.externalservices.LocationExternalServicesPort;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.servicesimpl.DesistProduct;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.DesistDTO;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.FollowUp;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.ReserveSura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DesistProductTest {

    @Mock
    private ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;

    @Mock
    private ProductQuotationRepositoryPort productQuotationRepositoryPort;

    @Mock
    private InsurerRepositoryPort insurerRepositoryPort;

    @Mock
    private ProductOrderRepositoryPort productOrderRepositoryPort;

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private StatusPartsRepositoryPort statusPartsRepositoryPort;

    @Mock
    private DesistDTO desistDTO;

    @Mock
    private SellOrderDetailRepositoryPort sellOrderDetailRepositoryPort;

    @Mock
    private SellOrderRepositoryPort sellOrderRepositoryPort;

    @Mock
    private FollowUp followUp;

    @Mock
    private ReserveSura reserveSura;

    @Mock
    private TaxRepositoryPort taxRepositoryPort;

    @Mock
    private DesistRepositoryPort desistRepositoryPort;

    @Mock
    private LocationExternalServicesPort locationExternalServicesPort;

    @InjectMocks
    private DesistProduct desistProduct;

    ProductOrder productOrder;

    Order order;

    Notice notice;
    Insurer insurer;

    SellOrder sellOrder;

    Subsidiary subsidiary;

    Location location;

    Tax tax;

    ManualPurchase manualPurchase;

    Map<String, Long> countsMap = new HashMap<>();

    @BeforeEach
    void setup() {
        desistDTO = DesistDTO.builder().causal("Precio mal cotizado.").ids(List.of(36616L)).observation("").userName("user name").build();

        subsidiary = Subsidiary.builder().id(10L).locationExternalId(10L).build();

        notice = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(123)
                .id(789L).eventId(456L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000001L).build();

        productOrder = ProductOrder.builder().id(54641L).price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(TimeZoneUtil.getTimestampByDefaultZone())
                .comment("ProductComment").positionPiece(1).status("Accepted").order(order).build();

        order = Order.builder().id(5593L).date(new Timestamp(1664579257)).status("assigned").workshop("SUBARU DEL CANAL")
                .time(null).notice(notice).priority(0).reference("Order-123").comment("null").workshopDeliveryDate(null)
                .documentUrl("https://example.com/document.pdf").orderIdDms(789).orderPurchaseDms(101112).orderPurchaseChile(131415L).orderIdSubocol("SUB123")
                .billingServiceId(1617).unforeseen(true).repairOrder(BigDecimal.valueOf(1000)).purchaseTypeId(1819).subsidiary(subsidiary).build();

        productOrder.setOrder(order);

        insurer = Insurer.builder().id(5345L).name("SURA")
                .insurerId(123456789L).countryId(5L).sdkActive(false).multimedia(true)
                .priceToUse("autosuministro/genuino/precio_medio").newSuggestedReferenceParameter(true).allowMaxCostPiece(true)
                .daysUpdateSuggestedReference(7L).useGrossPriceCostoverrun(false).usePriceList(false)
                .prioritizePriceList(false).useOrbikaValuation(true).flowReserveSura(true).sendOrderFact(true).sdkActive(true).sendOrderEmailWinner(true).build();

        manualPurchase = ManualPurchase.builder().id(24898L).externalEvent("21920").brand("KIA").line("CERATO").plate("985745").description("Bateria").quantity(1)
                .reference("").suggestedReference("").status("bought").date(new Timestamp(1664582461)).cause("PARAMETRY")
                .position(7).eventId(7106L).auth(true).deleted(false).purchaseSubsidiary(true)
                .build();

        SellOrderDetail sellOrderDetail1=SellOrderDetail.builder()
                .total(50D).amount(1).description("Tapa motor").comment("")
                .discount(10D).promiseDelivery(Timestamp.valueOf(LocalDateTime.now()))
                .grossPrice(60D).positionPiece(1).reference("455667165").unitPrice(60D)
                .id(45441L)
                .build();

        sellOrder = SellOrder.builder().id(1L).order(order).creationDate(new Timestamp(1664579257))
                .lastUpdateDate(new Timestamp(1664579257)).subtotal(100.0).iva(10.0).total(110.0)
                .pdfUrl("https://example.com/sellorder.pdf").details(Set.of(sellOrderDetail1)).build();

        location = Location.builder().id(1L).countryId(1L).build();

        tax=Tax.builder().id(300L).taxName("iva").countryId(1L).percentage(19).build();

        countsMap.put("countDesisted", 1L);
        countsMap.put("totalCount", 1L);

    }

    @Test
    void testDesistProductSuccess() throws ExceptionUtil {

        when(productOrderRepositoryPort.findAllById(anyList())).thenReturn(List.of(productOrder));
        when(productOrderRepositoryPort.updateDesistProductOrder(anyList(),anyString(),any(Timestamp.class))).thenReturn(1);
        when(sellOrderDetailRepositoryPort.findAllByOrderIdAndPosition(anyLong(),anyList())).thenReturn(List.of(45441L));
        when(sellOrderRepositoryPort.findByOrderId(anyLong())).thenReturn(Optional.of(sellOrder));
        when(locationExternalServicesPort.findLocation(anyLong())).thenReturn(location);
        when(taxRepositoryPort.findTaxByCountry(anyLong())).thenReturn(Optional.of(tax));
        when(sellOrderRepositoryPort.updateSubtotalTotalIva(anyLong(),anyDouble(),anyDouble(),anyDouble())).thenReturn(1);
        when(desistRepositoryPort.saveAllNative(anyList())).thenReturn(1);
        when(statusPartsRepositoryPort.updateStatusByProductOrderId(anyString(),anyList())).thenReturn(1);
        when(productOrderRepositoryPort.getCountsDesistAndTotal(anyLong())).thenReturn(countsMap);
        when(orderRepositoryPort.updateStatusByOrderId(anyString(),anyLong())).thenReturn(1);
        when(productQuotationRepositoryPort.updateStatusByPositionPieceAndProductOrderId(anyList())).thenReturn(1);
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(reserveSura.sendPurchaseTotalReserveDesistSura(any(Notice.class))).thenReturn(true);
        when(productQuotationRepositoryPort.updatePurchaseSubsidiary(anyString(),anyList(),anyBoolean())).thenReturn(1);
        when(manualPurchaseRepositoryPort.updatePurchaseSubsidiary(anyString(),anyList(),anyBoolean())).thenReturn(1);
        when(followUp.sendPurchasesToFollowUpDesist(anyLong(),anyList())).thenReturn(true);

        ResponseDTO responseDTO = desistProduct.desistProduct(desistDTO);


        verify(productOrderRepositoryPort, times(1)).findAllById(anyList());
        verify(productOrderRepositoryPort, times(1)).updateDesistProductOrder(anyList(), anyString(), any(Timestamp.class));
        verify(sellOrderDetailRepositoryPort, times(1)).findAllByOrderIdAndPosition(anyLong(), anyList());
        verify(sellOrderRepositoryPort, times(1)).findByOrderId(anyLong());
        verify(locationExternalServicesPort, times(1)).findLocation(anyLong());
        verify(taxRepositoryPort, times(1)).findTaxByCountry(anyLong());
        verify(sellOrderRepositoryPort, times(1)).updateSubtotalTotalIva(anyLong(), anyDouble(), anyDouble(), anyDouble());
        verify(desistRepositoryPort, times(1)).saveAllNative(anyList());
        verify(statusPartsRepositoryPort, times(1)).updateStatusByProductOrderId(anyString(), anyList());
        verify(productQuotationRepositoryPort, times(1)).updateStatusByPositionPieceAndProductOrderId(anyList());
        verify(insurerRepositoryPort, times(1)).findByInsurerId(anyLong());
        verify(reserveSura, times(1)).sendPurchaseTotalReserveDesistSura(any(Notice.class));
        verify(productQuotationRepositoryPort, times(1)).updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean());
        verify(manualPurchaseRepositoryPort, times(1)).updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean());
        verify(followUp, times(1)).sendPurchasesToFollowUpDesist(anyLong(), anyList());

        String successResponseMessage = "Se desistio correctamente los productos con ids : " + desistDTO.getIds();
        assertEquals(responseDTO.getMessage(), successResponseMessage);

    }

    @Test
    void testDesistProduct_Exception() throws ExceptionUtil {

        ExceptionUtil exceptionUtil = assertThrows(ExceptionUtil.class, () -> desistProduct.desistProduct(null));

        verify(productOrderRepositoryPort, never()).updateDesistProductOrder(anyList(), anyString(), any(Timestamp.class));
        verify(sellOrderDetailRepositoryPort, never()).findAllByOrderIdAndPosition(anyLong(), anyList());
        verify(sellOrderRepositoryPort, never()).findByOrderId(anyLong());
        verify(locationExternalServicesPort, never()).findLocation(anyLong());
        verify(taxRepositoryPort, never()).findTaxByCountry(anyLong());
        verify(sellOrderRepositoryPort, never()).updateSubtotalTotalIva(anyLong(), anyDouble(), anyDouble(), anyDouble());
        verify(desistRepositoryPort, never()).saveAllNative(anyList());
        verify(statusPartsRepositoryPort, never()).updateStatusByProductOrderId(anyString(), anyList());
        verify(productQuotationRepositoryPort, never()).updateStatusByPositionPieceAndProductOrderId(anyList());
        verify(insurerRepositoryPort, never()).findByInsurerId(anyLong());
        verify(reserveSura, never()).sendPurchaseTotalReserveDesistSura(any(Notice.class));
        verify(productQuotationRepositoryPort, never()).updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean());
        verify(manualPurchaseRepositoryPort, never()).updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean());
        verify(followUp, never()).sendPurchasesToFollowUpDesist(anyLong(), anyList());


    }

    @Test
    void testCreateManualPurchaseDesist() throws ExceptionUtil {
        when(manualPurchaseRepositoryPort.findByPositionAndExternalEvent(anyInt(),anyString())).thenReturn(Optional.empty());

        desistProduct.createManualPurchaseDesist(List.of(productOrder));

        verify(manualPurchaseRepositoryPort, times(1)).findByPositionAndExternalEvent(anyInt(),anyString());
        verify(manualPurchaseRepositoryPort, times(1)).save(any(ManualPurchase.class));
    }

    @Test
    void testCreateManualPurchaseDesist_Exception() throws ExceptionUtil {

        ExceptionUtil exceptionUtil = assertThrows(ExceptionUtil.class, () -> desistProduct.createManualPurchaseDesist(null));

        verify(manualPurchaseRepositoryPort, never()).findByPositionAndExternalEvent(anyInt(),anyString());
        verify(manualPurchaseRepositoryPort, never()).save(any(ManualPurchase.class));
    }

    @Test
    void testCreateManualPurchaseDesistManualPurchaseEmpty() throws ExceptionUtil {
        when(manualPurchaseRepositoryPort.findByPositionAndExternalEvent(anyInt(),anyString())).thenReturn(Optional.of(manualPurchase));
        when(manualPurchaseRepositoryPort.updateDesistCauseAndStatusById(anyLong())).thenReturn(1);

        desistProduct.createManualPurchaseDesist(List.of(productOrder));

        verify(manualPurchaseRepositoryPort, times(1)).findByPositionAndExternalEvent(anyInt(),anyString());
        verify(manualPurchaseRepositoryPort, times(1)).updateDesistCauseAndStatusById(anyLong());
        verify(manualPurchaseRepositoryPort, never()).save(any(ManualPurchase.class));
    }

}
