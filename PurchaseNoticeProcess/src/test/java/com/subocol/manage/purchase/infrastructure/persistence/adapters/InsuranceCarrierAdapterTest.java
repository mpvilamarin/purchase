package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.domain.models.InsuranceCarrier;
import com.subocol.manage.purchase.domain.models.Subsidiary;
import com.subocol.manage.purchase.infrastructure.persistence.entities.InsuranceCarrierModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.InsuranceCarrierRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class InsuranceCarrierAdapterTest {

    @Mock
    private InsuranceCarrierRepository repository;

    @InjectMocks
    private InsuranceCarrierAdapter insuranceAdapter;


    @Test
    void insuranceCarrierAdapter_findById_ReturnInsuranceCarrierEntity() {
        Long insuranceCarrierId = 1L;

        InsuranceCarrierModel insuranceCarrierModel = InsuranceCarrierModel.builder()
                .id(insuranceCarrierId)
                .name("Seguros Generales Suramericana S.A.")
                .countryId("1")
                .nit("8909034079")
                .taxAbbreviation("NIT")
                .logo("logoSura.png")
                .prefix("$")
                .ivaItbms("IVA")
                .build();

        when(repository.findById(anyLong())).thenReturn(Optional.of(insuranceCarrierModel));

        Optional<InsuranceCarrier> resultInsuranceCarrier = insuranceAdapter.findById(insuranceCarrierId);

        assertThat(resultInsuranceCarrier).isPresent();
        assertThat(resultInsuranceCarrier.get()).isNotNull();
        assertThat(resultInsuranceCarrier.get()).isExactlyInstanceOf(InsuranceCarrier.class);

        InsuranceCarrier domainInsuranceCarrier = resultInsuranceCarrier.get();

        assertThat(domainInsuranceCarrier.getId()).isEqualTo(insuranceCarrierId);
        assertThat(insuranceCarrierModel).isNotNull();
        Assertions.assertEquals(domainInsuranceCarrier.getName(), insuranceCarrierModel.getName());
        Assertions.assertEquals(domainInsuranceCarrier.getCountryId(), insuranceCarrierModel.getCountryId());
        Assertions.assertEquals(domainInsuranceCarrier.getNit(), insuranceCarrierModel.getNit());
        Assertions.assertEquals(domainInsuranceCarrier.getTaxAbbreviation(), insuranceCarrierModel.getTaxAbbreviation());
        Assertions.assertEquals(domainInsuranceCarrier.getLogo(), insuranceCarrierModel.getLogo());
        Assertions.assertEquals(domainInsuranceCarrier.getPrefix(), insuranceCarrierModel.getPrefix());
        Assertions.assertEquals(domainInsuranceCarrier.getIvaItbms(), insuranceCarrierModel.getIvaItbms());
    }


    @Test
    void insuranceCarrierAdapter_findById_ReturnEmptyOptional() {

        Long insuranceCarrierId = 1L;

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<InsuranceCarrier> resultInsuranceCarrier = insuranceAdapter.findById(insuranceCarrierId);

        assertThat(resultInsuranceCarrier).isNotPresent();

    }
}
