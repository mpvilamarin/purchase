package com.subocol.manage.purchase.domain;

import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.services.CreateOrder;
import com.subocol.manage.purchase.domain.servicesimpl.AuthNoticeWithPieces;
import com.subocol.manage.purchase.domain.servicesimpl.ManageNoticeCC;
import com.subocol.manage.purchase.domain.servicesimpl.ManageNoticeMM;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.AuthNoticePiecesDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeClaimNumberDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesQuoteAuthDTO;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.Integrations;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOrderModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.query.InvalidJpaQueryMethodException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthNoticeWithPiecesTest {

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
    private DataEventRepositoryPort dataEventRepositoryPort;

    @Mock
    private QuotationRepositoryPort quotationRepositoryPort;

    @Mock
    private ProductOrderRepositoryPort productOrderRepositoryPort;

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private ManageNoticeCC manageNoticeCC;

    @Mock
    private ManageNoticeMM manageNoticeMM;

    @Mock
    private CreateOrder createOrder;

    @Mock
    private Integrations integrations;

    @InjectMocks
    private AuthNoticeWithPieces authNoticeWithPieces;

    private AuthNoticePiecesDTO authNoticePiecesDto;
    private Notice notice;
    private NoticeClaimNumberDTO noticeClaimNumberDTO;
    private Insurer insurer;
    private Quotation quotation;
    private ProductQuotation productQuotation;
    private Order order;
    private ProductOrder productOrder2;
    private Subsidiary subsidiary;
    private ProductOrder productOrder;

    @BeforeEach
    void setup() {

        List<PiecesQuoteAuthDTO> spareParts = new ArrayList<>();
        spareParts.add(PiecesQuoteAuthDTO.builder()
                .position(1)
                .code("06030006nitn")
                .reference("REPU")
                .quantity(1)
                .description("llanta trasero izquierdo")
                .unitValue(0D)
                .sparePartQuality("ORIGINAL")
                .build());

        spareParts.add(PiecesQuoteAuthDTO.builder()
                .position(2)
                .code("06030006nitn")
                .reference("REPU")
                .quantity(1)
                .description("llanta trasero izquierdo")
                .unitValue(0D)
                .sparePartQuality("ORIGINAL")
                .build());

        this.authNoticePiecesDto = AuthNoticePiecesDTO.builder()
                .externalEvent("999999999")
                .claimNumber("4899")
                .spareParts(spareParts)
                .build();

        this.notice = Notice.builder().auth(false).brand("FORD").line("FUSION").externalEvent(999999999)
                .id(999999999L).eventId(999999999L).workshop("WorkshopName")
                .quotationEstimatedDate(Timestamp.valueOf("2099-08-10 10:35:12"))
                .unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .date(Timestamp.valueOf("2023-08-10 10:25:12"))
                .orders(new HashSet<>())
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000001L)
                .build();

        this.noticeClaimNumberDTO = NoticeClaimNumberDTO.builder()
                .externalEvent(notice.getExternalEvent())
                .countPackages(1L)
                .countClaimEquals(1L)
                .build();

        this.productQuotation = ProductQuotation.builder().id(999999999L)
                .position(1).reference("ProductReference").netPrice(10.0).description("tapa motor")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2)
                .discountCampaigns(1.0).discountAdditional(2.0)
                .discountManual(3.0).comment("ProductComment").build();

        this.insurer = Insurer.builder().build();
        this.quotation = Quotation.builder().id(999999999L).build();

        subsidiary = Subsidiary.builder().id(10L).locationExternalId(10L).build();

        productOrder2 = ProductOrder.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(TimeZoneUtil.getTimestampByDefaultZone())
                .comment("ProductComment").positionPiece(1).status("Accepted").build();

        order = Order.builder().id(5593L).date(new Timestamp(1664579257)).subsidiary(subsidiary).status("assigned").workshop("SUBARU DEL CANAL")
                    .time(null).notice(notice).priority(0).products(Set.of(productOrder2)).reference("Order-123").comment("null").workshopDeliveryDate(null)
                    .documentUrl("https://example.com/document.pdf").orderIdDms(789).orderPurchaseDms(101112).orderPurchaseChile(131415L).orderIdSubocol("SUB123")
                    .billingServiceId(1617).unforeseen(true).repairOrder(BigDecimal.valueOf(1000)).purchaseTypeId(1819).build();

        productOrder = ProductOrder.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(new Timestamp(1664579257))
                .comment("ProductComment").positionPiece(1).status("Accepted").build();
    }

    @Test
    void testAuthThrowPreviousAuthorizedNotice() throws ExceptionUtil {
        this.notice.setAuth(Boolean.TRUE);

        //INJECTED FAILURE
        when(noticeRepositoryPort.findAllByExternalEventAndAuth(anyString(), anyBoolean())).thenReturn(Collections.singletonList(this.notice));

        Executable invocation = () -> authNoticeWithPieces.auth(authNoticePiecesDto);

        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        verify(noticeRepositoryPort, times(1)).findAllByExternalEventAndAuth(anyString(), anyBoolean());

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(ERROR_PREVIOUSLY_AUTHORIZED_NOTICE, exception.getMessage());
    }

    @Test
    void testAuthThrowEmptyPositionPieces() throws ExceptionUtil {
        //INJECTED FAILURE
        this.authNoticePiecesDto.getSpareParts().forEach(sp -> sp.setPosition(0));

        when(noticeRepositoryPort.findAllByExternalEventAndAuth(anyString(), anyBoolean())).thenReturn(Collections.emptyList());

        Executable invocation = () -> authNoticeWithPieces.auth(authNoticePiecesDto);

        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        verify(noticeRepositoryPort, times(1)).findAllByExternalEventAndAuth(anyString(), anyBoolean());

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(ERROR_EMPTY_SPARE_PARTS, exception.getMessage());
    }

    @Test
    void testAuthThrowShippedPartsDeleted() throws ExceptionUtil {

        when(noticeRepositoryPort.findAllByExternalEventAndAuth(anyString(), anyBoolean())).thenReturn(Collections.emptyList());
        //INJECTED FAILURE
        when(manualPurchaseRepositoryPort.countDeletedPiecesByPositions(anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());


        Executable invocation = () -> authNoticeWithPieces.auth(authNoticePiecesDto);

        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        verify(noticeRepositoryPort, times(1)).findAllByExternalEventAndAuth(anyString(), anyBoolean());
        verify(manualPurchaseRepositoryPort, times(1)).countDeletedPiecesByPositions(anyString(), anyList());

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(ERROR_SHIPPED_PARTS_DELETED, exception.getMessage());

    }

    @Test
    void testAuthThrowPiecesNotFound() throws ExceptionUtil {

        when(noticeRepositoryPort.findAllByExternalEventAndAuth(anyString(), anyBoolean())).thenReturn(Collections.emptyList());
        when(manualPurchaseRepositoryPort.countDeletedPiecesByPositions(anyString(), anyList())).thenReturn(0);
        //INJECTED FAILURE
        when(pieceRepositoryPort.countPiecesByExternalEventAndPositions(anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size() - 1);

        Executable invocation = () -> authNoticeWithPieces.auth(authNoticePiecesDto);

        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        verify(noticeRepositoryPort, times(1)).findAllByExternalEventAndAuth(anyString(), anyBoolean());
        verify(manualPurchaseRepositoryPort, times(1)).countDeletedPiecesByPositions(anyString(), anyList());

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(ERROR_AUTHORIZING_NOTICE_PIECE_NOT_FOUND, exception.getMessage());

    }


    @Test
    void testAuthThrowInsurerNotFound() throws ExceptionUtil {

        when(noticeRepositoryPort.findAllByExternalEventAndAuth(anyString(), anyBoolean())).thenReturn(Collections.emptyList());
        when(manualPurchaseRepositoryPort.countDeletedPiecesByPositions(anyString(), anyList())).thenReturn(0);
        when(pieceRepositoryPort.countPiecesByExternalEventAndPositions(anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(productQuotationRepositoryPort.updateAllAuthByIdInAndEventId(anyBoolean(), anyList(), anyString())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(manualPurchaseRepositoryPort.updateAuthByExternalEventAndPosition(anyBoolean(), anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(noticeRepositoryPort.findDistinctClaimNumberByExternalEvent(anyString(), anyString())).thenReturn(noticeClaimNumberDTO);
        when(noticeRepositoryPort.updateAuthByExternalEvent(anyString(), anyBoolean())).thenReturn(1);
        when(noticeRepositoryPort.findAllByExternalEvent(anyString())).thenReturn(Collections.singletonList(notice));
        //INJECTED FAILURE
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.empty());

        Executable invocation = () -> authNoticeWithPieces.auth(authNoticePiecesDto);

        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        verify(noticeRepositoryPort, times(1)).findAllByExternalEventAndAuth(anyString(), anyBoolean());
        verify(manualPurchaseRepositoryPort, times(1)).countDeletedPiecesByPositions(anyString(), anyList());
        verify(pieceRepositoryPort, times(1)).countPiecesByExternalEventAndPositions(anyString(), anyList());
        verify(productQuotationRepositoryPort, times(1)).updateAllAuthByIdInAndEventId(anyBoolean(), anyList(), anyString());
        verify(manualPurchaseRepositoryPort, times(1)).updateAuthByExternalEventAndPosition(anyBoolean(), anyString(), anyList());
        verify(noticeRepositoryPort, times(1)).findDistinctClaimNumberByExternalEvent(anyString(), anyString());
        verify(noticeRepositoryPort, times(1)).updateAuthByExternalEvent(anyString(), anyBoolean());
        verify(noticeRepositoryPort, times(1)).findAllByExternalEvent(anyString());
        verify(insurerRepositoryPort, times(1)).findByInsurerId(anyLong());

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(INSURER_NOT_FOUND, exception.getMessage());

    }

    @Test
    void testAuthSelfSupplyFlow() throws ExceptionUtil {
        notice.setWorkshopType(ManageNoticeConstant.SELF_SUPPLY);

        when(noticeRepositoryPort.findAllByExternalEventAndAuth(anyString(), anyBoolean())).thenReturn(Collections.emptyList());
        when(manualPurchaseRepositoryPort.countDeletedPiecesByPositions(anyString(), anyList())).thenReturn(0);
        when(pieceRepositoryPort.countPiecesByExternalEventAndPositions(anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(productQuotationRepositoryPort.updateAllAuthByIdInAndEventId(anyBoolean(), anyList(), anyString())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(manualPurchaseRepositoryPort.updateAuthByExternalEventAndPosition(anyBoolean(), anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(noticeRepositoryPort.findDistinctClaimNumberByExternalEvent(anyString(), anyString())).thenReturn(noticeClaimNumberDTO);
        when(noticeRepositoryPort.updateAuthByExternalEvent(anyString(), anyBoolean())).thenReturn(1);
        when(noticeRepositoryPort.findAllByExternalEvent(anyString())).thenReturn(Collections.singletonList(notice));
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(quotationRepositoryPort.findQuotationByNoticeIdAndFlowType(anyLong())).thenReturn(Optional.of(quotation));
        when(productQuotationRepositoryPort.findIdsByQuotationIdAndAuthTrue(anyLong())).thenReturn(Collections.emptyList());
        when(manageNoticeCC.manageNoticeCC(anyLong(), anyLong(), anyList(), anyBoolean())).thenReturn(Boolean.TRUE);
        when(productQuotationRepositoryPort.findAllWinnersByNotice(anyLong())).thenReturn(Collections.singletonList(productQuotation));
        when(createOrder.createOrderQuotation(any(Notice.class), anyList(), anyInt())).thenReturn(Collections.emptySet());
        when(productQuotationRepositoryPort.updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean())).thenReturn(1);
        when(manualPurchaseRepositoryPort.updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean())).thenReturn(0);
        when(dataEventRepositoryPort.updateAuthByExternalEvent(anyInt())).thenReturn(1);
        when(quotationRepositoryPort.findQuotationManagedByExternalEvent(anyString())).thenReturn(Collections.singletonList(quotation.getId()));
        when(quotationRepositoryPort.updateQuotationManaged(anyList())).thenReturn(1);
        when(orderRepositoryPort.findAllByNoticeId(anyLong())).thenReturn(List.of(order));
        when(productOrderRepositoryPort.findAllByIdOrder(anyLong())).thenReturn(List.of(productOrder));

        when(integrations.integrationByNotice(any(Notice.class), any(Insurer.class), anySet())).thenReturn(Boolean.TRUE);

        ResponseDTO responseDTO = authNoticeWithPieces.auth(authNoticePiecesDto);

        verify(noticeRepositoryPort, times(1)).findAllByExternalEventAndAuth(anyString(), anyBoolean());
        verify(manualPurchaseRepositoryPort, times(1)).countDeletedPiecesByPositions(anyString(), anyList());
        verify(pieceRepositoryPort, times(1)).countPiecesByExternalEventAndPositions(anyString(), anyList());
        verify(productQuotationRepositoryPort, times(1)).updateAllAuthByIdInAndEventId(anyBoolean(), anyList(), anyString());
        verify(manualPurchaseRepositoryPort, times(1)).updateAuthByExternalEventAndPosition(anyBoolean(), anyString(), anyList());
        verify(noticeRepositoryPort, times(1)).findDistinctClaimNumberByExternalEvent(anyString(), anyString());
        verify(noticeRepositoryPort, times(1)).updateAuthByExternalEvent(anyString(), anyBoolean());
        verify(noticeRepositoryPort, times(1)).findAllByExternalEvent(anyString());
        verify(insurerRepositoryPort, times(1)).findByInsurerId(anyLong());
        verify(quotationRepositoryPort, times(1)).findQuotationByNoticeIdAndFlowType(anyLong());
        verify(productQuotationRepositoryPort, times(1)).findIdsByQuotationIdAndAuthTrue(anyLong());
        verify(manageNoticeCC, times(1)).manageNoticeCC(anyLong(), anyLong(), anyList(), anyBoolean());
        verify(productQuotationRepositoryPort, times(1)).findAllWinnersByNotice(anyLong());
        verify(createOrder, times(1)).createOrderQuotation(any(Notice.class), anyList(), anyInt());
        verify(productQuotationRepositoryPort, times(1)).updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean());
        verify(manualPurchaseRepositoryPort, times(1)).updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean());
        verify(dataEventRepositoryPort, times(1)).updateAuthByExternalEvent(anyInt());
        verify(quotationRepositoryPort, times(1)).findQuotationManagedByExternalEvent(anyString());
        verify(quotationRepositoryPort, times(1)).updateQuotationManaged(anyList());
        verify(integrations, times(1)).integrationByNotice(any(Notice.class), any(Insurer.class), anySet());


        String successResponseMessage = "Se autorizo aviso " + notice.getExternalEvent() + " NoticeId " + notice.getId();
        assertResponseDto(responseDTO, successResponseMessage);

    }

    @Test
    void testAuthSelfSupplyFlowWithoutQuotation() throws ExceptionUtil {
        notice.setWorkshopType(ManageNoticeConstant.SELF_SUPPLY);

        when(noticeRepositoryPort.findAllByExternalEventAndAuth(anyString(), anyBoolean())).thenReturn(Collections.emptyList());
        when(manualPurchaseRepositoryPort.countDeletedPiecesByPositions(anyString(), anyList())).thenReturn(0);
        when(pieceRepositoryPort.countPiecesByExternalEventAndPositions(anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(productQuotationRepositoryPort.updateAllAuthByIdInAndEventId(anyBoolean(), anyList(), anyString())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(manualPurchaseRepositoryPort.updateAuthByExternalEventAndPosition(anyBoolean(), anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(noticeRepositoryPort.findDistinctClaimNumberByExternalEvent(anyString(), anyString())).thenReturn(noticeClaimNumberDTO);
        when(noticeRepositoryPort.updateAuthByExternalEvent(anyString(), anyBoolean())).thenReturn(1);
        when(noticeRepositoryPort.findAllByExternalEvent(anyString())).thenReturn(Collections.singletonList(notice));
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(productOrderRepositoryPort.findAllByIdOrder(anyLong())).thenReturn(List.of(productOrder));
        //CASE TRIGGER
        when(quotationRepositoryPort.findQuotationByNoticeIdAndFlowType(anyLong())).thenReturn(Optional.empty());
        when(productQuotationRepositoryPort.findAllWinnersByNotice(anyLong())).thenReturn(Collections.singletonList(productQuotation));
        when(createOrder.createOrderQuotation(any(Notice.class), anyList(), anyInt())).thenReturn(Collections.emptySet());
        when(productQuotationRepositoryPort.updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean())).thenReturn(1);
        when(manualPurchaseRepositoryPort.updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean())).thenReturn(0);
        when(dataEventRepositoryPort.updateAuthByExternalEvent(anyInt())).thenReturn(1);
        when(quotationRepositoryPort.findQuotationManagedByExternalEvent(anyString())).thenReturn(Collections.singletonList(quotation.getId()));
        when(quotationRepositoryPort.updateQuotationManaged(anyList())).thenReturn(1);
        when(orderRepositoryPort.findAllByNoticeId(anyLong())).thenReturn(List.of(order));
        when(integrations.integrationByNotice(any(Notice.class), any(Insurer.class), anySet())).thenReturn(Boolean.TRUE);

        ResponseDTO responseDTO = authNoticeWithPieces.auth(authNoticePiecesDto);

        verify(noticeRepositoryPort, times(1)).findAllByExternalEventAndAuth(anyString(), anyBoolean());
        verify(manualPurchaseRepositoryPort, times(1)).countDeletedPiecesByPositions(anyString(), anyList());
        verify(pieceRepositoryPort, times(1)).countPiecesByExternalEventAndPositions(anyString(), anyList());
        verify(productQuotationRepositoryPort, times(1)).updateAllAuthByIdInAndEventId(anyBoolean(), anyList(), anyString());
        verify(manualPurchaseRepositoryPort, times(1)).updateAuthByExternalEventAndPosition(anyBoolean(), anyString(), anyList());
        verify(noticeRepositoryPort, times(1)).findDistinctClaimNumberByExternalEvent(anyString(), anyString());
        verify(noticeRepositoryPort, times(1)).updateAuthByExternalEvent(anyString(), anyBoolean());
        verify(noticeRepositoryPort, times(1)).findAllByExternalEvent(anyString());
        verify(insurerRepositoryPort, times(1)).findByInsurerId(anyLong());
        verify(quotationRepositoryPort, times(1)).findQuotationByNoticeIdAndFlowType(anyLong());
        verify(productQuotationRepositoryPort, times(1)).findAllWinnersByNotice(anyLong());
        verify(createOrder, times(1)).createOrderQuotation(any(Notice.class), anyList(), anyInt());
        verify(productQuotationRepositoryPort, times(1)).updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean());
        verify(manualPurchaseRepositoryPort, times(1)).updatePurchaseSubsidiary(anyString(), anyList(), anyBoolean());
        verify(dataEventRepositoryPort, times(1)).updateAuthByExternalEvent(anyInt());
        verify(quotationRepositoryPort, times(1)).findQuotationManagedByExternalEvent(anyString());
        verify(quotationRepositoryPort, times(1)).updateQuotationManaged(anyList());
        verify(integrations, times(1)).integrationByNotice(any(Notice.class), any(Insurer.class), anySet());


        String successResponseMessage = "Se autorizo aviso " + notice.getExternalEvent() + " NoticeId " + notice.getId();
        assertResponseDto(responseDTO, successResponseMessage);

    }

    @Test
    void testAuthSelfSupplyFlowWithoutProductQuotationWinners() throws ExceptionUtil {
        notice.setWorkshopType(ManageNoticeConstant.SELF_SUPPLY);

        when(noticeRepositoryPort.findAllByExternalEventAndAuth(anyString(), anyBoolean())).thenReturn(Collections.emptyList());
        when(manualPurchaseRepositoryPort.countDeletedPiecesByPositions(anyString(), anyList())).thenReturn(0);
        when(pieceRepositoryPort.countPiecesByExternalEventAndPositions(anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(productQuotationRepositoryPort.updateAllAuthByIdInAndEventId(anyBoolean(), anyList(), anyString())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(manualPurchaseRepositoryPort.updateAuthByExternalEventAndPosition(anyBoolean(), anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(noticeRepositoryPort.findDistinctClaimNumberByExternalEvent(anyString(), anyString())).thenReturn(noticeClaimNumberDTO);
        when(noticeRepositoryPort.updateAuthByExternalEvent(anyString(), anyBoolean())).thenReturn(1);
        when(noticeRepositoryPort.findAllByExternalEvent(anyString())).thenReturn(Collections.singletonList(notice));
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(quotationRepositoryPort.findQuotationByNoticeIdAndFlowType(anyLong())).thenReturn(Optional.of(quotation));
        when(orderRepositoryPort.findAllByNoticeId(anyLong())).thenReturn(List.of(order));
        when(productOrderRepositoryPort.findAllByIdOrder(anyLong())).thenReturn(List.of(productOrder));
        //CASE TRIGGER
        when(productQuotationRepositoryPort.findIdsByQuotationIdAndAuthTrue(anyLong())).thenReturn(Collections.emptyList());
        when(manageNoticeCC.manageNoticeCC(anyLong(), anyLong(), anyList(), anyBoolean())).thenReturn(Boolean.TRUE);
        when(productQuotationRepositoryPort.findAllWinnersByNotice(anyLong())).thenReturn(Collections.emptyList());
        when(dataEventRepositoryPort.updateAuthByExternalEvent(anyInt())).thenReturn(1);
        when(quotationRepositoryPort.findQuotationManagedByExternalEvent(anyString())).thenReturn(Collections.singletonList(quotation.getId()));
        when(quotationRepositoryPort.updateQuotationManaged(anyList())).thenReturn(1);
        when(integrations.integrationByNotice(any(Notice.class), any(Insurer.class), anySet())).thenReturn(Boolean.TRUE);

        ResponseDTO responseDTO = authNoticeWithPieces.auth(authNoticePiecesDto);

        verify(noticeRepositoryPort, times(1)).findAllByExternalEventAndAuth(anyString(), anyBoolean());
        verify(manualPurchaseRepositoryPort, times(1)).countDeletedPiecesByPositions(anyString(), anyList());
        verify(pieceRepositoryPort, times(1)).countPiecesByExternalEventAndPositions(anyString(), anyList());
        verify(productQuotationRepositoryPort, times(1)).updateAllAuthByIdInAndEventId(anyBoolean(), anyList(), anyString());
        verify(manualPurchaseRepositoryPort, times(1)).updateAuthByExternalEventAndPosition(anyBoolean(), anyString(), anyList());
        verify(noticeRepositoryPort, times(1)).findDistinctClaimNumberByExternalEvent(anyString(), anyString());
        verify(noticeRepositoryPort, times(1)).updateAuthByExternalEvent(anyString(), anyBoolean());
        verify(noticeRepositoryPort, times(1)).findAllByExternalEvent(anyString());
        verify(insurerRepositoryPort, times(1)).findByInsurerId(anyLong());
        verify(quotationRepositoryPort, times(1)).findQuotationByNoticeIdAndFlowType(anyLong());
        verify(productQuotationRepositoryPort, times(1)).findIdsByQuotationIdAndAuthTrue(anyLong());
        verify(manageNoticeCC, times(1)).manageNoticeCC(anyLong(), anyLong(), anyList(), anyBoolean());
        verify(productQuotationRepositoryPort, times(1)).findAllWinnersByNotice(anyLong());
        verify(dataEventRepositoryPort, times(1)).updateAuthByExternalEvent(anyInt());
        verify(quotationRepositoryPort, times(1)).findQuotationManagedByExternalEvent(anyString());
        verify(quotationRepositoryPort, times(1)).updateQuotationManaged(anyList());
        verify(integrations, times(1)).integrationByNotice(any(Notice.class), any(Insurer.class), anySet());


        String successResponseMessage = "Se autorizo aviso " + notice.getExternalEvent() + " NoticeId " + notice.getId();
        assertResponseDto(responseDTO, successResponseMessage);

    }

    @Test
    void testAuthAutomaticFlow() throws ExceptionUtil {
        notice.setWorkshopType(ManageNoticeConstant.AUTOMATIC)
                .setQuotationEstimatedDate(Timestamp.valueOf("2023-08-10 10:24:12"));

        when(noticeRepositoryPort.findAllByExternalEventAndAuth(anyString(), anyBoolean())).thenReturn(Collections.emptyList());
        when(manualPurchaseRepositoryPort.countDeletedPiecesByPositions(anyString(), anyList())).thenReturn(0);
        when(pieceRepositoryPort.countPiecesByExternalEventAndPositions(anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(productQuotationRepositoryPort.updateAllAuthByIdInAndEventId(anyBoolean(), anyList(), anyString())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(manualPurchaseRepositoryPort.updateAuthByExternalEventAndPosition(anyBoolean(), anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(noticeRepositoryPort.findDistinctClaimNumberByExternalEvent(anyString(), anyString())).thenReturn(noticeClaimNumberDTO);
        when(noticeRepositoryPort.updateAuthByExternalEvent(anyString(), anyBoolean())).thenReturn(1);
        when(noticeRepositoryPort.findAllByExternalEvent(anyString())).thenReturn(Collections.singletonList(notice));
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(manageNoticeMM.manageNoticeByNoticeId(anyLong(), anyBoolean())).thenReturn(Boolean.TRUE);
        when(dataEventRepositoryPort.updateAuthByExternalEvent(anyInt())).thenReturn(1);
        when(quotationRepositoryPort.findQuotationManagedByExternalEvent(anyString())).thenReturn(Collections.singletonList(quotation.getId()));
        when(quotationRepositoryPort.updateQuotationManaged(anyList())).thenReturn(1);
        when(orderRepositoryPort.findAllByNoticeId(anyLong())).thenReturn(List.of(order));
        when(productOrderRepositoryPort.findAllByIdOrder(anyLong())).thenReturn(List.of(productOrder));
        when(integrations.integrationByNotice(any(Notice.class), any(Insurer.class), anySet())).thenReturn(Boolean.TRUE);

        ResponseDTO responseDTO = authNoticeWithPieces.auth(authNoticePiecesDto);

        verify(noticeRepositoryPort, times(1)).findAllByExternalEventAndAuth(anyString(), anyBoolean());
        verify(manualPurchaseRepositoryPort, times(1)).countDeletedPiecesByPositions(anyString(), anyList());
        verify(pieceRepositoryPort, times(1)).countPiecesByExternalEventAndPositions(anyString(), anyList());
        verify(productQuotationRepositoryPort, times(1)).updateAllAuthByIdInAndEventId(anyBoolean(), anyList(), anyString());
        verify(manualPurchaseRepositoryPort, times(1)).updateAuthByExternalEventAndPosition(anyBoolean(), anyString(), anyList());
        verify(noticeRepositoryPort, times(1)).findDistinctClaimNumberByExternalEvent(anyString(), anyString());
        verify(noticeRepositoryPort, times(1)).updateAuthByExternalEvent(anyString(), anyBoolean());
        verify(noticeRepositoryPort, times(1)).findAllByExternalEvent(anyString());
        verify(insurerRepositoryPort, times(1)).findByInsurerId(anyLong());
        verify(manageNoticeMM, times(1)).manageNoticeByNoticeId(anyLong(), anyBoolean());
        verify(dataEventRepositoryPort, times(1)).updateAuthByExternalEvent(anyInt());
        verify(quotationRepositoryPort, times(1)).findQuotationManagedByExternalEvent(anyString());
        verify(quotationRepositoryPort, times(1)).updateQuotationManaged(anyList());
        verify(integrations, times(1)).integrationByNotice(any(Notice.class), any(Insurer.class), anySet());


        String successResponseMessage = "Se autorizo aviso " + notice.getExternalEvent() + " NoticeId " + notice.getId();
        assertResponseDto(responseDTO, successResponseMessage);

    }

    @Test
    void testAuthWithoutGoingSelfSupplyAndAutomaticFlows() throws ExceptionUtil {
        notice.setWorkshopType(ManageNoticeConstant.AUTOMATIC).setDate(Timestamp.valueOf("2099-12-24 11:10:12"));

        when(noticeRepositoryPort.findAllByExternalEventAndAuth(anyString(), anyBoolean())).thenReturn(Collections.emptyList());
        when(manualPurchaseRepositoryPort.countDeletedPiecesByPositions(anyString(), anyList())).thenReturn(0);
        when(pieceRepositoryPort.countPiecesByExternalEventAndPositions(anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(productQuotationRepositoryPort.updateAllAuthByIdInAndEventId(anyBoolean(), anyList(), anyString())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(manualPurchaseRepositoryPort.updateAuthByExternalEventAndPosition(anyBoolean(), anyString(), anyList())).thenReturn(authNoticePiecesDto.getSpareParts().size());
        when(noticeRepositoryPort.findDistinctClaimNumberByExternalEvent(anyString(), anyString())).thenReturn(noticeClaimNumberDTO);
        when(noticeRepositoryPort.updateAuthByExternalEvent(anyString(), anyBoolean())).thenReturn(1);
        when(noticeRepositoryPort.findAllByExternalEvent(anyString())).thenReturn(Collections.singletonList(notice));
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(dataEventRepositoryPort.updateAuthByExternalEvent(anyInt())).thenReturn(1);
        when(quotationRepositoryPort.findQuotationManagedByExternalEvent(anyString())).thenReturn(Collections.singletonList(quotation.getId()));
        when(quotationRepositoryPort.updateQuotationManaged(anyList())).thenReturn(1);
        when(orderRepositoryPort.findAllByNoticeId(anyLong())).thenReturn(List.of(order));
        when(productOrderRepositoryPort.findAllByIdOrder(anyLong())).thenReturn(List.of(productOrder));
        when(integrations.integrationByNotice(any(Notice.class), any(Insurer.class), anySet())).thenReturn(Boolean.TRUE);

        ResponseDTO responseDTO = authNoticeWithPieces.auth(authNoticePiecesDto);

        verify(noticeRepositoryPort, times(1)).findAllByExternalEventAndAuth(anyString(), anyBoolean());
        verify(manualPurchaseRepositoryPort, times(1)).countDeletedPiecesByPositions(anyString(), anyList());
        verify(pieceRepositoryPort, times(1)).countPiecesByExternalEventAndPositions(anyString(), anyList());
        verify(productQuotationRepositoryPort, times(1)).updateAllAuthByIdInAndEventId(anyBoolean(), anyList(), anyString());
        verify(manualPurchaseRepositoryPort, times(1)).updateAuthByExternalEventAndPosition(anyBoolean(), anyString(), anyList());
        verify(noticeRepositoryPort, times(1)).findDistinctClaimNumberByExternalEvent(anyString(), anyString());
        verify(noticeRepositoryPort, times(1)).updateAuthByExternalEvent(anyString(), anyBoolean());
        verify(noticeRepositoryPort, times(1)).findAllByExternalEvent(anyString());
        verify(insurerRepositoryPort, times(1)).findByInsurerId(anyLong());
        verify(dataEventRepositoryPort, times(1)).updateAuthByExternalEvent(anyInt());
        verify(quotationRepositoryPort, times(1)).findQuotationManagedByExternalEvent(anyString());
        verify(quotationRepositoryPort, times(1)).updateQuotationManaged(anyList());
        verify(integrations, times(1)).integrationByNotice(any(Notice.class), any(Insurer.class), anySet());


        String successResponseMessage = "Se autorizo aviso " + notice.getExternalEvent() + " NoticeId " + notice.getId();
        assertResponseDto(responseDTO, successResponseMessage);

    }

    private void assertResponseDto(ResponseDTO responseDTO, String successResponseMessage) {
        assertEquals(successResponseMessage, responseDTO.getMessage());
        assertEquals(200, responseDTO.getStatus());
        Assertions.assertTrue(responseDTO.isSuccess());
        assertThat(responseDTO.getData()).isExactlyInstanceOf(HashMap.class);

        HashMap<String, Object> dataResponse = (HashMap<String, Object>) responseDTO.getData();

        Assertions.assertTrue(dataResponse.containsKey(notice.getId().toString()));

        Map<String, String> dataResponseNotice = (Map<String, String>) dataResponse.get(notice.getId().toString());

        assertEquals("200", dataResponseNotice.get("status"));
        assertEquals(successResponseMessage, dataResponseNotice.get("message"));
    }

    @Test
    void testUpdateClaimNumberReturnTrue() throws ExceptionUtil {

        this.noticeClaimNumberDTO.setCountPackages(2L);

        when(noticeRepositoryPort.findDistinctClaimNumberByExternalEvent(anyString(), anyString())).thenReturn(noticeClaimNumberDTO);
        when(dataEventRepositoryPort.updateClaimNumberByExternalEvent(anyString(), anyString())).thenReturn(1);
        when(noticeRepositoryPort.updateClaimNumberByExternalEvent(anyString(), anyString())).thenReturn(2);

        boolean updateClaimNumberResult = authNoticeWithPieces.updateClaimNumber(authNoticePiecesDto.getExternalEvent(), authNoticePiecesDto.getClaimNumber());

        verify(noticeRepositoryPort, times(1)).findDistinctClaimNumberByExternalEvent(anyString(), anyString());
        verify(dataEventRepositoryPort, times(1)).updateClaimNumberByExternalEvent(anyString(), anyString());
        verify(noticeRepositoryPort, times(1)).updateClaimNumberByExternalEvent(anyString(), anyString());

        Assertions.assertTrue(updateClaimNumberResult);

    }

    @Test
    void testUpdateClaimNumberWithNullDtoReturnTrue() throws ExceptionUtil {

        when(noticeRepositoryPort.findDistinctClaimNumberByExternalEvent(anyString(), anyString())).thenReturn(null);

        boolean updateClaimNumberResult = authNoticeWithPieces.updateClaimNumber(authNoticePiecesDto.getExternalEvent(), authNoticePiecesDto.getClaimNumber());

        verify(noticeRepositoryPort, times(1)).findDistinctClaimNumberByExternalEvent(anyString(), anyString());

        Assertions.assertTrue(updateClaimNumberResult);

    }

    @Test
    void testUpdateClaimNumberWithNullDtoReturnFalse() throws ExceptionUtil {
        //INJECTED FAILURE
        when(noticeRepositoryPort.findDistinctClaimNumberByExternalEvent(anyString(), anyString())).thenThrow(new InvalidJpaQueryMethodException("Query could not executed"));

        boolean updateClaimNumberResult = authNoticeWithPieces.updateClaimNumber(authNoticePiecesDto.getExternalEvent(), authNoticePiecesDto.getClaimNumber());

        verify(noticeRepositoryPort, times(1)).findDistinctClaimNumberByExternalEvent(anyString(), anyString());

        Assertions.assertFalse(updateClaimNumberResult);

    }

}
