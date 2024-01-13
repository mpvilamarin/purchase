package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.Currency;

import java.util.Optional;

@Port
public interface CurrencyRepositoryPort {

    Optional<Currency> findByCountryId(Long countryId);

}
