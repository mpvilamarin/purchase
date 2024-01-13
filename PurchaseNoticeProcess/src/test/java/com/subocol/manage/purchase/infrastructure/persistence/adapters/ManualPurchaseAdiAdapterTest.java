package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.domain.models.ManualPurchaseAdi;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ManualPurchaseAdiModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ManualPurchaseAdiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManualPurchaseAdiAdapterTest {

    @Mock
    private ManualPurchaseAdiRepository repository;

    @InjectMocks
    private ManualPurchaseAdiAdapter manualPurchaseAdiAdapter;
    ManualPurchaseAdiModel manualPurchase1;
    ManualPurchaseAdiModel manualPurchase2;
    Long externalEvent = 1L;
    Long eventId = 2L;
    Long noticeId = 3L;
    boolean totParameter = true;

    @BeforeEach
    void setup() {
        manualPurchase1 = ManualPurchaseAdiModel.builder()
                .idRegister(6514L).position(1).suggestedReference("ProductReference").pieces("Bateria")
                .quantity(2).brand("KIA").line("CERATO").build();

        manualPurchase2 = ManualPurchaseAdiModel.builder()
                .idRegister(6515L).position(1).suggestedReference("").pieces("Tapa motor")
                .quantity(2).brand("KIA").line("CERATO").build();

    }

    @Test
    void findManualPurchaseAdiByEventFilterManualPurchaseForAuth_ReturnsManualPurchaseAdiList() {

        List<ManualPurchaseAdiModel> manualPurchaseAdiList = List.of(manualPurchase1, manualPurchase2);

        when(repository.getAllByEventFilterManualPurchaseForAuth(externalEvent, eventId, noticeId, totParameter))
                .thenReturn(manualPurchaseAdiList);

        List<ManualPurchaseAdi> actualResult = manualPurchaseAdiAdapter.findManualPurchaseAdiByEventFilterManualPurchaseForAuth(
                externalEvent, eventId, noticeId, totParameter);

        assertEquals(manualPurchaseAdiList.size(), actualResult.size());
        assertTrue(actualResult.stream().allMatch(Objects::nonNull));
    }

    @Test
    void findManualPurchaseAdiByEventFilterManualPurchaseForAuth_ReturnsEmptyList() {

        List<ManualPurchaseAdiModel> emptyResult = Collections.emptyList();

        when(repository.getAllByEventFilterManualPurchaseForAuth(externalEvent, eventId, noticeId, totParameter))
                .thenReturn(emptyResult);

        List<ManualPurchaseAdi> actualResult = manualPurchaseAdiAdapter.findManualPurchaseAdiByEventFilterManualPurchaseForAuth(
                externalEvent, eventId, noticeId, totParameter);

        assertTrue(actualResult.isEmpty());
    }

    @Test
    void testFindManualPurchaseAdiByEventFilterManualPurchase() {
        List<ManualPurchaseAdiModel> manualPurchaseAdiList = List.of(manualPurchase1, manualPurchase2);

        when(repository.getAllByEventFilterManualPurchase(externalEvent, eventId, totParameter))
                .thenReturn(manualPurchaseAdiList);

        List<ManualPurchaseAdi> actualResult = manualPurchaseAdiAdapter.findManualPurchaseAdiByEventFilterManualPurchase(
                externalEvent, eventId, totParameter);

        assertEquals(manualPurchaseAdiList.size(), actualResult.size());
        assertTrue(actualResult.stream().allMatch(Objects::nonNull));
    }


    @Test
    void findManualPurchaseAdiByEventFilterManualPurchase_ReturnsManualPurchaseAdiList() {

        List<ManualPurchaseAdiModel> emptyResult = Collections.emptyList();

        when(repository.getAllByEventFilterManualPurchase(externalEvent, eventId, totParameter))
                .thenReturn(emptyResult);

        List<ManualPurchaseAdi> actualResult = manualPurchaseAdiAdapter.findManualPurchaseAdiByEventFilterManualPurchase(
                externalEvent, eventId, totParameter);

        assertTrue(actualResult.isEmpty());
    }
}
