package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeClaimNumberDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.NoticeModel;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeModel, Long>, JpaSpecificationExecutor<NoticeModel> {

    @Query("SELECT count(n) FROM NoticeModel n "
            + "WHERE n.externalEvent = :externalEvent AND n.unforeseen = :unforeseen")
    Long countAllByExternalEventAndUnforeseen(Integer externalEvent, boolean unforeseen);

    @Query(" select new com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO ( p.position, p.quantity, " +
            "  ?4, ?3) " +
            "  from NoticeModel n " +
            " inner join PieceModel p on p.dataEvent.id = n.eventId " +
            " where n.externalEvent = ?1 and (p.type <> 'TOT' or (p.unitValue = 0 and p.type = 'TOT')) " +
            " and p.position in ?2 " +
            " order by 1 asc ")
    List<SpareDetailToFollowUpDTO> findSpareToFollowUpByExternalEventAndPositions(Long externalEvent, List<Integer> positions, boolean deleted, boolean quoted);

    @Query(value = " select  p.position_piece , p.quantity , false as esCotizado,  false as deleted " +
            " from provider.notice n " +
            " inner join sbcevent.pieces p on p.event_id = n.event_id " +
            " where n.external_event  = ?1 and (p.type != 'TOT' or (p.unit_value = 0 and p.type = 'TOT')) " +
            " and p.position_piece in ( " +
            " select pq.position_piece from quotation.quotation q  " +
            " inner join quotation.product_quotation pq on q.id = pq.quotation_id " +
            " where q.aviso = cast(n.external_event as text) and pq.auth = false " +
            " union " +
            " select mp.position_piece from provider.manual_purchase mp " +
            " where mp.external_event = cast(n.external_event as text) and mp.auth = false  " +
            " ) " +
            " order by 1 asc ", nativeQuery = true)
    List<Tuple> findSpareToFollowUpByExternalEventAndPositionsNoAuth(Long externalEvent);


    @Query("SELECT  new com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeClaimNumberDTO("
            + " n.externalEvent, count(n), SUM (CASE when n.claimNumber in (?2) then 1 else 0 end) )"
            + " from NoticeModel n where n.externalEvent = ?1 "
            + " group by n.externalEvent")
    NoticeClaimNumberDTO findDistinctClaimNumberByExternalEvent(Integer externalEvent, String claimNumber);

    @Modifying
    @Query("UPDATE NoticeModel n SET n.claimNumber = ?2 WHERE n.externalEvent = ?1")
    int updateClaimNumberByExternalEvent(Integer externalEvent, String claimNumber);

    List<NoticeModel> findAllByExternalEvent(Integer externalEvent);

    @Modifying
    @Query(value = "UPDATE NoticeModel n SET n.auth = ?2 WHERE n.externalEvent = ?1")
    int updateAuthByExternalEvent(Integer externalEvent, Boolean auth);

    @Query(value = " SELECT distinct(ntc)" +
            " FROM NoticeModel ntc " +
            " where ntc.externalEvent = ?1 AND ntc.auth = ?2 " +
            " ORDER BY ntc.id ASC ")
    List<NoticeModel> findByExternalEventAndAuth(Integer externalEvent, boolean auth);

    @Query(value = "select new com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO ( p.position, p.quantity, " +
            ":quoted, :deleted) " +
            "from NoticeModel n "
            + "inner join PieceModel p on p.dataEvent.id = n.eventId "
            + "where n.externalEvent = :externalEvent and (p.type <> 'TOT' or (p.unitValue = 0 and p.type = 'TOT')) "
            + "and p.position in (:positions) "
            + "order by 1 asc")
    List<SpareDetailToFollowUpDTO> findInfoFollowUpByExternalEventAndPositions(Long externalEvent, List<Integer> positions, boolean deleted, boolean quoted);

    @Query(value = "select  p.position_piece , p.quantity , false as esCotizado,  false as deleted " +
            "from provider.notice n "
            + "inner join sbcevent.pieces p on p.event_id = n.event_id "
            + "where n.external_event  = :externalEvent and (p.type <> 'TOT' or (p.unit_value = 0 and p.type = 'TOT')) "
            + "and p.position_piece in ( "
            + "select pq.position_piece from quotation.quotation q "
            + "inner join quotation.product_quotation pq on q.id = pq.quotation_id "
            + "where q.aviso = cast(n.external_event as text) and pq.auth = false "
            + "union "
            + "select mp.position_piece from provider.manual_purchase mp "
            + "where mp.external_event = cast(n.external_event as text) and mp.auth = false "
            + ") "
            + "order by 1 asc", nativeQuery = true)
    List<Object[]> findInfoFollowUpByExternalEventAndPositionsNoAut(Long externalEvent);


    @Modifying
    @Query("UPDATE NoticeModel n SET n.processedNotice = true WHERE n.id = ?1")
    int updateProcessedNoticeByNoticeId(Long noticeId);
}