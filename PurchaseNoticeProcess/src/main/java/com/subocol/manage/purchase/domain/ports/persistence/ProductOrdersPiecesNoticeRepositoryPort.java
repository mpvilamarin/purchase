package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO;

import java.util.List;

@Port
public interface ProductOrdersPiecesNoticeRepositoryPort {

    Double totalPriceOrdersByExternalEventAndEventId(Integer externalEvent, boolean type, List<Integer> positionPiece, boolean unforeseen);

    ReserveCalculationTotalSuraDTO totalGrossPriceOrdersByExternalEventAndEventId(Integer externalEvent, List<Integer> positionPiece, boolean unforeseen);

    List<ReserveRepuestosSuraDTO> findPiecesOrdersByExternalEvent(Integer externalEvent, List<String> type, List<Integer> positionPiece);
}
