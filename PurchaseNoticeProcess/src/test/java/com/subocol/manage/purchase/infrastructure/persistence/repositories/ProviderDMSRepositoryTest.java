package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.infrastructure.persistence.entities.DataEventModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProviderDMS;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProviderModel;
import jakarta.persistence.Column;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProviderDMSRepositoryTest {
    @Autowired
    private ProviderDMSRepository providerDMSRepository;
    private ProviderDMS providerDMS;
    @BeforeEach
    void setup(){
        providerDMS =ProviderDMS.builder().companyDMS(1L).idProviderDMS(1L)
                .idSellerDMS(1L).idUserDMS(1L).contactId(1L)
                .provider(new ProviderModel().setId(1L))
                .build();
    }



    @Test
    void saveProviderDMSModel() {
        //Given-preconditions

        //when-action to do
        ProviderDMS result= providerDMSRepository.save(providerDMS);
        //then-verify result
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(providerDMS.getIdProviderDMS(), result.getIdProviderDMS());
        Assertions.assertEquals(providerDMS.getCompanyDMS(), result.getCompanyDMS());
        Assertions.assertEquals(providerDMS.getIdSellerDMS(), result.getIdSellerDMS());
        Assertions.assertEquals(providerDMS.getIdUserDMS(), result.getIdUserDMS());
        Assertions.assertEquals(providerDMS.getContactId(), result.getContactId());

    }

    @Test
    void findProviderDMSById() {
        //Given-preconditions

        ProviderDMS providerDMSSave= providerDMSRepository.save(providerDMS);

        //when-action to do
        Optional<ProviderDMS> result= providerDMSRepository.findById(providerDMSSave.getId());

        //then-verify result
        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(ProviderDMS.class);
        Assertions.assertEquals(providerDMSSave, result.get());

    }

    @Test
    void updateProviderDMS() {
        //Given-preconditions
        ProviderDMS providerDMSSave= providerDMSRepository.save(providerDMS);
        providerDMSSave.setContactId(2L).setIdUserDMS(2L).setCompanyDMS(2L);
        //when-action to do
        ProviderDMS providerDMSUpdate= providerDMSRepository.save(providerDMSSave);

        //then-verify result
        Assertions.assertEquals(providerDMSSave.getContactId(), providerDMSUpdate.getContactId());
        Assertions.assertEquals(providerDMSSave.getIdUserDMS(), providerDMSUpdate.getIdUserDMS());
        Assertions.assertEquals(providerDMSSave.getCompanyDMS(), providerDMSUpdate.getCompanyDMS());

    }
    @Test
    void deleteProviderDMS() {
        //Given-preconditions
        ProviderDMS providerDMSSave= providerDMSRepository.save(providerDMS);

        //when-action to do
        providerDMSRepository.deleteById(providerDMSSave.getId());

        Optional<ProviderDMS> result= providerDMSRepository.findById(providerDMSSave.getId());
        //then-verify result
        assertThat(providerDMSSave.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

}
