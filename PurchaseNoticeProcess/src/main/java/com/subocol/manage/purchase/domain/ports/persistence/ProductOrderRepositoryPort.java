package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.ProductOrder;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Port
public interface ProductOrderRepositoryPort {

    List<ProductOrder> findAllByIdOrder(Long orderId);

    ProductOrder save(ProductOrder productOrder);

    Long countProductOrderByExternalEventAndPosition(Integer externalEvent, List<Integer> positionPiece);

    int saveAllNative(List<ProductOrder> productOrderList);

    List<ProductOrder> findAllById(List<Long> productOrderIds);

    int updateDesistProductOrder(List<Long> productOrderIds, String userName, Timestamp timestamp);

    Map<String, Long> getCountsDesistAndTotal(Long orderId);

    List<SpareDetailToFollowUpDTO> findSpareToFollowUpByExternalEventAndPositionsDesist(List<Long> productOrderId);

    Double totalPriceOrdersByExternalEventAndEventId(Integer externalEvent, boolean type, List<Integer> positionPiece, boolean unforeseen);

    ReserveCalculationTotalSuraDTO totalGrossPriceOrdersByExternalEventAndEventId(Integer externalEvent, boolean type, List<Integer> positionPiece, boolean unforeseen);

    List<ReserveRepuestosSuraDTO> findPiecesOrdersByExternalEvent(Integer externalEvent, boolean type, List<Integer> positionPiece);
}
