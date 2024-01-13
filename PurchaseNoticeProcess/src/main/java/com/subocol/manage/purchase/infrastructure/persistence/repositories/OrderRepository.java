package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long>, JpaSpecificationExecutor<OrderModel> {

    List<OrderModel> findAllByNoticeId(Long id);

    @Modifying
    @Query("UPDATE OrderModel o "
            + "SET o.status = ?1 "
            + "WHERE o.id = ?2")
    int updateStatusByOrderId(String status, Long orderId);
}