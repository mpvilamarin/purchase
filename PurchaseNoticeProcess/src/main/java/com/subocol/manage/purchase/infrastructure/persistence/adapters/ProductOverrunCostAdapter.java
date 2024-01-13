package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.ProductOverrunCost;
import com.subocol.manage.purchase.domain.ports.persistence.ProductOverrunCostRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOverrunCostModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ProductOverrunCostRepository;

import java.util.List;
import java.util.Optional;

@Adapter
public class ProductOverrunCostAdapter implements ProductOverrunCostRepositoryPort {

    private final ProductOverrunCostRepository repository;

    public ProductOverrunCostAdapter(ProductOverrunCostRepository repository) {
        this.repository = repository;
    }

    @Override
    public int updatePurchaseByStatusAndIdIn(List<Long> idsProductQuotation) {
        return repository.updatePurchaseByStatusAndIdIn(idsProductQuotation);
    }

    @Override
    public Optional<ProductOverrunCost> findAllByPieceId(Long id) {
        List<ProductOverrunCostModel> listProductOverruncosts= repository.findAllByPieceId(id);
        if(listProductOverruncosts != null && !listProductOverruncosts.isEmpty()) {

            return Optional.of(listProductOverruncosts.get(0))
                    .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, ProductOverrunCost.class)));
        }
        return Optional.empty();
    }

    @Override
    public ProductOverrunCost save(ProductOverrunCost productOverrunCost) {
        ProductOverrunCostModel productOverrunCostSaved = repository.save(MapperUtil.convert(productOverrunCost, ProductOverrunCostModel.class));
        return MapperUtil.convert(productOverrunCostSaved, ProductOverrunCost.class);
    }
}