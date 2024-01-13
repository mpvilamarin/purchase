package com.subocol.manage.purchase.domain.ports.persistence;

import com.subocol.manage.purchase.common.annotations.Port;
import com.subocol.manage.purchase.domain.models.ProductQuotation;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterProductQuotation;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;

import java.util.List;

@Port
public interface ProductQuotationRepositoryPort {

    int updateActiveProductQuotation(Long noticeId, boolean active);

    List<ProductQuotation> findByEventAndPriorityQuotation(Long noticeId, String externalEvent);

    int updateStatusManageQuotationPieces(Long noticeId, String externalEvent, String flowType, String status, String newStatus);

    int updateStatusManageQuotationPiecesOptionQuote(List<Long> listsIdsProductQuotation, String newStatus);

    int updatePurchaseProductQuotation(List<Long> lsIdsProductQuotation, boolean purchase);

    List<ProductQuotation> findAllWinnersByNotice(Long noticeId);

    int updateProductQuotationStatusTrueMP(List<Long> idsProductQuotation, String status, boolean purchase);

    int updatePurchaseSubsidiary(String externalEvent, List<Integer> positions, boolean purchase);

    List<CounterProductQuotation> findCounterStatusProductQuotationDTOForSubmissionManualPurchase(Long noticeId, List<Integer> positions);

    List<PiecesValuationDTO> findPiecesValuationConcessionaireAccepted(Integer externalEvent);

    List<PiecesValuationDTO> findPiecesValuationConcessionaireQuoted(Integer externalEvent);

    List<PiecesValuationDTO> findPiecesValuationMultibrandBought(Integer externalEvent);

    int updateSendValuationQuotation(List<Long> lsIdsProductQuotation, boolean flag);

    int updateSendValuationPurchase(List<Long> lsIdsProductQuotation, boolean flag);

    int updateStatusAndPurchaseById(List<Long> lsIdsProductQuotation);

    List<ProductQuotation> findPiecesByIdAndOverTimeOverCost(List<Long> listIds);

    List<ProductQuotation> findPiecesByIdAndOverTime(List<Long> listIds);

    List<Long> findIdsByQuotationIdAndAuthTrue(Long quotationId);

    int updateAllAuthByIdInAndEventId(Boolean auth, List<Integer> positions, String eventId);

    int updateStatusByPositionPieceAndProductOrderId(List<Long> positions);

    int updateDeletePiecesTrueByExternalEventAndPosition(String externalEvent, Integer position);

}