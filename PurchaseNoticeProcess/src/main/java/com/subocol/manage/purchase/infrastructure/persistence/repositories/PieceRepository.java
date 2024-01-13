package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.PieceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PieceRepository extends JpaRepository<PieceModel, Integer>, JpaSpecificationExecutor<PieceModel> {

    @Query(value = "select distinct p.position_piece from provider.notice n" +
            " inner join sbcevent.pieces p on p.event_id = n.event_id" +
            " where n.external_event = ?1 and p.unit_value = 0" +
            " and n.unforeseen = ?2" +
            " and n.event_id = (select min(n1.event_id) from provider.notice n1" +
            " where n.external_event = n1.external_event and n1.unforeseen = n.unforeseen)"+
            " and p.position_piece in (" +
            " select distinct (pq.position_piece) from quotation.product_quotation pq" +
            " inner join quotation.quotation q on q.id = pq.quotation_id" +
            " where q.aviso = CAST(n.external_event as TEXT)" +
            " and pq.auth = true and pq.deleted = false" +
            " union" +
            " select distinct (mp.position_piece) from provider.manual_purchase mp" +
            " where mp.external_event = cast(n.external_event as TEXT)" +
            " and mp.auth = true and mp.deleted = false)"
            , nativeQuery = true)
    List<Integer> findInitialPiecesByExternalEventBolivarConditionTrue(
            @Param("externalEvent") Integer externalEvent,
            @Param("unforeseen") boolean unforeseen);

    @Query(value = "select distinct p.position_piece from provider.notice n" +
            " inner join sbcevent.pieces p on p.event_id = n.event_id" +
            " where n.external_event = ?1 and p.unit_value = 0" +
            " and n.unforeseen = ?2" +
            " and p.position_piece in (" +
            " select distinct (pq.position_piece) from quotation.product_quotation pq" +
            " inner join quotation.quotation q on q.id = pq.quotation_id" +
            " where q.aviso = CAST(n.external_event as TEXT)" +
            " and pq.auth = true and pq.deleted = false" +
            " union" +
            " select distinct (mp.position_piece) from provider.manual_purchase mp" +
            " where mp.external_event = cast(n.external_event as TEXT)" +
            " and mp.auth = true and mp.deleted = false)"
            , nativeQuery = true)
    List<Integer> findInitialPiecesByExternalEventBolivarConditionFalse(
            @Param("externalEvent") Integer externalEvent,
            @Param("unforeseen") boolean unforeseen);

    @Query(value = "select distinct p.position_piece from provider.notice n" +
            " inner join sbcevent.pieces p on p.event_id = n.event_id" +
            " inner join provider.orders o ON o.notice_id = n.id"+
            " inner join provider.product_order po on po.order_id = o.id and po.position_piece = p.position_piece"+
            " where n.external_event = ?1 and p.unit_value = 0" +
            " and n.unforeseen = ?2" +
            " and po.status in ('desist')"+
            " and n.event_id = (select min(n1.event_id) from provider.notice n1" +
            " where n.external_event = n1.external_event and n1.unforeseen = n.unforeseen)"+
            " and p.position_piece in (" +
            " select distinct (pq.position_piece) from quotation.product_quotation pq" +
            " inner join quotation.quotation q on q.id = pq.quotation_id" +
            " where q.aviso = CAST(n.external_event as TEXT)" +
            " and pq.auth = true and pq.deleted = false" +
            " union" +
            " select distinct (mp.position_piece) from provider.manual_purchase mp" +
            " where mp.external_event = cast(n.external_event as TEXT)" +
            " and mp.auth = true and mp.deleted = false)"
            , nativeQuery = true)
    List<Integer> findInitialPiecesByExternalEventWithOrders(
            @Param("externalEvent") Integer externalEvent,
            @Param("unforeseen") boolean unforeseen);

    @Query(value = "select distinct p.position_piece from provider.notice n" +
            " inner join sbcevent.pieces p on p.event_id = n.event_id" +
            " inner join provider.orders o ON o.notice_id = n.id"+
            " inner join provider.product_order po on po.order_id = o.id and po.position_piece = p.position_piece"+
            " where n.external_event = ?1 and p.unit_value = 0" +
            " and n.unforeseen = ?2" +
            " and po.status in ('desist')"+
            " and n.event_id = (select min(n1.event_id) from provider.notice n1" +
            " where n.external_event = n1.external_event and n1.unforeseen = n.unforeseen)"+
            " and p.position_piece in (" +
            " select distinct (pq.position_piece) from quotation.product_quotation pq" +
            " inner join quotation.quotation q on q.id = pq.quotation_id" +
            " where q.aviso = CAST(n.external_event as TEXT)" +
            " and pq.auth = true and pq.deleted = false" +
            " union" +
            " select distinct (mp.position_piece) from provider.manual_purchase mp" +
            " where mp.external_event = cast(n.external_event as TEXT)" +
            " and mp.auth = true and mp.deleted = false)"
            , nativeQuery = true)
    List<Integer> findInitialPiecesByExternalWithOrdersConditionTrue(
            @Param("externalEvent") Integer externalEvent,
            @Param("unforeseen") boolean unforeseen);

    @Query(value = "select distinct p.position_piece from provider.notice n" +
            " inner join sbcevent.pieces p on p.event_id = n.event_id" +
            " inner join provider.orders o ON o.notice_id = n.id"+
            " inner join provider.product_order po on po.order_id = o.id and po.position_piece = p.position_piece"+
            " where n.external_event = ?1 and p.unit_value = 0" +
            " and n.unforeseen = ?2" +
            " and po.status in ('desist')"+
            " and p.position_piece in (" +
            " select distinct (pq.position_piece) from quotation.product_quotation pq" +
            " inner join quotation.quotation q on q.id = pq.quotation_id" +
            " where q.aviso = CAST(n.external_event as TEXT)" +
            " and pq.auth = true and pq.deleted = false" +
            " union" +
            " select distinct (mp.position_piece) from provider.manual_purchase mp" +
            " where mp.external_event = cast(n.external_event as TEXT)" +
            " and mp.auth = true and mp.deleted = false)"
            , nativeQuery = true)
    List<Integer> findInitialPiecesByExternalWithOrdersConditionFalse(
            @Param("externalEvent") Integer externalEvent,
            @Param("unforeseen") boolean unforeseen);

    @Query(value = "select distinct p.position_piece from provider.notice n" +
            " inner join sbcevent.pieces p on p.event_id = n.event_id" +
            " where n.external_event = ?1 and p.unit_value = 0" +
            " and n.unforeseen = ?2" +
            " and n.event_id = (select min(n1.event_id) from provider.notice n1" +
            " where n.external_event = n1.external_event and n1.unforeseen = n.unforeseen)"+
            " and p.position_piece in (" +
            " select distinct (pq.position_piece) from quotation.product_quotation pq" +
            " inner join quotation.quotation q on q.id = pq.quotation_id" +
            " where q.aviso = CAST(n.external_event as TEXT)" +
            " and pq.auth = true and pq.deleted = false" +
            " union" +
            " select distinct (mp.position_piece) from provider.manual_purchase mp" +
            " where mp.external_event = cast(n.external_event as TEXT)" +
            " and mp.auth = true and mp.deleted = false)"
            , nativeQuery = true)
    List<Integer> findInitialPiecesByExternalEventSuraConditionTrue(
            @Param("externalEvent") Integer externalEvent,
            @Param("unforeseen") boolean unforeseen);

    @Query(value = "select distinct p.position_piece from provider.notice n" +
            " inner join sbcevent.pieces p on p.event_id = n.event_id" +
            " where n.external_event = ?1 and p.unit_value = 0" +
            " and n.unforeseen = ?2" +
            " and p.position_piece in (" +
            " select distinct (pq.position_piece) from quotation.product_quotation pq" +
            " inner join quotation.quotation q on q.id = pq.quotation_id" +
            " where q.aviso = CAST(n.external_event as TEXT)" +
            " and pq.auth = true and pq.deleted = false" +
            " union" +
            " select distinct (mp.position_piece) from provider.manual_purchase mp" +
            " where mp.external_event = cast(n.external_event as TEXT)" +
            " and mp.auth = true and mp.deleted = false)"
            , nativeQuery = true)
    List<Integer> findInitialPiecesByExternalEventSuraConditionFalse(
            @Param("externalEvent") Integer externalEvent,
            @Param("unforeseen") boolean unforeseen);

    @Query(value = "select distinct p.position_piece from provider.notice n" +
            " inner join sbcevent.pieces p on p.event_id = n.event_id" +
            " inner join provider.orders o ON o.notice_id = n.id"+
            " inner join provider.product_order po on po.order_id = o.id and po.position_piece = p.position_piece"+
            " where n.external_event = ?1 and p.unit_value = 0" +
            " and n.unforeseen = ?2" +
            " and po.status in ('desist')"+
            " and n.event_id = (select min(n1.event_id) from provider.notice n1" +
            " where n.external_event = n1.external_event and n1.unforeseen = n.unforeseen)"+
            " and p.position_piece in (" +
            " select distinct (pq.position_piece) from quotation.product_quotation pq" +
            " inner join quotation.quotation q on q.id = pq.quotation_id" +
            " where q.aviso = CAST(n.external_event as TEXT)" +
            " and pq.auth = true and pq.deleted = false" +
            " union" +
            " select distinct (mp.position_piece) from provider.manual_purchase mp" +
            " where mp.external_event = cast(n.external_event as TEXT)" +
            " and mp.auth = true and mp.deleted = false)"
            , nativeQuery = true)
    List<Integer> findInitialPiecesByExternalEventWithOrdersSura(
            @Param("externalEvent") Integer externalEvent,
            @Param("unforeseen") boolean unforeseen);

    @Query(value = "select distinct p.position_piece from provider.notice n" +
            " inner join sbcevent.pieces p on p.event_id = n.event_id" +
            " inner join provider.orders o ON o.notice_id = n.id"+
            " inner join provider.product_order po on po.order_id = o.id and po.position_piece = p.position_piece"+
            " where n.external_event = ?1 and p.unit_value = 0" +
            " and n.unforeseen = ?2" +
            " and po.status in ('desist')"+
            " and n.event_id = (select min(n1.event_id) from provider.notice n1" +
            " where n.external_event = n1.external_event and n1.unforeseen = n.unforeseen)"+
            " and p.position_piece in (" +
            " select distinct (pq.position_piece) from quotation.product_quotation pq" +
            " inner join quotation.quotation q on q.id = pq.quotation_id" +
            " where q.aviso = CAST(n.external_event as TEXT)" +
            " and pq.auth = true and pq.deleted = false" +
            " union" +
            " select distinct (mp.position_piece) from provider.manual_purchase mp" +
            " where mp.external_event = cast(n.external_event as TEXT)" +
            " and mp.auth = true and mp.deleted = false)"
            , nativeQuery = true)
    List<Integer> findInitialPiecesByExternalWithOrdersSuraConditionTrue(
            @Param("externalEvent") Integer externalEvent,
            @Param("unforeseen") boolean unforeseen);

    @Query(value = "select distinct p.position_piece from provider.notice n" +
            " inner join sbcevent.pieces p on p.event_id = n.event_id" +
            " inner join provider.orders o ON o.notice_id = n.id"+
            " inner join provider.product_order po on po.order_id = o.id and po.position_piece = p.position_piece"+
            " where n.external_event = ?1 and p.unit_value = 0" +
            " and n.unforeseen = ?2" +
            " and po.status in ('desist')"+
            " and n.event_id = (select min(n1.event_id) from provider.notice n1" +
            " where n.external_event = n1.external_event and n1.unforeseen = n.unforeseen)"+
            " and p.position_piece in (" +
            " select distinct (pq.position_piece) from quotation.product_quotation pq" +
            " inner join quotation.quotation q on q.id = pq.quotation_id" +
            " where q.aviso = CAST(n.external_event as TEXT)" +
            " and pq.auth = true and pq.deleted = false" +
            " union" +
            " select distinct (mp.position_piece) from provider.manual_purchase mp" +
            " where mp.external_event = cast(n.external_event as TEXT)" +
            " and mp.auth = true and mp.deleted = false)"
            , nativeQuery = true)
    List<Integer> findInitialPiecesByExternalWithOrdersSuraConditionFalse(
            @Param("externalEvent") Integer externalEvent,
            @Param("unforeseen") boolean unforeseen);

    @Query(value = "SELECT piece FROM PieceModel piece " +
            "INNER JOIN NoticeModel n ON n.eventId = piece.dataEvent.id " +
            "WHERE n.externalEvent = :externalEvent and piece.position = :position ")
    Optional<PieceModel> findByExternalEventAndPosition(Integer externalEvent, Integer position);

    @Query(value = " SELECT COUNT(distinct( p.position)) from NoticeModel n "
            + " INNER JOIN PieceModel p on n.eventId = p.dataEvent.id "
            + " WHERE n.externalEvent = ?1 and p.position IN ?2 ")
    int countPiecesByExternalEventAndPositions(Integer externalEvent, List<Integer> positions);
}