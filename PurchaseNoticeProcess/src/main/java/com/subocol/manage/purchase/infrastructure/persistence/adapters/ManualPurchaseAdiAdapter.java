package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.ManualPurchaseAdi;
import com.subocol.manage.purchase.domain.ports.persistence.ManualPurchaseAdiRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ManualPurchaseAdiModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ManualPurchaseAdiRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author DANR
 * @version 1.0
 * @since 8/06/2023
 */
@Adapter
public class ManualPurchaseAdiAdapter implements ManualPurchaseAdiRepositoryPort {

    private final ManualPurchaseAdiRepository repository;

    public ManualPurchaseAdiAdapter(ManualPurchaseAdiRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ManualPurchaseAdi> findManualPurchaseAdiByEventFilterManualPurchaseForAuth(Long externalEvent, Long eventId, Long noticeId, boolean totParameter) {

        List<ManualPurchaseAdiModel> result = repository.getAllByEventFilterManualPurchaseForAuth(externalEvent, eventId, noticeId, totParameter);

        if (!result.isEmpty())
            return MapperUtil.convertList(result, ManualPurchaseAdi.class);

        return Collections.emptyList();
    }

    @Override
    public List<ManualPurchaseAdi> findManualPurchaseAdiByEventFilterManualPurchase(Long externalEvent, Long eventId, boolean totParameter) {
        List<ManualPurchaseAdiModel> result = repository.getAllByEventFilterManualPurchase(externalEvent, eventId, totParameter);

        if (!result.isEmpty())
            return MapperUtil.convertList(result, ManualPurchaseAdi.class);

        return Collections.emptyList();
    }

    @Override
    public Optional<ManualPurchaseAdi> findByExternalEventAndPosition(Integer externalEvent, Integer position) {
        return repository
                .findByExternalEventAndPosition(externalEvent, position)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, ManualPurchaseAdi.class)));
    }
}
