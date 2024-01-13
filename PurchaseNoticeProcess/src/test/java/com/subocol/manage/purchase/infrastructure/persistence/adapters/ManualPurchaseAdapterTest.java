package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.domain.models.ManualPurchase;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ManualPurchaseModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ManualPurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManualPurchaseAdapterTest {

    @Mock
    private ManualPurchaseRepository repository;

    @InjectMocks
    private ManualPurchaseAdapter manualPurchaseAdapter;

    ManualPurchase manualPurchase;

    ManualPurchaseModel mpSaved;

    @BeforeEach
    void setup() {
        manualPurchase = ManualPurchase.builder().id(24898L).externalEvent("21920").brand("KIA").line("CERATO").plate("985745").description("Bateria").quantity(1)
                .reference("").suggestedReference("").status("bought").date(new Timestamp(1664582461)).cause("PARAMETRY")
                .position(7).eventId(7106L).auth(true).deleted(false).purchaseSubsidiary(true)
                .build();

        mpSaved = ManualPurchaseModel.builder().id(24898L).externalEvent("21920").brand("KIA").line("CERATO").plate("985745").description("Bateria").quantity(1)
                .reference("").suggestedReference("").status("bought").date(new Timestamp(1664582461)).cause("PARAMETRY")
                .position(7).eventId(7106L).auth(true).deleted(false).purchaseSubsidiary(true)
                .build();

    }

    @Test
    void updateManualPurchaseByPosition_ReturnsUpdatedCount() {
        String status = "ACCEPTED";
        String externalEvent = "465314";
        List<Integer> positions = Arrays.asList(1, 2, 3);

        int expectedUpdatedCount = 3;

        when(repository.setStatusByExternalEventAndPositionIn(status, externalEvent, positions))
                .thenReturn(expectedUpdatedCount);

        int actualUpdatedCount = manualPurchaseAdapter.updateManualPurchaseByPosition(status, externalEvent, positions);

        assertEquals(expectedUpdatedCount, actualUpdatedCount);
    }

    @Test
    void updatePurchaseSubsidiary_ReturnsUpdatedCount() {
        String externalEvent = "465314";
        List<Integer> positions = Arrays.asList(1, 2, 3);
        boolean purchase = true;

        int expectedUpdatedCount = 3;

        when(repository.setPurchaseSubsidiaryByExternalEventAndPositionIn(purchase, externalEvent, positions))
                .thenReturn(expectedUpdatedCount);

        int actualUpdatedCount = manualPurchaseAdapter.updatePurchaseSubsidiary(externalEvent, positions, purchase);

        assertEquals(expectedUpdatedCount, actualUpdatedCount);
    }

    @Test
    void testSave() {

        when(repository.save(any(ManualPurchaseModel.class))).thenReturn(mpSaved);

        ManualPurchase savedManualPurchase = manualPurchaseAdapter.save(manualPurchase);

        assertEquals(manualPurchase.getId(), savedManualPurchase.getId());
        assertEquals(manualPurchase.getExternalEvent(), savedManualPurchase.getExternalEvent());
        assertEquals(manualPurchase.getBrand(), savedManualPurchase.getBrand());
        assertEquals(manualPurchase.getLine(), savedManualPurchase.getLine());
        assertEquals(manualPurchase.getPlate(), savedManualPurchase.getPlate());
        assertEquals(manualPurchase.getDescription(), savedManualPurchase.getDescription());
        assertEquals(manualPurchase.getQuantity(), savedManualPurchase.getQuantity());
        assertEquals(manualPurchase.getReference(), savedManualPurchase.getReference());
        assertEquals(manualPurchase.getSuggestedReference(), savedManualPurchase.getSuggestedReference());
        assertEquals(manualPurchase.getStatus(), savedManualPurchase.getStatus());
        assertEquals(manualPurchase.getDate(), savedManualPurchase.getDate());
        assertEquals(manualPurchase.getCause(), savedManualPurchase.getCause());
        assertEquals(manualPurchase.getPosition(), savedManualPurchase.getPosition());
        assertEquals(manualPurchase.getEventId(), savedManualPurchase.getEventId());
        assertEquals(manualPurchase.isAuth(), savedManualPurchase.isAuth());
        assertEquals(manualPurchase.isDeleted(), savedManualPurchase.isDeleted());
        assertEquals(manualPurchase.isPurchaseSubsidiary(), savedManualPurchase.isPurchaseSubsidiary());
    }

    @Test
    void testCountDeletedPiecesByPositionsReturnCount() {

        String externalEvent = "999999999";
        List<Integer> positions = Arrays.asList(1, 2, 3);
        int expectedUpdatedCount = 3;

        when(repository.countDeletedPiecesByPositions(externalEvent, positions)).thenReturn(expectedUpdatedCount);

        int count = manualPurchaseAdapter.countDeletedPiecesByPositions(externalEvent, positions);

        verify(repository, times(1)).countDeletedPiecesByPositions(externalEvent, positions);
        assertEquals(expectedUpdatedCount, count);

    }

    @Test
    void testUpdateAuthByExternalEventAndPositionReturnRowUpdated() {

        String externalEvent = "999999999";
        List<Integer> positions = Arrays.asList(1, 2);
        int expectedUpdatedCount = 2;

        when(repository.updateAuthByExternalEventAndPosition(Boolean.TRUE, externalEvent, positions)).thenReturn(expectedUpdatedCount);

        int count = manualPurchaseAdapter.updateAuthByExternalEventAndPosition(Boolean.TRUE, externalEvent, positions);

        verify(repository, times(1)).updateAuthByExternalEventAndPosition(Boolean.TRUE, externalEvent, positions);
        assertEquals(expectedUpdatedCount, count);

    }

    @Test
    void testSetPurchaseSubsidiaryByExternalEventAndPositionInReturnRowUpdated() {

        String externalEvent = "999999999";
        List<Integer> positions = Arrays.asList(1, 2);
        int expectedUpdatedCount = 2;

        when(repository.setPurchaseSubsidiaryByExternalEventAndPositionIn(Boolean.TRUE, externalEvent, positions)).thenReturn(expectedUpdatedCount);

        int count = manualPurchaseAdapter.updatePurchaseSubsidiary(externalEvent, positions, Boolean.TRUE);

        verify(repository, times(1)).setPurchaseSubsidiaryByExternalEventAndPositionIn(Boolean.TRUE, externalEvent, positions);
        assertEquals(expectedUpdatedCount, count);

    }
}
