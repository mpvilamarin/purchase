package com.subocol.manage.purchase.domain.servicesimpl.integrations;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.opensdk.SdkRequestConfig;
import com.subocol.integracionorbikachile.IntegracionOrbikaChileSdk;
import com.subocol.integracionorbikachile.model.EnviarOrdenCompraRequest;
import com.subocol.integracionorbikachile.model.Item;
import com.subocol.integracionorbikachile.model.OrdenCompra;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.common.utils.SingletonProperties;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ErrorMessageHandler;
import com.subocol.manage.purchase.domain.constant.PropertiesConstants;
import com.subocol.manage.purchase.domain.models.*;
import com.subocol.manage.purchase.domain.models.enums.ProductOrderStatus;
import com.subocol.manage.purchase.domain.models.enums.ProviderType;
import com.subocol.manage.purchase.domain.ports.externalservices.LocationExternalServicesPort;
import com.subocol.manage.purchase.domain.ports.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author DANR
 * @version 1.0
 * @since 4/07/2023
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChileOrderImpl {

    private static final String PREFIX_CHILE = "CL";

    private static final String TIPO_MOSTRADOR = "M";
    private static final String TIPO_AUTOSUMINISTRO = "A";
    private static final String TIPO_MIXTO = "X";
    private static final String EXTERNO = "E";

    private static final String GUION = "-";

    private final LocationExternalServicesPort locationExternalServicesPort;

    private final SellOrderRepositoryPort sellOrderRepositoryPort;

    private final CurrencyRepositoryPort currencyRepositoryPort;

    private final DataEventRepositoryPort dataEventRepositoryPort;

    private final TaxRepositoryPort taxRepositoryPort;

    private final InsuranceCarrierRepositoryPort insuranceCarrierRepositoryPort;

    private final ManualPurchaseAdiRepositoryPort manualPurchaseAdiRepositoryPort;

    private final PieceRepositoryPort pieceRepositoryPort;

    private final SingletonProperties propertiesBean;

    public boolean chileBuyingOrder(Order order) {

        Notice notice = order.getNotice();
        Location location = locationExternalServicesPort.findLocation(order.getSubsidiary().getLocationExternalId());
        SellOrder sellOrder = sellOrderRepositoryPort.findByOrderId(order.getId()).orElseGet(() -> sellOrderRepositoryPort.save(createSellOrderWithOrder(order)));

        Currency currency = currencyRepositoryPort.findByCountryId(notice.getIdCountry())
                .orElseThrow(() ->
                        new ExceptionUtil(HttpStatus.NOT_FOUND.value(),
                                ErrorMessageHandler.concatenateStringAndObject(ErrorMessageHandler.CURRENCY_NOT_FOUND_BY_COUNTRY_ID, notice.getIdCountry())));

        DataEvent dataEvent = dataEventRepositoryPort.findById(notice.getEventId())
                .orElseThrow(() ->
                        new ExceptionUtil(HttpStatus.NOT_FOUND.value(),
                                ErrorMessageHandler.concatenateStringAndObject(ErrorMessageHandler.DATA_EVENT_NOT_FOUND, notice.getEventId())));

        Tax tax = taxRepositoryPort.findTaxByCountry(location.getCountryId())
                .orElseThrow(() ->
                        new ExceptionUtil(HttpStatus.NOT_FOUND.value(),
                                ErrorMessageHandler.concatenateStringAndObject(ErrorMessageHandler.TAX_NOT_FOUND_BY_COUNTRY_ID, location.getCountryId())));

        InsuranceCarrier insuranceCarrier = insuranceCarrierRepositoryPort.findById(notice.getInsuranceNumber())
                .orElseThrow(() ->
                        new ExceptionUtil(HttpStatus.NOT_FOUND.value(),
                                ErrorMessageHandler.concatenateStringAndObject(ErrorMessageHandler.INSURANCE_CARRIER_NOT_FOUND, notice.getInsuranceNumber())));

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        OrdenCompra ordenCompra = new OrdenCompra();
        List<Item> items = new ArrayList<>();

        double totalDiscount = 0;
        Integer iva = tax.getPercentage();

        List<ProductOrder> productOrders = order.getProducts().stream()
                .filter(productOrder -> !ProductOrderStatus.DESIST.toString().equals(productOrder.getStatus()))
                .toList();

        for (ProductOrder piece : productOrders) {
            Item item = new Item();
            item.setCodigoPieza("");

            manualPurchaseAdiRepositoryPort
                    .findByExternalEventAndPosition(dataEvent.getExternalEvent(), piece.getPositionPiece())
                    .ifPresent(mp -> item.setCodigoPieza(mp.getIrs()));

            item.setCantidad(piece.getAmount());
            item.setNombrePieza(piece.getDescription());
            item.setUnidadMedida("UND");
            item.setFechaCompromiso(dateFormat.format(piece.getPromiseDelivery()));
            item.setPorcentajeIvaPieza(Double.valueOf(iva));
            item.setDescuento(piece.getTotalDiscount() == null ? (double) 0 : BigDecimal.valueOf(piece.getTotalDiscount()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            item.setCostoTotal(piece.getPrice() == null ? (double) 0 : BigDecimal.valueOf(piece.getPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            item.setItem(piece.getId().intValue());
            item.setTiempoDeEntrega(piece.getPromisedDeliveryDays());
            item.setCalidad(piece.getQuality());
            item.setValorUnitario(piece.getPrice() == null ? (double) 0 : BigDecimal.valueOf(piece.getPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            double valorDescuentoRepuesto = (piece.getGrossPrice() - piece.getPrice()) * item.getCantidad();
            totalDiscount = totalDiscount + valorDescuentoRepuesto;

            pieceRepositoryPort
                    .findByExternalEventAndPosition(dataEvent.getExternalEvent(), piece.getPositionPiece())
                    .ifPresent(p -> item.setGrupoRepuesto(p.getGroupName()));

            items.add(item);
        }

        String providerType = EXTERNO;

        if (ProviderType.AUTOSUMINISTRO.toString().equalsIgnoreCase(order.getSubsidiary().getProvider().getProviderClassification()))
            providerType = TIPO_AUTOSUMINISTRO;
        else if (ProviderType.MOSTRADOR.toString().equalsIgnoreCase(order.getSubsidiary().getProvider().getProviderClassification()))
            providerType = TIPO_MOSTRADOR;
        else if (ProviderType.MIXTO.toString().equalsIgnoreCase(order.getSubsidiary().getProvider().getProviderClassification()))
            providerType = TIPO_MIXTO;

        ordenCompra.setAnio(dataEvent.getModel());
        ordenCompra.setCiudadProveedor(location.getCityName());
        ordenCompra.setCiudadTaller(dataEvent.getWorkshopCity());
        ordenCompra.setCodigoAseguradora(insuranceCarrier.getId().toString());
        ordenCompra.setDireccionProveedor(location.getAddress());
        ordenCompra.setFechaEmisionOrdenCompra(dateFormat.format(order.getDate()));
        ordenCompra.setFechaEnvio(dateFormat.format(TimeZoneUtil.getTimestampByDefaultZone()));
        ordenCompra.setFormaPago("0");
        ordenCompra.setIdOrdenCompraSubocol(PREFIX_CHILE + GUION + insuranceCarrier.getPrefix() + providerType + GUION + order.getId());
        ordenCompra.setIdEnvio(order.getId().intValue());
        ordenCompra.setItems(items);
        ordenCompra.setLinea(dataEvent.getLine());
        ordenCompra.setMarca(dataEvent.getBrand());
        ordenCompra.setMoneda(currency.getPrefix());
        ordenCompra.setNit(insuranceCarrier.getNit());
        String providerNit = order.getSubsidiary().getProvider().getNit();
        ordenCompra.setNitProveedor(Arrays.stream(providerNit.split("-")).findFirst().orElse(providerNit));
        ordenCompra.setNombreProveedor(order.getSubsidiary().getProvider().getName());
        ordenCompra.setNombreTaller(dataEvent.getWorkshopName());
        ordenCompra.setNumSiniestro(dataEvent.getClaimNumber());
        ordenCompra.setObservaciones(order.getComment() == null ? "No disponible" : order.getComment());
        ordenCompra.setPlaca(dataEvent.getPlate());
        ordenCompra.setRutTaller(dataEvent.getWorkshopNit());
        ordenCompra.setDireccionTaller(dataEvent.getWorkshopAddress());
        ordenCompra.setTelefonoTaller(dataEvent.getWorkshopPhone().toString());
        ordenCompra.setTelefonoProveedor(order.getSubsidiary().getProvider().getPhone());
        ordenCompra.setSubtotal(sellOrder.getSubtotal() == null ? (double) 0 : BigDecimal.valueOf(sellOrder.getSubtotal()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        ordenCompra.setTotalDescuento(BigDecimal.valueOf(totalDiscount).setScale(2, RoundingMode.HALF_UP).doubleValue());
        ordenCompra.setPreciototalIVA(sellOrder.getIva() == null ? (double) 0 : BigDecimal.valueOf(sellOrder.getIva()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        ordenCompra.setValorTotal(BigDecimal.valueOf(sellOrder.getTotal() == null ? 0 : sellOrder.getTotal()).setScale(2, RoundingMode.HALF_UP).doubleValue());
        ordenCompra.setVersion(dataEvent.getVersion());

        ordenCompra.setImprevistos(order.getUnforeseen() != null ? order.getUnforeseen() : Boolean.FALSE);

        ordenCompra.setCodigoSucursal(
                order.getSubsidiary() == null || order.getSubsidiary().getIdJob() == null ?
                        0 : Integer.parseInt(order.getSubsidiary().getIdJob()));

        ordenCompra.setOrdenReparacion(order.getRepairOrder() != null ? order.getRepairOrder().longValue() : 0L);

        dataEventRepositoryPort
                .findByExternalEventAndId(order.getNotice().getExternalEvent(), order.getNotice().getEventId())
                .ifPresent(source -> ordenCompra.setPuestoTrabajo(source.getIdJob()));

        log.info("Orden de compra Chile: " + ordenCompra);

        EnviarOrdenCompraRequest enviarOrdenCompraRequest = new EnviarOrdenCompraRequest();
        enviarOrdenCompraRequest.setOrdenCompra(ordenCompra);
        enviarOrdenCompraRequest.sdkRequestConfig(SdkRequestConfig.builder().customHeader("idCanal", insuranceCarrier.getId().toString()).build());

        String errorMessage = "";

        try {
            getIntegrationsChileSDKInstance().enviarOrdenCompra(enviarOrdenCompraRequest).getOrdenCompraResponse().getRtaMensaje();
        } catch (Exception e) {
            if (e instanceof com.subocol.integracionorbikachile.model.BadRequestException) {
                com.subocol.integracionorbikachile.model.BadRequestException b = (com.subocol.integracionorbikachile.model.BadRequestException) e;
                log.error("Se genero BadRequestException al momento de enviar la orden de compra a Chile: " + b.getRtaMensaje());
                errorMessage = (b.getRtaMensaje());
            } else if (e instanceof com.subocol.integracionorbikachile.model.InternalServerErrorException) {
                com.subocol.integracionorbikachile.model.InternalServerErrorException b = (com.subocol.integracionorbikachile.model.InternalServerErrorException) e;
                log.error("Se genero InternalServerErrorException al momento de enviar la orden de compra a Chile: " + b.getRtaMensaje());
                errorMessage = (b.getRtaMensaje());
            } else {
                log.error("Se genero error interno al momento de enviar la orden de compra a Chile: " + e.getMessage());
                errorMessage = (e.getMessage());
            }

            return false;
        }

        return true;
    }

    public IntegracionOrbikaChileSdk getIntegrationsChileSDKInstance() {

        IntegrationAWS credentials = propertiesBean.getCurrentAwsPropertyByKey(PropertiesConstants.AWS_CREDENTIALS_CHILE);
        AWSCredentials awsSessionCredentials = new BasicAWSCredentials(credentials.getAccessKey(), credentials.getSecretAccessKey());

        return IntegracionOrbikaChileSdk.builder()
                .iamCredentials(new AWSStaticCredentialsProvider(awsSessionCredentials))
                .build();

    }

    public SellOrder createSellOrderWithOrder(Order order) {
        return new SellOrder()
                .setOrder(order)
                .setCreationDate(TimeZoneUtil.getTimestampByDefaultZone())
                .setLastUpdateDate(TimeZoneUtil.getTimestampByDefaultZone())
                .setSubtotal(0.0)
                .setIva(0.0)
                .setTotal(0.0)
                .setPdfUrl(null)
                .setDetails(new HashSet<>());
    }

}
