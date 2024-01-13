package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.domain.models.SellOrderDetail;

import java.util.List;

public interface SellOrderDetailRepositoryPort {

    int saveAllNative(List<SellOrderDetail> sellOrderDetails);

    int deleteAllById(List<Long> sellOrderIds);

    List<Long> findAllByOrderIdAndPosition(Long orderId, List<Integer> positions);

}
