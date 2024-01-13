package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.InsuranceCarrier;

import java.util.Optional;

@Port
public interface InsuranceCarrierRepositoryPort {

    Optional<InsuranceCarrier> findById(Long id);

}
