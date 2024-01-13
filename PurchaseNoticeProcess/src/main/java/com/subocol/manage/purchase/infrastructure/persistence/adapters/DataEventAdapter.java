package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.DataEvent;
import com.subocol.manage.purchase.domain.ports.persistence.DataEventRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.DataEventRepository;

import java.util.Optional;

/**
 * @author DANR
 * @version 1.0
 * @since 6/07/2023
 */
@Adapter
public class DataEventAdapter implements DataEventRepositoryPort {

    private final DataEventRepository repository;

    public DataEventAdapter(DataEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<DataEvent> findById(Long id) {
        return repository
                .findById(id)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, DataEvent.class)));

    }

    @Override
    public Optional<DataEvent> findByExternalEventAndId(Integer externalEvent, Long id) {
        return repository
                .findByExternalEventAndId(externalEvent, id)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, DataEvent.class)));
    }

    @Override
    public int updateClaimNumberByExternalEvent(String event, String claimNumber) {
        return repository.updateClaimNumberByExternalEvent(Integer.valueOf(event), claimNumber);
    }

    @Override
    public int updateAuthByExternalEvent(Integer event) {
        return repository.updateAuthByExternalEvent(event);
    }
}
