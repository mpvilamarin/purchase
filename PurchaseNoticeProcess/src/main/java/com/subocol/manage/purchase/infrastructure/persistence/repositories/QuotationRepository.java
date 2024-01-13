package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterStatusProductQuotation;
import com.subocol.manage.purchase.infrastructure.persistence.entities.QuotationModel;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuotationRepository extends JpaRepository<QuotationModel, Long>, JpaSpecificationExecutor<QuotationModel> {


    @Query("SELECT new com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterStatusProductQuotation(" +
            "pq.quotation.id, " +
            "COUNT(pq), " +
            "SUM(CASE WHEN pq.status = 'omitted' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN pq.status = 'quoted' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN pq.status = 'rejected_quoted' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN pq.status = 'accepted' THEN 1 ELSE 0 END) ) " +
            "FROM QuotationModel q " +
            "JOIN ProductQuotationModel pq ON q.id = pq.quotation.id " +
            "WHERE q.noticeId = ?1 " +
            "AND q.flowType = 'Automatico' " +
            "GROUP BY pq.quotation.id")
    List<CounterStatusProductQuotation> getCounterStatusProductQuotation(Long noticeId);

    @Modifying
    @Query("UPDATE QuotationModel q SET q.status = ?1 WHERE q.id = ?2")
    int updateQuotationStatusById(String status, Long id);

    @Query(value = " update quotation.quotation set quotation_managed = true where id in "
            + "( select repuestos.id from ( "
            + "select q.id, count(pq.id) as cantrepu, "
            + "sum(case when pq.status in ('desist','bought','rejected_quoted','quoted','accepted') then 1 else 0 end) as cantmanaged "
            + "from quotation.product_quotation pq inner join quotation.quotation q on q.id = pq.quotation_id "
            + "where q.id in (?1) and pq.deleted = false "
            + "and pq.auth = (select n.auth from provider.notice n "
            + "inner join quotation.quotation quo on quo.aviso = cast(n.external_event as text) "
            + "where quo.id = q.id limit 1 )"
            + "group by q.id ) as repuestos "
            + "where repuestos.cantmanaged = repuestos.cantrepu ) ", nativeQuery = true)
    int updateQuotationManaged(Long quotationId);

    @Query("SELECT new com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterStatusProductQuotation(pq.quotation.id AS id, "
            + "COUNT(pq) AS totalProducts, "
            + "SUM(CASE WHEN pq.status = 'omitted' THEN 1 ELSE 0 END) AS omittedProducts, "
            + "SUM(CASE WHEN pq.status = 'quoted' THEN 1 ELSE 0 END) AS quotedProducts, "
            + "SUM(CASE WHEN pq.status = 'rejected_quoted' THEN 1 ELSE 0 END) AS rejectedQuotedProducts, "
            + "SUM(CASE WHEN pq.status = 'accepted' THEN 1 ELSE 0 END) AS acceptedProducts) "
            + "FROM QuotationModel q " + "JOIN ProductQuotationModel pq ON q.id = pq.quotation.id "
            + "AND q.id  = ?1 " + "GROUP BY pq.quotation.id")
    CounterStatusProductQuotation countStatusProductQuotation(Long quotationId);

    @Query(value = "select repuestos.id from ( "
            + " select q.id, count(pq.id) as cantrepu, "
            + " sum(case when pq.status in ('desist','bought','rejected_quoted','quoted','accepted') then 1 else 0 end) as cantmanaged "
            + " from quotation.product_quotation pq inner join quotation.quotation q on q.id = pq.quotation_id  "
            + " where q.id in (?1) and pq.deleted = false "
            + " group by q.id "
            + " ) "
            + " as repuestos "
            + " where repuestos.cantmanaged = repuestos.cantrepu ", nativeQuery = true)
    List<Tuple> findQuotationWithAllProductManage(Long quotationId);

    @Modifying
    @Query("update QuotationModel set quotationManaged = true where id in ?1")
    int updateQuotationManaged(List<Long> ids);

    @Query("SELECT q FROM QuotationModel q "
            + "WHERE q.noticeId = ?1 AND q.flowType = 'Autosuministro'")
    Optional<QuotationModel> findQuotationByNoticeIdAndFlowType(Long noticeId);

    @Modifying
    @Query(value = "select repuestos.id from ( "
            + " select q.id, count(pq.id) as cantrepu, "
            + " sum(case when pq.status in ('desist','bought','rejected_quoted','quoted','accepted') then 1 else 0 end) as cantmanaged "
            + " from quotation.product_quotation pq "
            + " inner join quotation.quotation q on q.id = pq.quotation_id  "
            + " where q.id in (select distinct quo.id from quotation.quotation quo where quo.aviso = ?1 ) and pq.deleted = false "
            + " and pq.auth = true and q.quotation_managed = false "
            + " group by q.id ) as repuestos "
            + " where repuestos.cantmanaged = repuestos.cantrepu", nativeQuery = true)
    List<Tuple> findQuotationManagedByExternalEvent(String externalEvent);
}