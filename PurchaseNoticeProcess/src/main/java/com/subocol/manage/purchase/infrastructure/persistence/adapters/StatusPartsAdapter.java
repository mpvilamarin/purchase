package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.common.utils.NativeQueryUtils;
import com.subocol.manage.purchase.domain.models.StatusParts;
import com.subocol.manage.purchase.domain.ports.persistence.StatusPartsRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.entities.StatusPartsModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ProductOrderRepository;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.StatusPartsRepository;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.StatusReplacementRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */
@Slf4j
@Adapter
@RequiredArgsConstructor
public class StatusPartsAdapter implements StatusPartsRepositoryPort {

    private final  EntityManager entityManager;
    private final StatusPartsRepository repository;

    @Override
    public int saveAllNative(List<StatusParts> statusPartsList) {
        String query = NativeQueryUtils.getInsertIntoQueryByList(StatusPartsModel.class, MapperUtil.convertList(statusPartsList, StatusPartsModel.class)).toString();

        int savedItems = entityManager.createNativeQuery(query).executeUpdate();
        log.info("Query Executed Successfully");
        return savedItems;
    }

    @Override
    public int updateStatusByProductOrderId(String status, List<Long> productOrderIds) {
        return repository.updateStatusByProductOrderId(status, productOrderIds);
    }
}
