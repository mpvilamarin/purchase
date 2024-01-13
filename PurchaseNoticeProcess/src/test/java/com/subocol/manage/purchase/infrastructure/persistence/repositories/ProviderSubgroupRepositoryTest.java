package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProviderGroupModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProviderSubgroupModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProviderSubgroupRepositoryTest {

    private final ProviderSubgroupRepository repository;

    private ProviderSubgroupModel providerSubgroupModel;

    @Autowired
    ProviderSubgroupRepositoryTest(ProviderSubgroupRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setup() {

        this.providerSubgroupModel = ProviderSubgroupModel.builder()
                .nameSubgroup("SubGroup Name")
                .description("Test description")
                .creationUser("UserCreation")
                .creationDate(TimeZoneUtil.getTimestampByDefaultZone())
                .updateUser("UserUpdate")
                .updateDate(TimeZoneUtil.getTimestampByDefaultZone())
                .providerGroup(new ProviderGroupModel().setId(1L))
                .build();

    }

    @Test
    void ProviderSubgroupRepository_save_ReturnSavedProviderSubgroup() {

        ProviderSubgroupModel providerSubGroupSaved = repository.save(providerSubgroupModel);

        assertNotNull(providerSubGroupSaved);
        assertThat(providerSubGroupSaved.getId()).isPositive();
        Assertions.assertEquals(providerSubgroupModel.getNameSubgroup(), providerSubGroupSaved.getNameSubgroup());
        Assertions.assertEquals(providerSubgroupModel.getDescription(), providerSubGroupSaved.getDescription());
        Assertions.assertEquals(providerSubgroupModel.getCreationUser(), providerSubGroupSaved.getCreationUser());
        Assertions.assertEquals(providerSubgroupModel.getCreationDate(), providerSubGroupSaved.getCreationDate());
        Assertions.assertEquals(providerSubgroupModel.getUpdateUser(), providerSubGroupSaved.getUpdateUser());
        Assertions.assertEquals(providerSubgroupModel.getUpdateDate(), providerSubGroupSaved.getUpdateDate());
        assertNotNull(providerSubGroupSaved.getProviderGroup());
        Assertions.assertEquals(providerSubgroupModel.getProviderGroup().getId(), providerSubGroupSaved.getProviderGroup().getId());

    }

    @Test
    void ProviderSubgroupRepository_findById_ReturnProviderSubgroup() {

        ProviderSubgroupModel providerSubGroupSaved = repository.save(providerSubgroupModel);
        Optional<ProviderSubgroupModel> result = repository.findById(providerSubGroupSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(ProviderSubgroupModel.class);
        Assertions.assertEquals(providerSubGroupSaved, result.get());

    }

    @Test
    void ProviderSubgroupRepository_update_ReturnUpdatedProviderSubgroup() {

        ProviderSubgroupModel providerSubGroupSaved = repository.save(providerSubgroupModel);

        providerSubGroupSaved.setDescription("Update description");

        ProviderSubgroupModel ProviderSubgroupUpdated = repository.save(providerSubGroupSaved);

        Assertions.assertEquals(providerSubGroupSaved.getId(), ProviderSubgroupUpdated.getId());
        Assertions.assertEquals(providerSubGroupSaved.getDescription(), ProviderSubgroupUpdated.getDescription());
    }

    @Test
    void ProviderSubgroupRepository_delete() {

        ProviderSubgroupModel providerSubGroupSaved = repository.save(providerSubgroupModel);

        repository.deleteById(providerSubGroupSaved.getId());

        Optional<ProviderSubgroupModel> result = repository.findById(providerSubGroupSaved.getId());

        assertThat(providerSubGroupSaved.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

}