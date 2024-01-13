package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.Tax;
import com.subocol.manage.purchase.domain.ports.persistence.TaxRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.TaxRepository;

import java.util.Optional;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */
@Adapter
public class TaxAdapter implements TaxRepositoryPort {

    private final TaxRepository repository;

    public TaxAdapter(TaxRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Tax> findTaxByCountry(Long countryId) {
        return repository
                .findByCountryId(countryId)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, Tax.class)));
    }
}
