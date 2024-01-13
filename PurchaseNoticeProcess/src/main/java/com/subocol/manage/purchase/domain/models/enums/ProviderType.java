package com.subocol.manage.purchase.domain.models.enums;

public enum ProviderType {

    MOSTRADOR, AUTOSUMINISTRO, MIXTO, EXTERNO;

    public String toString() {
        return this.name().toLowerCase();
    }

}
