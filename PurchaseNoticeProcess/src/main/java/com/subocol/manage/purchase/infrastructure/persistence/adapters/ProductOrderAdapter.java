package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.common.utils.NativeQueryUtils;
import com.subocol.manage.purchase.domain.models.ProductOrder;
import com.subocol.manage.purchase.domain.ports.persistence.ProductOrderRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ProductOrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */

@Slf4j
@Adapter
@RequiredArgsConstructor
public class ProductOrderAdapter implements ProductOrderRepositoryPort {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProductOrderRepository repository;


    @Override
    @Transactional(readOnly = true)
    public List<ProductOrder> findAllByIdOrder(Long orderId) {

        List<ProductOrderModel> result = repository.findAllByOrder_Id(orderId);

        if (!result.isEmpty())
            return MapperUtil.convertList(result, ProductOrder.class);

        return Collections.emptyList();
    }

    @Override
    public ProductOrder save(ProductOrder productOrder) {
        ProductOrderModel productOrderModelSaved = repository.save(MapperUtil.convert(productOrder, ProductOrderModel.class));
        return MapperUtil.convert(productOrderModelSaved, ProductOrder.class);
    }

    @Override
    public Long countProductOrderByExternalEventAndPosition(Integer externalEvent, List<Integer> positionPiece) {
        return repository.countProductOrderByExternalEventAndPosition(externalEvent, positionPiece);
    }

    @Override
    public int saveAllNative(List<ProductOrder> productOrderList) {

        String query = NativeQueryUtils.getInsertIntoQueryByList(
                ProductOrderModel.class,
                MapperUtil.convertList(productOrderList, ProductOrderModel.class)).toString();

        int savedItems = entityManager.createNativeQuery(query).executeUpdate();
        log.info("Query Executed Successfully");

        return savedItems;
    }

    @Override
    public List<ProductOrder> findAllById(List<Long> productOrderIds) {
        List<ProductOrderModel> result = repository.findAllById(productOrderIds);

        if (!result.isEmpty())
            return MapperUtil.convertList(result, ProductOrder.class);

        return Collections.emptyList();
    }


    @Override
    public int updateDesistProductOrder(List<Long> productOrderIds, String userName, Timestamp timestamp) {
        return repository.updateDesistProductOrder(productOrderIds, userName, timestamp);
    }

    @Override
    public Map<String, Long> getCountsDesistAndTotal(Long orderId) {
        return repository.getCountsDesistAndTotal(orderId);
    }

    @Override
    public List<SpareDetailToFollowUpDTO> findSpareToFollowUpByExternalEventAndPositionsDesist(List<Long> productOrderId) {
        return repository.findSpareToFollowUpByExternalEventAndPositionsDesist(productOrderId);
    }

    @Override
    public Double totalPriceOrdersByExternalEventAndEventId(Integer externalEvent, boolean type, List<Integer> positionPiece, boolean unforeseen) {
        return repository.totalPriceOrdersByExternalEventAndEventId(externalEvent, getTypes(type), positionPiece, unforeseen);
    }

    @Override
    public ReserveCalculationTotalSuraDTO totalGrossPriceOrdersByExternalEventAndEventId(Integer externalEvent, boolean type, List<Integer> positionPiece, boolean unforeseen) {
        return repository.totalGrossPriceOrdersByExternalEventAndEventId(externalEvent, getTypes(type), positionPiece, unforeseen);
    }

    @Override
    public List<ReserveRepuestosSuraDTO> findPiecesOrdersByExternalEvent(Integer externalEvent, boolean type, List<Integer> positionPiece) {
        return repository.findPiecesOrdersByExternalEvent(externalEvent, getTypes(type), positionPiece);
    }

    private static List<String> getTypes(boolean type) {
        return type ? Arrays.asList("REPU", "Repuesto") : List.of("TOT");
    }
}
