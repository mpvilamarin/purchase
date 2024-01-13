package com.subocol.manage.purchase.infrastructure.persistence.repositories;



import com.subocol.manage.purchase.infrastructure.persistence.entities.InsuranceCarrierModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.InsurerModel;
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
public class InsurerRepositoryTest {
    @Autowired
    private InsurerRepository insurerRepository;
    private InsurerModel insurerModel;
    @BeforeEach
    void setup(){
        insurerModel = InsurerModel.builder().id(5345L).name("TestInsurer")
                .insurerId(123456789L).countryId(5L).sdkActive(false).multimedia(true)
                .priceToUse("el menor precio").newSuggestedReferenceParameter(true).allowMaxCostPiece(true)
                .daysUpdateSuggestedReference(7L).useGrossPriceCostoverrun(false).usePriceList(false)
                .prioritizePriceList(false).build();
    }

    @Test
    void saveInsurerModel() {
        //Given-preconditions

        //when-action to do
        InsurerModel result= insurerRepository.save(insurerModel);
        //then-verify result
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(insurerModel.getName(), result.getName());
        Assertions.assertEquals(insurerModel.getCountryId(), result.getCountryId());
        Assertions.assertEquals(insurerModel.getPriceToUse(), result.getPriceToUse());
        Assertions.assertEquals(insurerModel.getAllowMaxCostPiece(), result.getAllowMaxCostPiece());
        Assertions.assertEquals(insurerModel.getInsurerId(), result.getInsurerId());
        Assertions.assertEquals(insurerModel.getDaysUpdateSuggestedReference(), result.getDaysUpdateSuggestedReference());
        Assertions.assertTrue(insurerModel.isMultimedia());
        Assertions.assertTrue(insurerModel.getNewSuggestedReferenceParameter());
        Assertions.assertTrue(insurerModel.getAllowMaxCostPiece());
        Assertions.assertFalse(insurerModel.getPrioritizePriceList());

    }

    @Test
    void findInsurerModel() {
        //Given-preconditions

        InsurerModel insurerModelSave= insurerRepository.save(insurerModel);

        //when-action to do
        Optional<InsurerModel> result= insurerRepository.findById(insurerModelSave.getId());

        //then-verify result
        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(InsurerModel.class);
        Assertions.assertEquals(insurerModelSave, result.get());

    }

    @Test
    void updateInsuranceCarrierModel() {
        //Given-preconditions
        InsurerModel insurerModelSave= insurerRepository.save(insurerModel);
        insurerModelSave.setName("InsurerUpdate").setPriceToUse("El mayor precio").setDaysUpdateSuggestedReference(1L);
        //when-action to do
        InsurerModel insurerModelUpdate= insurerRepository.save(insurerModelSave);

        //then-verify result
        Assertions.assertEquals(insurerModelSave.getName(), insurerModelUpdate.getName());
        Assertions.assertEquals(insurerModelSave.getPriceToUse(), insurerModelUpdate.getPriceToUse());
        Assertions.assertEquals(insurerModelSave.getDaysUpdateSuggestedReference(), insurerModelUpdate.getDaysUpdateSuggestedReference());

    }
    @Test
    void deleteInsuranceCarrierModel() {
        //Given-preconditions
        InsurerModel insurerModelSave= insurerRepository.save(insurerModel);

        //when-action to do
        insurerRepository.deleteById(insurerModelSave.getId());

        Optional<InsurerModel> result= insurerRepository.findById(insurerModelSave.getId());
        //then-verify result
        assertThat(insurerModelSave.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

//    Optional<InsurerModel> findByInsurerId(Long insurerId);
    @Test
    void findInsurerModelByInsurerId() {
        //Given-preconditions
        Long insurerId=200000002L;
        String name="SURA";

        //when-action to do
        Optional<InsurerModel> result= insurerRepository.findByInsurerId(insurerId);

        //then-verify result
        assertThat(result).isPresent().get().isExactlyInstanceOf(InsurerModel.class)
                .extracting("name").isEqualTo(name);

    }
}