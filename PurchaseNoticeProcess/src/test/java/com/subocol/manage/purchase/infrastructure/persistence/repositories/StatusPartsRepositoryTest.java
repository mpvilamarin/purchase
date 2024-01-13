package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.infrastructure.persistence.entities.StatusPartsModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.StatusReplacementModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StatusPartsRepositoryTest {

    private final StatusPartsRepository repository;

    private StatusPartsModel statusPartsModel;

    @Autowired
    StatusPartsRepositoryTest(StatusPartsRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setup() {

        this.statusPartsModel = StatusPartsModel.builder()
                .namePart("Abanico de condensador")
                .reference("Sin Referencia")
                .importPart(Boolean.FALSE)
                .totalOrdersEvent(null)
                .idOrder(12535L)
                .idProductOrder(23013L)
                .totalParts(1)
                .status("accepted")
                .approvedOrderDate(TimeZoneUtil.getTimestampByDefaultZone())
                .estimateDeliveryDate(TimeZoneUtil.getTimestampByDefaultZone())
                .deliveredDate(TimeZoneUtil.getTimestampByDefaultZone())
                .onRouteDate(TimeZoneUtil.getTimestampByDefaultZone())
                .devolutionDate(TimeZoneUtil.getTimestampByDefaultZone())
                .lateDate(TimeZoneUtil.getTimestampByDefaultZone())
                .completedDate(TimeZoneUtil.getTimestampByDefaultZone())
                .receivedWorkshopDate(TimeZoneUtil.getTimestampByDefaultZone())
                .statusReplacement(new StatusReplacementModel().setId(6811L))
                .build();

    }

    @Test
    void StatusPartsRepository_save_ReturnSavedStatusParts() {

        StatusPartsModel statusPartsSaved = repository.save(this.statusPartsModel);

        assertNotNull(statusPartsSaved);
        assertThat(statusPartsSaved.getId()).isPositive();
        Assertions.assertEquals(statusPartsSaved.getNamePart(), statusPartsModel.getNamePart());
        Assertions.assertEquals(statusPartsSaved.getReference(), statusPartsModel.getReference());
        Assertions.assertEquals(statusPartsSaved.getImportPart(), statusPartsModel.getImportPart());
        Assertions.assertEquals(statusPartsSaved.getTotalOrdersEvent(), statusPartsModel.getTotalOrdersEvent());
        Assertions.assertEquals(statusPartsSaved.getIdOrder(), statusPartsModel.getIdOrder());
        Assertions.assertEquals(statusPartsSaved.getIdProductOrder(), statusPartsModel.getIdProductOrder());
        Assertions.assertEquals(statusPartsSaved.getTotalParts(), statusPartsModel.getTotalParts());
        Assertions.assertEquals(statusPartsSaved.getStatus(), statusPartsModel.getStatus());
        Assertions.assertEquals(statusPartsSaved.getApprovedOrderDate(), statusPartsModel.getApprovedOrderDate());
        Assertions.assertEquals(statusPartsSaved.getEstimateDeliveryDate(), statusPartsModel.getEstimateDeliveryDate());
        Assertions.assertEquals(statusPartsSaved.getDeliveredDate(), statusPartsModel.getDeliveredDate());
        Assertions.assertEquals(statusPartsSaved.getOnRouteDate(), statusPartsModel.getOnRouteDate());
        Assertions.assertEquals(statusPartsSaved.getDevolutionDate(), statusPartsModel.getDevolutionDate());
        Assertions.assertEquals(statusPartsSaved.getLateDate(), statusPartsModel.getLateDate());
        Assertions.assertEquals(statusPartsSaved.getCompletedDate(), statusPartsModel.getCompletedDate());
        Assertions.assertEquals(statusPartsSaved.getReceivedWorkshopDate(), statusPartsModel.getReceivedWorkshopDate());
        Assertions.assertEquals(statusPartsSaved.getStatusReplacement().getId(), statusPartsModel.getStatusReplacement().getId());
    }


    @Test
    void StatusPartsRepository_findById_ReturnStatusParts() {

        StatusPartsModel statusPartsSaved = repository.save(statusPartsModel);
        Optional<StatusPartsModel> result = repository.findById(statusPartsSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(StatusPartsModel.class);
        Assertions.assertEquals(statusPartsSaved, result.get());

    }

    @Test
    void StatusPartsRepository_update_ReturnUpdatedStatusParts() {

        StatusPartsModel statusPartsSaved = repository.save(statusPartsModel);

        statusPartsSaved.setStatus("bought")
                .setNamePart("Llanta derecha");

        StatusPartsModel statusPartsUpdated = repository.save(statusPartsSaved);

        Assertions.assertEquals(statusPartsSaved.getId(), statusPartsUpdated.getId());
        Assertions.assertEquals(statusPartsSaved.getStatus(), statusPartsUpdated.getStatus());
        Assertions.assertEquals(statusPartsSaved.getNamePart(), statusPartsUpdated.getNamePart());

    }

    @Test
    void StatusPartsRepository_delete() {

        StatusPartsModel statusPartsSaved = repository.save(statusPartsModel);

        repository.deleteById(statusPartsSaved.getId());

        Optional<StatusPartsModel> result = repository.findById(statusPartsSaved.getId());

        assertThat(statusPartsSaved.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

}