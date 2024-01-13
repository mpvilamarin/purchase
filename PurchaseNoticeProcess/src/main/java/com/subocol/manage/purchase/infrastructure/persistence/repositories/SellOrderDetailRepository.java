package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.domain.models.SellOrderDetail;
import com.subocol.manage.purchase.infrastructure.persistence.dtos.SellOrderValuesDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SellOrderDetailModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellOrderDetailRepository extends JpaRepository<SellOrderDetailModel, Long>, JpaSpecificationExecutor<SellOrderDetailModel> {


    @Modifying
    @Query(" UPDATE SellOrderModel SET" +
            " subtotal = ?2," +
            " total = ?3," +
            " iva = ?4 " +
            " WHERE id = ?1")
    void deleteSellOrderDetail(Long sellOrderId, Double subTotal, Double total, Double iva);


    @Query(" SELECT so.id " +
            " FROM SellOrderDetailModel so" +
            " WHERE so.sellOrder.order.id = ?1 and so.positionPiece IN ?2")
    List<Long>  findAllByOrderIdAndPosition(Long orderId, List<Integer> positions);

    @Modifying
    @Query(" DELETE FROM SellOrderDetailModel " +
            " WHERE id IN ?1")
    int deleteAllById(List<Long> sellOrderIds);



}