package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OptionQuoteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OptionQuoteRepository extends JpaRepository<OptionQuoteModel, Long>, JpaSpecificationExecutor<OptionQuoteModel> {

    @Query("SELECT distinct new com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO(pq.id, pq.position, manu.irs, pq.reference, "
            + "pq.amount, pq.grossPrice, pq.netPrice, pq.status, pq.deliveryTime, pq.quality, "
            + "(COALESCE(pq.discountAdditional,0)) + (COALESCE(pq.discountBrand,0)) + (COALESCE(pq.discountCampaigns,0)) + (COALESCE(pq.discountManual,0)), s.alias) "
            + " FROM OptionQuoteModel opt "
            + " INNER JOIN ProductQuotationModel pq on opt.id = pq.id "
            + " INNER JOIN QuotationModel quo ON quo.id = pq.quotation.id "
            + " INNER JOIN ManualPurchaseAdiModel manu ON CAST(manu.externalEvent as text) = quo.externalEvent "
            + " INNER JOIN NoticeModel n ON n.externalEvent = ?1 "
            + " INNER JOIN SubsidiaryModel s ON s.id = quo.subsidiary.id "
            + " WHERE opt.priority = 1 and opt.event = CAST(?1 as text) "
            + " AND pq.sendValuationPurchase = false AND pq.status IN ('accepted') AND pq.position = manu.position "
            + " GROUP BY pq.id, pq.position, manu.irs, pq.reference, pq.amount, pq.grossPrice, pq.netPrice, pq.status, pq.deliveryTime, pq.quality, (COALESCE(pq.discountAdditional,0)) + (COALESCE(pq.discountBrand,0)) + (COALESCE(pq.discountCampaigns,0)) + (COALESCE(pq.discountManual,0)), s.alias")
    List<PiecesValuationDTO> getPiecesValuationMultibrandAccepted(Integer event);

    @Query("SELECT distinct new com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO(pq.id, pq.position, opt.irs, pq.reference, pq.amount, pq.grossPrice, pq.netPrice, pq.status, pq.deliveryTime, pq.quality, (COALESCE(pq.discountAdditional,0)) + (COALESCE(pq.discountBrand,0)) + (COALESCE(pq.discountCampaigns,0)) + (COALESCE(pq.discountManual,0)), pq.quotation.quotationSubsidiaryName) "
            + " FROM OptionQuoteModel opt "
            + " INNER JOIN ProductQuotationModel pq on opt.id = pq.id "
            + " WHERE opt.priority = 1 and opt.event = CAST(?1 as text) "
            + " AND pq.sendValuationQuotation = false AND pq.status IN ('quoted')"
            + " AND pq.position NOT IN (SELECT "
            + "		pq2.position FROM ProductQuotationModel pq2 "
            + "		INNER JOIN QuotationModel quo2 ON quo2.id = pq2.quotation.id "
            + "		WHERE quo2.externalEvent = CAST(?1 as text) AND pq2.status IN ('bought','accepted')) "
            + " GROUP BY pq.id, pq.position, opt.irs, pq.reference, pq.amount, pq.grossPrice, pq.netPrice, pq.status, pq.deliveryTime, pq.quality, (COALESCE(pq.discountAdditional,0)) + (COALESCE(pq.discountBrand,0)) + (COALESCE(pq.discountCampaigns,0)) + (COALESCE(pq.discountManual,0)), pq.quotation.quotationSubsidiaryName")
    List<PiecesValuationDTO> getPiecesValuationMultibrandQuoted(Integer event);
}
