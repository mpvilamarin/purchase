package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.ProviderGroupModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProviderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProviderSubgroupModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProviderGroupRepositoryTest {
    private final ProviderGroupRepository providerGroupRepository;

    private ProviderGroupModel providerGroupModel;

    @Autowired
    ProviderGroupRepositoryTest(ProviderGroupRepository providerGroupRepository) {
        this.providerGroupRepository = providerGroupRepository;
    }

    @BeforeEach
    void setup() {
        ProviderModel providerModel = ProviderModel.builder().id(39L).nit("555551234562001").name("TestAdmin Proveedor 18-1").contactName("Juan Arias")
                .phone("8888888").email("juan@subocol.com").active(true).locationExternalId(1713L).typeProvider('I').negotiatedDiscount(0.0f)
                .providerClassification("Autosuministro").intermediationDiscount(0.0).contactId(null).devolutionTime(null).authorizedNetwork(false)
                .minimumAmount(0.0).invoiceSubsidiary(false).emailAccounting("juan@subocol.com").emailElectronicInvoice("juan@subocol.com").daysIntermediationCounter(0)
                .build();

        ProviderSubgroupModel providerSubgroupModel = ProviderSubgroupModel.builder().id(390L).nameSubgroup("lujos").description(null).creationUser("sininformacion@subocol.com")
                .creationDate(new Timestamp(1620742767)).updateUser(null).updateDate(null).providerGroup(providerGroupModel)
                .build();

        providerGroupModel = ProviderGroupModel.builder().id(5L).nameGroup("electricidad").description("prueba").creationUser("sininformacion@subocol.com")
                .creationDate(new Timestamp(1664582461)).updateUser(null).updateDate(null).provider(providerModel).subgroups(Set.of(providerSubgroupModel))
                .build();

    }

    @Test
    void ProviderGroupRepository_save_ReturnSavedProviderGroup() {

        ProviderGroupModel result = providerGroupRepository.save(providerGroupModel);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(result.getId(), providerGroupModel.getId());
        Assertions.assertEquals(result.getNameGroup(), providerGroupModel.getNameGroup());
        Assertions.assertEquals(result.getDescription(), providerGroupModel.getDescription());
        Assertions.assertEquals(result.getCreationUser(), providerGroupModel.getCreationUser());
        Assertions.assertEquals(result.getCreationDate(), providerGroupModel.getCreationDate());
        Assertions.assertEquals(result.getUpdateUser(), providerGroupModel.getUpdateUser());
        Assertions.assertEquals(result.getUpdateDate(), providerGroupModel.getUpdateDate());
        Assertions.assertEquals(result.getProvider().getId(), providerGroupModel.getProvider().getId());
        Assertions.assertEquals(result.getSubgroups().size(), providerGroupModel.getSubgroups().size());
    }

    @Test
    void ProviderGroupRepository_update_ReturnSavedProviderGroup() {
        ProviderGroupModel providerGroupSaved = providerGroupRepository.save(providerGroupModel);

        Optional<ProviderGroupModel> providerGroupOptional = providerGroupRepository.findById(providerGroupSaved.getId());
        Assertions.assertTrue(providerGroupOptional.isPresent());

        ProviderGroupModel providerGroupToUpdate = providerGroupOptional.get();
        providerGroupToUpdate.setNameGroup("carroceria");
        providerGroupToUpdate.setDescription("Updated Description");

        ProviderGroupModel providerGroupUpdated = providerGroupRepository.save(providerGroupToUpdate);

        Assertions.assertEquals(providerGroupToUpdate.getId(), providerGroupUpdated.getId());
        Assertions.assertEquals(providerGroupToUpdate.getNameGroup(), providerGroupUpdated.getNameGroup());
        Assertions.assertEquals(providerGroupToUpdate.getDescription(), providerGroupUpdated.getDescription());
    }

    @Test
    void ProviderGroupRepository_delete() {

        ProviderGroupModel providerGroupSaved = providerGroupRepository.save(providerGroupModel);

        providerGroupRepository.deleteById(providerGroupSaved.getId());

        Optional<ProviderGroupModel> result = providerGroupRepository.findById(providerGroupSaved.getId());

        assertThat(providerGroupSaved.getId()).isPositive();
        assertThat(result).isNotPresent();
    }
}
