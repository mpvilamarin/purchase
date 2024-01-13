package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.annotations.Adapter;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.ProductQuotation;
import com.subocol.manage.purchase.domain.ports.persistence.ProductQuotationRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterProductQuotation;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductQuotationModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ProductQuotationRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author DANR
 * @version 1.0
 * @since 1/06/2023
 */
@Adapter
public class ProductQuotationAdapter implements ProductQuotationRepositoryPort {

    private final ProductQuotationRepository repository;

    public ProductQuotationAdapter(ProductQuotationRepository repository) {
        this.repository = repository;
    }

    @Override
    public int updateActiveProductQuotation(Long noticeId, boolean active) {
        return repository.updateActiveByNoticeId(noticeId, active);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductQuotation> findByEventAndPriorityQuotation(Long noticeId, String externalEvent) {

        List<ProductQuotationModel> result = repository.findAllByOptionQuote_EventAndOptionQuote_NoticeId(noticeId, externalEvent);

        if (!result.isEmpty())
            return MapperUtil.convertList(result, ProductQuotation.class);

        return Collections.emptyList();

    }

    @Override
    public int updateStatusManageQuotationPieces(Long noticeId, String externalEvent, String flowType, String status, String newStatus) {
        return repository.updateStatusByExternalEventAndFlowTypeAndNoticeIdAndStatus(noticeId, externalEvent, flowType, status, newStatus);
    }

    @Override
    public int updateStatusManageQuotationPiecesOptionQuote(List<Long> listsIdsProductQuotation, String newStatus) {
        return repository.updateStatusByIdIn(listsIdsProductQuotation, newStatus);
    }

    @Override
    public int updatePurchaseProductQuotation(List<Long> lsIdsProductQuotation, boolean purchase) {
        return repository.updatePurchaseByIdIn(lsIdsProductQuotation, purchase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductQuotation> findAllWinnersByNotice(Long noticeId) {
        List<ProductQuotationModel> result = repository.findAllWinnersByNoticeId(noticeId);

        if (!result.isEmpty())
            return MapperUtil.convertList(result, ProductQuotation.class);

        return Collections.emptyList();
    }

    @Override
    public int updateProductQuotationStatusTrueMP(List<Long> idsProductQuotation, String status, boolean purchase) {
        return repository.updatePurchaseByStatusAndIdIn(idsProductQuotation, status, purchase);
    }

    @Override
    public int updatePurchaseSubsidiary(String externalEvent, List<Integer> positions, boolean purchaseSubsidiary) {
        return repository.updatePurchaseSubsidiaryByExternalEventAndPositionIn(externalEvent, positions, purchaseSubsidiary);
    }

    @Override
    public List<CounterProductQuotation> findCounterStatusProductQuotationDTOForSubmissionManualPurchase(Long noticeId, List<Integer> positions) {
        return repository.getProductQuotationStats(noticeId, positions)
                .stream()
                .map(tuple -> new CounterProductQuotation(tuple.get("position", Integer.class).longValue(),
                        tuple.get("auth", Boolean.class),
                        tuple.get("totalProducts", Long.class),
                        tuple.get("omittedProducts", Long.class),
                        tuple.get("rejectedQuotedProducts", Long.class),
                        tuple.get("alertAndWinnerProducts", Long.class),
                        tuple.get("extraCost", Long.class),
                        tuple.get("overTime", Long.class),
                        tuple.get("maxCostPiece", Long.class))
                ).toList();
    }

    @Override
    public List<PiecesValuationDTO> findPiecesValuationConcessionaireAccepted(Integer externalEvent) {
        return repository.getPiecesValuationConcessionaireAccepted(externalEvent);
    }

    @Override
    public List<PiecesValuationDTO> findPiecesValuationConcessionaireQuoted(Integer externalEvent) {
        return repository.getPiecesValuationConcessionaireQuoted(externalEvent);
    }

    @Override
    public List<PiecesValuationDTO> findPiecesValuationMultibrandBought(Integer externalEvent) {
        return repository.getPiecesValuationMultibrandBought(externalEvent);
    }

    @Override
    public int updateSendValuationQuotation(List<Long> lsIdsProductQuotation, boolean flag) {
        return repository.updateValuationQuotationInProductQuotation(lsIdsProductQuotation, flag);
    }

    @Override
    public int updateSendValuationPurchase(List<Long> lsIdsProductQuotation, boolean flag) {
        return repository.updateValuationPurchaseInProductQuotation(lsIdsProductQuotation, flag);
    }

    @Override
    public int updateStatusAndPurchaseById(List<Long> lsIdsProductQuotation) {
        return repository.updateStatusAndPurchaseById(lsIdsProductQuotation);
    }

    @Override
    public List<ProductQuotation> findPiecesByIdAndOverTimeOverCost(List<Long> listIds) {
        List<ProductQuotationModel> result = repository.findPiecesByIdAndOverTimeOverCost(listIds);

        if (!result.isEmpty())
            return MapperUtil.convertList(result, ProductQuotation.class);

        return Collections.emptyList();
    }

    @Override
    public List<ProductQuotation> findPiecesByIdAndOverTime(List<Long> listIds) {
        List<ProductQuotationModel> result = repository.findPiecesByIdAndOverTime(listIds);

        if (!result.isEmpty())
            return MapperUtil.convertList(result, ProductQuotation.class);

        return Collections.emptyList();
    }

    @Override
    public List<Long> findIdsByQuotationIdAndAuthTrue(Long quotationId) {
        return repository.findIdsByQuotationIdAndAuthTrue(quotationId);
    }

    @Override
    public int updateAllAuthByIdInAndEventId(Boolean auth, List<Integer> positions, String externalEvent) {
        return repository.updateAllAuthByIdInAndEventId(auth, positions, externalEvent);
    }

    @Override
    public int updateStatusByPositionPieceAndProductOrderId(List<Long> positions) {
        return repository.updateStatusByPositionPieceAndProductOrderId(positions);
    }

    @Override
    public int updateDeletePiecesTrueByExternalEventAndPosition(String externalEvent, Integer position) {
        return repository.updateDeletePiecesTrueByExternalEventAndPosition(externalEvent, position);
    }
}
