package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.Tax;

import java.util.Optional;

@Port
public interface TaxRepositoryPort {
    Optional<Tax> findTaxByCountry(Long countryId);
}
