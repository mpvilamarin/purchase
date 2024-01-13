package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.StatusPartsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusPartsRepository extends JpaRepository<StatusPartsModel, Long>, JpaSpecificationExecutor<StatusPartsModel> {

    @Modifying
    @Query("UPDATE StatusPartsModel sp "
            + "SET sp.status = ?1 "
            + "WHERE sp.idProductOrder in ?2")
    int updateStatusByProductOrderId(String status, List<Long> productOrderIds);
}