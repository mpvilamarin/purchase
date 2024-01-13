package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.ProviderModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProviderRepositoryTest {

    private final ProviderRepository repository;

    private ProviderModel providerModel;

    @Autowired
    ProviderRepositoryTest(ProviderRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setup() {

        this.providerModel = ProviderModel.builder()
                .nit("123456789")
                .name("Provider Name")
                .contactName("John Doe")
                .phone("123-456-7890")
                .email("provider@example.com")
                .active(true)
                .locationExternalId(123L)
                .typeProvider('A')
                .negotiatedDiscount(10.0f)
                .providerClassification("Class A")
                .intermediationDiscount(5.0)
                .contactId(1)
                .devolutionTime(3600L)
                .authorizedNetwork(true)
                .minimumAmount(100.0)
                .invoiceSubsidiary(true)
                .emailAccounting("accounting@example.com")
                .emailElectronicInvoice("invoice@example.com")
                .daysIntermediationCounter(30)
                .providerDMS(new HashSet<>())
                .subsidiaries(new HashSet<>())
                .groups(new HashSet<>())
                .build();


    }

    @Test
    void ProviderRepository_save_ReturnSavedProvider() {

        ProviderModel providerSaved = repository.save(providerModel);

        assertNotNull(providerSaved);
        assertThat(providerSaved.getId()).isPositive();
        Assertions.assertEquals(providerModel.getNit(), providerSaved.getNit());
        Assertions.assertEquals(providerModel.getName(), providerSaved.getName());
        Assertions.assertEquals(providerModel.getContactName(), providerSaved.getContactName());
        Assertions.assertEquals(providerModel.getPhone(), providerSaved.getPhone());
        Assertions.assertEquals(providerModel.getEmail(), providerSaved.getEmail());
        Assertions.assertEquals(providerModel.getActive(), providerSaved.getActive());
        Assertions.assertEquals(providerModel.getLocationExternalId(), providerSaved.getLocationExternalId());
        Assertions.assertEquals(providerModel.getTypeProvider(), providerSaved.getTypeProvider());
        Assertions.assertEquals(providerModel.getNegotiatedDiscount(), providerSaved.getNegotiatedDiscount());
        Assertions.assertEquals(providerModel.getProviderClassification(), providerSaved.getProviderClassification());
        Assertions.assertEquals(providerModel.getIntermediationDiscount(), providerSaved.getIntermediationDiscount());
        Assertions.assertEquals(providerModel.getContactId(), providerSaved.getContactId());
        Assertions.assertEquals(providerModel.getDevolutionTime(), providerSaved.getDevolutionTime());
        Assertions.assertEquals(providerModel.getAuthorizedNetwork(), providerSaved.getAuthorizedNetwork());
        Assertions.assertEquals(providerModel.getMinimumAmount(), providerSaved.getMinimumAmount());
        Assertions.assertEquals(providerModel.getInvoiceSubsidiary(), providerSaved.getInvoiceSubsidiary());
        Assertions.assertEquals(providerModel.getEmailAccounting(), providerSaved.getEmailAccounting());
        Assertions.assertEquals(providerModel.getEmailElectronicInvoice(), providerSaved.getEmailElectronicInvoice());
        Assertions.assertEquals(providerModel.getDaysIntermediationCounter(), providerSaved.getDaysIntermediationCounter());
    }


    @Test
    void ProviderRepository_findById_ReturnProvider() {

        ProviderModel providerSaved = repository.save(providerModel);
        Optional<ProviderModel> result = repository.findById(providerSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(ProviderModel.class);
        Assertions.assertEquals(providerSaved, result.get());

    }

    @Test
    void ProviderRepository_update_ReturnUpdatedProvider() {

        ProviderModel providerSaved = repository.save(providerModel);

        providerSaved.setActive(Boolean.FALSE)
                .setContactName("Eduard Testing");

        ProviderModel ProviderUpdated = repository.save(providerSaved);

        Assertions.assertEquals(providerSaved.getId(), ProviderUpdated.getId());
        Assertions.assertEquals(providerSaved.getActive(), ProviderUpdated.getActive());
        Assertions.assertEquals(providerSaved.getContactName(), ProviderUpdated.getContactName());
    }

    @Test
    void ProviderRepository_delete() {

        ProviderModel providerSaved = repository.save(providerModel);

        repository.deleteById(providerSaved.getId());

        Optional<ProviderModel> result = repository.findById(providerSaved.getId());

        assertThat(providerSaved.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

}