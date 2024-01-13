package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.common.utils.MapperUtil;
import com.subocol.manage.purchase.domain.models.ProductQuotation;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterProductQuotation;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeValuationDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductQuotationModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ProductQuotationRepository;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductQuotationAdapterTest {

    @Mock
    private ProductQuotationRepository repository;

    @InjectMocks
    private ProductQuotationAdapter productQuotationAdapter;
    ProductQuotationModel productQuotation1;
    ProductQuotationModel productQuotation2;
    Long noticeId = 123L;
    Integer externalEvent = 31624;
    NoticeValuationDTO noticeValuationDTO = new NoticeValuationDTO();
    List<PiecesValuationDTO> piecesListQuoted = new ArrayList<>();
    List<PiecesValuationDTO> piecesListAccepted = new ArrayList<>();

    @BeforeEach
    void setup() {
        productQuotation1 = ProductQuotationModel.builder()
                .position(1).reference("ProductReference").netPrice(10.0).description("ProductDescription")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2)
                .discountCampaigns(1.0).discountAdditional(2.0)
                .discountManual(3.0).comment("ProductComment").build();

        productQuotation2 = ProductQuotationModel.builder()
                .position(1).reference("ProductReference").netPrice(10.0).description("ProductDescription")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2)
                .discountCampaigns(1.0).discountAdditional(2.0)
                .discountManual(3.0).comment("ProductComment").build();

        noticeValuationDTO = NoticeValuationDTO.builder().numeroAviso(484620).build();

        PiecesValuationDTO piecesAccepted = PiecesValuationDTO.builder().calidadRepuesto("Original")
                .cantidad(1).codigo("").comprada("S").descuento(10.0).id(1413776L).nombreSucursalGanadora("EXCLUSIVE MOTORS SA")
                .posicion(3).valorUnitario(10.0).valorUnitarioConDescuento(9.0).tiempoEstimadoEntrega(3).build();

        PiecesValuationDTO piecesQuoted = PiecesValuationDTO.builder().calidadRepuesto("Original")
                .cantidad(1).codigo("").comprada("N").descuento(10.0).id(1413776L).nombreSucursalGanadora("EXCLUSIVE MOTORS SA")
                .posicion(3).valorUnitario(10.0).valorUnitarioConDescuento(9.0).tiempoEstimadoEntrega(3).build();

        piecesListQuoted.add(piecesQuoted);
        piecesListAccepted.add(piecesAccepted);
        piecesListQuoted.addAll(piecesListAccepted);
        noticeValuationDTO.setPiezas(piecesListQuoted);

    }

    @Test
    void productQuotationAdapter_updateActiveProductQuotation_ReturnsUpdatedCount() {
        boolean active = true;
        int updatedCount = 5;

        when(repository.updateActiveByNoticeId(anyLong(), anyBoolean())).thenReturn(updatedCount);

        int result = productQuotationAdapter.updateActiveProductQuotation(noticeId, active);

        assertEquals(updatedCount, result);
    }

    @Test
    void productQuotationAdapter_updateStatusManageQuotationPieces_ReturnsUpdatedCount() {
        String externalEvent = "4623453";
        String flowType = "Automatico";
        String status = "sended";
        String newStatus = "omitted";
        int updatedCount = 5;

        when(repository.updateStatusByExternalEventAndFlowTypeAndNoticeIdAndStatus(anyLong(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(updatedCount);

        int result = productQuotationAdapter.updateStatusManageQuotationPieces(noticeId, externalEvent, flowType, status, newStatus);

        assertEquals(updatedCount, result);
    }

    @Test
    void productQuotationAdapter_updateStatusManageQuotationPiecesOptionQuote_ReturnsUpdatedCount() {
        List<Long> listsIdsProductQuotation = Arrays.asList(1L, 2L, 3L);
        String newStatus = "accepted";
        int updatedCount = 3;

        when(repository.updateStatusByIdIn(anyList(), anyString()))
                .thenReturn(updatedCount);

        int result = productQuotationAdapter.updateStatusManageQuotationPiecesOptionQuote(listsIdsProductQuotation, newStatus);

        assertEquals(updatedCount, result);
    }

    @Test
    void productQuotationAdapter_updatePurchaseProductQuotation_ReturnsUpdatedCount() {

        List<Long> lsIdsProductQuotation = Arrays.asList(1L, 2L, 3L);
        boolean purchase = true;
        int updatedCount = 3;

        when(repository.updatePurchaseByIdIn(anyList(), anyBoolean()))
                .thenReturn(updatedCount);

        int result = productQuotationAdapter.updatePurchaseProductQuotation(lsIdsProductQuotation, purchase);

        assertEquals(updatedCount, result);
    }

    @Test
    void productQuotationAdapter_updateProductQuotationStatusTrueMP_ReturnsUpdatedCount() {

        List<Long> idsProductQuotation = Arrays.asList(1L, 2L, 3L);
        String status = "accepted";
        boolean purchase = true;
        int updatedCount = 3;

        when(repository.updatePurchaseByStatusAndIdIn(anyList(), anyString(), anyBoolean()))
                .thenReturn(updatedCount);

        int result = productQuotationAdapter.updateProductQuotationStatusTrueMP(idsProductQuotation, status, purchase);

        assertEquals(updatedCount, result);
    }

    @Test
    void productQuotationAdapter_updatePurchaseSubsidiary_ReturnsUpdatedCount() {

        String externalEvent = "Event001";
        List<Integer> positions = Arrays.asList(1, 2, 3);
        boolean purchaseSubsidiary = true;
        int updatedCount = 3;

        when(repository.updatePurchaseSubsidiaryByExternalEventAndPositionIn(anyString(), anyList(), anyBoolean()))
                .thenReturn(updatedCount);

        int result = productQuotationAdapter.updatePurchaseSubsidiary(externalEvent, positions, purchaseSubsidiary);

        assertEquals(updatedCount, result);
    }

    @Test
    void productQuotationAdapter_findAllWinnersByNotice_ReturnsProductQuotationList() {

        List<ProductQuotationModel> productOrderList = List.of(productQuotation1, productQuotation2);

        when(repository.findAllWinnersByNoticeId(anyLong())).thenReturn(productOrderList);

        List<ProductQuotation> result = productQuotationAdapter.findAllWinnersByNotice(noticeId);

        assertNotNull(result);
        assertEquals(productOrderList.size(), result.size());
    }

    @Test
    void productQuotationAdapter_findAllWinnersByNotice_ReturnsEmptyList() {

        List<ProductQuotationModel> productQuotationModels = Collections.emptyList();

        when(repository.findAllWinnersByNoticeId(anyLong())).thenReturn(productQuotationModels);

        List<ProductQuotation> result = productQuotationAdapter.findAllWinnersByNotice(noticeId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void productQuotationAdapter_findCounterStatusProductQuotationDTOForSubmissionManualPurchase_ReturnsCounterProductQuotationList() {

        List<Integer> positions = List.of(1, 2, 3);

        Tuple tuple1 = Mockito.mock(Tuple.class);
        Tuple tuple2 = Mockito.mock(Tuple.class);

        when(tuple1.get("position", Integer.class)).thenReturn(1);
        when(tuple1.get("auth", Boolean.class)).thenReturn(true);
        when(tuple1.get("totalProducts", Long.class)).thenReturn(10L);
        when(tuple1.get("omittedProducts", Long.class)).thenReturn(2L);
        when(tuple1.get("rejectedQuotedProducts", Long.class)).thenReturn(1L);
        when(tuple1.get("alertAndWinnerProducts", Long.class)).thenReturn(5L);
        when(tuple1.get("extraCost", Long.class)).thenReturn(100L);
        when(tuple1.get("overTime", Long.class)).thenReturn(3L);
        when(tuple1.get("maxCostPiece", Long.class)).thenReturn(50L);

        when(tuple2.get("position", Integer.class)).thenReturn(2);
        when(tuple2.get("auth", Boolean.class)).thenReturn(false);
        when(tuple2.get("totalProducts", Long.class)).thenReturn(8L);
        when(tuple2.get("omittedProducts", Long.class)).thenReturn(0L);
        when(tuple2.get("rejectedQuotedProducts", Long.class)).thenReturn(3L);
        when(tuple2.get("alertAndWinnerProducts", Long.class)).thenReturn(2L);
        when(tuple2.get("extraCost", Long.class)).thenReturn(50L);
        when(tuple2.get("overTime", Long.class)).thenReturn(1L);
        when(tuple2.get("maxCostPiece", Long.class)).thenReturn(40L);

        List<Tuple> tuples = List.of(tuple1, tuple2);

        when(repository.getProductQuotationStats(anyLong(), anyList())).thenReturn(tuples);

        List<CounterProductQuotation> result = productQuotationAdapter.findCounterStatusProductQuotationDTOForSubmissionManualPurchase(noticeId, positions);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(tuples.size(), result.size());

        CounterProductQuotation counter1 = result.get(0);
        assertEquals(1L, counter1.getPosition());
        assertTrue(counter1.getAuth());
        assertEquals(10L, counter1.getTotalProducts());
        assertEquals(2L, counter1.getOmittedProducts());
        assertEquals(1L, counter1.getRejectedQuotedProducts());
        assertEquals(5L, counter1.getAlertAndWinnerProducts());
        assertEquals(100L, counter1.getExtraCost());
        assertEquals(3L, counter1.getOverTime());
        assertEquals(50L, counter1.getMaxCostPiece());

        CounterProductQuotation counter2 = result.get(1);
        assertEquals(2L, counter2.getPosition());
        assertFalse(counter2.getAuth());
        assertEquals(8L, counter2.getTotalProducts());
        assertEquals(0L, counter2.getOmittedProducts());
        assertEquals(3L, counter2.getRejectedQuotedProducts());
        assertEquals(2L, counter2.getAlertAndWinnerProducts());
        assertEquals(50L, counter2.getExtraCost());
        assertEquals(1L, counter2.getOverTime());
        assertEquals(40L, counter2.getMaxCostPiece());
    }

    @Test
    void findCounterStatusProductQuotationDTOForSubmissionManualPurchase_ReturnsEmptyList() {

        List<Integer> positions = List.of(1, 2, 3);

        when(repository.getProductQuotationStats(anyLong(), anyList())).thenReturn(Collections.emptyList());

        List<CounterProductQuotation> result = productQuotationAdapter.findCounterStatusProductQuotationDTOForSubmissionManualPurchase(noticeId, positions);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findPiecesValuationConcessionaireAccepted() {

        when(repository.getPiecesValuationConcessionaireAccepted(anyInt())).thenReturn(piecesListAccepted);

        List<PiecesValuationDTO> result = productQuotationAdapter.findPiecesValuationConcessionaireAccepted(externalEvent);

        assertNotNull(result);
        assertEquals(piecesListAccepted.size(), result.size());
        assertEquals("S", result.get(0).getComprada());

    }

    @Test
    void findPiecesValuationConcessionaireQuoted() {

        when(repository.getPiecesValuationConcessionaireQuoted(anyInt())).thenReturn(piecesListQuoted);

        List<PiecesValuationDTO> result = productQuotationAdapter.findPiecesValuationConcessionaireQuoted(externalEvent);

        assertNotNull(result);
        assertEquals(piecesListQuoted.size(), result.size());
        assertEquals("N", result.get(0).getComprada());
    }

    @Test
    void testFindPiecesValuationMultiBrandBought() {

        when(repository.getPiecesValuationMultibrandBought(anyInt())).thenReturn(piecesListAccepted);

        List<PiecesValuationDTO> result = productQuotationAdapter.findPiecesValuationMultibrandBought(externalEvent);

        assertNotNull(result);
        assertEquals(piecesListAccepted.size(), result.size());
        assertEquals("S", result.get(0).getComprada());

    }

    @Test
    void updateSendValuationQuotation_ReturnsUpdatedRowCount() {

        List<Long> idProductQuotation = Arrays.asList(1438052L, 1438053L, 1438050L, 1438051L, 1438054L);

        boolean flag = true;
        int updatedRowCount = 5;
        when(repository.updateValuationQuotationInProductQuotation(anyList(), anyBoolean())).thenReturn(updatedRowCount);

        int result = productQuotationAdapter.updateSendValuationQuotation(idProductQuotation, flag);

        assertEquals(updatedRowCount, result);
    }

    @Test
    void updateValuationPurchaseInProductQuotation_ReturnsUpdatedRowCount() {

        List<Long> idProductQuotation = Arrays.asList(1438074L, 1438075L, 1438073L, 1438072L, 1438071L);

        boolean flag = true;
        int updatedRowCount = 5;
        when(repository.updateValuationPurchaseInProductQuotation(anyList(), anyBoolean())).thenReturn(updatedRowCount);

        int result = productQuotationAdapter.updateSendValuationPurchase(idProductQuotation, flag);

        assertEquals(updatedRowCount, result);
    }

    @Test
    void findPiecesByIdAndOverTimeOverCost_ReturnRow() {

        List<Long> idProductQuotation = Arrays.asList(1438074L, 1438075L);
        List<ProductQuotationModel> productQuotationList = List.of(productQuotation1, productQuotation2);

        when(repository.findPiecesByIdAndOverTimeOverCost(anyList())).thenReturn(productQuotationList);

        List<ProductQuotation> lsProductQuotationOverTimeOverCost = productQuotationAdapter.findPiecesByIdAndOverTimeOverCost(idProductQuotation);

        assertNotNull(lsProductQuotationOverTimeOverCost);
        assertEquals(lsProductQuotationOverTimeOverCost.size(), idProductQuotation.size());
    }

    @Test
    void findPiecesByIdAndOverTimeOverCost_ReturnsEmptyList() {
        List<Long> idProductQuotation = Arrays.asList(1438074L, 1438075L);

        when(repository.findPiecesByIdAndOverTimeOverCost(anyList())).thenReturn(Collections.emptyList());

        List<ProductQuotation> lsProductQuotationOverTimeOverCost = productQuotationAdapter.findPiecesByIdAndOverTimeOverCost(idProductQuotation);

        assertNotNull(lsProductQuotationOverTimeOverCost);
        assertTrue(lsProductQuotationOverTimeOverCost.isEmpty());
    }

    @Test
    void findPiecesByIdAndOverTime_ReturnRow() {

        List<Long> idProductQuotation = List.of(1438074L);
        List<ProductQuotationModel> productQuotationList = List.of(productQuotation1);

        when(repository.findPiecesByIdAndOverTime(anyList())).thenReturn(productQuotationList);

        List<ProductQuotation> lsProductQuotationOverTimeOverCost = productQuotationAdapter.findPiecesByIdAndOverTime(idProductQuotation);

        assertNotNull(lsProductQuotationOverTimeOverCost);
        assertEquals(lsProductQuotationOverTimeOverCost.size(), idProductQuotation.size());
    }

    @Test
    void findPiecesByIdAndOverTime_ReturnsEmptyList() {
        List<Long> idProductQuotation = List.of(1438074L);

        when(repository.findPiecesByIdAndOverTime(anyList())).thenReturn(Collections.emptyList());

        List<ProductQuotation> lsProductQuotationOverTime = productQuotationAdapter.findPiecesByIdAndOverTime(idProductQuotation);

        assertNotNull(lsProductQuotationOverTime);
        assertTrue(lsProductQuotationOverTime.isEmpty());
    }

    @Test
    void updateStatusAndPurchaseById_ReturnsUpdatedRowCount() {

        List<Long> idProductQuotation = Arrays.asList(1438074L, 1438075L, 1438073L, 1438072L, 1438071L);

        int updatedRowCount = 5;
        when(repository.updateStatusAndPurchaseById(anyList())).thenReturn(updatedRowCount);

        int result = productQuotationAdapter.updateStatusAndPurchaseById(idProductQuotation);

        assertEquals(updatedRowCount, result);
    }

    @Test
    void productQuotationAdapter_updateAllAuthByIdInAndEventId_ReturnRowCount() {

        String externalEvent = "999999999";
        List<Integer> positions = Arrays.asList(1, 2);
        int updatedRowCount = 2;

        when(repository.updateAllAuthByIdInAndEventId(true, positions, externalEvent)).thenReturn(updatedRowCount);

        int productQuotationUpdated = productQuotationAdapter.updateAllAuthByIdInAndEventId(true, positions, externalEvent);

        verify(repository, times(1)).updateAllAuthByIdInAndEventId(true, positions, externalEvent);

        assertThat(productQuotationUpdated).isPositive().isEqualTo(positions.size());

    }


    @Test
    void productQuotationAdapter_findIdsByQuotationIdAndAuthTrue_ReturnIdsList() {

        List<Long> ids = Arrays.asList(productQuotation1.getId(), productQuotation1.getId());
        Long quotationId = 999L;

        when(repository.findIdsByQuotationIdAndAuthTrue(quotationId)).thenReturn(ids);

        List<Long> pqIdsResult = productQuotationAdapter.findIdsByQuotationIdAndAuthTrue(quotationId);

        verify(repository, times(1)).findIdsByQuotationIdAndAuthTrue(quotationId);

        assertThat(pqIdsResult).isNotEmpty()
                .hasSize(ids.size())
                .containsExactlyInAnyOrderElementsOf(ids);
    }

    @Test
    void productQuotationAdapter_findAllWinnersByNoticeId_ReturnEntities() {

        List<ProductQuotationModel> productQuotationModels = Arrays.asList(productQuotation1, productQuotation1);
        Long noticeId = 999L;

        when(repository.findAllWinnersByNoticeId(noticeId)).thenReturn(productQuotationModels);

        List<ProductQuotation> productQuotationsResult = productQuotationAdapter.findAllWinnersByNotice(noticeId);

        verify(repository, times(1)).findAllWinnersByNoticeId(noticeId);

        assertThat(productQuotationsResult).isNotEmpty()
                .hasSize(productQuotationModels.size());

        try {
            for (int i = 0; i < productQuotationsResult.size(); i++) {
                AttributeAssertions.assertAttributesEqual(productQuotationModels.get(i), productQuotationsResult.get(i));
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }


    }

    @Test
    void productQuotationAdapter_updatePurchaseSubsidiaryByExternalEventAndPositionIn_ReturnRowCount() {

        String externalEvent = "999999999";
        List<Integer> positions = Arrays.asList(1, 2);
        int updatedRowCount = 2;

        when(repository.updatePurchaseSubsidiaryByExternalEventAndPositionIn(externalEvent, positions, true)).thenReturn(updatedRowCount);

        int productQuotationUpdated = productQuotationAdapter.updatePurchaseSubsidiary(externalEvent, positions, true);

        verify(repository, times(1)).updatePurchaseSubsidiaryByExternalEventAndPositionIn(externalEvent, positions, true);

        assertThat(productQuotationUpdated).isPositive().isEqualTo(positions.size());

    }

    @Test
    void productQuotationAdapter_findByEventAndPriorityQuotation_ReturnListProductQuotation() {

        String externalEvent = "999999999";

        when(repository.findAllByOptionQuote_EventAndOptionQuote_NoticeId(noticeId, externalEvent)).thenReturn(List.of(productQuotation1, productQuotation2));

        List<ProductQuotation> result = productQuotationAdapter.findByEventAndPriorityQuotation(noticeId, externalEvent);

        verify(repository, times(1)).findAllByOptionQuote_EventAndOptionQuote_NoticeId(noticeId, externalEvent);

        assertThat(result).isNotEmpty().hasSize(2);

        try {
            //Assertions.assertEquals for all entity attributes
            AttributeAssertions.assertAttributesEqual(productQuotation1, result.get(0));
            AttributeAssertions.assertAttributesEqual(productQuotation2, result.get(1));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }
    }

    @Test
    void productQuotationAdapter_findByEventAndPriorityQuotation_ReturnEmpty() {

        String externalEvent = "999999999";

        when(repository.findAllByOptionQuote_EventAndOptionQuote_NoticeId(noticeId, externalEvent)).thenReturn(List.of());

        List<ProductQuotation> result = productQuotationAdapter.findByEventAndPriorityQuotation(noticeId, externalEvent);

        verify(repository, times(1)).findAllByOptionQuote_EventAndOptionQuote_NoticeId(noticeId, externalEvent);

        assertThat(result).isEmpty();

    }
}
