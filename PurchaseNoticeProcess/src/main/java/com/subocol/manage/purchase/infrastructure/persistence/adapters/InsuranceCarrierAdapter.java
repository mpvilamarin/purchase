package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.InsuranceCarrier;
import com.subocol.manage.purchase.domain.ports.persistence.InsuranceCarrierRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.InsuranceCarrierRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Adapter
public class InsuranceCarrierAdapter implements InsuranceCarrierRepositoryPort {

    private final InsuranceCarrierRepository repository;

    public InsuranceCarrierAdapter(InsuranceCarrierRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InsuranceCarrier> findById(Long id) {
        return repository
                .findById(id)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, InsuranceCarrier.class)));
    }
}

