package com.subocol.manage.purchase.domain.integrations;

import com.subocol.integracionorbikachile.IntegracionOrbikaChileSdk;
import com.subocol.integracionorbikachile.model.BadRequestException;
import com.subocol.integracionorbikachile.model.EnviarOrdenCompraRequest;
import com.subocol.integracionorbikachile.model.EnviarOrdenCompraResult;
import com.subocol.integracionorbikachile.model.OrdenCompraResponse;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.models.enums.ProductOrderStatus;
import com.subocol.manage.purchase.domain.models.enums.ProviderType;
import com.subocol.manage.purchase.domain.ports.externalservices.LocationExternalServicesPort;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.ChileOrderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.subocol.manage.purchase.domain.constant.ErrorMessageHandler.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChileOrderTest {

    @Mock
    private LocationExternalServicesPort locationExternalServicesPort;

    @Mock
    private SellOrderRepositoryPort sellOrderRepositoryPort;

    @Mock
    private CurrencyRepositoryPort currencyRepositoryPort;

    @Mock
    private DataEventRepositoryPort dataEventRepositoryPort;

    @Mock
    private TaxRepositoryPort taxRepositoryPort;

    @Mock
    private InsuranceCarrierRepositoryPort insuranceCarrierRepositoryPort;

    @Mock
    private ManualPurchaseAdiRepositoryPort manualPurchaseAdiRepositoryPort;

    @Mock
    private PieceRepositoryPort pieceRepositoryPort;

    @Mock
    private SingletonProperties propertiesBean;

    @Mock
    private IntegracionOrbikaChileSdk orbikaChileSdk;

    @Mock
    private ChileOrderImpl chileOrder2;

    @InjectMocks
    private ChileOrderImpl chileOrder;

    private Location location;
    private SellOrder sellOrder;
    private Order order;
    private Currency currency;
    private DataEvent dataEvent;
    private Tax tax;
    private InsuranceCarrier insuranceCarrier;
    private IntegrationAWS integrationAWS;
    private EnviarOrdenCompraResult enviarOrdenCompraResult;

    @BeforeEach
    void setup() {

        enviarOrdenCompraResult = new EnviarOrdenCompraResult();
        OrdenCompraResponse ordenCompraResponse = new OrdenCompraResponse();
        ordenCompraResponse.setRtaMensaje("Servicio chile respondio bien");
        enviarOrdenCompraResult.setOrdenCompraResponse(ordenCompraResponse);


        integrationAWS = new IntegrationAWS().setAccessKey("OUHEUHAD12312").setSecretAccessKey("dnoianduiadh11823u131");

        insuranceCarrier = InsuranceCarrier.builder()
                .id(999999999L).name("Seguros Generales Suramericana S.A.").countryId("1")
                .nit("8909034079").taxAbbreviation("NIT").logo("logoSura.png").prefix("$").ivaItbms("IVA")
                .build();

        tax = Tax.builder()
                .taxIdDms(123).description("Sample tax description").percentage(10).type(1).countryId(456L).taxName("Sample Tax")
                .build();

        dataEvent = DataEvent.builder().id(1L).externalEvent(123)
                .workshopCity("Test City").workshopName("Test Workshop").line("Test Line").brand("Test Brand").coverage("Test Coverage")
                .deductible(100.0).repairConcl("Test Repair Conclusion").fixedDeductible(200.0).insuredValue(300.0).vin("Test VIN")
                .plate("Test Plate").idJob("Test Job ID").unexpected("Test Unexpected")
                .countryId(2L).model("Test Model").version("Test Version").workshopRut("Test Workshop Rut").workshopAddress("Test Workshop Address")
                .workshopPhone(1234567890L).claimNumber("Test Claim Number")
                .insuranceNumber("Test Insurance Number").authorization("Test Authorization").workshopType("Test Workshop Type").workshopNit("Test Workshop Nit").vehicleType("Test Vehicle Type")
                .noticeSofia(3L).date(Timestamp.valueOf(LocalDateTime.now())).repairOrder(987654321L).lossIndicator(1.0).totalWorkforce(500.0)
                .workshopId(4)
                .build();

        currency = Currency.builder().id(999999999L).description("currency").prefix("$").fixedRate(5D).divide(10).build();

        location = Location.builder().id(1L).countryId(1L).build();

        Subsidiary subsidiary = Subsidiary.builder().id(10L).locationExternalId(10L)
                .provider(new Provider()
                        .setProviderClassification(ProviderType.AUTOSUMINISTRO.toString())
                        .setNit("123456789"))
                .build();

        Notice notice = Notice.builder().auth(true).brand("FORD").line("FUSION").externalEvent(123)
                .id(789L).eventId(456L).workshop("WorkshopName").unforeseen(false).repairOrder(BigDecimal.valueOf(52.56))
                .idCountry((long) ManageNoticeConstant.COLOMBIA).insuranceNumber(500000001L).build();

        ProductOrder productOrder = ProductOrder.builder().id(999999999L).price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(TimeZoneUtil.getTimestampByDefaultZone())
                .comment("ProductComment").positionPiece(1).status("Accepted").build();

        order = Order.builder().id(5593L).date(new Timestamp(1664579257)).subsidiary(subsidiary).status("assigned").workshop("SUBARU DEL CANAL")
                .time(null).notice(notice).priority(0).products(Set.of(productOrder)).reference("Order-123").comment("null").workshopDeliveryDate(null)
                .documentUrl("https://example.com/document.pdf").orderIdDms(789).orderPurchaseDms(101112).orderPurchaseChile(131415L).orderIdSubocol("SUB123")
                .billingServiceId(1617).unforeseen(true).repairOrder(BigDecimal.valueOf(1000)).purchaseTypeId(1819).build();

        sellOrder = SellOrder.builder().id(1L).order(order).creationDate(new Timestamp(1664579257))
                .lastUpdateDate(new Timestamp(1664579257)).subtotal(100.0).iva(10.0).total(110.0)
                .pdfUrl("https://example.com/sellorder.pdf").details(new HashSet<>()).build();
    }

    @Test
    void testChileBuyingOrderThrowCurrencyNotFound() {

        when(locationExternalServicesPort.findLocation(anyLong())).thenReturn(location);
        when(sellOrderRepositoryPort.findByOrderId(anyLong())).thenReturn(Optional.ofNullable(sellOrder));
        //INJECTED FAILURE
        when(currencyRepositoryPort.findByCountryId(anyLong())).thenReturn(Optional.empty());

        Executable invocation = () -> chileOrder.chileBuyingOrder(order);

        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        verify(locationExternalServicesPort, times(1)).findLocation(anyLong());
        verify(sellOrderRepositoryPort, times(1)).findByOrderId(anyLong());
        verify(currencyRepositoryPort, times(1)).findByCountryId(anyLong());

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        assertEquals(
                ErrorMessageHandler.concatenateStringAndObject(CURRENCY_NOT_FOUND_BY_COUNTRY_ID,
                        order.getNotice().getIdCountry()), exception.getMessage());


    }

    @Test
    void testChileBuyingOrderThrowDataEventNotFound() {

        when(locationExternalServicesPort.findLocation(anyLong())).thenReturn(location);
        when(sellOrderRepositoryPort.findByOrderId(anyLong())).thenReturn(Optional.ofNullable(sellOrder));
        when(currencyRepositoryPort.findByCountryId(anyLong())).thenReturn(Optional.ofNullable(currency));
        //INJECTED FAILURE
        when(dataEventRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        Executable invocation = () -> chileOrder.chileBuyingOrder(order);

        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        verify(locationExternalServicesPort, times(1)).findLocation(anyLong());
        verify(sellOrderRepositoryPort, times(1)).findByOrderId(anyLong());
        verify(currencyRepositoryPort, times(1)).findByCountryId(anyLong());
        verify(dataEventRepositoryPort, times(1)).findById(anyLong());

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        assertEquals(
                ErrorMessageHandler.concatenateStringAndObject(DATA_EVENT_NOT_FOUND,
                        order.getNotice().getEventId()), exception.getMessage());


    }

    @Test
    void testChileBuyingOrderThrowTaxNotFound() {

        when(locationExternalServicesPort.findLocation(anyLong())).thenReturn(location);
        when(sellOrderRepositoryPort.findByOrderId(anyLong())).thenReturn(Optional.ofNullable(sellOrder));
        when(currencyRepositoryPort.findByCountryId(anyLong())).thenReturn(Optional.ofNullable(currency));
        when(dataEventRepositoryPort.findById(anyLong())).thenReturn(Optional.ofNullable(dataEvent));
        //INJECTED FAILURE
        when(taxRepositoryPort.findTaxByCountry(anyLong())).thenReturn(Optional.empty());

        Executable invocation = () -> chileOrder.chileBuyingOrder(order);

        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        verify(locationExternalServicesPort, times(1)).findLocation(anyLong());
        verify(sellOrderRepositoryPort, times(1)).findByOrderId(anyLong());
        verify(currencyRepositoryPort, times(1)).findByCountryId(anyLong());
        verify(dataEventRepositoryPort, times(1)).findById(anyLong());
        verify(taxRepositoryPort, times(1)).findTaxByCountry(anyLong());

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        assertEquals(
                ErrorMessageHandler.concatenateStringAndObject(TAX_NOT_FOUND_BY_COUNTRY_ID,
                        location.getCountryId()), exception.getMessage());


    }

    @Test
    void testChileBuyingOrderThrowInsuranceCarrierNotFound() {

        when(locationExternalServicesPort.findLocation(anyLong())).thenReturn(location);
        when(sellOrderRepositoryPort.findByOrderId(anyLong())).thenReturn(Optional.ofNullable(sellOrder));
        when(currencyRepositoryPort.findByCountryId(anyLong())).thenReturn(Optional.ofNullable(currency));
        when(dataEventRepositoryPort.findById(anyLong())).thenReturn(Optional.ofNullable(dataEvent));
        when(taxRepositoryPort.findTaxByCountry(anyLong())).thenReturn(Optional.ofNullable(tax));
        //INJECTED FAILURE
        when(insuranceCarrierRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        Executable invocation = () -> chileOrder.chileBuyingOrder(order);

        ExceptionUtil exception = Assertions.assertThrows(ExceptionUtil.class, invocation);

        verify(locationExternalServicesPort, times(1)).findLocation(anyLong());
        verify(sellOrderRepositoryPort, times(1)).findByOrderId(anyLong());
        verify(currencyRepositoryPort, times(1)).findByCountryId(anyLong());
        verify(dataEventRepositoryPort, times(1)).findById(anyLong());
        verify(taxRepositoryPort, times(1)).findTaxByCountry(anyLong());
        verify(insuranceCarrierRepositoryPort, times(1)).findById(anyLong());

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        assertEquals(
                ErrorMessageHandler.concatenateStringAndObject(INSURANCE_CARRIER_NOT_FOUND,
                        order.getNotice().getInsuranceNumber()), exception.getMessage());

    }

    @Test
    void testChileBuyingOrderSuccess() {

        when(locationExternalServicesPort.findLocation(anyLong())).thenReturn(location);
        when(sellOrderRepositoryPort.findByOrderId(anyLong())).thenReturn(Optional.ofNullable(sellOrder));
        when(currencyRepositoryPort.findByCountryId(anyLong())).thenReturn(Optional.ofNullable(currency));
        when(dataEventRepositoryPort.findById(anyLong())).thenReturn(Optional.ofNullable(dataEvent));
        when(taxRepositoryPort.findTaxByCountry(anyLong())).thenReturn(Optional.ofNullable(tax));
        when(insuranceCarrierRepositoryPort.findById(anyLong())).thenReturn(Optional.ofNullable(insuranceCarrier));
        when(manualPurchaseAdiRepositoryPort.findByExternalEventAndPosition(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(pieceRepositoryPort.findByExternalEventAndPosition(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(dataEventRepositoryPort.findByExternalEventAndId(anyInt(), anyLong())).thenReturn(Optional.ofNullable(dataEvent));
        when(propertiesBean.getCurrentAwsPropertyByKey(anyString())).thenReturn(integrationAWS);

        //when(orbikaChileSdk.enviarOrdenCompra(any())).thenReturn(enviarOrdenCompraResult);

        boolean result=chileOrder.chileBuyingOrder(order);
        assertFalse(result);
        verify(locationExternalServicesPort, times(1)).findLocation(anyLong());
        verify(sellOrderRepositoryPort, times(1)).findByOrderId(anyLong());
        verify(currencyRepositoryPort, times(1)).findByCountryId(anyLong());
        verify(dataEventRepositoryPort, times(1)).findById(anyLong());
        verify(taxRepositoryPort, times(1)).findTaxByCountry(anyLong());
        verify(insuranceCarrierRepositoryPort, times(1)).findById(anyLong());
        verify(manualPurchaseAdiRepositoryPort, times(1)).findByExternalEventAndPosition(anyInt(), anyInt());
        verify(pieceRepositoryPort, times(1)).findByExternalEventAndPosition(anyInt(), anyInt());
        verify(dataEventRepositoryPort, times(1)).findByExternalEventAndId(anyInt(), anyLong());
        verify(propertiesBean, times(1)).getCurrentAwsPropertyByKey(anyString());
        //verify(orbikaChileSdk, times(1)).enviarOrdenCompra(any());

//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getStatusCode());

    }

    @Test
    void testChileBuyingOrderSuccessWithoutSellOrder() {
        order.getSubsidiary().getProvider().setProviderClassification(ProviderType.MOSTRADOR.toString());
        when(locationExternalServicesPort.findLocation(anyLong())).thenReturn(location);
        //TODO:
        when(sellOrderRepositoryPort.findByOrderId(anyLong())).thenReturn(Optional.empty());
        when(sellOrderRepositoryPort.save(any())).thenReturn(sellOrder);
        when(currencyRepositoryPort.findByCountryId(anyLong())).thenReturn(Optional.ofNullable(currency));
        when(dataEventRepositoryPort.findById(anyLong())).thenReturn(Optional.ofNullable(dataEvent));
        when(taxRepositoryPort.findTaxByCountry(anyLong())).thenReturn(Optional.ofNullable(tax));
        when(insuranceCarrierRepositoryPort.findById(anyLong())).thenReturn(Optional.ofNullable(insuranceCarrier));
        when(manualPurchaseAdiRepositoryPort.findByExternalEventAndPosition(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(pieceRepositoryPort.findByExternalEventAndPosition(anyInt(), anyInt())).thenReturn(Optional.empty());
        when(dataEventRepositoryPort.findByExternalEventAndId(anyInt(), anyLong())).thenReturn(Optional.ofNullable(dataEvent));
        when(propertiesBean.getCurrentAwsPropertyByKey(anyString())).thenReturn(integrationAWS);

        //when(orbikaChileSdk.enviarOrdenCompra(any())).thenReturn(enviarOrdenCompraResult);

        boolean result=chileOrder.chileBuyingOrder(order);

        assertFalse(result);

        verify(locationExternalServicesPort, times(1)).findLocation(anyLong());
        verify(sellOrderRepositoryPort, times(1)).findByOrderId(anyLong());
        verify(currencyRepositoryPort, times(1)).findByCountryId(anyLong());
        verify(dataEventRepositoryPort, times(1)).findById(anyLong());
        verify(taxRepositoryPort, times(1)).findTaxByCountry(anyLong());
        verify(insuranceCarrierRepositoryPort, times(1)).findById(anyLong());
        verify(manualPurchaseAdiRepositoryPort, times(1)).findByExternalEventAndPosition(anyInt(), anyInt());
        verify(pieceRepositoryPort, times(1)).findByExternalEventAndPosition(anyInt(), anyInt());
        verify(dataEventRepositoryPort, times(1)).findByExternalEventAndId(anyInt(), anyLong());
        verify(propertiesBean, times(1)).getCurrentAwsPropertyByKey(anyString());
        //verify(orbikaChileSdk, times(1)).enviarOrdenCompra(any());

//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getStatusCode());

    }

    @Test
    void testChileBuyingOrderSuccessWithProductOrderDesists() {

        order.getSubsidiary().getProvider().setProviderClassification(ProviderType.MOSTRADOR.toString());
        order.getProducts().forEach(po -> po.setStatus(ProductOrderStatus.DESIST.toString()));

        when(locationExternalServicesPort.findLocation(anyLong())).thenReturn(location);
        when(sellOrderRepositoryPort.findByOrderId(anyLong())).thenReturn(Optional.ofNullable(sellOrder));
        when(currencyRepositoryPort.findByCountryId(anyLong())).thenReturn(Optional.ofNullable(currency));
        when(dataEventRepositoryPort.findById(anyLong())).thenReturn(Optional.ofNullable(dataEvent));
        when(taxRepositoryPort.findTaxByCountry(anyLong())).thenReturn(Optional.ofNullable(tax));
        when(insuranceCarrierRepositoryPort.findById(anyLong())).thenReturn(Optional.ofNullable(insuranceCarrier));
        when(dataEventRepositoryPort.findByExternalEventAndId(anyInt(), anyLong())).thenReturn(Optional.ofNullable(dataEvent));
        when(propertiesBean.getCurrentAwsPropertyByKey(anyString())).thenReturn(integrationAWS);

        //when(orbikaChileSdk.enviarOrdenCompra(any())).thenReturn(enviarOrdenCompraResult);

        boolean result= chileOrder.chileBuyingOrder(order);
        assertFalse(result);
        verify(locationExternalServicesPort, times(1)).findLocation(anyLong());
        verify(sellOrderRepositoryPort, times(1)).findByOrderId(anyLong());
        verify(currencyRepositoryPort, times(1)).findByCountryId(anyLong());
        verify(dataEventRepositoryPort, times(1)).findById(anyLong());
        verify(taxRepositoryPort, times(1)).findTaxByCountry(anyLong());
        verify(insuranceCarrierRepositoryPort, times(1)).findById(anyLong());
        verify(dataEventRepositoryPort, times(1)).findByExternalEventAndId(anyInt(), anyLong());
        verify(propertiesBean, times(1)).getCurrentAwsPropertyByKey(anyString());
        //verify(orbikaChileSdk, times(1)).enviarOrdenCompra(any());

//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getStatusCode());

    }

}
