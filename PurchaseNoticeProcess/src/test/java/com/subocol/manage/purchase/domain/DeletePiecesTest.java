package com.subocol.manage.purchase.domain;

import com.subocol.manage.purchase.application.dtos.DeletePiecesManualPurchaseDTO;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.externalservices.FollowUpPort;
import com.subocol.manage.purchase.domain.ports.externalservices.ReserveCalculationPort;
import com.subocol.manage.purchase.domain.ports.externalservices.ReserveSuraPort;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.servicesimpl.DeletePieces;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletePiecesTest {

    @Mock
    private NoticeRepositoryPort noticeRepositoryPort;
    @Mock
    private ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;

    @Mock
    private PieceRepositoryPort pieceRepositoryPort;
    @Mock
    private ProductQuotationRepositoryPort productQuotationRepositoryPort;

    @Mock
    private InsurerRepositoryPort insurerRepositoryPort;

    @Mock
    private SendReserveManageRepositoryPort sendReserveManageRepositoryPort;
    @Mock
    private ProductOrderRepositoryPort productOrderRepositoryPort;

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private StatusPartsRepositoryPort statusPartsRepositoryPort;


    @Mock
    private SellOrderDetailRepositoryPort sellOrderDetailRepositoryPort;

    @Mock
    private SellOrderRepositoryPort sellOrderRepositoryPort;

    @Mock
    private FollowUpPort followUp;

    @Mock
    private ReserveSuraPort reserveSuraPort;
    @Mock
    private ReserveCalculationPort reserveCalculationPort;


    @Mock
    private TaxRepositoryPort taxRepositoryPort;

    @Mock
    private DeletedPiecesHistoryPort deletedPiecesHistoryPort;

    @Mock
    private ProductOrdersPiecesNoticeRepositoryPort productOrdersPiecesNoticeRepositoryPort;

    @InjectMocks
    private DeletePieces deletePieces;

    ProductOrder productOrder;

    Order order;

    Notice noticeSura;
    Insurer insurerSura;
    Notice noticeBolivar;
    Insurer insurerBolivar;

    SellOrder sellOrder;

    Subsidiary subsidiary;

    Location location;

    Tax tax;

    DeletePiecesManualPurchaseDTO deletePiecesManualPurchaseDTO;
    Map<String, Long> countsMap = new HashMap<>();

    SendReserveManage sendReserveManage;
    ReserveCalculationTotalSuraDTO reserveCalculationTotalSuraDTO;
    List<ReserveRepuestosSuraDTO> reserveRepuestosSuraDTOS=new ArrayList<>();
    SpareDetailToFollowUpDTO spareDetailToFollowUpDTO1;
    SpareDetailToFollowUpDTO spareDetailToFollowUpDTO2;
    SpareDetailToFollowUpDTO spareDetailToFollowUpDTO3;
    @BeforeEach
    void setup() {
        deletePiecesManualPurchaseDTO= DeletePiecesManualPurchaseDTO.builder()
                .positionPiece(1).userName("Jhon117").externalEvent("1234").deletedCause("This is a test!").build();

        subsidiary = Subsidiary.builder().id(10L).locationExternalId(10L).build();

        noticeSura = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(123)
                .id(789L).eventId(456L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000001L).build();

        noticeBolivar = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(123)
                .id(789L).eventId(456L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000002L).build();

        productOrder = ProductOrder.builder().id(54641L).price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(TimeZoneUtil.getTimestampByDefaultZone())
                .comment("ProductComment").positionPiece(1).status("Accepted").order(order).build();

        order = Order.builder().id(5593L).date(new Timestamp(1664579257)).status("assigned").workshop("SUBARU DEL CANAL")
                .time(null).notice(noticeSura).priority(0).reference("Order-123").comment("null").workshopDeliveryDate(null)
                .documentUrl("https://example.com/document.pdf").orderIdDms(789).orderPurchaseDms(101112).orderPurchaseChile(131415L).orderIdSubocol("SUB123")
                .billingServiceId(1617).unforeseen(true).repairOrder(BigDecimal.valueOf(1000)).purchaseTypeId(1819).subsidiary(subsidiary).build();

        productOrder.setOrder(order);

        insurerSura = Insurer.builder().id(5345L).name("SURA")
                .insurerId(500000001L).countryId(5L).sdkActive(false).multimedia(true)
                .priceToUse("autosuministro/genuino/precio_medio").newSuggestedReferenceParameter(true).allowMaxCostPiece(true)
                .daysUpdateSuggestedReference(7L).useGrossPriceCostoverrun(false).usePriceList(false)
                .prioritizePriceList(false).useOrbikaValuation(true).flowReserveSura(true).sendOrderFact(true).sdkActive(true).sendOrderEmailWinner(true).build();

        insurerBolivar = Insurer.builder().id(5345L).name("BOLIVAR")
                .insurerId(500000002L).countryId(5L).sdkActive(false).multimedia(true)
                .priceToUse("autosuministro/genuino/precio_medio").newSuggestedReferenceParameter(true).allowMaxCostPiece(true)
                .daysUpdateSuggestedReference(7L).useGrossPriceCostoverrun(false).usePriceList(false)
                .prioritizePriceList(false).useOrbikaValuation(true).flowReserveSura(false).flowReserveBolivar(true)
                .sendOrderFact(true).sdkActive(true).sendOrderEmailWinner(true).build();

        SellOrderDetail sellOrderDetail1=SellOrderDetail.builder()
                .total(50D).amount(1).description("Tapa motor").comment("")
                .discount(10D).promiseDelivery(Timestamp.valueOf(LocalDateTime.now()))
                .grossPrice(60D).positionPiece(1).reference("455667165").unitPrice(60D)
                .build();

        sellOrder = SellOrder.builder().id(1L).order(order).creationDate(new Timestamp(1664579257))
                .lastUpdateDate(new Timestamp(1664579257)).subtotal(100.0).iva(10.0).total(110.0)
                .pdfUrl("https://example.com/sellorder.pdf").details(Set.of(sellOrderDetail1)).build();

        location = Location.builder().id(1L).countryId(1L).build();

        tax=Tax.builder().id(300L).taxName("iva").countryId(1L).percentage(19).build();

        countsMap.put("countDesisted", 1L); // Ejemplo de valores simulados
        countsMap.put("totalCount", 1L);
        sendReserveManage = SendReserveManage.builder().id(5415L).externalEvent(97345).initCarSended(true).date(new Timestamp(1664579257)).build();

        reserveCalculationTotalSuraDTO = new ReserveCalculationTotalSuraDTO(100.0, 110.0, 10.0);

        reserveRepuestosSuraDTOS.add(new ReserveRepuestosSuraDTO(
                "123", 1, "Pieza1", "REF1", 10, 50.0, 5.0, 10.0, "ORIGEN1", true, 15.0, "PROV1", 10D
        ));
        reserveRepuestosSuraDTOS.add(new ReserveRepuestosSuraDTO(
                "456", 2, "Pieza2", "REF2", 20, 30.0, 3.0, 5.0, "ORIGEN2", false, 10.0, "PROV2", 10D
        ));
        spareDetailToFollowUpDTO1= SpareDetailToFollowUpDTO.builder()
                .cantidad(1).esCotizado(true)
                .posicion(1).deleted(false)
                .build();

        spareDetailToFollowUpDTO2=SpareDetailToFollowUpDTO.builder()
                .cantidad(1).esCotizado(true)
                .posicion(2).deleted(false)
                .build();

        spareDetailToFollowUpDTO3=SpareDetailToFollowUpDTO.builder()
                .cantidad(1).esCotizado(true)
                .posicion(2).deleted(false)
                .build();
    }

    @Test
    void testDeletePieces_SuraAndSendReserveManageIsNull() throws ExceptionUtil {
        when(noticeRepositoryPort.findAllByExternalEvent(anyString())).thenReturn(List.of(noticeSura));

        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurerSura));
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(1L);
        when(pieceRepositoryPort.findInitialPiecesByExternalEventSuraConditionTrue(anyInt(), anyBoolean())).thenReturn(List.of(1, 2, 3));
        when(pieceRepositoryPort.findInitialPiecesByExternalWithOrdersSura(anyInt(), anyBoolean())).thenReturn(List.of(3));
//        when(sendReserveManageRepositoryPort.findByExternalEvent(anyInt())).thenReturn(Optional.of(sendReserveManage));

        when(noticeRepositoryPort.findInfoFollowUpByExternalEventAndPositionsNoAut(anyLong())).thenReturn(List.of(spareDetailToFollowUpDTO1, spareDetailToFollowUpDTO2, spareDetailToFollowUpDTO3));

        boolean result = deletePieces.deletePieces(deletePiecesManualPurchaseDTO);

            assertTrue(result);

            verify(productQuotationRepositoryPort, times(1)).updateDeletePiecesTrueByExternalEventAndPosition(anyString(), anyInt());
            verify(insurerRepositoryPort, times(2)).findByInsurerId(anyLong());
            verify(noticeRepositoryPort, times(4)).countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean());
            verify(pieceRepositoryPort, times(1)).findInitialPiecesByExternalEventSuraConditionTrue(anyInt(), anyBoolean());
            verify(pieceRepositoryPort, times(1)).findInitialPiecesByExternalWithOrdersSura(anyInt(), anyBoolean());
            verify(sendReserveManageRepositoryPort, times(1)).findByExternalEvent(anyInt());

            verify(reserveSuraPort, times(1)).sendPiecesReserveAdminSura(any(ReserveCalculationSuraDTO.class));
            verify(followUp, times(1)).sendDataToSFollowUp(any(SendSparesToFollowUPDTO.class));


        }

    @Test
    void testDeletePieces_BolivarAndSendReserveManageIsNull() throws ExceptionUtil {
        when(noticeRepositoryPort.findAllByExternalEvent(anyString())).thenReturn(List.of(noticeBolivar));

        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurerBolivar));
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(1L);
        when(pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionTrue(anyInt(), anyBoolean())).thenReturn(List.of(1, 2, 3));
        when(pieceRepositoryPort.findInitialPiecesByExternalWithOrders(anyInt(), anyBoolean())).thenReturn(List.of(3));
        when(productOrderRepositoryPort.countProductOrderByExternalEventAndPosition(anyInt(), anyList())).thenReturn(3L);
        //        when(sendReserveManageRepositoryPort.findByExternalEvent(anyInt())).thenReturn(Optional.of(sendReserveManage));

        when(noticeRepositoryPort.findInfoFollowUpByExternalEventAndPositionsNoAut(anyLong())).thenReturn(List.of(spareDetailToFollowUpDTO1, spareDetailToFollowUpDTO2, spareDetailToFollowUpDTO3));

        boolean result = deletePieces.deletePieces(deletePiecesManualPurchaseDTO);

        assertTrue(result);

        verify(productQuotationRepositoryPort, times(1)).updateDeletePiecesTrueByExternalEventAndPosition(anyString(), anyInt());
        verify(insurerRepositoryPort, times(2)).findByInsurerId(anyLong());
        verify(noticeRepositoryPort, times(3)).countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean());
        verify(pieceRepositoryPort, times(3)).findInitialPiecesByExternalEventBolivarConditionTrue(anyInt(), anyBoolean());
        verify(pieceRepositoryPort, times(1)).findInitialPiecesByExternalWithOrders(anyInt(), anyBoolean());
        verify(sendReserveManageRepositoryPort, times(1)).findByExternalEvent(anyInt());
        verify(productOrderRepositoryPort, times(1)).countProductOrderByExternalEventAndPosition(anyInt(), anyList());
//        verify(reserveCalculationPort, times(1)).sendReserveCalculationAdmin(any(ReserveCalculationDTO.class));
        verify(followUp, times(1)).sendDataToSFollowUp(any(SendSparesToFollowUPDTO.class));


    }
    @Test
    void calculateValuesReserveSuraSuccess(){
        when(productOrderRepositoryPort.totalGrossPriceOrdersByExternalEventAndEventId(
                anyInt(), anyBoolean(), anyList(), anyBoolean())).thenReturn(reserveCalculationTotalSuraDTO);
        when(productOrderRepositoryPort.findPiecesOrdersByExternalEvent(
                anyInt(), anyBoolean(), anyList())).thenReturn(reserveRepuestosSuraDTOS);


        ReserveCalculationSuraDTO result = deletePieces.calculateValuesReserveSura(noticeSura, List.of(1,2,3), List.of(4,5));

        assertEquals(reserveRepuestosSuraDTOS,result.getRepuesto());
        assertEquals(noticeSura.getExternalEvent(), result.getNumeroAviso());
        assertEquals(reserveCalculationTotalSuraDTO,result.getPedidoInicial());
        assertEquals(reserveCalculationTotalSuraDTO,result.getImprevistos());

        verify(productOrderRepositoryPort, times(2))
                .totalGrossPriceOrdersByExternalEventAndEventId(anyInt(),anyBoolean(), anyList(),anyBoolean());
        verify(productOrderRepositoryPort, times(1))
                .findPiecesOrdersByExternalEvent(anyInt(), anyBoolean(), anyList());

    }


    @Test
    void testDeletePieces_Exception() throws ExceptionUtil {

        String messageException="this is a exception!";
        when(deletedPiecesHistoryPort.save(any(DeletedPiecesHistory.class))).thenThrow(new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), messageException));

        ExceptionUtil exceptionUtil = assertThrows(ExceptionUtil.class, () ->  deletePieces.deletePieces(deletePiecesManualPurchaseDTO));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
        assertEquals(messageException, exceptionUtil.getMessage());
        verify(productQuotationRepositoryPort, never()).updateDeletePiecesTrueByExternalEventAndPosition(anyString(), anyInt());
        verify(insurerRepositoryPort, never()).findByInsurerId(anyLong());
        verify(noticeRepositoryPort, never()).countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean());
        verify(pieceRepositoryPort, never()).findInitialPiecesByExternalEventBolivarConditionTrue(anyInt(), anyBoolean());
        verify(pieceRepositoryPort, never()).findInitialPiecesByExternalWithOrders(anyInt(), anyBoolean());
        verify(sendReserveManageRepositoryPort, never()).findByExternalEvent(anyInt());
        verify(productOrderRepositoryPort, never()).countProductOrderByExternalEventAndPosition(anyInt(), anyList());
        verify(followUp, never()).sendDataToSFollowUp(any(SendSparesToFollowUPDTO.class));

    }

    @Test
    void createSendReserveManage_withException(){
        String messageException="this is a exception!";
        when(sendReserveManageRepositoryPort.save(any(SendReserveManage.class))).thenThrow(new RuntimeException( messageException));

        ExceptionUtil exceptionUtil = assertThrows(ExceptionUtil.class, () ->  deletePieces.createSendReserveManage(123));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
        assertEquals(ErrorMessageHandler.ERROR_CREATING_SEND_RESERVE, exceptionUtil.getMessage());
        assertEquals(messageException, exceptionUtil.getEx());
    }

    @Test
    void validateInitPositions_withException(){
        String messageException="this is a exception!";
        when(pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(), anyBoolean())).thenThrow(new RuntimeException( messageException));
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(2L);

        ExceptionUtil exceptionUtil = assertThrows(ExceptionUtil.class, () ->  deletePieces.validateInitPositions(123, true));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
        assertEquals(ErrorMessageHandler.ERROR_VALIDATING_INIT_POSITIONS, exceptionUtil.getMessage());
        assertEquals(messageException, exceptionUtil.getEx());
    }

    @Test
    void validateReserveSura_withException(){

        when(noticeRepositoryPort.findAllByExternalEvent(anyString())).thenThrow(new RuntimeException());
        boolean result =  deletePieces.validateReserveSura( "123");
        assertFalse(result);
        verify(insurerRepositoryPort, never()).findByInsurerId(anyLong());
        verify(noticeRepositoryPort, never()).countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean());
        verify(pieceRepositoryPort, never()).findInitialPiecesByExternalEventBolivarConditionTrue(anyInt(), anyBoolean());
        verify(pieceRepositoryPort, never()).findInitialPiecesByExternalWithOrders(anyInt(), anyBoolean());

    }

    @Test
    void calculateValuesReserve_withException(){


        ExceptionUtil exceptionUtil = assertThrows(ExceptionUtil.class, () ->  deletePieces.calculateValuesReserve(noticeSura, List.of(), null));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exceptionUtil.getCode());
        assertEquals(ErrorMessageHandler.ERROR_CALCULATING_VALUES_RESERVE, exceptionUtil.getMessage());

    }
    @Test
    void testCalculateValuesReserveSura_withException() throws Exception{
        // Given-preconditions

        String messageException="this is a exception!";
        when(productOrderRepositoryPort.findPiecesOrdersByExternalEvent(anyInt(), anyBoolean(), anyList())).thenThrow(new RuntimeException(messageException));

        // When-Action to do
        ReserveCalculationSuraDTO result=
                deletePieces.calculateValuesReserveSura(noticeSura, List.of(1,2), List.of(3,4));

        // then-validations
        assertNull(result.getNumeroAviso());
        assertNull(result.getPedidoInicial());
        assertNull(result.getImprevistos());
        assertNull(result.getRepuesto());
        verify(productOrderRepositoryPort, times(2)).totalGrossPriceOrdersByExternalEventAndEventId(anyInt(), anyBoolean(), anyList(), anyBoolean());
   }

    @Test
    void validateInitPositionsSura_withException(){
        String messageException="this is a exception!";
        when(pieceRepositoryPort.findInitialPiecesByExternalEventBolivarConditionFalse(anyInt(), anyBoolean())).thenThrow(new RuntimeException( messageException));
        when(noticeRepositoryPort.countAllByExternalEventAndUnforeseen(anyInt(), anyBoolean())).thenReturn(2L);

        List<Integer> result=deletePieces.validateInitPositionsSura(123, true);
        assertEquals(Collections.emptyList(), result);
        verify(pieceRepositoryPort, never()).findInitialPiecesByExternalWithOrdersSura(anyInt(), anyBoolean());

    }

    @Test
    void sendSparePartInformationFollowUpDeleted_withException(){
        when(noticeRepositoryPort.findAllByExternalEvent(anyString())).thenThrow(new RuntimeException());

        deletePieces.sendSparePartInformationFollowUpDeleted(123L, 1);
        verify(noticeRepositoryPort, never()).findInfoFollowUpByExternalEventAndPositions(anyLong(), anyList(), anyBoolean(), anyBoolean());
        verify(noticeRepositoryPort, never()).findInfoFollowUpByExternalEventAndPositionsNoAut(anyLong());

    }

    @Test
    void createRequestAndSendInformationFollowUp_withException(){
        when(followUp.sendDataToSFollowUp(any(SendSparesToFollowUPDTO.class))).thenThrow(new RuntimeException());

        boolean result=deletePieces.createRequestAndSendInformationFollowUp(123L, List.of(spareDetailToFollowUpDTO1));
        assertFalse(result);

    }
}