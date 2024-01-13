package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.domain.models.DataEvent;
import com.subocol.manage.purchase.infrastructure.persistence.entities.DataEventModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.DataEventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataEventAdapterTest {

    @Mock
    private DataEventRepository repository;

    @InjectMocks
    private DataEventAdapter dataEventAdapter;

    @Test
    void DataEventAdapter_findById_ReturnsDataEventEntity() {

        DataEventModel dataEventModel = DataEventModel.builder()
                .id(1L)
                .externalEvent(123)
                .workshopCity("Test City")
                .workshopName("Test Workshop")
                .line("Test Line")
                .brand("Test Brand")
                .coverage("Test Coverage")
                .deductible(100.0)
                .repairConcl("Test Repair Conclusion")
                .fixedDeductible(200.0)
                .insuredValue(300.0)
                .vin("Test VIN")
                .plate("Test Plate")
                .idJob("Test Job ID")
                .unexpected("Test Unexpected")
                .countryId(2L)
                .model("Test Model")
                .version("Test Version")
                .workshopRut("Test Workshop Rut")
                .workshopAddress("Test Workshop Address")
                .workshopPhone(BigDecimal.valueOf(1234567890L))
                .claimNumber("Test Claim Number")
                .insuranceNumber("Test Insurance Number")
                .authorization("Test Authorization")
                .workshopType("Test Workshop Type")
                .workshopNit("Test Workshop Nit")
                .vehicleType("Test Vehicle Type")
                .noticeSofia(3L)
                .date(Timestamp.valueOf(LocalDateTime.now()))
                .repairOrder(BigDecimal.valueOf(987654321L))
                .lossIndicator(1.0)
                .totalWorkforce(500.0)
                .workshopId(4)
                .build();

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(dataEventModel));

        Optional<DataEvent> resultDataEvent = dataEventAdapter.findById(anyLong());

        assertThat(resultDataEvent).isPresent()
                .get().isNotNull()
                .isExactlyInstanceOf(DataEvent.class);

        DataEvent domainDataEvent = resultDataEvent.get();

        assertThat(dataEventModel).isNotNull();
        assertAllDomainAndInfrastructureFields(dataEventModel, domainDataEvent);

    }

    private static void assertAllDomainAndInfrastructureFields(DataEventModel dataEventModel, DataEvent domainDataEvent) {
        Assertions.assertEquals(domainDataEvent.getId(), dataEventModel.getId());
        Assertions.assertEquals(domainDataEvent.getExternalEvent(), dataEventModel.getExternalEvent());
        Assertions.assertEquals(domainDataEvent.getWorkshopCity(), dataEventModel.getWorkshopCity());
        Assertions.assertEquals(domainDataEvent.getWorkshopName(), dataEventModel.getWorkshopName());
        Assertions.assertEquals(domainDataEvent.getLine(), dataEventModel.getLine());
        Assertions.assertEquals(domainDataEvent.getBrand(), dataEventModel.getBrand());
        Assertions.assertEquals(domainDataEvent.getCoverage(), dataEventModel.getCoverage());
        Assertions.assertEquals(domainDataEvent.getDeductible(), dataEventModel.getDeductible(), 0.001);
        Assertions.assertEquals(domainDataEvent.getRepairConcl(), dataEventModel.getRepairConcl());
        Assertions.assertEquals(domainDataEvent.getFixedDeductible(), dataEventModel.getFixedDeductible(), 0.001);
        Assertions.assertEquals(domainDataEvent.getInsuredValue(), dataEventModel.getInsuredValue(), 0.001);
        Assertions.assertEquals(domainDataEvent.getVin(), dataEventModel.getVin());
        Assertions.assertEquals(domainDataEvent.getPlate(), dataEventModel.getPlate());
        Assertions.assertEquals(domainDataEvent.getIdJob(), dataEventModel.getIdJob());
        Assertions.assertEquals(domainDataEvent.getUnexpected(), dataEventModel.getUnexpected());
        Assertions.assertEquals(domainDataEvent.getCountryId(), dataEventModel.getCountryId());
        Assertions.assertEquals(domainDataEvent.getModel(), dataEventModel.getModel());
        Assertions.assertEquals(domainDataEvent.getVersion(), dataEventModel.getVersion());
        Assertions.assertEquals(domainDataEvent.getWorkshopRut(), dataEventModel.getWorkshopRut());
        Assertions.assertEquals(domainDataEvent.getWorkshopAddress(), dataEventModel.getWorkshopAddress());
        Assertions.assertEquals(domainDataEvent.getWorkshopPhone(), dataEventModel.getWorkshopPhone().longValue());
        Assertions.assertEquals(domainDataEvent.getClaimNumber(), dataEventModel.getClaimNumber());
        Assertions.assertEquals(domainDataEvent.getInsuranceNumber(), dataEventModel.getInsuranceNumber());
        Assertions.assertEquals(domainDataEvent.getAuthorization(), dataEventModel.getAuthorization());
        Assertions.assertEquals(domainDataEvent.getWorkshopType(), dataEventModel.getWorkshopType());
        Assertions.assertEquals(domainDataEvent.getWorkshopNit(), dataEventModel.getWorkshopNit());
        Assertions.assertEquals(domainDataEvent.getVehicleType(), dataEventModel.getVehicleType());
        Assertions.assertEquals(domainDataEvent.getNoticeSofia(), dataEventModel.getNoticeSofia());
        Assertions.assertEquals(domainDataEvent.getDate(), dataEventModel.getDate());
        Assertions.assertEquals(domainDataEvent.getRepairOrder(), dataEventModel.getRepairOrder().longValue());
        Assertions.assertEquals(domainDataEvent.getLossIndicator(), dataEventModel.getLossIndicator(), 0.001);
        Assertions.assertEquals(domainDataEvent.getTotalWorkforce(), dataEventModel.getTotalWorkforce(), 0.001);
        Assertions.assertEquals(domainDataEvent.getWorkshopId(), dataEventModel.getWorkshopId());
    }

    @Test
    void DataEventAdapter_findById_ReturnsEmptyOptional() {

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<DataEvent> result = dataEventAdapter.findById(anyLong());

        assertThat(result).isNotPresent();

    }

    @Test
    void DataEventAdapter_updateClaimNumberByExternalEvent_ReturnsPositiveUpdateCount() {

        String externalEvent = "2345";
        String claimNumber = "45321";

        when(repository.updateClaimNumberByExternalEvent(anyInt(), anyString())).thenReturn(1);

        int result = dataEventAdapter.updateClaimNumberByExternalEvent(externalEvent, claimNumber);

        assertThat(result).isPositive();
        verify(repository, times(1)).updateClaimNumberByExternalEvent(Integer.valueOf(externalEvent), claimNumber);
    }

    @Test
    void DataEventAdapter_updateClaimNumberByExternalEvent_ReturnsZeroUpdateCount() {

        String externalEvent = "23465";
        String claimNumber = "45321";

        when(repository.updateClaimNumberByExternalEvent(anyInt(), anyString())).thenReturn(0);

        int result = dataEventAdapter.updateClaimNumberByExternalEvent(externalEvent, claimNumber);

        assertThat(result).isZero();
        verify(repository, times(1)).updateClaimNumberByExternalEvent(Integer.valueOf(externalEvent), claimNumber);
    }

    @Test
    void DataEventAdapter_updateAuthDataEvent_ReturnsPositiveUpdateCount() {

        int externalEvent = 2345;

        when(repository.updateAuthByExternalEvent(anyInt())).thenReturn(1);

        int result = dataEventAdapter.updateAuthByExternalEvent(externalEvent);

        assertThat(result).isPositive();
        verify(repository, times(1)).updateAuthByExternalEvent(externalEvent);
    }

    @Test
    void DataEventAdapter_updateAuthDataEvent_ReturnsZeroUpdateCount() {

        int externalEvent = 2345;

        when(repository.updateAuthByExternalEvent(anyInt())).thenReturn(0);

        int result = dataEventAdapter.updateAuthByExternalEvent(externalEvent);

        assertThat(result).isZero();
        verify(repository, times(1)).updateAuthByExternalEvent(externalEvent);

    }
}