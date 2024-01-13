package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.infrastructure.persistence.entities.EnvironmentsModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EnvironmentsRepositoryTest {

    private final EnvironmentsRepository environmentsRepository;

    private EnvironmentsModel environmentsModel;

    @Autowired
    EnvironmentsRepositoryTest(EnvironmentsRepository environmentsRepository) {
        this.environmentsRepository = environmentsRepository;
    }

    @BeforeEach
    void setup() {

        environmentsModel = EnvironmentsModel.builder()
                .id(999999999L)
                .environments("this is my environment")
                .build();

    }

    @Test
    void testFindByIdReturnEntity() {

        EnvironmentsModel environmentsModelSaved = environmentsRepository.save(environmentsModel);
        Optional<EnvironmentsModel> result = environmentsRepository.findById(environmentsModelSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(EnvironmentsModel.class);
        try {
            //Assertions.assertEquals for all entity attributes
            AttributeAssertions.assertAttributesEqual(environmentsModelSaved, result.get());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }

}