package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.ProviderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SubsidiaryModel;
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
class SubsidiaryRepositoryTest {

    private final SubsidiaryRepository repository;

    private SubsidiaryModel subsidiaryModel;

    @Autowired
    public SubsidiaryRepositoryTest(SubsidiaryRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setup() {

        subsidiaryModel = SubsidiaryModel.builder()
                .alias("ADVANCE MOTORS SA CHIRIQI TEST")
                .email("sucpan101TEST@gmail.com")
                .name("ADVANCE MOTORS SA CHIRIQI TEST")
                .phone("7752509")
                .status(Boolean.TRUE)
                .locationExternalId(40L)
                .warehouseIdDms(null)
                .classification("Multimarca")
                .dmsCode(null)
                .idJob(null)
                .intermediation(null)
                .provider(new ProviderModel().setId(1L))
                .build();

    }

    @Test
    void SubsidiaryRepository_save_ReturnSavedSubsidiary() {

        SubsidiaryModel result = repository.save(subsidiaryModel);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(result.getAlias(), subsidiaryModel.getAlias());
        Assertions.assertEquals(result.getEmail(), subsidiaryModel.getEmail());
        Assertions.assertEquals(result.getName(), subsidiaryModel.getName());
        Assertions.assertEquals(result.getPhone(), subsidiaryModel.getPhone());
        Assertions.assertEquals(result.getStatus(), subsidiaryModel.getStatus());
        Assertions.assertEquals(result.getLocationExternalId(), subsidiaryModel.getLocationExternalId());
        Assertions.assertEquals(result.getWarehouseIdDms(), subsidiaryModel.getWarehouseIdDms());
        Assertions.assertEquals(result.getClassification(), subsidiaryModel.getClassification());
        Assertions.assertEquals(result.getDmsCode(), subsidiaryModel.getDmsCode());
        Assertions.assertEquals(result.getIdJob(), subsidiaryModel.getIdJob());
        Assertions.assertEquals(result.getIntermediation(), subsidiaryModel.getIntermediation());
    }

    @Test
    void SubsidiaryRepository_findById_ReturnSubsidiary() {

        SubsidiaryModel subsidiarySaved = repository.save(subsidiaryModel);
        Optional<SubsidiaryModel> result = repository.findById(subsidiarySaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(SubsidiaryModel.class);
        Assertions.assertEquals(subsidiarySaved, result.get());

    }

    @Test
    void SubsidiaryRepository_update_ReturnUpdatedSubsidiary() {

        SubsidiaryModel subsidiarySaved = repository.save(subsidiaryModel);

        subsidiarySaved.setName("Updated Name Test")
                .setStatus(Boolean.FALSE);

        SubsidiaryModel subsidiaryUpdated = repository.save(subsidiarySaved);

        Assertions.assertEquals(subsidiarySaved.getId(), subsidiaryUpdated.getId());
        Assertions.assertEquals(subsidiarySaved.getName(), subsidiaryUpdated.getName());
        Assertions.assertEquals(subsidiarySaved.getStatus(), subsidiaryUpdated.getStatus());
    }

    @Test
    void SubsidiaryRepository_delete() {

        SubsidiaryModel subsidiarySaved = repository.save(subsidiaryModel);

        repository.deleteById(subsidiarySaved.getId());

        Optional<SubsidiaryModel> result = repository.findById(subsidiarySaved.getId());

        assertThat(subsidiarySaved.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

    @Test
    void SubsidiaryRepository_findFirstByOrders_Id_ReturnSubsidiary() {

        Optional<SubsidiaryModel> result = repository.findFirstByOrders_Id(1469L);

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(SubsidiaryModel.class);
        Assertions.assertEquals(1L, result.get().getId());

    }


}