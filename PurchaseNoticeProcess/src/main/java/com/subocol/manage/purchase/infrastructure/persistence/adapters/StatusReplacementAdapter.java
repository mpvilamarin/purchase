package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.StatusReplacement;
import com.subocol.manage.purchase.domain.ports.persistence.StatusReplacementRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.entities.StatusReplacementModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.StatusReplacementRepository;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */
@Adapter
public class StatusReplacementAdapter implements StatusReplacementRepositoryPort {

    private final StatusReplacementRepository repository;

    public StatusReplacementAdapter(StatusReplacementRepository repository) {
        this.repository = repository;
    }

    @Override
    public StatusReplacement save(StatusReplacement statusReplacement) {
        StatusReplacementModel statusReplacementSaved = repository.save(MapperUtil.convert(statusReplacement, StatusReplacementModel.class));
        return MapperUtil.convert(statusReplacementSaved, StatusReplacement.class);
    }
}
