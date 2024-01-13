package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.Subsidiary;
import com.subocol.manage.purchase.domain.ports.persistence.SubsidiaryRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.SubsidiaryRepository;

import java.util.Optional;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */
@Adapter
public class SubsidiaryAdapter implements SubsidiaryRepositoryPort {

    private final SubsidiaryRepository repository;

    public SubsidiaryAdapter(SubsidiaryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Subsidiary> findById(Long subsidiaryId) {
        return repository
                .findById(subsidiaryId)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, Subsidiary.class)));
    }

    @Override
    public Optional<Subsidiary> findByOrderId(Long orderId) {
        return repository
                .findFirstByOrders_Id(orderId)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, Subsidiary.class)));
    }
}
