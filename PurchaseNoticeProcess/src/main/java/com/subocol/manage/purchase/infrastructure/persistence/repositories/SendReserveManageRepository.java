package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.domain.models.SendReserveManage;
import com.subocol.manage.purchase.infrastructure.persistence.entities.PieceModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SendReserveManageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SendReserveManageRepository extends JpaRepository<SendReserveManageModel, Integer>, JpaSpecificationExecutor<SendReserveManageModel> {

    @Query(value = "SELECT srm FROM SendReserveManageModel srm"
            + " WHERE srm.externalEvent = ?1"
            , nativeQuery = false)
    Optional<SendReserveManageModel> findByExternalEvent(
            @Param("externalEvent") Integer externalEvent);
}
