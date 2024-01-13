package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.domain.ports.persistence.ProductOrdersPiecesNoticeRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ProductOrdersPiecesNoticeRepository;
import jakarta.persistence.Tuple;

import java.util.List;

@Adapter
public class ProductOrdersPiecesNoticeAdapter implements ProductOrdersPiecesNoticeRepositoryPort {

    public final ProductOrdersPiecesNoticeRepository productOrdersPiecesNoticeRepository;

    public ProductOrdersPiecesNoticeAdapter(ProductOrdersPiecesNoticeRepository productOrdersPiecesNoticeRepository) {
        this.productOrdersPiecesNoticeRepository = productOrdersPiecesNoticeRepository;
    }

    @Override
    public Double totalPriceOrdersByExternalEventAndEventId(Integer externalEvent, boolean type, List<Integer> positionPiece, boolean unforeseen) {
        return productOrdersPiecesNoticeRepository.totalPriceOrdersByExternalEventAndEventId(externalEvent, type, positionPiece, unforeseen);
    }

    @Override
    public ReserveCalculationTotalSuraDTO totalGrossPriceOrdersByExternalEventAndEventId(Integer externalEvent, List<Integer> positionPiece, boolean unforeseen) {
        Tuple tuple = productOrdersPiecesNoticeRepository.totalGrossPriceOrdersByExternalEventAndEventIdTuple(externalEvent, positionPiece, unforeseen);

        return new ReserveCalculationTotalSuraDTO(tuple.get("total", Double.class), tuple.get("totalIva", Double.class), tuple.get("totalDescuento", Double.class));
    }

    @Override
    public List<ReserveRepuestosSuraDTO> findPiecesOrdersByExternalEvent(Integer externalEvent, List<String> type, List<Integer> positionPiece) {
        return productOrdersPiecesNoticeRepository.findPiecesOrdersByExternalEvent(externalEvent, type, positionPiece);
    }
}
