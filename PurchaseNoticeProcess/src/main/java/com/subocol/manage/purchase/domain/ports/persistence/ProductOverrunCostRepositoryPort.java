package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.ProductOverrunCost;

import java.util.List;
import java.util.Optional;

@Port
public interface ProductOverrunCostRepositoryPort {

    int updatePurchaseByStatusAndIdIn(List<Long> listsIdsProductQuotation);

    Optional<ProductOverrunCost> findAllByPieceId(Long id);

    ProductOverrunCost save(ProductOverrunCost productOverrunCost);
}