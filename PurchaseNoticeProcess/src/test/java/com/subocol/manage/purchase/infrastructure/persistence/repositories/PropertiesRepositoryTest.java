package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.PropertiesModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PropertiesRepositoryTest {

    private final PropertiesRepository repository;

    private PropertiesModel propertiesModel;

    @Autowired
    PropertiesRepositoryTest(PropertiesRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setup() {

        this.propertiesModel = PropertiesModel.builder()
                .id(9982L)
                .property("https://www.google.com/")
                .consumeBy("PROVIDER")
                .description("This is the Google URL")
                .exposeBy("NOTICE")
                .build();

    }


    @Test
    void PropertiesModelRepository_findById_ReturnPropertiesModel() {

        PropertiesModel propertiesModelSaved = repository.save(propertiesModel);
        Optional<PropertiesModel> result = repository.findById(propertiesModelSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(PropertiesModel.class);
        Assertions.assertEquals(propertiesModelSaved, result.get());

    }

    @Test
    void PropertiesModelRepository_findAll_ReturnAllPropertiesModels() {
        PropertiesModel propertiesModelSaved = repository.save(propertiesModel);
        List<PropertiesModel> propertiesModels = repository.findAll();

        assertThat(propertiesModels).hasSize((int) repository.count())
                .contains(propertiesModelSaved);
    }


}