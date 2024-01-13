package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.Piece;

import java.util.List;
import java.util.Optional;

@Port
public interface PieceRepositoryPort {

    List<Integer> findInitialPiecesByExternalEventBolivarConditionTrue(Integer externalEvent, boolean unforeseen);

    List<Integer> findInitialPiecesByExternalEventBolivarConditionFalse(Integer externalEvent, boolean unforeseen);

    List<Integer> findInitialPiecesByExternalWithOrders(Integer externalEvent, boolean unforeseen);

    List<Integer> findInitialPiecesByExternalWithOrdersConditionTrue(Integer externalEvent, boolean unforeseen);

    List<Integer> findInitialPiecesByExternalWithOrdersConditionFalse(Integer externalEvent, boolean unforeseen);

    List<Integer> findInitialPiecesByExternalEventSuraConditionTrue(Integer externalEvent, boolean unforeseen);

    List<Integer> findInitialPiecesByExternalEventSuraConditionFalse(Integer externalEvent, boolean unforeseen);

    List<Integer> findInitialPiecesByExternalWithOrdersSura(Integer externalEvent, boolean unforeseen);

    List<Integer> findInitialPiecesByExternalWithOrdersSuraConditionTrue(Integer externalEvent, boolean unforeseen);

    List<Integer> findInitialPiecesByExternalWithOrdersSuraConditionFalse(Integer externalEvent, boolean unforeseen);

    Optional<Piece> findByExternalEventAndPosition(Integer externalEvent, Integer position);

    int countPiecesByExternalEventAndPositions(String externalEvent, List<Integer> positions);

}
