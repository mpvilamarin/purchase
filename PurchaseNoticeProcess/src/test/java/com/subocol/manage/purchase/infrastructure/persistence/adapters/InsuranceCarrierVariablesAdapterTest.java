package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.domain.models.InsuranceCarrierVariables;
import com.subocol.manage.purchase.infrastructure.persistence.entities.InsuranceCarrierVariablesModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.InsuranceCarrierVariablesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsuranceCarrierVariablesAdapterTest {

    @Mock
    private InsuranceCarrierVariablesRepository repository;

    @InjectMocks
    private InsurerCarrierVariablesAdapter insurerCarrierVariablesAdapter;

    @Test
    void InsuranceCarrierVariablesAdapter_findByInsuranceId_ReturnEntity() {

        Long insuranceId = 152000003L;

        InsuranceCarrierVariablesModel insuranceCarrierVariablesModel = InsuranceCarrierVariablesModel.builder()
                .id(123984L)
                .insuranceId(insuranceId)
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

        when(repository.findFirstByInsuranceId(insuranceId)).thenReturn(Optional.ofNullable(insuranceCarrierVariablesModel));

        Optional<InsuranceCarrierVariables> result = insurerCarrierVariablesAdapter.findByInsuranceId(insuranceId);

        verify(repository, times(1)).findFirstByInsuranceId(insuranceId);

        assertThat(result).isPresent();
        assertThat(result.get()).isNotNull();
        assertThat(result.get()).isExactlyInstanceOf(InsuranceCarrierVariables.class);

        InsuranceCarrierVariables domainInsuranceCarrierVariables = result.get();

        assertThat(domainInsuranceCarrierVariables.getInsuranceId()).isEqualTo(insuranceId);
        assertThat(insuranceCarrierVariablesModel).isNotNull();

        try {
            //Assertions.assertEquals for all entity attributes
            AttributeAssertions.assertAttributesEqual(insuranceCarrierVariablesModel, domainInsuranceCarrierVariables);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {}

    }

    @Test
    void InsuranceCarrierVariablesAdapter_findByInsuranceId_ReturnsEmptyOptional() {

        Long insuranceId = 152000003L;

        when(repository.findFirstByInsuranceId(insuranceId)).thenReturn(Optional.empty());

        Optional<InsuranceCarrierVariables> result = insurerCarrierVariablesAdapter.findByInsuranceId(insuranceId);

        verify(repository, times(1)).findFirstByInsuranceId(insuranceId);

        assertThat(result).isNotPresent();

    }

}