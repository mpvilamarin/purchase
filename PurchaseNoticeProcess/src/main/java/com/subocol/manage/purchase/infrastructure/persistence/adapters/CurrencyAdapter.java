package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.Currency;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.ports.persistence.CurrencyRepositoryPort;
import com.subocol.manage.purchase.domain.ports.persistence.NoticeRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.entities.NoticeModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.CurrencyRepository;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.NoticeRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */

@Adapter
public class CurrencyAdapter implements CurrencyRepositoryPort {

    private final CurrencyRepository repository;

    public CurrencyAdapter(CurrencyRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Currency> findByCountryId(Long countryId) {
        return repository.findByCountryId(countryId).flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, Currency.class)));

    }
}
