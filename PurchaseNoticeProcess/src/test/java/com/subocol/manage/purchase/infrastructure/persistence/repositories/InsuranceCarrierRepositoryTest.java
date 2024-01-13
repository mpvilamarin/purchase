package com.subocol.manage.purchase.infrastructure.persistence.repositories;



import com.subocol.manage.purchase.infrastructure.persistence.entities.DataEventModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.InsuranceCarrierModel;
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
public class InsuranceCarrierRepositoryTest {
    @Autowired
    private InsuranceCarrierRepository insuranceCarrierRepository;
    private InsuranceCarrierModel insuranceCarrierModel;
    @BeforeEach
    void setup(){
        insuranceCarrierModel = InsuranceCarrierModel.builder().id(5345L).countryId("2").nit("89564104231849")
                .logo("logoSura.png").taxAbbreviation("NIT").prefix("$").ivaItbms("ITBMS").build();
    }

    @Test
    void saveInsuranceCarrierModel() {
        //Given-preconditions

        //when-action to do
        InsuranceCarrierModel result= insuranceCarrierRepository.save(insuranceCarrierModel);
        //then-verify result
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(insuranceCarrierModel.getNit(), result.getNit());
        Assertions.assertEquals(insuranceCarrierModel.getCountryId(), result.getCountryId());
        Assertions.assertEquals(insuranceCarrierModel.getLogo(), result.getLogo());
        Assertions.assertEquals(insuranceCarrierModel.getPrefix(), result.getPrefix());
        Assertions.assertEquals(insuranceCarrierModel.getId(), result.getId());

    }

    @Test
    void findInsuranceCarrierModel() {
        //Given-preconditions

        InsuranceCarrierModel dataEventModelSave= insuranceCarrierRepository.save(insuranceCarrierModel);

        //when-action to do
        Optional<InsuranceCarrierModel> result= insuranceCarrierRepository.findById(dataEventModelSave.getId());

        //then-verify result
        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(InsuranceCarrierModel.class);
        Assertions.assertEquals(dataEventModelSave, result.get());

    }

    @Test
    void updateInsuranceCarrierModel() {
        //Given-preconditions
        InsuranceCarrierModel insuranceCarrierModelSave= insuranceCarrierRepository.save(insuranceCarrierModel);
        insuranceCarrierModelSave.setLogo("milogo.png").setPrefix("€").setTaxAbbreviation("RUT");
        //when-action to do
        InsuranceCarrierModel insuranceCarrierModelUpdate= insuranceCarrierRepository.save(insuranceCarrierModelSave);

        //then-verify result
        Assertions.assertEquals(insuranceCarrierModelSave.getLogo(), insuranceCarrierModelUpdate.getLogo());
        Assertions.assertEquals(insuranceCarrierModelSave.getPrefix(), insuranceCarrierModelUpdate.getPrefix());
        Assertions.assertEquals(insuranceCarrierModelSave.getTaxAbbreviation(), insuranceCarrierModelUpdate.getTaxAbbreviation());

    }
    @Test
    void deleteInsuranceCarrierModel() {
        //Given-preconditions
        InsuranceCarrierModel insuranceCarrierModelSave= insuranceCarrierRepository.save(insuranceCarrierModel);

        //when-action to do
        insuranceCarrierRepository.deleteById(insuranceCarrierModelSave.getId());

        Optional<InsuranceCarrierModel> result= insuranceCarrierRepository.findById(insuranceCarrierModelSave.getId());
        //then-verify result
        assertThat(insuranceCarrierModelSave.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

}
