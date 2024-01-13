package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.InsuranceCarrierVariables;

import java.util.Optional;

@Port
public interface InsuranceCarrierVariablesRepositoryPort {
    Optional<InsuranceCarrierVariables> findByInsuranceId(Long insuranceId);
}
