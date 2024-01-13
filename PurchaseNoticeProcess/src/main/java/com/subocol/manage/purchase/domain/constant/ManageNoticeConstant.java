package com.subocol.manage.purchase.domain.constant;

import java.util.List;

public class ManageNoticeConstant {

    private ManageNoticeConstant() {
    }

    public static final String AUTOMATIC = "Automatico";
    public static final String SENT = "sended";
    public static final String OMITTED = "omitted";
    public static final String ACCEPTED = "accepted";
    public static final String BOUGHT = "bought";
    public static final String QUOTED = "quoted";
    public static final String REJECTED_QUOTED = "rejected_quoted";
    public static final String ASSIGNED = "assigned";
    public static final Integer TYPE_AUTOMATIC = 1;
    public static final String AUTOMATIC_PURCHASE = "Compra Automática";
    public static final Integer TYPE_MANUAL = 2;
    public static final Integer TYPE_RENEGOTIATION = 3;
    public static final int COLOMBIA = 1;
    public static final int CHILE = 152;
    public static final int PANAMA = 2;
    public static final String TYPE_MULTI_BRAND = "M";
    public static final String TYPE_CONCESSIONAIRE = "A";
    public static final String TYPE_MIXED = "X";
    public static final String EXTERNAL = "E";
    public static final String HYPHEN = "-";
    public static final String PREFIX_COLOMBIA = "CO";
    public static final String PREFIX_CHILE = "CL";
    public static final String PREFIX_PANAMA = "PA";
    public static final String DEFAULT_MANUAL_PURCHASE_STATUS = "bought";
    public static final String MAX_COST_PIECE = "MAX_COST_PIECE";
    public static final String EXTRA_COST = "EXTRA_COST";
    public static final String MAX_DELIVERY_DAYS = "MAX_DELIVERY_DAYS";

    public static final String SELF_SUPPLY = "Autosuministro";
    public static final String DESIST="DESIST";

    public static final List<String> LIST_SPARE =List.of("REPU","Repuesto");
    public static final String TOT="TOT";
    public static final String BOLIVAR = "BOLIVAR";
    public static final String SURA = "SURA";
}

	

