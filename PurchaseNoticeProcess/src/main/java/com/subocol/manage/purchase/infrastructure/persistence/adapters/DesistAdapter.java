package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.common.utils.NativeQueryUtils;
import com.subocol.manage.purchase.domain.models.Desist;
import com.subocol.manage.purchase.domain.models.ProductOrder;
import com.subocol.manage.purchase.domain.models.SellOrder;
import com.subocol.manage.purchase.domain.ports.persistence.DesistRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.entities.DesistModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SellOrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.DesistRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Adapter
public class DesistAdapter implements DesistRepositoryPort {

    @PersistenceContext
    private EntityManager entityManager;
    private final DesistRepository repository;

    public DesistAdapter(DesistRepository repository) {
        this.repository = repository;
    }

    @Override
    public Desist save(Desist desist) {
        DesistModel desistSaved = repository.save(MapperUtil.convert(desist, DesistModel.class));
        return MapperUtil.convert(desistSaved, Desist.class);
    }

    @Override
    public int saveAllNative(List<Desist> desistList) {
        String query = NativeQueryUtils.getInsertIntoQueryByList(
                DesistModel.class,
                MapperUtil.convertList(desistList, DesistModel.class)).toString();

        return entityManager.createNativeQuery(query).executeUpdate();
    }
}
