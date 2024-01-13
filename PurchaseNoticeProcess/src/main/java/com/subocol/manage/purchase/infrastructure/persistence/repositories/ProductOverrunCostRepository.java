package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOverrunCostModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductOverrunCostRepository extends JpaRepository<ProductOverrunCostModel, Long>, JpaSpecificationExecutor<ProductOverrunCostModel> {

    @Modifying
    @Query(" UPDATE ProductOverrunCostModel po SET po.status = 'accepted' " +
            " WHERE po.pieceId IN ?1 ")
    int updatePurchaseByStatusAndIdIn(Collection<Long> listProductOverUnCost);

    @Query("SELECT po FROM ProductOverrunCostModel po where po.pieceId = ?1")
    List<ProductOverrunCostModel> findAllByPieceId(Long id);
}