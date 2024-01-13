package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.ManualPurchaseAdi;

import java.util.List;
import java.util.Optional;


@Port
public interface ManualPurchaseAdiRepositoryPort {

    List<ManualPurchaseAdi> findManualPurchaseAdiByEventFilterManualPurchaseForAuth(Long externalEvent, Long eventId, Long noticeId, boolean totParameter);

    List<ManualPurchaseAdi> findManualPurchaseAdiByEventFilterManualPurchase(Long externalEvent, Long eventId, boolean totParameter);

    Optional<ManualPurchaseAdi> findByExternalEventAndPosition(Integer externalEvent, Integer position);

}
