package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.DeletedPiecesHistory;
import com.subocol.manage.purchase.domain.ports.persistence.DeletedPiecesHistoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.entities.DeletedPiecesHistoryModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.DeletedPiecesHistoryRepository;

/**
 * @author DANR
 * @version 1.0
 * @since 22/08/2023
 */
@Adapter
public class DeletedPiecesHistoryAdapter implements DeletedPiecesHistoryPort {

    private final DeletedPiecesHistoryRepository repository;

    public DeletedPiecesHistoryAdapter(DeletedPiecesHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public DeletedPiecesHistory save(DeletedPiecesHistory deletedPiecesHistory) {
        DeletedPiecesHistoryModel entitySaved = repository.save(MapperUtil.convert(deletedPiecesHistory, DeletedPiecesHistoryModel.class));
        return MapperUtil.convert(entitySaved, DeletedPiecesHistory.class);
    }
}
