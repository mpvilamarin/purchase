package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.DataEventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataEventRepository extends JpaRepository<DataEventModel, Long>, JpaSpecificationExecutor<DataEventModel> {

    Optional<DataEventModel> findByExternalEventAndId(Integer externalEvent, Long id);

    @Modifying
    @Query(value = "UPDATE DataEventModel d SET d.claimNumber = ?2 WHERE d.externalEvent = ?1")
    int updateClaimNumberByExternalEvent(Integer externalEvent, String claimNumber);

    @Modifying
    @Query("UPDATE DataEventModel d SET d.authorization = 'X' WHERE d.externalEvent = ?1")
    int updateAuthByExternalEvent(Integer externalEvent);

}