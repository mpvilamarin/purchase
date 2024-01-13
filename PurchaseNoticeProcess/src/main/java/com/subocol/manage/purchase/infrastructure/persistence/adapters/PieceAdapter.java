package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.Piece;
import com.subocol.manage.purchase.domain.ports.persistence.PieceRepositoryPort;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.PieceRepository;

import java.util.List;
import java.util.Optional;

@Adapter
public class PieceAdapter implements PieceRepositoryPort {

    private final PieceRepository pieceRepository;

    public PieceAdapter(PieceRepository pieceRepository) {
        this.pieceRepository = pieceRepository;
    }

    @Override
    public List<Integer> findInitialPiecesByExternalEventBolivarConditionTrue(Integer externalEvent, boolean unforeseen) {
        return pieceRepository.findInitialPiecesByExternalEventBolivarConditionTrue(externalEvent, unforeseen);
    }

    @Override
    public List<Integer> findInitialPiecesByExternalEventBolivarConditionFalse(Integer externalEvent, boolean unforeseen) {
        return pieceRepository.findInitialPiecesByExternalEventBolivarConditionFalse(externalEvent, unforeseen);
    }

    @Override
    public List<Integer> findInitialPiecesByExternalWithOrders(Integer externalEvent, boolean unforeseen) {
        return pieceRepository.findInitialPiecesByExternalEventWithOrders(externalEvent, unforeseen);
    }

    @Override
    public List<Integer> findInitialPiecesByExternalWithOrdersConditionTrue(Integer externalEvent, boolean unforeseen) {
        return pieceRepository.findInitialPiecesByExternalWithOrdersConditionTrue(externalEvent, unforeseen);
    }

    @Override
    public List<Integer> findInitialPiecesByExternalWithOrdersConditionFalse(Integer externalEvent, boolean unforeseen) {
        return pieceRepository.findInitialPiecesByExternalWithOrdersConditionFalse(externalEvent, unforeseen);
    }

    @Override
    public List<Integer> findInitialPiecesByExternalEventSuraConditionTrue(Integer externalEvent, boolean unforeseen) {
        return pieceRepository.findInitialPiecesByExternalEventSuraConditionTrue(externalEvent, unforeseen);
    }

    @Override
    public List<Integer> findInitialPiecesByExternalEventSuraConditionFalse(Integer externalEvent, boolean unforeseen) {
        return pieceRepository.findInitialPiecesByExternalEventSuraConditionFalse(externalEvent, unforeseen);
    }

    @Override
    public List<Integer> findInitialPiecesByExternalWithOrdersSura(Integer externalEvent, boolean unforeseen) {
        return pieceRepository.findInitialPiecesByExternalEventWithOrdersSura(externalEvent, unforeseen);
    }

    @Override
    public List<Integer> findInitialPiecesByExternalWithOrdersSuraConditionTrue(Integer externalEvent, boolean unforeseen) {
        return pieceRepository.findInitialPiecesByExternalWithOrdersSuraConditionTrue(externalEvent, unforeseen);
    }

    @Override
    public List<Integer> findInitialPiecesByExternalWithOrdersSuraConditionFalse(Integer externalEvent, boolean unforeseen) {
        return pieceRepository.findInitialPiecesByExternalWithOrdersSuraConditionFalse(externalEvent, unforeseen);
    }

    @Override
    public Optional<Piece> findByExternalEventAndPosition(Integer externalEvent, Integer position) {
        return pieceRepository
                .findByExternalEventAndPosition(externalEvent, position)
                .flatMap(source -> Optional.ofNullable(MapperUtil.convert(source, Piece.class)));
    }

    @Override
    public int countPiecesByExternalEventAndPositions(String externalEvent, List<Integer> positions) {
        return pieceRepository.countPiecesByExternalEventAndPositions(Integer.parseInt(externalEvent), positions);
    }
}
