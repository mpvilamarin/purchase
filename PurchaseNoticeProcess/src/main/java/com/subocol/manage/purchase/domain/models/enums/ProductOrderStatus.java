package com.subocol.manage.purchase.domain.models.enums;

public enum ProductOrderStatus {

    ASSIGNED, QUOTED, ON_ROUTE, DELIVERED_SUBSIDIARY, DELIVERED, DELIVERED_NEWS, ACCEPTED, DELAYED, COMPLETED, REJECTED, DEVOLUTION, IMPORTATION,
    MANUAL_PURCHASE, REJECTED_QUOTED, OMITTED, SENDED, PURCHASE, UNAUTHORIZED, BUDGETED, LATE, BOUGHT, DESIST;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }

}
