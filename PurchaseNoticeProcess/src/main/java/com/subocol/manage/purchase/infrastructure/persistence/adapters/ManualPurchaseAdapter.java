package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.ManualPurchase;
import com.subocol.manage.purchase.domain.models.Quotation;
import com.subocol.manage.purchase.domain.models.Tax;
import com.subocol.manage.purchase.domain.ports.persistence.ManualPurchaseRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ManualPurchaseModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ManualPurchaseRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */

@Adapter
public class ManualPurchaseAdapter implements ManualPurchaseRepositoryPort {

    private final ManualPurchaseRepository repository;

    public ManualPurchaseAdapter(ManualPurchaseRepository repository) {
        this.repository = repository;
    }

    @Override
    public int updateManualPurchaseByPosition(String status, String externalEvent, List<Integer> positions) {
        return repository.setStatusByExternalEventAndPositionIn(status, externalEvent, positions);
    }

    @Override
    public int updatePurchaseSubsidiary(String externalEvent, List<Integer> positions, boolean purchase) {
        return repository.setPurchaseSubsidiaryByExternalEventAndPositionIn(purchase, externalEvent, positions);
    }

    @Override
    public ManualPurchase save(ManualPurchase manualPurchase) {
        ManualPurchaseModel mpSaved = repository.save(MapperUtil.convert(manualPurchase, ManualPurchaseModel.class));
        return MapperUtil.convert(mpSaved, ManualPurchase.class);
    }

    @Override
    public int countDeletedPiecesByPositions(String externalEvent, List<Integer> positions) {
        return repository.countDeletedPiecesByPositions(externalEvent, positions);
    }

    @Override
    public int updateAuthByExternalEventAndPosition(Boolean auth, String externalEvent, List<Integer> positions) {
        return repository.updateAuthByExternalEventAndPosition(auth, externalEvent, positions);
    }

    @Override
    public Optional<ManualPurchase> findByPositionAndExternalEvent(Integer position, String externalEvent) {
        return repository
                .findByPositionAndExternalEvent(position, externalEvent)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, ManualPurchase.class)));
    }

    @Override
    public int updateDesistCauseAndStatusById(Long manualPurchaseId) {
        return repository.updateDesistCauseAndStatusById(manualPurchaseId);
    }

    @Override
    public int updateDeletePiecesTrueByExternalEventAndPosition(String externalEvent, Integer position) {
        return repository.updateDeletePiecesTrueByExternalEventAndPosition(externalEvent, position);
    }

}
