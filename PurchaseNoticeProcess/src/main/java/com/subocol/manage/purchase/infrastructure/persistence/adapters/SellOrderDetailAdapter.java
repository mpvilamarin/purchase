package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.common.utils.NativeQueryUtils;
import com.subocol.manage.purchase.domain.models.SellOrderDetail;
import com.subocol.manage.purchase.domain.ports.persistence.SellOrderDetailRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SellOrderDetailModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.SellOrderDetailRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 *
 * @author DANR
 * @version 1.0
 * @since 31/07/2023
 */
@Adapter
@Slf4j
@RequiredArgsConstructor
public class SellOrderDetailAdapter implements SellOrderDetailRepositoryPort {


    private final EntityManager entityManager;

    private final SellOrderDetailRepository repository;

    @Override
    public int saveAllNative(List<SellOrderDetail> sellOrderDetails) {
        String query = NativeQueryUtils.getInsertIntoQueryByList(
                SellOrderDetailModel.class,
                MapperUtil.convertList(sellOrderDetails, SellOrderDetailModel.class)).toString();

        int savedItems = entityManager.createNativeQuery(query).executeUpdate();
        log.info("Query Executed Successfully");

        return savedItems;
    }

    @Override
    public int deleteAllById(List<Long> sellOrderIds) {
        return repository.deleteAllById(sellOrderIds);
    }

    @Override
    public List<Long> findAllByOrderIdAndPosition(Long orderId, List<Integer> positions) {
        return repository.findAllByOrderIdAndPosition(orderId, positions);

    }

}
