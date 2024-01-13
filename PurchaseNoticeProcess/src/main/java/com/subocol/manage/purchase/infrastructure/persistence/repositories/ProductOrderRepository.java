package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrderModel, Long>, JpaSpecificationExecutor<ProductOrderModel> {

    List<ProductOrderModel> findAllByOrder_Id(Long orderId);

    @Query("select count (distinct po.positionPiece) from OrderModel o "
            + " INNER JOIN PieceModel p ON p.dataEvent.id = o.notice.eventId "
            + " INNER JOIN ProductOrderModel po ON po.order.id = o.id "
            + " WHERE o.notice.externalEvent = :externalEvent "
            + " AND po.positionPiece in (:positionPiece) "
            + " AND po.status NOT IN ('desist') "
            + " AND p.type IN ('REPU', 'TOT', 'Repuesto')")
    public Long countProductOrderByExternalEventAndPosition(@Param("externalEvent") Integer externalEvent,
                                                            @Param("positionPiece") List<Integer> positionPiece);


    @Modifying
    @Query("UPDATE ProductOrderModel po "
            + "SET po.dateDesist = ?3, "
            + "po.desist = true, "
            + "po.status = 'desist', "
            + "po.userName = ?2 "
            + "where po.id in ?1")
    int updateDesistProductOrder(List<Long> productOrderIds, String userName, Timestamp timestamp);


    @Query("SELECT " +
            "SUM(CASE WHEN pord.desist = true THEN 1 ELSE 0 END) as countDesisted, " +
            "COUNT(pord) as totalCount " +
            "FROM ProductOrderModel pord " +
            "WHERE pord.order.id = ?1")
    Map<String, Long> getCountsDesistAndTotal(Long orderId);

    @Query(" SELECT new com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO ( po.positionPiece, po.amount, " +
            "  true as esCotizado, false as deleted, sp.id) " +
            "  FROM ProductOrderModel po " +
            " INNER JOIN StatusPartsModel sp ON sp.idProductOrder = po.id " +
            " INNER JOIN OrderModel ord ON po.order.id = ord.id " +
            " WHERE po.id in ?1 order by 1 asc")
    List<SpareDetailToFollowUpDTO> findSpareToFollowUpByExternalEventAndPositionsDesist(List<Long> productOrderId);

    @Query(value = "select sum(popn.price * popn.amount) from "
            + " ProductOrdersPiecesNoticeModel popn"
            + " INNER JOIN NoticeModel n ON n.externalEvent = popn.externalEvent "
            + " INNER JOIN PieceModel p ON p.dataEvent.id = n.eventId AND p.position = popn.positionPiece"
            + " WHERE n.externalEvent = :externalEvent "
            + " AND p.position in (:positionPiece)"
            + " AND popn.status not in ('desist') "
            + " AND p.type in (:type) "
            + " AND n.unforeseen = :unforeseen")
    Double totalPriceOrdersByExternalEventAndEventId(Integer externalEvent, List<String> type, List<Integer> positionPiece, boolean unforeseen);

    @Query(value = "select new com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalSuraDTO (sum(popn.grossPrice * popn.amount) as total, "
            + " sum(((so.iva/so.subtotal)*(popn.price*popn.amount))) as totalIva, "
            + " (sum(popn.grossPrice * popn.amount) - sum(popn.price * popn.amount)) as totalDescuento) from "
            + " ProductOrdersPiecesNoticeModel popn"
            + " INNER JOIN NoticeModel n ON n.externalEvent = popn.externalEvent "
            + " INNER JOIN PieceModel p ON p.dataEvent.id = n.eventId AND p.position = popn.positionPiece"
            + " INNER JOIN SellOrderModel so ON so.order.id = popn.order.id"
            + " WHERE n.externalEvent = :externalEvent "
            + " AND p.position in (:positionPiece)"
            + " AND popn.status not in ('desist') "
            + " AND p.type in (:type) "
            + " AND n.unforeseen = :unforeseen")
    ReserveCalculationTotalSuraDTO totalGrossPriceOrdersByExternalEventAndEventId(Integer externalEvent, List<String> type, List<Integer> positionPiece, boolean unforeseen);

    @Query(value = "select new com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO (p.code, " +
            "p.position, " +
            "p.description, " +
            "popn.reference, " +
            "popn.amount, " +
            "(popn.grossPrice*popn.amount) as price, " +
            "((so.iva/so.subtotal) * (popn.price*popn.amount)) as iva, " +
            "((so.iva*100.0)/so.subtotal) as porcentaje, " +
            "popn.quality, " +
            "n.unforeseen, " +
            "popn.totalDiscount, " +
            "popn.order.subsidiary.idJob) "
            + " FROM ProductOrdersPiecesNoticeModel popn"
            + " INNER JOIN NoticeModel n ON n.externalEvent = popn.externalEvent "
            + " INNER JOIN PieceModel p ON p.dataEvent.id = n.eventId AND p.position = popn.positionPiece"
            + " INNER JOIN SellOrderModel so ON so.order.id = popn.order.id"
            + " WHERE n.externalEvent = :externalEvent "
            + " AND p.position in (:positionPiece)"
            + " AND popn.status not in ('desist') "
            + " AND p.type in (:type) "
            + " ORDER BY p.position ASC")
    List<ReserveRepuestosSuraDTO> findPiecesOrdersByExternalEvent(Integer externalEvent, List<String> type, List<Integer> positionPiece);

}