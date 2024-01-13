package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.IntegrationAWSModel;
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
class IntegrationAWSRepositoryTest {

    private final IntegrationAWSRepository repository;

    private IntegrationAWSModel integrationAWSModel;

    @Autowired
    IntegrationAWSRepositoryTest(IntegrationAWSRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setup() {

        this.integrationAWSModel = IntegrationAWSModel.builder()
                .id(12877L)
                .accessKey("SAD!!!!EF3421fad!")
                .name("BUCKET KEY")
                .secretAccessKey("NDAIUDAIHD(/!H(")
                .build();

    }


    @Test
    void IntegrationAWSModelRepository_findById_ReturnIntegrationAWSModel() {

        IntegrationAWSModel integrationAWSModelSaved = repository.save(integrationAWSModel);
        Optional<IntegrationAWSModel> result = repository.findById(integrationAWSModelSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(IntegrationAWSModel.class);
        Assertions.assertEquals(integrationAWSModelSaved, result.get());

    }

    @Test
    void IntegrationAWSModelRepository_findAll_ReturnAllIntegrationAWSModels() {
        IntegrationAWSModel integrationAWSModelSaved = repository.save(integrationAWSModel);
        List<IntegrationAWSModel> propertiesModels = repository.findAll();

        assertThat(propertiesModels).hasSize((int) repository.count())
                .contains(integrationAWSModelSaved);
    }

}