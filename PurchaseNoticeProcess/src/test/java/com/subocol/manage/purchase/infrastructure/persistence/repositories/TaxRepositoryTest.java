package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.TaxModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaxRepositoryTest {

    private final TaxRepository taxRepository;

    private TaxModel taxModel;

    @Autowired
    public TaxRepositoryTest(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }

    @BeforeEach
    void setup() {

        taxModel = TaxModel.builder()
                .taxIdDms(123)
                .description("Sample tax description")
                .percentage(10)
                .type(1)
                .countryId(456L)
                .taxName("Sample Tax")
                .build();

    }

    @Test
    void TaxRepository_save_ReturnSavedTax() {

        TaxModel result = taxRepository.save(taxModel);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(result.getTaxIdDms(), taxModel.getTaxIdDms());
        Assertions.assertEquals(result.getDescription(), taxModel.getDescription());
        Assertions.assertEquals(result.getPercentage(), taxModel.getPercentage());
        Assertions.assertEquals(result.getType(), taxModel.getType());
        Assertions.assertEquals(result.getCountryId(), taxModel.getCountryId());
        Assertions.assertEquals(result.getTaxName(), taxModel.getTaxName());
    }

    @Test
    void TaxRepository_findById_ReturnTax() {

        TaxModel taxSaved = taxRepository.save(taxModel);
        Optional<TaxModel> result = taxRepository.findById(taxSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(TaxModel.class);
        Assertions.assertEquals(taxSaved, result.get());

    }

    @Test
    void TaxRepository_update_ReturnUpdatedTax() {

        TaxModel taxSaved = taxRepository.save(taxModel);

        taxSaved.setPercentage(20)
                .setDescription("Updated Description");

        TaxModel TaxUpdated = taxRepository.save(taxSaved);

        Assertions.assertEquals(taxSaved.getId(), TaxUpdated.getId());
        Assertions.assertEquals(taxSaved.getPercentage(), TaxUpdated.getPercentage());
        Assertions.assertEquals(taxSaved.getDescription(), TaxUpdated.getDescription());
    }

    @Test
    void TaxRepository_delete() {

        TaxModel taxSaved = taxRepository.save(taxModel);

        taxRepository.deleteById(taxSaved.getId());

        Optional<TaxModel> result = taxRepository.findById(taxSaved.getId());

        assertThat(taxSaved.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

    @Test
    void TaxRepository_FindByCountryId_ReturnTax() {

        TaxModel taxSaved = taxRepository.save(taxModel);

        Optional<TaxModel> optionalTaxModel = taxRepository.findByCountryId(taxSaved.getCountryId());

        assertThat(optionalTaxModel).isPresent();
        assertThat(optionalTaxModel.get()).isNotNull();
        assertThat(optionalTaxModel.get().getCountryId()).isEqualTo(taxModel.getCountryId());

    }

}