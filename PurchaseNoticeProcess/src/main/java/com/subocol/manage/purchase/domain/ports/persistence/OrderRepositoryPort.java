package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.Order;

import java.util.List;
import java.util.Optional;

@Port
public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(Long orderId);

    List<Order> findAllById(List<Long> orderIds);

    List<Order> findAllByNoticeId(Long noticeId);

    int updateStatusByOrderId(String status, Long orderId);
}
