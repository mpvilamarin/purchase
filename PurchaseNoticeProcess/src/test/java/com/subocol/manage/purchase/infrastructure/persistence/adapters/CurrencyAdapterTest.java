package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.domain.models.Currency;
import com.subocol.manage.purchase.domain.models.Tax;
import com.subocol.manage.purchase.infrastructure.persistence.entities.CurrencyModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.TaxModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.CurrencyRepository;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.TaxRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyAdapterTest {

    @Mock
    private CurrencyRepository repository;

    @InjectMocks
    private CurrencyAdapter currencyAdapter;

    @Test
    void TaxAdapter_findTaxByCountry_ReturnsTaxEntity() {

        Long countryId = 456L;

        CurrencyModel currencyModel = CurrencyModel.builder()
                .id(45L)
                .description("currency")
                .countryId(countryId)
                .prefix("$")
                .fixedRate(5D)
                .divide(10)
                .build();

        when(repository.findByCountryId(anyLong())).thenReturn(Optional.ofNullable(currencyModel));

        Optional<Currency> result = currencyAdapter.findByCountryId(countryId);

        verify(repository, times(1)).findByCountryId(countryId);

        assertThat(result).isPresent();
        assertThat(result.get()).isNotNull();
        assertThat(result.get()).isExactlyInstanceOf(Currency.class);

        Currency domainCurrency = result.get();
        assert currencyModel != null;
        Assertions.assertEquals(countryId,domainCurrency.getCountryId());
        Assertions.assertEquals(currencyModel.getPrefix(), domainCurrency.getPrefix());
        Assertions.assertEquals(currencyModel.getDescription(), domainCurrency.getDescription());
        Assertions.assertEquals(currencyModel.getId(), domainCurrency.getId());

    }


}