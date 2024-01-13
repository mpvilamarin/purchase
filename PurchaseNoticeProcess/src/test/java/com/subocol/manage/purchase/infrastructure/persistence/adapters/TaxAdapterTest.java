package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.domain.models.Tax;
import com.subocol.manage.purchase.infrastructure.persistence.entities.TaxModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.TaxRepository;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxAdapterTest {

    @Mock
    private TaxRepository repository;

    @InjectMocks
    private TaxAdapter taxAdapter;

    @Test
    void TaxAdapter_findTaxByCountry_ReturnsTaxEntity() {

        Long countryId = 456L;

        TaxModel taxModel = TaxModel.builder()
                .taxIdDms(123)
                .description("Sample tax description")
                .percentage(10)
                .type(1)
                .countryId(456L)
                .taxName("Sample Tax")
                .build();

        when(repository.findByCountryId(anyLong())).thenReturn(Optional.ofNullable(taxModel));

        Optional<Tax> resultTax = taxAdapter.findTaxByCountry(countryId);

        verify(repository, times(1)).findByCountryId(countryId);

        assertThat(resultTax).isPresent();
        assertThat(resultTax.get()).isNotNull();
        assertThat(resultTax.get()).isExactlyInstanceOf(Tax.class);

        Tax domainTax = resultTax.get();

        assertThat(domainTax.getCountryId()).isEqualTo(countryId);
        assertThat(taxModel).isNotNull();
        Assertions.assertEquals(domainTax.getTaxIdDms(), taxModel.getTaxIdDms());
        Assertions.assertEquals(domainTax.getDescription(), taxModel.getDescription());
        Assertions.assertEquals(domainTax.getPercentage(), taxModel.getPercentage());
        Assertions.assertEquals(domainTax.getType(), taxModel.getType());
        Assertions.assertEquals(domainTax.getCountryId(), taxModel.getCountryId());
        Assertions.assertEquals(domainTax.getTaxName(), taxModel.getTaxName());

    }

    @Test
    void TaxAdapter_findTaxByCountry_ReturnsEmptyOptional() {

        Long countryId = 456L;

        when(repository.findByCountryId(anyLong())).thenReturn(Optional.empty());

        Optional<Tax> resultTax = taxAdapter.findTaxByCountry(countryId);

        verify(repository, times(1)).findByCountryId(countryId);

        assertThat(resultTax).isNotPresent();

    }

}