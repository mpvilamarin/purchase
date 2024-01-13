package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.DeletedPiecesHistory;

@Port
public interface DeletedPiecesHistoryPort {

    DeletedPiecesHistory save(DeletedPiecesHistory deletedPiecesHistory);

}
