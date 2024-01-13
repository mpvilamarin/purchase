package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.ManualPurchase;

import java.util.List;
import java.util.Optional;

@Port
public interface ManualPurchaseRepositoryPort {

    int updateManualPurchaseByPosition(String status, String externalEvent, List<Integer> positions);

    int updatePurchaseSubsidiary(String externalEvent, List<Integer> positions, boolean purchase);

    ManualPurchase save(ManualPurchase manualPurchase);

    int countDeletedPiecesByPositions(String externalEvent, List<Integer> positions);

    int updateAuthByExternalEventAndPosition(Boolean auth, String externalEvent, List<Integer> positions);

    Optional<ManualPurchase> findByPositionAndExternalEvent(Integer position, String externalEvent);

    int updateDesistCauseAndStatusById(Long manualPurchaseId);

    int updateDeletePiecesTrueByExternalEventAndPosition(String externalEvent, Integer position);
}
