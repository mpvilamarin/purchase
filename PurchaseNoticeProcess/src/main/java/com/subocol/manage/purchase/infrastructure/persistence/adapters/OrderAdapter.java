package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.Order;
import com.subocol.manage.purchase.domain.ports.persistence.OrderRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */

@Adapter
public class OrderAdapter implements OrderRepositoryPort {

    private final OrderRepository repository;

    public OrderAdapter(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order save(Order order) {
        OrderModel orderSaved = repository.save(MapperUtil.convert(order, OrderModel.class));
        return MapperUtil.convert(orderSaved, Order.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return repository
                .findById(id)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, Order.class)));
    }

    @Override
    public List<Order> findAllById(List<Long> orderIds) {
        List<OrderModel> result = repository.findAllById(orderIds);

        if (!result.isEmpty())
            return MapperUtil.convertList(result, Order.class);

        return Collections.emptyList();
    }

    @Override
    public List<Order> findAllByNoticeId(Long noticeId) {
        List<OrderModel> result = repository.findAllByNoticeId(noticeId);

        if (!result.isEmpty())
            return MapperUtil.convertList(result, Order.class);

        return Collections.emptyList();
    }

    @Override
    public int updateStatusByOrderId(String status, Long orderId) {
        return repository.updateStatusByOrderId(status, orderId);
    }
}
