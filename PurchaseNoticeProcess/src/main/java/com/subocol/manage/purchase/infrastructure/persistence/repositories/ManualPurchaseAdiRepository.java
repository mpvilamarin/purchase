package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.ManualPurchaseAdiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManualPurchaseAdiRepository extends JpaRepository<ManualPurchaseAdiModel, Long>, JpaSpecificationExecutor<ManualPurchaseAdiModel> {

    @Query("SELECT DISTINCT (mpa) " +
            "FROM ManualPurchaseAdiModel mpa " +
            "WHERE mpa.externalEvent = :externalEvent " +
            "AND mpa.eventId = :eventId " +
            "AND mpa.position NOT IN ( " +
            "SELECT mp.position FROM ManualPurchaseModel mp " +
            "INNER JOIN ManualPurchaseAdiModel mpa1 on mp.externalEvent = CAST(mpa1.externalEvent as string) " +
            "AND mp.position = mpa1.position " +
            "WHERE mp.externalEvent = CAST(:externalEvent as string) " +
            "AND mp.eventId = :eventId) " +
            "AND mpa.position NOT IN ( " +
            "SELECT pq.position FROM ProductQuotationModel pq " +
            "INNER JOIN QuotationModel q ON pq.quotation.id = q.id " +
            "INNER JOIN OptionQuoteModel oq ON pq.id = oq.id " +
            "WHERE q.externalEvent = CAST(:externalEvent as string) " +
            "AND q.noticeId = :noticeId " +
            "AND (pq.status = 'accepted' " +
            "OR (pq.status = 'quoted' AND oq.priority = 1 AND pq.auth = false) " +
            // "OR (pq.auth = false AND (pq.extraCost = true OR pq.maxDeliveryDays = true))
            ")) " +
            " AND mpa.position NOT IN ( " +
            "SELECT pq.position FROM ProductQuotationModel pq " +
            "INNER JOIN QuotationModel q ON pq.quotation.id = q.id " +
            "WHERE q.externalEvent = CAST(:externalEvent as string) " +
            "AND q.noticeId = :noticeId " +
            "AND pq.auth = false AND (pq.extraCost = true OR pq.maxDeliveryDays = true " +
            ") ) " +
            "AND mpa.position NOT IN ( " +
            "SELECT p.position FROM PieceModel p " +
            "INNER JOIN ManualPurchaseAdiModel mpa2 ON mpa2.eventId = p.dataEvent.id " +
            "AND mpa2.position = p.position " +
            "WHERE true = :totParameter " +
            "AND p.dataEvent.id = :eventId " +
            "AND UPPER(mpa2.type) = 'TOT' " +
            "AND p.unitValue > 0) " +
            "AND mpa.position NOT IN (SELECT po.positionPiece FROM OrderModel o " +
            "INNER JOIN ProductOrderModel po ON po.order.id = o.id " +
            "WHERE o.notice.id = :noticeId AND po.positionPiece = mpa.position " +
            "AND po.status NOT IN ('desist','devolution')) ")
    List<ManualPurchaseAdiModel> getAllByEventFilterManualPurchaseForAuth(
            @Param("externalEvent") Long externalEvent,
            @Param("eventId") Long eventId,
            @Param("noticeId") Long noticeId,
            @Param("totParameter") boolean totParameter);


    @Query("SELECT DISTINCT (mpa) " +
            "FROM ManualPurchaseAdiModel mpa " +
            "WHERE mpa.externalEvent = :externalEvent " +
            "AND mpa.eventId = :eventId " +
            "AND mpa.position NOT IN ( " +
            "SELECT mp.position FROM ManualPurchaseModel mp " +
            "INNER JOIN ManualPurchaseAdiModel mpa1 on mp.externalEvent = CAST(mpa1.externalEvent as string) " +
            "AND mp.position = mpa1.position " +
            "WHERE mp.externalEvent = CAST(:externalEvent as string) " +
            "AND mp.eventId = :eventId) " +
            "AND mpa.position NOT IN ( " +
            "SELECT p.position FROM PieceModel p " +
            "INNER JOIN ManualPurchaseAdiModel mpa2 ON mpa2.eventId = p.dataEvent.id " +
            "AND mpa2.position = p.position " +
            "WHERE true = :totParameter " +
            "AND p.dataEvent.id = :eventId " +
            "AND UPPER(mpa2.type) = 'TOT' " +
            "AND p.unitValue > 0)")
    List<ManualPurchaseAdiModel> getAllByEventFilterManualPurchase(
            @Param("externalEvent") Long externalEvent,
            @Param("eventId") Long eventId,
            @Param("totParameter") boolean totParameter);

    Optional<ManualPurchaseAdiModel> findByExternalEventAndPosition(Integer externalEvent, Integer position);

}