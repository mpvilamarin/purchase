package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.dtos.SellOrderValuesDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SellOrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellOrderRepository extends JpaRepository<SellOrderModel, Long>, JpaSpecificationExecutor<SellOrderModel> {

    Optional<SellOrderModel> findByOrder_Id(Long orderId);

    @Query(" SELECT new com.subocol.manage.purchase.infrastructure.persistence.dtos.SellOrderValuesDTO(" +
            "s.subtotal, s.total " +
            " ) FROM SellOrderModel s " +
            " WHERE s.order.id = ?1")
    SellOrderValuesDTO findSellOrderValuesDTOByOrderId(Long orderId);

    @Modifying
    @Query(" UPDATE SellOrderModel SET" +
            " subtotal = ?2," +
            " total = ?3," +
            " iva = ?4 " +
            " WHERE id = ?1")
    int updateSubtotalAndTotalAndIvaById(Long sellOrderId, Double subTotal, Double total, Double iva);
}