package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.common.TestConstants;
import com.subocol.manage.purchase.infrastructure.persistence.entities.DataEventModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DataEventRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    private final DataEventRepository dataEventRepository;

    private DataEventModel dataEventModel;

    @Autowired
    public DataEventRepositoryTest(DataEventRepository dataEventRepository) {
        this.dataEventRepository = dataEventRepository;
    }

    @BeforeEach
    void DataEventRepository_setup() {
        dataEventModel = DataEventModel.builder()
                .externalEvent(5432)
                .brand("Ford")
                .countryId(2L)
                .model("2020")
                .claimNumber("123456789")
                .insuranceNumber("200000002")
                .workshopType("Multimarca")
                .workshopNit("432410028290966")
                .workshopPhone(BigDecimal.valueOf(2245380))
                .workshopAddress("PAR LEFEVRE  AV IRA  Y CL 5TA  39")
                .workshopRut("432410028290966")
                .version("Titanium")
                .plate("AU6043").unexpected("N")
                .vin("3hgrm4870fg601108")
                .lossIndicator(0D)
                .totalWorkforce(0D)
                .build();
    }


    @Test
    void DataEventRepository_saveDataEventModel() {
        //Given-preconditions

        //when-action to do
        DataEventModel result = dataEventRepository.save(dataEventModel);
        //then-verify result
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(dataEventModel.getExternalEvent(), result.getExternalEvent());
        Assertions.assertEquals(dataEventModel.getExternalEvent(), result.getExternalEvent());
        Assertions.assertEquals(dataEventModel.getVin(), result.getVin());
        Assertions.assertEquals(dataEventModel.getModel(), result.getModel());
        Assertions.assertEquals(dataEventModel.getInsuranceNumber(), result.getInsuranceNumber());

    }

    @Test
    void DataEventRepository_findDataEventById() {
        //Given-preconditions

        DataEventModel dataEventModelSave = dataEventRepository.save(dataEventModel);

        //when-action to do
        Optional<DataEventModel> result = dataEventRepository.findById(dataEventModelSave.getId());

        //then-verify result
        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(DataEventModel.class);
        Assertions.assertEquals(dataEventModelSave, result.get());

    }

    @Test
    void DataEventRepository_updateDataEvent() {
        //Given-preconditions
        DataEventModel dataEventModelSave = dataEventRepository.save(dataEventModel);
        dataEventModelSave.setBrand("CHEVROLET").setModel("2010").setLine("SAIL");
        //when-action to do
        DataEventModel dataEventModelUpdate = dataEventRepository.save(dataEventModelSave);

        //then-verify result
        Assertions.assertEquals(dataEventModelSave.getBrand(), dataEventModelUpdate.getBrand());
        Assertions.assertEquals(dataEventModelSave.getModel(), dataEventModelUpdate.getModel());
        Assertions.assertEquals(dataEventModelSave.getLine(), dataEventModelUpdate.getLine());

    }

    @Test
    void DataEventRepository_deleteDataEvent() {
        //Given-preconditions
        DataEventModel dataEventModelSave = dataEventRepository.save(dataEventModel);

        //when-action to do
        dataEventRepository.deleteById(dataEventModelSave.getId());

        Optional<DataEventModel> result = dataEventRepository.findById(dataEventModelSave.getId());
        //then-verify result
        assertThat(dataEventModelSave.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

    @Test
    void DataEventRepository_updateClaimNumberByEvent_UpdateEntity() {

        DataEventModel dataEventModelSaved = dataEventRepository.save(dataEventModel);

        String claimNumber = "876251";

        int updateCount = dataEventRepository.updateClaimNumberByExternalEvent(dataEventModelSaved.getExternalEvent(), claimNumber);

        assertThat(updateCount).isPositive();
        assertThat(dataEventModelSaved.getId()).isPositive();

        entityManager.refresh(dataEventModelSaved);

        assertThat(dataEventModelSaved).isNotNull();
        Assertions.assertEquals(claimNumber, dataEventModelSaved.getClaimNumber());
    }


    @Test
    @Disabled(TestConstants.SKIP_DATABASE_DEPENDENCE)
    void DataEventRepository_updateAuthDataEvent() {
        DataEventModel dataEventModelSaved = dataEventRepository.save(dataEventModel);

        int updateCount = dataEventRepository.updateAuthByExternalEvent(dataEventModelSaved.getExternalEvent());

        assertThat(updateCount).isPositive();
        assertThat(dataEventModelSaved.getId()).isPositive();

        entityManager.refresh(dataEventModelSaved);

        assertThat(dataEventModelSaved).isNotNull();
        Assertions.assertEquals("X", dataEventModelSaved.getAuthorization());
    }


}
