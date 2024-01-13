package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.ManualPurchaseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManualPurchaseRepository extends JpaRepository<ManualPurchaseModel, Long>, JpaSpecificationExecutor<ManualPurchaseModel> {

    @Modifying
    @Query("update ManualPurchaseModel mp set mp.status = ?1 where mp.externalEvent = ?2 and mp.position in ?3 ")
    int setStatusByExternalEventAndPositionIn(String status, String externalEvent, List<Integer> positions);

    @Modifying
    @Query("update ManualPurchaseModel mp set mp.purchaseSubsidiary = ?1 where mp.externalEvent = ?2 and mp.position in ?3")
    int setPurchaseSubsidiaryByExternalEventAndPositionIn(Boolean status, String externalEvent, List<Integer> positions);

    @Query(" SELECT COUNT(mp) from ManualPurchaseModel mp " +
            " WHERE mp.externalEvent = ?1 and mp.position IN ?2 AND mp.deleted = true")
    int countDeletedPiecesByPositions(String externalEvent, List<Integer> positions);

    @Modifying
    @Query(value = "UPDATE ManualPurchaseModel " +
            "SET auth = ?1 " +
            "WHERE externalEvent = ?2 and position in ?3 AND deleted = false")
    int updateAuthByExternalEventAndPosition(Boolean auth, String externalEvent, List<Integer> positions);

    @Query(" SELECT mp from ManualPurchaseModel mp " +
            " WHERE mp.externalEvent = ?2 and mp.position = ?1 ")
    Optional<ManualPurchaseModel> findByPositionAndExternalEvent(Integer position, String externalEvent);

    @Modifying
    @Query(value = "UPDATE ManualPurchaseModel mp "
            + " SET mp.cause = 'DESIST', "
            + " mp.status = null "
            + " WHERE mp.id = ?1 ")
    int updateDesistCauseAndStatusById(Long manualPurchaseId);

    @Modifying
    @Query(value = "UPDATE ManualPurchaseModel "
            + "SET deleted = true "
            + "WHERE externalEvent = :externalEvent and position = :position")
    int updateDeletePiecesTrueByExternalEventAndPosition(String externalEvent, Integer position);
}