package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.models.StatusReplacement;
import com.subocol.manage.purchase.infrastructure.persistence.entities.StatusReplacementModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.StatusReplacementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusReplacementAdapterTest {

    @Mock
    private StatusReplacementRepository repository;

    @InjectMocks
    private StatusReplacementAdapter statusReplacementAdapter;

    @Test
    void StatusReplacementAdapter_save_ReturnStatusReplacementEntity() {
        StatusReplacementModel statusReplacementModel = StatusReplacementModel.builder()
                .id(1L)
                .externalEvent("11200")
                .seller("Orbika")
                .dateOrder(TimeZoneUtil.getTimestampByDefaultZone())
                .provider("COMPANIA CHIRICANA DE AUTOMOVILES")
                .email("gubarahonaTest@excelautomotriz.com")
                .phone("4567738")
                .approvedDate(TimeZoneUtil.getTimestampByDefaultZone())
                .providerObservation("Provider Observations")
                .quantityParts(2)
                .subsidiary("COMPANIA CHIRICANA DE AUTOMOVILES CDA PANAMA")
                .emailSubsidiary("dapertuzTest@excelautomotriz.com")
                .phoneSubsidiary("3542160")
                .statusParts(new HashSet<>())
                .build();

        StatusReplacement statusReplacement = MapperUtil.convert(statusReplacementModel, StatusReplacement.class);
        statusReplacement.setId(null);

        when(repository.save(Mockito.any(StatusReplacementModel.class))).thenReturn(statusReplacementModel);

        StatusReplacement statusReplacementSaved = statusReplacementAdapter.save(statusReplacement);

        verify(repository, times(1)).save(any());

        assertNotNull(statusReplacementSaved);
        assertThat(statusReplacementSaved.getId()).isPositive();
        try {
            //Assertions.assertEquals for all entity attributes
            AttributeAssertions.assertAttributesEqual(statusReplacementModel, statusReplacementSaved);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }
    }
}