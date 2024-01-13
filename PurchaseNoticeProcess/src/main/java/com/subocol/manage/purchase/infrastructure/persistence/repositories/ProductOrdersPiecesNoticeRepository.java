package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOrdersPiecesNoticeModel;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOrdersPiecesNoticeRepository extends JpaRepository<ProductOrdersPiecesNoticeModel, Integer>, JpaSpecificationExecutor<ProductOrdersPiecesNoticeModel> {

    @Query(value = "SELECT COALESCE(SUM(popn.price * popn.amount), 0) "
            + " FROM ProductOrdersPiecesNoticeModel popn"
            + " INNER JOIN NoticeModel n ON n.externalEvent = popn.externalEvent "
            + " INNER JOIN PieceModel p ON p.dataEvent.id = n.eventId AND p.position = popn.positionPiece"
            + " WHERE n.externalEvent = ?1"
            + " AND p.position IN ?3"
            + " AND popn.status NOT IN ('desist')"
            + " AND ((?2 = true AND p.type IN ('REPU', 'Repuesto')) OR (?2 = false AND p.type IN ('TOT')))"
            + " AND n.unforeseen = ?4"
            , nativeQuery = false)
    Double totalPriceOrdersByExternalEventAndEventId(
            @Param("externalEvent") Integer externalEvent,
            @Param("type") boolean type,
            @Param("positionPiece") List<Integer> positionPiece,
            @Param("unforeseen") boolean unforeseen);

    @Query(value = "select COALESCE(sum(popn.gross_price * popn.amount), 0) as total, " +
            "         sum(CASE WHEN (so.subtotal = 0) THEN 0 ELSE ((so.iva/so.subtotal)*(popn.price*popn.amount)) END ) as totalIva, " +
            "         (sum(popn.gross_price * popn.amount) - sum(popn.price * popn.amount)) as totalDescuento from " +
            "          provider.product_orders_pieces_notice popn " +
            "         INNER JOIN provider.notice n ON n.external_event = popn.external_event " +
            "         INNER JOIN sbcevent.pieces p ON p.event_id = n.event_id AND p.position_piece = popn.position_piece " +
            "         INNER JOIN provider.sell_order so ON so.order_id = popn.order_id " +
            "         WHERE n.external_event = :externalEvent " +
            "         AND p.position_piece in :positionPiece " +
            "         AND popn.status not in ('desist') " +
            "         AND p.type in ('REPU', 'Repuesto') " +
            "         AND n.unforeseen = :unforeseen " , nativeQuery = true)
    public Tuple totalGrossPriceOrdersByExternalEventAndEventIdTuple(@Param("externalEvent") Integer externalEvent,
                                                                     @Param("positionPiece") List<Integer> positionPiece,
                                                                     @Param("unforeseen") boolean unforeseen );

    @Query("select new com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO (p.code, p.position, " +
            " p.description, popn.reference, popn.amount, " +
            " (popn.grossPrice*popn.amount) as price, ((so.iva) * (popn.price*popn.amount)) as iva, ((so.iva*100.0)) as porcentaje, " +
            " popn.quality, n.unforeseen, popn.totalDiscount, popn.order.subsidiary.idJob, so.subtotal ) "
            + " FROM ProductOrdersPiecesNoticeModel popn"
            + " INNER JOIN NoticeModel n ON n.externalEvent = popn.externalEvent "
            + " INNER JOIN PieceModel p ON p.dataEvent.id = n.eventId AND p.position = popn.positionPiece"
            + " INNER JOIN SellOrderModel so ON so.order.id = popn.order.id"
            + " WHERE n.externalEvent = :externalEvent "
            + " AND p.position in (:positionPiece)"
            + " AND popn.status not in ('desist') "
            + " AND p.type in (:type) "
            + " ORDER BY p.position ASC")
    public List<ReserveRepuestosSuraDTO> findPiecesOrdersByExternalEvent(@Param("externalEvent") Integer externalEvent,
                                                                         @Param("type") List<String> type,
                                                                         @Param("positionPiece") List<Integer> positionPiece);

}
