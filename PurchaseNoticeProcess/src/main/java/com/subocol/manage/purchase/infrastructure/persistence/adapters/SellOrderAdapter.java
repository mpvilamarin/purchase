package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.SellOrder;
import com.subocol.manage.purchase.domain.ports.persistence.SellOrderRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.dtos.SellOrderValuesDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SellOrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.SellOrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */
@Adapter
@Slf4j
public class SellOrderAdapter implements SellOrderRepositoryPort {

    private final SellOrderRepository repository;

    public SellOrderAdapter(SellOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public SellOrder save(SellOrder sellOrder) {
        SellOrderModel sellOrderSaved = repository.save(MapperUtil.convert(sellOrder, SellOrderModel.class));
        return MapperUtil.convert(sellOrderSaved, SellOrder.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SellOrder> findByOrderId(Long orderId) {
        return repository
                .findByOrder_Id(orderId)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, SellOrder.class)));
    }

    @Override
    public Pair<Double, Double> findValuesSellOrderByOrderId(Long orderId) {
        SellOrderValuesDTO result= repository.findSellOrderValuesDTOByOrderId(orderId);
        return Pair.of(result.getSubTotal(), result.getTotal());
         }

    @Override
    public int updateSubtotalTotalIva(Long sellOrderId, Double subtotal, Double total, Double iva) {
        return repository.updateSubtotalAndTotalAndIvaById(sellOrderId, subtotal, total, iva);
    }
}
