package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.domain.models.SellOrder;
import org.springframework.data.util.Pair;

import java.util.Optional;

public interface SellOrderRepositoryPort {

    SellOrder save(SellOrder sellOrder);

    Optional<SellOrder> findByOrderId(Long orderId);

    Pair<Double, Double> findValuesSellOrderByOrderId(Long orderId);

    int updateSubtotalTotalIva(Long sellOrderId, Double subTotal, Double total, Double iva);
}
