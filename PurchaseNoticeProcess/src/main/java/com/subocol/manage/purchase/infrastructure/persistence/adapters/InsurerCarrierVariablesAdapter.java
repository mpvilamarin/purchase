package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.InsuranceCarrierVariables;
import com.subocol.manage.purchase.domain.ports.persistence.InsuranceCarrierVariablesRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.InsuranceCarrierVariablesRepository;

import java.util.Optional;

@Adapter
public class InsurerCarrierVariablesAdapter implements InsuranceCarrierVariablesRepositoryPort {

    private final InsuranceCarrierVariablesRepository repository;

    public InsurerCarrierVariablesAdapter(InsuranceCarrierVariablesRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<InsuranceCarrierVariables> findByInsuranceId(Long insuranceId) {
        return repository
                .findFirstByInsuranceId(insuranceId)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, InsuranceCarrierVariables.class)));
    }
}
