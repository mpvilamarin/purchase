package com.subocol.manage.purchase.domain.services;

import com.subocol.manage.purchase.common.annotations.ServiceDeclaration;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.models.Insurer;
import com.subocol.manage.purchase.domain.models.ManualPurchase;
import com.subocol.manage.purchase.domain.models.ManualPurchaseAdi;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterProductQuotation;

import java.util.List;

@ServiceDeclaration
public interface SendManualPurchase {

    void setManualPurchasePieces(Notice notice, Insurer insurer) throws ExceptionUtil;

    List<ManualPurchaseAdi> findManualPurchaseADIToManualProcess(boolean auth, Long externalEvent, Long eventId, Long noticeId, boolean totParameter);

    List<CounterProductQuotation> findCounterProductQuotationToManualProcess(Long noticeId, List<ManualPurchaseAdi> listProductsInAdiSchema);

    ManualPurchase settingManualPurchase(ManualPurchaseAdi productInAdiSchema, CounterProductQuotation dataProduct, Notice notice, Insurer insurer);

}