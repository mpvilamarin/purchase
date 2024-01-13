package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.InsurerModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ReplacementQualityModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReplacementQualityRepositoryTest {

    private final ReplacementQualityRepository repository;

    private ReplacementQualityModel replacementQualityModel;

    @Autowired
    ReplacementQualityRepositoryTest(ReplacementQualityRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setup() {

        this.replacementQualityModel = ReplacementQualityModel.builder()
                .id(123L)
                .name("Alterno")
                .defaultQuality(Boolean.FALSE)
                .insurer(new InsurerModel().setId(3L))
                .build();

    }

    @Test
    void ReplacementQualityRepository_save_ReturnSavedReplacementQuality() {

        ReplacementQualityModel replacementQualitySaved = repository.save(replacementQualityModel);

        assertNotNull(replacementQualitySaved);
        assertThat(replacementQualitySaved.getId()).isPositive();
        Assertions.assertEquals(replacementQualityModel.getName(), replacementQualitySaved.getName());
        Assertions.assertEquals(replacementQualityModel.getDefaultQuality(), replacementQualitySaved.getDefaultQuality());
        assertNotNull(replacementQualitySaved.getInsurer());
        Assertions.assertEquals(replacementQualityModel.getInsurer().getId(), replacementQualitySaved.getInsurer().getId());

    }

    @Test
    void ReplacementQualityRepository_findById_ReturnReplacementQuality() {

        ReplacementQualityModel replacementQualitySaved = repository.save(replacementQualityModel);
        Optional<ReplacementQualityModel> result = repository.findById(replacementQualitySaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(ReplacementQualityModel.class);
        Assertions.assertEquals(replacementQualitySaved, result.get());

    }

    @Test
    void ReplacementQualityRepository_update_ReturnUpdatedReplacementQuality() {

        ReplacementQualityModel replacementQualitySaved = repository.save(replacementQualityModel);

        replacementQualitySaved.setName("Alterno Updated");

        ReplacementQualityModel replacementQualityUpdated = repository.save(replacementQualitySaved);

        Assertions.assertEquals(replacementQualitySaved.getId(), replacementQualityUpdated.getId());
        Assertions.assertEquals(replacementQualitySaved.getName(), replacementQualityUpdated.getName());
    }

    @Test
    void ReplacementQualityRepository_delete() {

        ReplacementQualityModel replacementQualitySaved = repository.save(replacementQualityModel);

        repository.deleteById(replacementQualitySaved.getId());

        Optional<ReplacementQualityModel> result = repository.findById(replacementQualitySaved.getId());

        assertThat(replacementQualitySaved.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

}