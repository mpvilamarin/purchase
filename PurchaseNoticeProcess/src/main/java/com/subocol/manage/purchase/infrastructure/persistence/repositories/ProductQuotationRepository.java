package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductQuotationModel;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductQuotationRepository extends JpaRepository<ProductQuotationModel, Long>, JpaSpecificationExecutor<ProductQuotationModel> {


    @Query(value = "SELECT pq.position_piece AS position, pq.auth, COUNT(pq) AS totalProducts, " +
            "COALESCE(SUM(CASE WHEN pq.status = 'omitted' THEN 1 ELSE 0 END), 0) AS omittedProducts, " +
            "COALESCE(SUM(CASE WHEN pq.status = 'rejected_quoted' THEN 1 ELSE 0 END), 0) AS rejectedQuotedProducts, " +
            "COALESCE(SUM(CASE WHEN pq.status = 'quoted' AND oq.priority = 1 AND (pq.extra_cost = true or pq.max_delivery_days = true) THEN 1 ELSE 0 END), 0) AS alertAndWinnerProducts, " +
            "COALESCE(SUM(CASE WHEN pq.status != 'rejected_quoted' AND pq.extra_cost = true THEN 1 ELSE 0 END), 0) AS extraCost, " +
            "COALESCE(SUM(CASE WHEN pq.status != 'rejected_quoted' AND pq.max_delivery_days = true THEN 1 ELSE 0 END), 0) AS overTime, " +
            "COALESCE(SUM(CASE WHEN pq.status != 'rejected_quoted' AND pq.max_cost_piece = true THEN 1 ELSE 0 END), 0) AS maxCostPiece " +
            "FROM quotation.product_quotation pq " +
            "INNER JOIN quotation.quotation q ON pq.quotation_id = q.id " +
            "LEFT JOIN adi_api.option_quote oq ON pq.id = oq.id " +
            "WHERE q.notice_id = ?1 " +
            "AND pq.position_piece IN ?2 " +
            "GROUP BY pq.position_piece, pq.auth", nativeQuery = true)
    List<Tuple> getProductQuotationStats(Long noticeId, List<Integer> positionPieces);

    @Query("SELECT pq " +
            "FROM ProductQuotationModel pq " +
            "INNER JOIN OptionQuoteModel optq on optq.id = pq.id " +
            "WHERE optq.priority = 1 " +
            "AND optq.event = ?2 " +
            "AND optq.noticeId = ?1 " +
            "AND pq.auth = true " +
            "AND (pq.maxDeliveryDays = false AND pq.extraCost = false) " +
            "AND pq.purchase = false " +
            "AND pq.quotation.subsidiary.status = true " +
            "AND pq.position <> 0 " +
            "AND coalesce(pq.description, '') <> '' " +
            "AND pq.position NOT IN (SELECT po.positionPiece FROM OrderModel o " +
            "INNER JOIN ProductOrderModel po ON po.order.id = o.id " +
            "WHERE o.notice.id = optq.noticeId AND po.positionPiece = pq.position " +
            "AND po.status NOT IN ('desist','devolution')) ")
    List<ProductQuotationModel> findAllByOptionQuote_EventAndOptionQuote_NoticeId(Long noticeId, String externalEvent);

    @Query("SELECT pq FROM ProductQuotationModel pq " +
            "WHERE pq.quotation.noticeId = ?1 " +
            "AND pq.quotation.flowType IN ('Manual', 'Automatico')" +
            "AND NOT pq.status = 'bought'  " +
            "AND pq.winner = true " +
            "AND pq.auth = true " +
            "AND pq.position NOT IN (SELECT po.positionPiece FROM OrderModel o " +
            "INNER JOIN ProductOrderModel po ON po.order.id = o.id " +
            "WHERE o.notice.id = pq.quotation.noticeId AND po.positionPiece = pq.position " +
            "AND po.status NOT IN ('desist','devolution')) ")
    List<ProductQuotationModel> findAllWinnersByNoticeId(Long noticeId);


    @Modifying
    @Query("UPDATE ProductQuotationModel pq SET pq.active = ?2 WHERE pq.id in " +
            " ( SELECT pq1.id FROM ProductQuotationModel pq1" +
            "  WHERE pq1.quotation.noticeId = ?1)")
    int updateActiveByNoticeId(Long noticeId, boolean active);

    @Modifying
    @Query(" UPDATE ProductQuotationModel pq " +
            " SET pq.status = ?5 " +
            " WHERE pq.id in ( " +
            " SELECT pq1.id FROM ProductQuotationModel pq1 " +
            " WHERE pq1.quotation.externalEvent = ?2 " +
            " AND pq1.quotation.flowType = ?3 " +
            " AND pq1.quotation.noticeId = ?1 " +
            " AND pq1.status = ?4 ) ")
    int updateStatusByExternalEventAndFlowTypeAndNoticeIdAndStatus(Long noticeId, String externalEvent, String flowType, String status, String newStatus);

    @Modifying
    @Query("UPDATE ProductQuotationModel pq SET pq.status = ?2 WHERE pq.id in ?1")
    int updateStatusByIdIn(Collection<Long> idsList, String newStatus);

    @Modifying
    @Query("UPDATE ProductQuotationModel pq SET pq.purchase = ?2 WHERE pq.id in ?1")
    int updatePurchaseByIdIn(Collection<Long> idsList, boolean newStatus);

    @Modifying
    @Query(" UPDATE ProductQuotationModel pq SET pq.purchase = ?3, " +
            " pq.status = ?2" +
            " WHERE pq.id IN ?1 ")
    int updatePurchaseByStatusAndIdIn(Collection<Long> idsList, String status, boolean purchase);

    @Modifying
    @Query(" UPDATE ProductQuotationModel pq SET pq.purchaseSubsidiary = ?3 " +
            " WHERE pq.id in ( " +
            " SELECT pq1.id FROM ProductQuotationModel pq1 " +
            " WHERE pq1.quotation.externalEvent = ?1 " +
            " AND pq1.position in ?2 ) ")
    int updatePurchaseSubsidiaryByExternalEventAndPositionIn(String externalEvent, Collection<Integer> positions, boolean purchase);

    @Query(" SELECT distinct new com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO(pq.id, pq.position, manu.irs, pq.reference, pq.amount, pq.grossPrice, pq.netPrice, pq.status, pq.deliveryTime, pq.quality, (COALESCE(pq.discountAdditional,0)) + (COALESCE(pq.discountBrand,0)) + (COALESCE(pq.discountCampaigns,0)) + (COALESCE(pq.discountManual,0)), s.alias) "
            + " FROM ProductQuotationModel pq "
            + " INNER JOIN QuotationModel quo ON quo.id = pq.quotation.id "
            + " INNER JOIN ManualPurchaseAdiModel manu ON CAST(manu.externalEvent as text) = quo.externalEvent "
            + " INNER JOIN NoticeModel n ON n.externalEvent = ?1 "
            + " INNER JOIN SubsidiaryModel s ON s.id = quo.subsidiary.id "
            + " WHERE quo.externalEvent = CAST(?1 as text)  AND pq.status IN ('accepted', 'bought') "
            + " AND pq.sendValuationPurchase = false AND pq.position = manu.position"
            + " GROUP BY pq.id, pq.position, manu.irs, pq.reference, pq.amount, pq.grossPrice, pq.netPrice, pq.status, pq.deliveryTime, pq.quality, (COALESCE(pq.discountAdditional,0)) + (COALESCE(pq.discountBrand,0)) + (COALESCE(pq.discountCampaigns,0)) + (COALESCE(pq.discountManual,0)), s.alias")
    List<PiecesValuationDTO> getPiecesValuationConcessionaireAccepted(Integer event);

    @Query("SELECT distinct new com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO(pq.id, pq.position, manu.irs, pq.reference, pq.amount, pq.grossPrice, pq.netPrice, pq.status, pq.deliveryTime, pq.quality, (COALESCE(pq.discountAdditional,0)) + (COALESCE(pq.discountBrand,0)) + (COALESCE(pq.discountCampaigns,0)) + (COALESCE(pq.discountManual,0)), pq.quotation.quotationSubsidiaryName) "
            + " FROM ProductQuotationModel pq "
            + " INNER JOIN QuotationModel quo ON quo.id = pq.quotation.id "
            + " INNER JOIN ManualPurchaseAdiModel manu ON CAST(manu.externalEvent as text) = quo.externalEvent "
            + " WHERE quo.externalEvent = CAST(?1 as text) AND manu.position = pq.position AND pq.status IN ('quoted') "
            + " AND pq.sendValuationQuotation = false AND pq.position "
            + "		NOT IN (SELECT "
            + "		pq2.position FROM ProductQuotationModel pq2"
            + " 	INNER JOIN QuotationModel quo2 ON quo2.id = pq2.quotation.id "
            + "     WHERE quo2.externalEvent = CAST(?1 as text) AND pq2.status IN ('bought','accepted'))"
            + " GROUP BY pq.id, pq.position, manu.irs, pq.reference, pq.amount, pq.grossPrice, pq.netPrice, pq.status, pq.deliveryTime, pq.quality, (COALESCE(pq.discountAdditional,0)) + (COALESCE(pq.discountBrand,0)) + (COALESCE(pq.discountCampaigns,0)) + (COALESCE(pq.discountManual,0)), pq.quotation.quotationSubsidiaryName")
    List<PiecesValuationDTO> getPiecesValuationConcessionaireQuoted(Integer event);


    @Query("SELECT distinct new com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO(pq.id, pq.position, manu.irs, pq.reference, pq.amount, pq.grossPrice, pq.netPrice, pq.status, pq.deliveryTime, pq.quality, (COALESCE(pq.discountAdditional,0)) + (COALESCE(pq.discountBrand,0)) + (COALESCE(pq.discountCampaigns,0)) + (COALESCE(pq.discountManual,0)), s.alias) "
            + " FROM ProductQuotationModel pq "
            + " INNER JOIN QuotationModel quo ON quo.id = pq.quotation.id "
            + " INNER JOIN ManualPurchaseAdiModel manu ON CAST(manu.externalEvent as text) = quo.externalEvent "
            + " INNER JOIN NoticeModel n ON n.externalEvent = ?1"
            + " INNER JOIN SubsidiaryModel s ON s.id = quo.subsidiary.id "
            + " WHERE quo.externalEvent = CAST(?1 as text)AND pq.status IN ('bought') "
            + " AND pq.sendValuationPurchase = false AND pq.position = manu.position"
            + " GROUP BY pq.id, pq.position, manu.irs, pq.reference, pq.amount, pq.grossPrice, pq.netPrice, pq.status, pq.deliveryTime, pq.quality, (COALESCE(pq.discountAdditional,0)) + (COALESCE(pq.discountBrand,0)) + (COALESCE(pq.discountCampaigns,0)) + (COALESCE(pq.discountManual,0)), s.alias")
    List<PiecesValuationDTO> getPiecesValuationMultibrandBought(Integer event);

    @Modifying
    @Query("UPDATE ProductQuotationModel pq "
            + "SET pq.sendValuationQuotation= ?2 "
            + "where pq.id in ?1")
    int updateValuationQuotationInProductQuotation(List<Long> idsList, boolean newStatus);

    @Modifying
    @Query("UPDATE ProductQuotationModel pq "
            + "SET pq.sendValuationPurchase= ?2 "
            + "where pq.id in ?1")
    int updateValuationPurchaseInProductQuotation(List<Long> idsList, boolean newStatus);

    @Modifying
    @Query("UPDATE ProductQuotationModel pq "
            + "SET pq.purchase= true, "
            + "pq.status = 'accepted'  "
            + "where pq.id in ?1")
    int updateStatusAndPurchaseById(List<Long> idsList);

    @Query("SELECT pq FROM ProductQuotationModel pq"
            + " WHERE (pq.extraCost = true OR pq.maxDeliveryDays = true) "
            + " AND pq.status = 'quoted' AND pq.auth = true "
            + " AND pq.id IN ?1 " +
            "AND pq.position <> 0 " +
            "AND coalesce(pq.description, '') <> '' ")
    List<ProductQuotationModel> findPiecesByIdAndOverTimeOverCost(List<Long> idsList);

    @Query("SELECT pq FROM ProductQuotationModel pq "
            + " INNER JOIN QuotationModel q on q.id = pq.quotation.id"
            + " WHERE (pq.extraCost = false AND pq.maxDeliveryDays = false " +
            "AND pq.auth = true " +
            "AND pq.position <> 0 " +
            "AND coalesce(pq.description, '') <> '' ) "
            + " AND q.flowType = 'Autosuministro' AND pq.status = 'quoted'"
            + " AND pq.id IN ?1 AND (SELECT COUNT(po) FROM OrderModel o"
            + " INNER JOIN ProductOrderModel po ON po.order.id = o.id "
            + " WHERE o.notice.id = q.noticeId AND po.positionPiece = pq.position"
            + " AND po.status NOT IN ('desist','devolution')) = 0")
    List<ProductQuotationModel> findPiecesByIdAndOverTime(List<Long> listIds);


    @Query("SELECT pq.id"
            + " FROM ProductQuotationModel pq"
            + " WHERE pq.quotation.id = ?1 AND pq.auth = true")
    List<Long> findIdsByQuotationIdAndAuthTrue(Long quotationId);

    @Modifying
    @Query(value = " UPDATE ProductQuotationModel pq1 " +
            " SET pq1.auth = ?1 " +
            " WHERE pq1.id in (" +
            "   SELECT pq2.id " +
            "    FROM QuotationModel q  " +
            "     INNER JOIN ProductQuotationModel pq2 ON q.id = pq2.quotation.id " +
            "    WHERE q.externalEvent = ?3 and pq2.position in ?2 AND pq2.deleted = false) ")
    int updateAllAuthByIdInAndEventId(Boolean auth, List<Integer> positions, String externalEvent);

    @Modifying
    @Query(value = "UPDATE ProductQuotationModel pq1 " +
            " SET pq1.status = 'desist'," +
            " pq1.winner = false, " +
            " pq1.purchase = false " +
            " WHERE pq1.id IN (" +
            "   SELECT pq2.id " +
            "   FROM ProductOrderModel po " +
            "   INNER JOIN OrderModel o ON po.order.id = o.id " +
            "   INNER JOIN NoticeModel n ON n.id = o.notice.id " + // Corregido: se cambió 'o' por 'n' para NoticeModel
            "   INNER JOIN QuotationModel q ON q.externalEvent = CAST(n.externalEvent AS text) AND o.subsidiary.id = q.subsidiary.id " +
            "   INNER JOIN ProductQuotationModel pq2 ON pq2.quotation.id = q.id AND pq2.position = po.positionPiece " + // Corregido: se cambió 'pq' por 'pq2'
            "   WHERE po.id IN ?1 AND (pq2.status = 'bought' OR pq2.status = 'accepted'))")
    int updateStatusByPositionPieceAndProductOrderId(List<Long> positions);

    @Modifying
    @Query(value = "UPDATE ProductQuotationModel SET deleted = true"
            + " WHERE id in ("
            + " SELECT pq.id"
            + " FROM QuotationModel q"
            + " INNER JOIN ProductQuotationModel pq on q.id = pq.quotation.id"
            + " WHERE q.externalEvent = :externalEvent and pq.position = :position)")
    int updateDeletePiecesTrueByExternalEventAndPosition(String externalEvent, Integer position);
}