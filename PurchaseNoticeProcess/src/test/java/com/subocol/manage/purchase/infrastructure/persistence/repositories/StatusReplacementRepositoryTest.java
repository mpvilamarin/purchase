package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.infrastructure.persistence.entities.StatusReplacementModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StatusReplacementRepositoryTest {

    private final StatusReplacementRepository repository;

    private StatusReplacementModel statusReplacementModel;

    @Autowired
    StatusReplacementRepositoryTest(StatusReplacementRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setup() {

        this.statusReplacementModel = StatusReplacementModel.builder()
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

    }

    @Test
    void StatusReplacementRepository_save_ReturnSavedStatusReplacement() {

        StatusReplacementModel statusReplacementSaved = repository.save(statusReplacementModel);

        assertNotNull(statusReplacementSaved);
        assertThat(statusReplacementSaved.getId()).isPositive();
        Assertions.assertEquals(statusReplacementSaved.getExternalEvent(), statusReplacementModel.getExternalEvent());
        Assertions.assertEquals(statusReplacementSaved.getSeller(), statusReplacementModel.getSeller());
        Assertions.assertEquals(statusReplacementSaved.getDateOrder(), statusReplacementModel.getDateOrder());
        Assertions.assertEquals(statusReplacementSaved.getProvider(), statusReplacementModel.getProvider());
        Assertions.assertEquals(statusReplacementSaved.getEmail(), statusReplacementModel.getEmail());
        Assertions.assertEquals(statusReplacementSaved.getPhone(), statusReplacementModel.getPhone());
        Assertions.assertEquals(statusReplacementSaved.getApprovedDate(), statusReplacementModel.getApprovedDate());
        Assertions.assertEquals(statusReplacementSaved.getProviderObservation(), statusReplacementModel.getProviderObservation());
        Assertions.assertEquals(statusReplacementSaved.getQuantityParts(), statusReplacementModel.getQuantityParts());
        Assertions.assertEquals(statusReplacementSaved.getSubsidiary(), statusReplacementModel.getSubsidiary());
        Assertions.assertEquals(statusReplacementSaved.getEmailSubsidiary(), statusReplacementModel.getEmailSubsidiary());
        Assertions.assertEquals(statusReplacementSaved.getPhoneSubsidiary(), statusReplacementModel.getPhoneSubsidiary());
        Assertions.assertEquals(statusReplacementSaved.getStatusParts(), statusReplacementModel.getStatusParts());

    }

    @Test
    void StatusReplacementRepository_findById_ReturnStatusReplacement() {

        StatusReplacementModel statusReplacementSaved = repository.save(statusReplacementModel);
        Optional<StatusReplacementModel> result = repository.findById(statusReplacementSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(StatusReplacementModel.class);
        Assertions.assertEquals(statusReplacementSaved, result.get());

    }

    @Test
    void StatusReplacementRepository_update_ReturnUpdatedStatusReplacement() {

        StatusReplacementModel statusReplacementSaved = repository.save(statusReplacementModel);

        statusReplacementSaved.setEmail("honaTest@excelautomotriz.com")
                .setPhone("5467832");

        StatusReplacementModel statusReplacementUpdated = repository.save(statusReplacementSaved);

        Assertions.assertEquals(statusReplacementSaved.getId(), statusReplacementUpdated.getId());
        Assertions.assertEquals(statusReplacementSaved.getEmail(), statusReplacementUpdated.getEmail());
        Assertions.assertEquals(statusReplacementSaved.getPhone(), statusReplacementUpdated.getPhone());
    }

    @Test
    void StatusReplacementRepository_delete() {

        StatusReplacementModel statusReplacementSaved = repository.save(statusReplacementModel);

        repository.deleteById(statusReplacementSaved.getId());

        Optional<StatusReplacementModel> result = repository.findById(statusReplacementSaved.getId());

        assertThat(statusReplacementSaved.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

}