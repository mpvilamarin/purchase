package com.subocol.manage.purchase.domain.integrations;

import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.MailOrderCreateDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeValuationDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_AUTHORIZING_NOTICE_PIECE_NOT_FOUND;
import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.ERROR_INTERNAL_EXTERNAL_SERVICES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IntegrationsTest {

    @Mock
    private AlertPiecesValuationImpl alertPiecesValuation;
    @Mock
    private NoticeRepositoryPort noticeRepositoryPort;
    @Mock
    private InsurerRepositoryPort insurerRepositoryPort;
    @Mock
    private OrderRepositoryPort orderRepositoryPort;
    @Mock
    private FollowUp followUp;
    @Mock
    private ReserveSura reserveSura;
    @Mock
    private BillingOrders billingOrders;
    @Mock
    private ChileOrderImpl chileOrder;
    @Mock
    private MailOrderImpl mailOrder;
    @Mock
    private ReserveBolivar reserveBolivar;
    @InjectMocks
    private Integrations integrations;
    private Notice notice;
    private Insurer insurer;
    private Insurer insurerBolivar;
    private final Set<ProductOrder> productOrders  = new HashSet<ProductOrder>();
    private final Set<Order> orders = new HashSet<Order>();
    private final List<Order> ordersExposed = new ArrayList<>();
    List<PiecesValuationDTO> piecesListQuoted = new ArrayList<>();
    private MailOrderCreateDTO mailOrderCreateDTO;
    NoticeValuationDTO noticeValuationDTO = new NoticeValuationDTO();

    @BeforeEach
    void setup() {

        Subsidiary subsidiary = Subsidiary.builder().alias("ADVANCE MOTORS SA CHIRIQI TEST").email("sucpan101TEST@gmail.com").name("ADVANCE MOTORS SA CHIRIQI TEST")
                .phone("7752509").status(Boolean.TRUE).locationExternalId(40L).warehouseIdDms(null).classification("Autosuministro").dmsCode(null)
                .idJob(null).intermediation(null).provider(new Provider().setId(1L)).build();

        notice = Notice.builder().id(49521L).externalEvent(5432).brand("Ford").auth(true).model("2020").claimNumber("123456789").insuranceNumber(200000002L)
                .workshopType("Autosuministro").version("Titanium").plate("AU6043").cellphone("309987878897").idCountry(2L).vin("3hgrm4870fg601108").lossIndicator(0D)
                .totalWorkforce(0D).date(Timestamp.valueOf(LocalDateTime.now())).workshop("taller panama").line("2000CC").city("PANAMA").insuredValue(0D)
                .unforeseen(true)
                .build();

        insurer = Insurer.builder().id(5345L).name("SURA")
                .insurerId(123456789L).countryId(5L).sdkActive(false).multimedia(true)
                .priceToUse("autosuministro/genuino/precio_medio").newSuggestedReferenceParameter(true).allowMaxCostPiece(true)
                .daysUpdateSuggestedReference(7L).useGrossPriceCostoverrun(false).usePriceList(false)
                .prioritizePriceList(false).useOrbikaValuation(true).flowReserveSura(true).sendOrderFact(true).sdkActive(true).sendOrderEmailWinner(true).build();

        insurerBolivar = Insurer.builder().id(5345L).name("BOLIVAR")
                .insurerId(123456789L).countryId(5L).sdkActive(false).multimedia(true)
                .priceToUse("autosuministro/genuino/precio_medio").newSuggestedReferenceParameter(true).allowMaxCostPiece(true)
                .daysUpdateSuggestedReference(7L).useGrossPriceCostoverrun(false).usePriceList(false).flowReserveBolivar(true)
                .prioritizePriceList(false).useOrbikaValuation(false).flowReserveSura(false).sendOrderFact(false).sdkActive(false).sendOrderEmailWinner(false).build();


        Order order = Order.builder().id(5221L).orderIdDms(2323).billingServiceId(2323)
                .comment("this is a order").documentUrl("myOrder.pdf").priority(1)
                .reference("TODAYORDER").status(ManageNoticeConstant.SENT)
                .date(Timestamp.valueOf(LocalDateTime.now()))
                .workshop("taller panama").notice(notice).products(productOrders)
                .subsidiary(subsidiary).purchaseTypeId(45121).build();

        ProductOrder productOrder = ProductOrder.builder()
                .amount(1).grossPrice(100D).guide("this is a guide")
                .importer(false).comment("this is a commet")
                .order(order).status(ManageNoticeConstant.SENT)
                .description("bomber delantero")
                .quality("ORIGINAL")
                .build();

        productOrders.add(productOrder);
        order.setProducts(productOrders);
        ordersExposed.add(order);
        orders.add(order);

        noticeValuationDTO = NoticeValuationDTO.builder().numeroAviso(484620).build();

        PiecesValuationDTO piecesQuoted = PiecesValuationDTO.builder().calidadRepuesto("Original")
                .cantidad(1).codigo("").comprada("N").descuento(10.0).id(1413776L).nombreSucursalGanadora("EXCLUSIVE MOTORS SA")
                .posicion(3).valorUnitario(10.0).valorUnitarioConDescuento(9.0).tiempoEstimadoEntrega(3).build();

        piecesListQuoted.add(piecesQuoted);
        noticeValuationDTO.setPiezas(piecesListQuoted);

        mailOrderCreateDTO = MailOrderCreateDTO.builder()
                .orderId(999999999L)
                .aviso("999999999")
                .email("test@test.com")
                .plate("XYZ123")
                .insurerName("SURA")
                .build();


    }

    @Test
    void testIntegrationByNotice() {

        doAnswer(invocation -> {
            Notice insideNotice = notice;
            Insurer insideInsurer = insurer;
            List<MailOrderCreateDTO> mailOrderCreated = invocation.getArgument(3);
            mailOrderCreated.add(mailOrderCreateDTO);
            return null;
        }).when(mailOrder).addMailOrderNotification("5432", 5221L, 45121, Collections.emptyList(), "AU6043", "SURA");

        when(alertPiecesValuation.alertPiecesValuation(anyInt(), anyBoolean(), anyString())).thenReturn(Boolean.TRUE);
        when(reserveSura.sendPiecesReserveSura(any(Insurer.class), any(Notice.class))).thenReturn(Boolean.TRUE);
        when(billingOrders.sendBillingOrder(any(Order.class), any(Notice.class))).thenReturn(Boolean.TRUE);
        when(chileOrder.chileBuyingOrder(any(Order.class))).thenReturn(Boolean.TRUE);

        boolean result = integrations.integrationByNotice(notice, insurer, orders);

        assertTrue(result);

        verify(alertPiecesValuation).alertPiecesValuation(anyInt(), anyBoolean(), anyString());
        verify(reserveSura).sendPiecesReserveSura(any(Insurer.class), any(Notice.class));
        verify(billingOrders).sendBillingOrder(any(Order.class), any(Notice.class));
        verify(chileOrder).chileBuyingOrder(any(Order.class));
        verify(followUp).sendPurchasesToFollowUp(notice.getExternalEvent().longValue(), orders);

    }


    @Test
    void testIntegrationByNoticeByInsurerBolivar() {

        when(reserveBolivar.sendPurchaseTotalReserve(any(Notice.class))).thenReturn(Boolean.TRUE);

        boolean result = integrations.integrationByNotice(notice, insurerBolivar, orders);

        assertTrue(result);

        verify(alertPiecesValuation ,  never()).alertPiecesValuation(anyInt(), anyBoolean(), anyString());
        verify(reserveSura ,  never()).sendPiecesReserveSura(any(Insurer.class), any(Notice.class));
        verify(billingOrders ,  never()).sendBillingOrder(any(Order.class), any(Notice.class));
        verify(chileOrder ,  never()).chileBuyingOrder(any(Order.class));
        verify(reserveBolivar).sendPurchaseTotalReserve(any(Notice.class));

    }

    @Test
    void testIntegrationByNoticeException() {

        when(alertPiecesValuation.alertPiecesValuation(anyInt(), anyBoolean(), anyString()))
                .thenThrow(new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), ERROR_AUTHORIZING_NOTICE_PIECE_NOT_FOUND, "Simulated exception"));

        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, () -> integrations.integrationByNotice(notice, insurer, orders));

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(ERROR_AUTHORIZING_NOTICE_PIECE_NOT_FOUND, exception.getMessage());

        verifyNoMoreInteractions(alertPiecesValuation, reserveSura, billingOrders, chileOrder, mailOrder, followUp);
    }



    @Test
    void testIntegrationServicesExposed() {
        when(noticeRepositoryPort.findById(anyLong())).thenReturn(Optional.of(notice));
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(orderRepositoryPort.findAllById(anyList())).thenReturn(ordersExposed);
        when(integrations.integrationByNotice(notice,insurer, orders)).thenReturn(true);


        ResponseDTO result = integrations.integrationServicesExposed(notice.getId(), insurerBolivar.getInsurerId(), List.of(1L));

        verify(noticeRepositoryPort).findById(notice.getId());
        verify(insurerRepositoryPort).findByInsurerId(insurerBolivar.getInsurerId());
        verify(orderRepositoryPort).findAllById(List.of(1L));

        assertEquals("Se proceso correctamente el aviso:  " + notice.getExternalEvent() + " NoticeId " + notice.getId(), result.getMessage());
        assertEquals(200, result.getStatus());
        assertTrue(result.isSuccess());
    }

    @Test
    void testIntegrationServicesExposedException() {
        when(noticeRepositoryPort.findById(anyLong())).thenReturn(Optional.of(notice));
        when(insurerRepositoryPort.findByInsurerId(anyLong())).thenReturn(Optional.of(insurer));
        when(orderRepositoryPort.findAllById(anyList())).thenReturn(ordersExposed);
        when(integrations.integrationByNotice(notice, insurer, orders)).thenThrow(new ExceptionUtil(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Test Error Message"));

        Executable invocation = () -> integrations.integrationServicesExposed(notice.getId(), insurerBolivar.getInsurerId(), List.of(1L));
        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        verify(noticeRepositoryPort).findById(notice.getId());
        verify(insurerRepositoryPort).findByInsurerId(insurerBolivar.getInsurerId());
        verify(orderRepositoryPort).findAllById(List.of(1L));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getStatusCode());
        assertEquals(ERROR_INTERNAL_EXTERNAL_SERVICES + notice.getId(), exception.getMessage());
    }

}
