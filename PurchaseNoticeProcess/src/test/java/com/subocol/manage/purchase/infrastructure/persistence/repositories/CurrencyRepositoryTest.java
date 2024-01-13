package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.infrastructure.persistence.entities.CurrencyModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.DataEventModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CurrencyRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CurrencyRepository repository;

    private CurrencyModel currencyModel;
    private long countryId=454L;

    @BeforeEach
    void setup() {
        currencyModel = CurrencyModel.builder()
                .description("currency")
                .countryId(countryId)
                .prefix("$")
                .fixedRate(5D)
                .divide(10)
                .currencyId(56)
                .build();
    }


    @Test
    void saveCurrencyModel() {
        //Given-preconditions

        //when-action to do
        CurrencyModel result = repository.save(currencyModel);
        //then-verify result
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(currencyModel.getCountryId(),result.getCountryId());
        Assertions.assertEquals(currencyModel.getPrefix(), result.getPrefix());
        Assertions.assertEquals(currencyModel.getDescription(), result.getDescription());


    }

    @Test
    void DataEventRepository_findDataEventById() {
        //Given-preconditions

        CurrencyModel currencyModelSaved = repository.save(currencyModel);

        //when-action to do
        Optional<CurrencyModel> result = repository.findByCountryId(countryId);

        //then-verify result
        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(CurrencyModel.class);
        Assertions.assertEquals(currencyModelSaved, result.get());

    }

}
