package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ManualPurchaseModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ManualPurchaseRepositoryTest {

    @Autowired
    private ManualPurchaseRepository manualPurchaseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    ManualPurchaseModel manualPurchase1;

    ManualPurchaseModel manualPurchase2;

    List<ManualPurchaseModel> lsManualPurchase = new ArrayList<>();

    @BeforeEach
    void setup() {
        manualPurchase1 = ManualPurchaseModel.builder().externalEvent("999999999").brand("KIA").line("CERATO").plate("985745").description("Bateria").quantity(1)
                .reference("").suggestedReference("").status("quoted").date(new Timestamp(1664582461)).cause("PARAMETRY")
                .position(6).eventId(7106L).auth(true).deleted(false).purchaseSubsidiary(false)
                .build();

        manualPurchase2 = ManualPurchaseModel.builder().externalEvent("999999999").brand("KIA").line("CERATO").plate("985745").description("Bateria").quantity(1)
                .reference("").suggestedReference("").status("quoted").date(new Timestamp(1664582461)).cause("PARAMETRY")
                .position(7).eventId(7106L).auth(true).deleted(false).purchaseSubsidiary(false)
                .build();

        lsManualPurchase.add(manualPurchase1);
        lsManualPurchase.add(manualPurchase2);

    }

    @Test
    void setStatusByExternalEventAndPositionIn() {
        List<ManualPurchaseModel> manualPurchaseModelSaved = manualPurchaseRepository.saveAll(lsManualPurchase);

        List<Integer> lsPositionManualPurchase = new ArrayList<>();
        manualPurchaseModelSaved.forEach(manualPurchase -> {
            lsPositionManualPurchase.add(manualPurchase.getPosition());

        });
        int result = manualPurchaseRepository.setStatusByExternalEventAndPositionIn(
                ManageNoticeConstant.ACCEPTED, manualPurchaseModelSaved.get(0).getExternalEvent(), lsPositionManualPurchase);

        entityManager.refresh(manualPurchaseModelSaved.get(0));
        entityManager.refresh(manualPurchaseModelSaved.get(1));

        assertThat(result).isPositive();
        Assertions.assertEquals(ManageNoticeConstant.ACCEPTED, manualPurchaseModelSaved.get(0).getStatus());
        Assertions.assertEquals(ManageNoticeConstant.ACCEPTED, manualPurchaseModelSaved.get(1).getStatus());

    }

    @Test
    void setPurchaseSubsidiaryByExternalEventAndPositionIn() {

        List<ManualPurchaseModel> manualPurchaseModelSaved = manualPurchaseRepository.saveAll(lsManualPurchase);
        //when-action to do

        List<Integer> lsPositionManualPurchase = new ArrayList<>();
        manualPurchaseModelSaved.forEach(manualPurchase -> {
            lsPositionManualPurchase.add(manualPurchase.getPosition());

        });

        int result = manualPurchaseRepository.setPurchaseSubsidiaryByExternalEventAndPositionIn(
                true, manualPurchaseModelSaved.get(0).getExternalEvent(), lsPositionManualPurchase);

        entityManager.refresh(manualPurchaseModelSaved.get(0));
        entityManager.refresh(manualPurchaseModelSaved.get(1));

        assertThat(result).isPositive();
        Assertions.assertTrue(manualPurchaseModelSaved.get(0).isPurchaseSubsidiary());
        Assertions.assertTrue(manualPurchaseModelSaved.get(1).isPurchaseSubsidiary());

    }

    @Test
    void testCountDeletedPiecesByPositionsReturnCount() {

        lsManualPurchase.forEach(p -> p.setDeleted(Boolean.TRUE));

        List<ManualPurchaseModel> manualPurchaseModelSaved = manualPurchaseRepository.saveAll(lsManualPurchase);

        List<Integer> positions = manualPurchaseModelSaved.stream().map(ManualPurchaseModel::getPosition).toList();

        int result = manualPurchaseRepository.countDeletedPiecesByPositions(manualPurchase1.getExternalEvent(), positions);

        assertThat(result).isPositive()
                .isEqualTo(manualPurchaseModelSaved.size());

    }

    @Test
    void testUpdateAuthByExternalEventAndPositionReturnRowUpdated() {

        lsManualPurchase.forEach(p -> p.setDeleted(Boolean.FALSE));

        List<ManualPurchaseModel> manualPurchaseModelSaved = manualPurchaseRepository.saveAll(lsManualPurchase);

        List<Integer> positions = manualPurchaseModelSaved.stream().map(ManualPurchaseModel::getPosition).toList();

        int result = manualPurchaseRepository.updateAuthByExternalEventAndPosition(Boolean.FALSE, manualPurchase1.getExternalEvent(), positions);

        entityManager.refresh(manualPurchaseModelSaved.get(0));
        entityManager.refresh(manualPurchaseModelSaved.get(1));

        assertThat(result).isPositive()
                .isEqualTo(manualPurchaseModelSaved.size());

        Assertions.assertFalse(manualPurchaseModelSaved.get(0).isAuth());
        Assertions.assertFalse(manualPurchaseModelSaved.get(1).isAuth());

    }

    @Test
    void testSetPurchaseSubsidiaryByExternalEventAndPositionInReturnRowUpdated() {

        List<ManualPurchaseModel> manualPurchaseModelSaved = manualPurchaseRepository.saveAll(lsManualPurchase);

        List<Integer> positions = manualPurchaseModelSaved.stream().map(ManualPurchaseModel::getPosition).toList();

        int result = manualPurchaseRepository.setPurchaseSubsidiaryByExternalEventAndPositionIn(Boolean.TRUE, manualPurchase1.getExternalEvent(), positions);

        entityManager.refresh(manualPurchaseModelSaved.get(0));
        entityManager.refresh(manualPurchaseModelSaved.get(1));

        assertThat(result).isPositive()
                .isEqualTo(manualPurchaseModelSaved.size());

        Assertions.assertTrue(manualPurchaseModelSaved.get(0).isPurchaseSubsidiary());
        Assertions.assertTrue(manualPurchaseModelSaved.get(1).isPurchaseSubsidiary());

    }


}