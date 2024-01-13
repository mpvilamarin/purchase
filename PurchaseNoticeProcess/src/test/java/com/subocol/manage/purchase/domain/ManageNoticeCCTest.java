package com.subocol.manage.purchase.domain;

import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.services.CreateOrder;
import com.subocol.manage.purchase.domain.services.ValidateProductFinishTimer;
import com.subocol.manage.purchase.domain.servicesimpl.ManageNoticeCC;
import com.subocol.manage.purchase.domain.servicesimpl.SendManualPurchaseImpl;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterStatusProductQuotation;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.Integrations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageNoticeCCTest {

    @Mock
    private ValidateProductFinishTimer validateProductFinishTimer;
    @Mock
    private CreateOrder createOrder;
    @Mock
    private NoticeRepositoryPort noticeRepositoryPort;
    @Mock
    private ProductQuotationRepositoryPort productQuotationRepositoryPort;
    @Mock
    private ManualPurchaseRepositoryPort manualPurchaseRepositoryPort;
    @Mock
    private InsurerRepositoryPort insurerRepositoryPort;
    @Mock
    private QuotationRepositoryPort quotationRepositoryPort;
    @Mock
    private  ProductOverrunCostRepositoryPort productOverrunCostRepositoryPort;
    @Mock
    private SendManualPurchaseImpl sendManualPurchase;
    @Mock
    private Integrations integrations;

    @InjectMocks
    private ManageNoticeCC manageNoticeCC;

    Long noticeId=454L;
    Notice notice;
    Insurer insurer;
    ProductQuotation productQuotation1;
    ProductQuotation productQuotation2;
    ProductQuotation productQuotation3;
    ProductQuotation productQuotation4;
    ProductOverrunCost productOverrunCost;
    ProductOrder productOrder1;
    Quotation quotation;
    Order order;
    CounterStatusProductQuotation counterStatusProductQuotation1;
    CounterStatusProductQuotation counterStatusProductQuotation2;
    CounterStatusProductQuotation counterStatusProductQuotation3;
    CounterStatusProductQuotation counterStatusProductQuotation4;

    @BeforeEach
    void setup(){
        notice = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(123)
                .id(789L).eventId(456L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000001L).build();

        insurer= Insurer.builder().totManual(true).newSuggestedReferenceParameter(true).cantRefToShow(2).build();

        productQuotation1 = ProductQuotation.builder().id(1L)
                .position(1).reference("ProductReference").netPrice(10.0).description("tapa motor")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2)
                .discountCampaigns(1.0).discountAdditional(2.0)
                .discountManual(3.0).comment("ProductComment").build();

        productQuotation2 = ProductQuotation.builder().id(2L)
                .position(2).reference("reference").netPrice(10.0).description("tapa motor")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2)
                .discountCampaigns(1.0).discountAdditional(2.0)
                .discountManual(3.0).comment("ProductComment").build();

        productQuotation3 = ProductQuotation.builder().id(3L)
                .position(3).reference("ProductReference").netPrice(10.0).description("tapa motor")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2)
                .discountCampaigns(1.0).discountAdditional(2.0)
                .discountManual(3.0).comment("ProductComment").build();

        productQuotation4 = ProductQuotation.builder().id(4L)
                .position(4).reference("reference").netPrice(10.0).description("tapa motor")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2)
                .discountCampaigns(1.0).discountAdditional(2.0)
                .discountManual(3.0).comment("ProductComment").build();

        quotation= Quotation.builder().providerName("ADVANCE MOTORS SA").nit("51316211150549-0").quotationSubsidiaryName("ProbandoSucursal").id(5415L)
                .replacementReference("No aplica").unities(10).price(99.99).quality("").importation(false).timeDelivery(7)
                .observations("").brand("Toyota").line("COROLLA").city("BOCAS DEL TORO").status("omitted").externalEvent("435679")
                .time(TimeZoneUtil.getTimestampByDefaultZone()).flowType("Automatico").noticeId(1L).unforeseen(true).repairOrder(BigDecimal.valueOf(123.45))
                .adiUpdated(true).dateUpdateQuotation(TimeZoneUtil.getTimestampByDefaultZone()).quotationManaged(true).quotationWinner(true)
                .build();

        productOverrunCost = ProductOverrunCost.builder().id(1175L).externalEvent("1469978686").brand("SUBARU").line("OUTBACK").plate("AJ7890")
                .description("Ampolleta intermitente").quantity(1).reference("Sin Referencia").suggestedReference("").timeDelivery(250).importer(true)
                .valueExtraCost(0.0).extraCost(false).netPrice(100.0).grossPrice(100.0).discountAdditional(0.0).discountBrand(0.0)
                .discountCampaigns(0.0).status("quoted").date(new Timestamp(System.currentTimeMillis())).maxDeliveryDays(true).quality("A")
                .comment("").pieceId(504893L).discountManual(0.0).position(41)
                .build();

        productOrder1 = ProductOrder.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(TimeZoneUtil.getTimestampByDefaultZone())
                .comment("ProductComment").positionPiece(1).status("Accepted").build();

        order = Order.builder().id(456L).date(TimeZoneUtil.getTimestampByDefaultZone()).notice(notice).products(Set.of(productOrder1)).build();

        counterStatusProductQuotation1 = CounterStatusProductQuotation.builder().id(5415L).acceptedProducts(4L).omittedProducts(1L)
                .quotedProducts(0L).rejectedQuotedProducts(0L).totalProducts(4L).alertAndWinnerProducts(0L).build();

        counterStatusProductQuotation2 = CounterStatusProductQuotation.builder().id(5415L).acceptedProducts(0L).omittedProducts(4L)
                .quotedProducts(0L).rejectedQuotedProducts(0L).totalProducts(4L).alertAndWinnerProducts(0L).build();

        counterStatusProductQuotation3 = CounterStatusProductQuotation.builder().id(5415L).acceptedProducts(0L).omittedProducts(1L)
                .quotedProducts(0L).rejectedQuotedProducts(4L).totalProducts(4L).alertAndWinnerProducts(0L).build();

        counterStatusProductQuotation4 = CounterStatusProductQuotation.builder().id(5415L).acceptedProducts(0L).omittedProducts(1L)
                .quotedProducts(0L).rejectedQuotedProducts(2L).totalProducts(4L).alertAndWinnerProducts(0L).build();
    }

    @Test
    void testManageNoticeByNoticeId_Auth() throws ExceptionUtil {

        Optional<ProductOverrunCost> productOverrunCostEmpty = Optional.empty();

        when(noticeRepositoryPort.findById(anyLong())).thenReturn(Optional.of(notice));
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.ofNullable(insurer));
        when(productQuotationRepositoryPort.findPiecesByIdAndOverTimeOverCost(anyList())).thenReturn(List.of(productQuotation1, productQuotation2));
        when(productQuotationRepositoryPort.findPiecesByIdAndOverTime(anyList())).thenReturn(List.of(productQuotation3, productQuotation4));
        when(quotationRepositoryPort.findById(anyLong())).thenReturn(Optional.of(quotation));
        when(createOrder.createOrderQuotationConcessionarie(anyList(), any(Notice.class), any(Quotation.class))).thenReturn(order);
        when(productOverrunCostRepositoryPort.findAllByPieceId(anyLong())).thenReturn(productOverrunCostEmpty);
        when(quotationRepositoryPort.countStatusProductQuotation(anyLong())).thenReturn(counterStatusProductQuotation1);
        when(insurerRepositoryPort.findByInsurerId(notice.getInsuranceNumber())).thenReturn(Optional.of(insurer));


        boolean result = manageNoticeCC.manageNoticeCC(noticeId, quotation.getId(),List.of(1L, 2L), true);

        // Then-validations
        assertTrue(result);
        verify(productOverrunCostRepositoryPort).findAllByPieceId(productQuotation1.getId());
        verify(productOverrunCostRepositoryPort, times(2)).save(any(ProductOverrunCost.class));
        verify(productQuotationRepositoryPort, times(1)).updateStatusAndPurchaseById(anyList());
        verify(productOverrunCostRepositoryPort, times(1)).updatePurchaseByStatusAndIdIn(anyList());
        verify(quotationRepositoryPort, times(1)).findQuotationWithAllProductManage(anyLong());
        verify(quotationRepositoryPort, times(1)).updateQuotationManaged(anyList());
        verify(createOrder).createOrderQuotationConcessionarie(anyList(), any(Notice.class), any(Quotation.class));

    }

    @Test
    void testManageNoticeByNoticeId_Exception() {
        when(noticeRepositoryPort.findById(anyLong())).thenThrow(new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), MessageResponse.REQUEST_NOT_SUCCESS));

        Executable invocation = () -> manageNoticeCC.manageNoticeCC(notice.getId(), quotation.getId(), List.of(1L, 2L), false);

        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        Mockito.verify(insurerRepositoryPort, Mockito.never()).findByInsurerId(anyLong());
        Mockito.verify(productQuotationRepositoryPort, Mockito.never()).findPiecesByIdAndOverTimeOverCost(anyList());
        Mockito.verify(productQuotationRepositoryPort, Mockito.never()).findPiecesByIdAndOverTime(anyList());

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(ERROR_MANAGE_NOTICE_CONCESSIONAIRE + notice.getId(), exception.getMessage());
    }


    @Test
    void testChangeStatusQuotationsVoid_committed() throws ExceptionUtil {
        when(quotationRepositoryPort.countStatusProductQuotation(quotation.getId())).thenReturn(counterStatusProductQuotation2);
        when(quotationRepositoryPort.updateStatusQuotation(quotation.getId(), ManageNoticeConstant.OMITTED)).thenReturn(1);

        manageNoticeCC.changeStatusQuotationsVoid(quotation.getId());

        Mockito.verify(quotationRepositoryPort,  times(1)).countStatusProductQuotation(quotation.getId());
        Mockito.verify(quotationRepositoryPort,  times(1)).updateStatusQuotation(quotation.getId(),ManageNoticeConstant.OMITTED);

    }

    @Test
    void testChangeStatusQuotationsVoid_rejected() throws ExceptionUtil {
        when(quotationRepositoryPort.countStatusProductQuotation(quotation.getId())).thenReturn(counterStatusProductQuotation3);
        when(quotationRepositoryPort.updateStatusQuotation(quotation.getId(), REJECTED_QUOTED)).thenReturn(1);

        manageNoticeCC.changeStatusQuotationsVoid(quotation.getId());

        Mockito.verify(quotationRepositoryPort,  times(1)).countStatusProductQuotation(anyLong());
        Mockito.verify(quotationRepositoryPort,  times(1)).updateStatusQuotation(anyLong(),anyString());

    }

    @Test
    void testChangeStatusQuotationsVoid_rejectedProducts() throws ExceptionUtil {
        when(quotationRepositoryPort.countStatusProductQuotation(quotation.getId())).thenReturn(counterStatusProductQuotation4);
        when(quotationRepositoryPort.updateStatusQuotation(quotation.getId(), REJECTED_QUOTED)).thenReturn(1);

        manageNoticeCC.changeStatusQuotationsVoid(quotation.getId());

        Mockito.verify(quotationRepositoryPort,  times(1)).countStatusProductQuotation(anyLong());
        Mockito.verify(quotationRepositoryPort,  times(1)).updateStatusQuotation(anyLong(),anyString());

    }

    @Test
    void testChangeStatusQuotationsVoid_Exception() {
        when(quotationRepositoryPort.countStatusProductQuotation(anyLong())).thenThrow(new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), MessageResponse.REQUEST_NOT_SUCCESS));

        Executable invocation = () -> manageNoticeCC.changeStatusQuotationsVoid(quotation.getId());
        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        verify(quotationRepositoryPort, times(1)).countStatusProductQuotation(anyLong());
        verify(quotationRepositoryPort, Mockito.never()).updateStatusQuotation(anyLong(), anyString());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(ERROR_CHANGE_STATUS + "quotation", exception.getMessage());
    }

    @Test
    void testSetProductOverRunCost_Exception() {
        when(productOverrunCostRepositoryPort.findAllByPieceId(anyLong())).thenThrow(new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), MessageResponse.REQUEST_NOT_SUCCESS));

        Executable invocation = () -> manageNoticeCC.setProductOverruncost(notice, List.of(productQuotation1, productQuotation2));
        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        verify(productOverrunCostRepositoryPort, Mockito.never()).save(any(ProductOverrunCost.class));
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(ERROR_SET_PRODUCT_OVERRUN_COST + notice.getId(), exception.getMessage());
    }


    @Test
    void testSetProductOverruncost() throws ExceptionUtil {

        List<ProductQuotation> productQuotationList = List.of(productQuotation1, productQuotation2);

        when(productOverrunCostRepositoryPort.findAllByPieceId(anyLong())).thenReturn(Optional.empty());

        boolean result = manageNoticeCC.setProductOverruncost(notice, productQuotationList);

        verify(productOverrunCostRepositoryPort, times(2)).save(any(ProductOverrunCost.class));

        assertTrue(result);
    }

}
