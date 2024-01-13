package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.infrastructure.persistence.entities.InsuranceCarrierVariablesModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InsuranceCarrierVariablesRepositoryTest {

    @Autowired
    private InsuranceCarrierVariablesRepository insuranceCarrierVariablesRepository;

    private InsuranceCarrierVariablesModel insuranceCarrierVariables;

    @BeforeEach
    void setup() {
        insuranceCarrierVariables = InsuranceCarrierVariablesModel.builder()
                .id(123984L)
                .insuranceId(152000003L)
                .timeMinuteQuote(5)
                .timeHourQuote(0)
                .startWorkHour(7)
                .endWorkHour(23)
                .maxDeliveryDays(15)
                .extraCost(5D)
                .antiquityQuoteLight(3)
                .timeMinuteServiceAdi(3)
                .nextCallServiceAdi(Timestamp.valueOf("2023-04-28 14:49:05.174000"))
                .maxCostPieceCC(0D)
                .maxCostPieceMM(2000000D)
                .build();
    }

    @Test
    void InsuranceCarrierVariables_save_ReturnEntity() {

        InsuranceCarrierVariablesModel insuranceCarrierSaved = insuranceCarrierVariablesRepository.save(insuranceCarrierVariables);

        assertThat(insuranceCarrierSaved.getId()).isNotNull();
        assertThat(insuranceCarrierSaved.getId()).isPositive();
        Assertions.assertEquals(insuranceCarrierVariables.getInsuranceId(), insuranceCarrierSaved.getInsuranceId());
        Assertions.assertEquals(insuranceCarrierVariables.getTimeMinuteQuote(), insuranceCarrierSaved.getTimeMinuteQuote());
        Assertions.assertEquals(insuranceCarrierVariables.getTimeHourQuote(), insuranceCarrierSaved.getTimeHourQuote());
        Assertions.assertEquals(insuranceCarrierVariables.getStartWorkHour(), insuranceCarrierSaved.getStartWorkHour());
        Assertions.assertEquals(insuranceCarrierVariables.getEndWorkHour(), insuranceCarrierSaved.getEndWorkHour());
        Assertions.assertEquals(insuranceCarrierVariables.getMaxDeliveryDays(), insuranceCarrierSaved.getMaxDeliveryDays());
        Assertions.assertEquals(insuranceCarrierVariables.getExtraCost(), insuranceCarrierSaved.getExtraCost());
        Assertions.assertEquals(insuranceCarrierVariables.getAntiquityQuoteLight(), insuranceCarrierSaved.getAntiquityQuoteLight());
        Assertions.assertEquals(insuranceCarrierVariables.getTimeMinuteServiceAdi(), insuranceCarrierSaved.getTimeMinuteServiceAdi());
        Assertions.assertEquals(insuranceCarrierVariables.getNextCallServiceAdi(), insuranceCarrierSaved.getNextCallServiceAdi());
        Assertions.assertEquals(insuranceCarrierVariables.getMaxCostPieceMM(), insuranceCarrierSaved.getMaxCostPieceMM());
        Assertions.assertEquals(insuranceCarrierVariables.getMaxCostPieceCC(), insuranceCarrierSaved.getMaxCostPieceCC());


    }

    @Test
    void InsuranceCarrierVariables_findById_ReturnEntity() {

        InsuranceCarrierVariablesModel insuranceCarrierVariablesSaved = insuranceCarrierVariablesRepository.save(insuranceCarrierVariables);

        Optional<InsuranceCarrierVariablesModel> result = insuranceCarrierVariablesRepository.findById(insuranceCarrierVariablesSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(InsuranceCarrierVariablesModel.class);
        Assertions.assertEquals(insuranceCarrierVariablesSaved, result.get());

    }

    @Test
    void InsuranceCarrierVariables_update_ReturnEntity() {

        InsuranceCarrierVariablesModel insuranceCarrierVariablesSaved = insuranceCarrierVariablesRepository.save(insuranceCarrierVariables);

        insuranceCarrierVariablesSaved.setExtraCost(3D);

        InsuranceCarrierVariablesModel insuranceCarrierVariablesUpdated = insuranceCarrierVariablesRepository.save(insuranceCarrierVariablesSaved);

        Assertions.assertEquals(insuranceCarrierVariablesSaved.getExtraCost(), insuranceCarrierVariablesUpdated.getExtraCost());

    }

    @Test
    void InsuranceCarrierVariables_delete() {

        InsuranceCarrierVariablesModel insuranceCarrierVariablesSaved = insuranceCarrierVariablesRepository.save(insuranceCarrierVariables);

        insuranceCarrierVariablesRepository.deleteById(insuranceCarrierVariablesSaved.getId());

        Optional<InsuranceCarrierVariablesModel> result = insuranceCarrierVariablesRepository.findById(insuranceCarrierVariablesSaved.getId());

        assertThat(insuranceCarrierVariablesSaved.getId()).isPositive();
        assertThat(result).isNotPresent();

    }

    @Test
    void InsuranceCarrierVariables_findByInsuranceId_ReturnEntity() {

        InsuranceCarrierVariablesModel insuranceCarrierVariablesSaved = insuranceCarrierVariablesRepository.save(insuranceCarrierVariables);

        Optional<InsuranceCarrierVariablesModel> result = insuranceCarrierVariablesRepository.findFirstByInsuranceId(insuranceCarrierVariablesSaved.getInsuranceId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(InsuranceCarrierVariablesModel.class);
        Assertions.assertEquals(insuranceCarrierVariablesSaved, result.get());

    }

}
