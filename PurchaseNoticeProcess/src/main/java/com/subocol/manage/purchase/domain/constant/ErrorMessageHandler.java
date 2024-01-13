package com.subocol.manage.purchase.domain.constant;


public class ErrorMessageHandler {

    private ErrorMessageHandler() {

    }

    public static final String ERROR_MANAGE_NOTICE = "An error occurred while managing the notice with noticeId: ";
    public static final String ERROR_CREATE_ORDER = "An error occurred while managing the order with noticeId: ";
    public static final String ERROR_CHANGE_STATUS = "Could not change status to ";
    public static final String ERROR_CREATE_ORDER_AND_PRODUCTS = "An error occurred while creating the order and products with noticeId: ";
    public static final String SUBSIDIARY_NOT_FOUND = "Subsidiary not found with id: ";
    public static final String SELL_ORDER_NOT_FOUND = "Sell_Order not found with order_id: ";
    public static final String QUOTATION_NOT_FOUND = "Quotation not found with id: ";
    public static final String QUOTATION_NOT_FOUND_BY_NOTICE_ID = "Quotation not found with notice_id: ";
    public static final String NOTICE_NOT_FOUND = "Notice not found with id:";
    public static final String INSURER_CARRIER_NOT_FOUND = "InsurerCarrier not found with id:";
    public static final String INSURER_CARRIER_VARIABLES_NOT_FOUND = "InsurerCarrierVariables not found with id:";
    public static final String INSURER_NOT_FOUND = "Insurer not found with id:";
    public static final String ERROR_CREATE_PRODUCT_ORDER = "An error occurred while creating the order products";
    public static final String ERROR_CREATE_PURCHASE_ORDER = "An error occurred in createPurchaseOrder";
    public static final String ERROR_SAVE_SELL_ORDER = "An error occurred in saveSellOrder";
    public static final String ERROR_CREATE_SELL_ORDER_DETAIL = "An error occurred creating the SellOrderDetail";
    public static final String ERROR_DESIST_PRODUCT = "An error occurred desist product";
    public static final String ERROR_CREATE_MANUAL_PURCHASE_DESIST = "An error occurred creating the ManualPurchase with status desist";
    public static final String ERROR_CREATE_STATUS_REPLACEMENT = "An error occurred creating the StatusReplacement";
    public static final String ERROR_CREATE_PURCHASE_ORDER_ID = "An error occurred in createPurchaseOrderId";
    public static final String ERROR_CREATE_STATUS_PARTS = "An error occurred creating the StatusParts";
    public static final String ERROR_SET_MANUAL_PURCHASE = "An error occurred sending the parts to the manual purchase with noticeId: ";
    public static final String ERROR_FIND_MANUAL_PURCHASE_ADI = "An error occurred in findManualPurchaseADIToManualProcess with noticeId: ";
    public static final String ERROR_COUNTERS_MANUAL_PURCHASE_PROCESS = "An error occurred in findCounterProductQuotationToManualProcess with noticeId: ";
    public static final String ERROR_SAVE_MANUAL_PURCHASE = "An error occurred saving parts to manual purchase with noticeId: ";
    public static final String ERROR_ALERT_PIECES_VALUATION = "An error occurred when sending the pieces for valuation";
    public static final String ERROR_EXTERNAL_SERVICE_ALERT_PIECES_VALUATION = "An error occurred in the external service sending a valuation";
    public static final String ERROR_SEND_RESERVE_BOLIVAR = "An error occurred when sending the reservation to bolivar with noticeId: ";
    public static final String ERROR_VALIDATE_POSITION_BOLIVAR = "An error occurred validating the initial positions with notice: ";
    public static final String ERROR_CALCULATING_VALUES_BOLIVAR = "An error occurred calculating the reservation values with noticeId: ";
    public static final String ERROR_VALIDATE_POSITION_BOLIVAR_WITH_DESIST = "An error occurred while calculating reservation values with desist orders with notice: ";
    public static final String ERROR_CREATE_SEND_RESERVE_MANAGE = "An error occurred creating the SendReserveManage";
    public static final String ERROR_EXTERNAL_SERVICE_RESERVE_BOLIVAR = "An error occurred in the external service sending reserve bolivar with notice: ";
    public static final String ERROR_SAVING_ORDERS_AND_PRODUCT_ORDERS = "An error occurred creating orders and productOrders";
    public static final String ERROR_PREVIOUSLY_AUTHORIZED_NOTICE = "An error occurred authorizing notices, the notices has been previously authorized";
    public static final String ERROR_SHIPPED_PARTS_DELETED = "An error occurred authorizing notices, shipped parts have been removed and cannot be authorized";
    public static final String ERROR_AUTHORIZING_NOTICE_PIECE_NOT_FOUND = "An error occurred authorizing notices, shipped part not found in database";
    public static final String ERROR_EMPTY_SPARE_PARTS = "An error occurred authorizing notices, spare parts are not being sent to authorize";
    public static final String ERROR_VALIDATING_QUOTATION_TIME = "An error occurred authorizing notices, getTimeToValidate error";
    public static final String ERROR_AUTHORIZING_NOTICE = "An error occurred authorizing notices";
    public static final String ERROR_UPDATING = "An error occurred authorizing notices";
    public static final String ERROR_VALIDATING_INIT_POSITIONS = "An error occurred in validateInitPositions";
    public static final String ERROR_CALCULATING_VALUES_RESERVE = "An error occurred in calculateValuesReserve";
    public static final String ERROR_CREATING_SEND_RESERVE = "An error occurred in createSendReserveManage";
    public static final String ERROR_VALIDATING_INIT_POSITIONS_SURA = "An error occurred in validateInitPositionsSura";

    public static final String ERROR_INTERNAL_EXTERNAL_SERVICES = "An error occurred in the external service with notice: ";

    public static final String ERROR_SEND_RESERVE_SURA = "An error occurred when sending the reservation to SURA with externalEvent: ";
    public static final String ERROR_VALIDATE_POSITION_SURA = "An error occurred validating the initial positions to SURA with externalEvent: ";
    public static final String ERROR_CALCULATING_VALUES_SURA = "An error occurred calculating the reservation values to SURA with noticeId: ";
    public static final String ERROR_VALIDATE_POSITION_WITH_DESIST_SURA = "An error occurred while calculating reservation values with desist orders to SURA with externalEvent: ";

    public static final String ERROR_SEND_MAIL_ORDER = "An error occurred when sending the mail order notification ";
    public static final String ERROR_CREATING_SEND_MAIL_ORDER_DTO = "An error occurred when in the creation of the mail order notification";

    public static final String CURRENCY_NOT_FOUND_BY_COUNTRY_ID = "Currency not found with countryId: ";

    public static final String DATA_EVENT_NOT_FOUND = "DataEvent not found with id: ";

    public static final String TAX_NOT_FOUND_BY_COUNTRY_ID = "TAx not found with countryId: ";

    public static final String INSURANCE_CARRIER_NOT_FOUND = "insuranceCarrier not found with id: ";

    public static final String PROPERTY_NOT_FOUND = "Could Not found property in database, the property id is: ";
    public static final String AWS_PROPERTY_NOT_FOUND = "Could Not found aws property in database, the property id is: ";
    public static final String PROPERTY_NOT_FOUND_IN_HASHMAP = "Could Not found property in Hashmap, the property key is: ";
    public static final String ERROR_SET_PRODUCT_OVERRUN_COST = "An error occurred while setProductOverrunCost with noticeId: ";
    public static final String ERROR_SPARE_FOLLOWUP = "An error occurred when sending the spares to follow-up";
    public static final String ERROR_MANAGE_NOTICE_CONCESSIONAIRE = "An error occurred while managing the notice with noticeId: ";

    public static final String ERROR_SEND_BILLING_ORDERS = "An error occurred when sending the pieces to billing";
    public static String concatenateStringAndObject(String constant, Object obj) {
        return constant + obj.toString();
    }
}
