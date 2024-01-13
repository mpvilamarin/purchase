package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.NoticeModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductQuotationModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.QuotationModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductQuotationRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ProductQuotationRepository productQuotationRepository;
    @Autowired
    private QuotationRepository quotationRepository;
    @Autowired
    private NoticeRepository noticeRepository;

    ProductQuotationModel productQuotation1;
    ProductQuotationModel productQuotation2;
    ProductQuotationModel productQuotation3;
    ProductQuotationModel productQuotation4;
    QuotationModel quotationModel;
    private NoticeModel noticeModel;

    List<ProductQuotationModel> lsProductQuotation = new ArrayList<>();
    List<ProductQuotationModel> lsProductQuotationOverUnCost = new ArrayList<>();

    @BeforeEach
    void setup() {

        noticeModel = NoticeModel.builder()
                .externalEvent(999999999)
                .brand("Ford")
                .auth(true)
                .model("2020")
                .claimNumber("123456789")
                .insuranceNumber(200000002L)
                .workshopType("Multimarca")
                .version("Titanium")
                .plate("AU6043")
                .cellphone("309987878897")
                .idCountry(2L)
                .vin("3hgrm4870fg601108")
                .lossIndicator(0D)
                .totalWorkforce(0D)
                .date(Timestamp.valueOf(LocalDateTime.now()))
                .workshop("taller panama")
                .line("2000CC")
                .city("PANAMA")
                .insuredValue(0D)
                .build();

        NoticeModel noticeModelSaved = noticeRepository.save(noticeModel);

        this.quotationModel = QuotationModel.builder().providerName("Provider Name").nit("123456789").quotationSubsidiaryName("Subsidiary Name")
                .replacementReference("Reference").unities(10).price(99.99).quality("High").importation(false).timeDelivery(7)
                .observations("Some observations").brand("Brand").line("Product Line").city("City").status("Status").externalEvent("999999999")
                .time(TimeZoneUtil.getTimestampByDefaultZone()).flowType("Autosuministro").noticeId(noticeModelSaved.getId()).unforeseen(true).repairOrder(BigDecimal.valueOf(123.45))
                .adiUpdated(true).dateUpdateQuotation(TimeZoneUtil.getTimestampByDefaultZone()).quotationManaged(false).quotationWinner(false).build();

        QuotationModel quotationModelSaved = quotationRepository.save(quotationModel);

        productQuotation1 = ProductQuotationModel.builder()
                .position(1).reference("ProductReference").netPrice(10.0).description("ProductDescription")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2).status("quoted")
                .discountCampaigns(1.0).discountAdditional(2.0).quotation(quotationModelSaved)
                .discountManual(3.0).comment("ProductComment").sendValuationPurchase(false).sendValuationQuotation(false).build();

        productQuotation2 = ProductQuotationModel.builder()
                .position(2).reference("ProductReference").netPrice(10.0).description("ProductDescription")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2).status("quoted")
                .discountCampaigns(1.0).discountAdditional(2.0).quotation(quotationModelSaved)
                .discountManual(3.0).comment("ProductComment").sendValuationPurchase(false).sendValuationQuotation(false).build();

        productQuotation3 = ProductQuotationModel.builder()
                .position(1).reference("ProductReference").netPrice(10.0).description("ProductDescription")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2).status("quoted")
                .discountCampaigns(1.0).discountAdditional(2.0).quotation(quotationModelSaved)
                .discountManual(3.0).comment("ProductComment").sendValuationPurchase(false).sendValuationQuotation(false)
                .extraCost(true).auth(true).build();

        productQuotation4 = ProductQuotationModel.builder()
                .position(2).reference("ProductReference").netPrice(10.0).description("ProductDescription")
                .quality("Original").amount(10).grossPrice(20.0).importer(false).deliveryTime(2).status("quoted")
                .discountCampaigns(1.0).discountAdditional(2.0).quotation(quotationModelSaved)
                .discountManual(3.0).comment("ProductComment").sendValuationPurchase(false).sendValuationQuotation(false)
                .maxDeliveryDays(true).auth(true).build();

        lsProductQuotation.add(productQuotation1);
        lsProductQuotation.add(productQuotation2);

        lsProductQuotationOverUnCost.add(productQuotation3);
        lsProductQuotationOverUnCost.add(productQuotation4);
    }


    @Test
    void getProductQuotationStats() {
        //Given-preconditions
        Long noticeId = 27915L;
        List<Integer> positions = List.of(1, 2, 3, 4);
        //when-action to do
        List<Tuple> result = productQuotationRepository.getProductQuotationStats(noticeId, positions);
        log.info(result.toString());
        //then-verify result
        assertThat(result).hasSize(4).asList().hasOnlyElementsOfType(Tuple.class);
        Tuple element1 = result.get(0);
        Tuple element2 = result.get(1);
        Tuple element3 = result.get(2);
        Tuple element4 = result.get(3);

        Assertions.assertEquals(1, element1.get("position", Integer.class));
        Assertions.assertEquals(52L, element1.get("totalProducts", Long.class));
        Assertions.assertEquals(12L, element1.get("omittedProducts", Long.class));
        Assertions.assertEquals(29L, element1.get("rejectedQuotedProducts", Long.class));
        Assertions.assertEquals(7L, element1.get("extraCost", Long.class));
        Assertions.assertEquals(4L, element1.get("overTime", Long.class));
        Assertions.assertEquals(3L, element1.get("maxCostPiece", Long.class));

        Assertions.assertEquals(2, element2.get("position", Integer.class));
        Assertions.assertEquals(52L, element2.get("totalProducts", Long.class));
        Assertions.assertEquals(15L, element2.get("omittedProducts", Long.class));
        Assertions.assertEquals(30L, element2.get("rejectedQuotedProducts", Long.class));
        Assertions.assertEquals(4L, element2.get("extraCost", Long.class));
        Assertions.assertEquals(3L, element2.get("overTime", Long.class));
        Assertions.assertEquals(2L, element2.get("maxCostPiece", Long.class));

        Assertions.assertEquals(3, element3.get("position", Integer.class));
        Assertions.assertEquals(52L, element3.get("totalProducts", Long.class));
        Assertions.assertEquals(14L, element3.get("omittedProducts", Long.class));
        Assertions.assertEquals(29L, element3.get("rejectedQuotedProducts", Long.class));
        Assertions.assertEquals(6L, element3.get("extraCost", Long.class));
        Assertions.assertEquals(3L, element3.get("overTime", Long.class));
        Assertions.assertEquals(3L, element3.get("maxCostPiece", Long.class));

        Assertions.assertEquals(4, element4.get("position", Integer.class));
        Assertions.assertEquals(47L, element4.get("totalProducts", Long.class));
        Assertions.assertEquals(14L, element4.get("omittedProducts", Long.class));
        Assertions.assertEquals(26L, element4.get("rejectedQuotedProducts", Long.class));
        Assertions.assertEquals(5L, element4.get("extraCost", Long.class));
        Assertions.assertEquals(2L, element4.get("overTime", Long.class));
        Assertions.assertEquals(2L, element4.get("maxCostPiece", Long.class));

    }

    @Test
    void findAllByOptionQuote_EventAndOptionQuote_NoticeId() {
        //Given-preconditions
        Long noticeId = 68L;
        String externalEvent = "13183";
        Long quotationId = 682L;
        //when-action to do
        List<ProductQuotationModel> result = productQuotationRepository.findAllByOptionQuote_EventAndOptionQuote_NoticeId(noticeId, externalEvent);
        log.info(result.toString());
        //then-verify result
        assertThat(result).hasSize(11).asList().hasOnlyElementsOfType(ProductQuotationModel.class);
        assertThat(result.stream().findAny()).isPresent().get().extracting("status").isEqualTo(ManageNoticeConstant.ACCEPTED.toLowerCase());
        assertThat(result.stream().findAny()).isPresent().get().extracting("quotation").extracting("id").isEqualTo(quotationId);

    }

    @Test
    void findAllWinnersByNoticeId() {
        //Given-preconditions
        Long noticeId = 12307L;

        Long quotationId = 108236L;
        //when-action to do
        List<ProductQuotationModel> result = productQuotationRepository.findAllWinnersByNoticeId(noticeId);
        log.info(result.toString());
        //then-verify result
        assertThat(result).hasSize(14).asList().hasOnlyElementsOfType(ProductQuotationModel.class);
        assertThat(result.stream().findAny()).isPresent().get().extracting("status").isEqualTo(ManageNoticeConstant.QUOTED.toLowerCase());
        assertThat(result.stream().findAny()).isPresent().get().extracting("quotation").extracting("id").isEqualTo(quotationId);

    }

    @Test
    void setStatusByExternalEventAndPositionIn() {
        //Given-preconditions
        Long noticeId = 12307L;
        //when-action to do
        int result = productQuotationRepository.updateActiveByNoticeId(
                noticeId, true);

        List<ProductQuotationModel> listUpdated = productQuotationRepository.findAllById(List.of(361320L, 361315L, 361313L));

        //then-verify result

        Assertions.assertEquals(244, result);
        Assertions.assertTrue(listUpdated.get(0).getActive());
        Assertions.assertTrue(listUpdated.get(1).getActive());
        Assertions.assertTrue(listUpdated.get(2).getActive());
    }

    @Test
    void updateStatusByExternalEventAndFlowTypeAndNoticeIdAndStatus() {
        //Given-preconditions
        Long noticeId = 28215L;
        String externalEvent = "534";
        String flowType = "Automatico";
        //when-action to do
        int result = productQuotationRepository.updateStatusByExternalEventAndFlowTypeAndNoticeIdAndStatus(
                noticeId, externalEvent, flowType, ManageNoticeConstant.OMITTED.toLowerCase(), ManageNoticeConstant.REJECTED_QUOTED);

        List<ProductQuotationModel> listUpdated = productQuotationRepository.findAllById(List.of(1412745L, 1412746L, 1412747L));

        //then-verify result

        Assertions.assertEquals(15, result);
        Assertions.assertEquals(ManageNoticeConstant.REJECTED_QUOTED, listUpdated.get(0).getStatus());
        Assertions.assertEquals(ManageNoticeConstant.REJECTED_QUOTED, listUpdated.get(1).getStatus());
        Assertions.assertEquals(ManageNoticeConstant.REJECTED_QUOTED, listUpdated.get(2).getStatus());
    }

    @Test
    void updatePurchaseByIdIn() {
        //Given-preconditions
        List<Long> ids = List.of(1L, 2L, 3L);
        //when-action to do
        int result = productQuotationRepository.updatePurchaseByIdIn(
                ids, true);

        List<ProductQuotationModel> listUpdated = productQuotationRepository.findAllById(ids);

        //then-verify result

        Assertions.assertEquals(3, result);
        Assertions.assertTrue(listUpdated.get(0).getPurchase());
        Assertions.assertTrue(listUpdated.get(1).getPurchase());
        Assertions.assertTrue(listUpdated.get(2).getPurchase());


    }

    @Test
    void updatePurchaseByStatusAndIdIn() {
        //Given-preconditions
        List<Long> ids = List.of(3L, 4L, 5L);
        //when-action to do
        int result = productQuotationRepository.updatePurchaseByStatusAndIdIn(
                ids, ManageNoticeConstant.SENT.toLowerCase(), true);

        List<ProductQuotationModel> listUpdated = productQuotationRepository.findAllById(ids);

        //then-verify result

        Assertions.assertEquals(3, result);
        Assertions.assertTrue(listUpdated.get(0).getPurchase());
        Assertions.assertTrue(listUpdated.get(1).getPurchase());
        Assertions.assertTrue(listUpdated.get(2).getPurchase());

    }

    @Test
    void updatePurchaseSubsidiaryByExternalEventAndPositionIn() {

        List<ProductQuotationModel> productQuotationSaved = productQuotationRepository.saveAll(lsProductQuotation);

        List<Integer> lsPositionPiece = new ArrayList<>();
        productQuotationSaved.forEach(productQuotation -> {
            lsPositionPiece.add(productQuotation.getPosition());

        });

        int result = productQuotationRepository.updatePurchaseSubsidiaryByExternalEventAndPositionIn(
                quotationModel.getExternalEvent(), lsPositionPiece, true);

        entityManager.refresh(productQuotationSaved.get(0));
        entityManager.refresh(productQuotationSaved.get(1));


        Assertions.assertEquals(2, result);
        Assertions.assertTrue(productQuotationSaved.get(0).isPurchaseSubsidiary());
        Assertions.assertTrue(productQuotationSaved.get(1).isPurchaseSubsidiary());

    }

    @Test
    void findPiecesValuationConcessionaireAccepted() {
        //Given-preconditions
        Integer externalEvent = 484617;

        //when-action to do
        List<PiecesValuationDTO> result = productQuotationRepository.getPiecesValuationConcessionaireAccepted(
                externalEvent);

        log.info(String.valueOf(result.size()));

        //then-verify result

        assertThat(result).hasSize(5).asList().hasOnlyElementsOfType(PiecesValuationDTO.class);
        assertThat(result.stream().findAny()).isPresent().get().extracting("comprada").isEqualTo("S");
        assertThat(result.stream().findAny()).isPresent().get().extracting("calidadRepuesto").isEqualTo("Original");

    }

    @Test
    void findPiecesValuationConcessionaireQuoted() {
        //Given-preconditions
        Integer externalEvent = 484617;

        //when-action to do
        List<PiecesValuationDTO> result = productQuotationRepository.getPiecesValuationConcessionaireQuoted(
                externalEvent);

        log.info(String.valueOf(result.size()));

        //then-verify result

        assertThat(result).hasSize(4).asList().hasOnlyElementsOfType(PiecesValuationDTO.class);
        assertThat(result.stream().findAny()).isPresent().get().extracting("comprada").isEqualTo("N");
        assertThat(result.stream().findAny()).isPresent().get().extracting("calidadRepuesto").isEqualTo("Original");

    }

    @Test
    void findPiecesValuationMultibrandBought() {
        //Given-preconditions
        Integer externalEvent = 484617;

        //when-action to do
        List<PiecesValuationDTO> result = productQuotationRepository.getPiecesValuationMultibrandBought(
                externalEvent);

        log.info(String.valueOf(result.size()));

        //then-verify result

        assertThat(result).isEmpty();

    }

    @Test
    void updateValuationQuotation() {
        //Given-preconditions
        List<Long> ids = List.of(1413776L, 1413778L, 1413773L, 1413772L);
        //when-action to do
        int result = productQuotationRepository.updateValuationQuotationInProductQuotation(
                ids, true);

        List<ProductQuotationModel> listUpdated = productQuotationRepository.findAllById(ids);

        //then-verify result

        Assertions.assertEquals(4, result);
        Assertions.assertTrue(listUpdated.get(0).getSendValuationQuotation());
        Assertions.assertTrue(listUpdated.get(1).getSendValuationQuotation());
        Assertions.assertTrue(listUpdated.get(2).getSendValuationQuotation());
        Assertions.assertTrue(listUpdated.get(3).getSendValuationQuotation());

    }

    @Test
    void updateValuationPurchase() {
        //Given-preconditions
        List<Long> ids = List.of(1413776L, 1413778L, 1413773L, 1413772L);
        //when-action to do
        int result = productQuotationRepository.updateValuationPurchaseInProductQuotation(
                ids, true);

        List<ProductQuotationModel> listUpdated = productQuotationRepository.findAllById(ids);

        //then-verify result

        Assertions.assertEquals(4, result);
        Assertions.assertTrue(listUpdated.get(0).getSendValuationPurchase());
        Assertions.assertTrue(listUpdated.get(1).getSendValuationPurchase());
        Assertions.assertTrue(listUpdated.get(2).getSendValuationPurchase());
        Assertions.assertTrue(listUpdated.get(3).getSendValuationPurchase());

    }

    @Test
    @Disabled
    void testGetPiecesValuationConcessionaireAccepted() {

        List<PiecesValuationDTO> result = productQuotationRepository.getPiecesValuationConcessionaireAccepted(492346);

        log.info(String.valueOf(result.size()));

        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals("S", result.get(0).getComprada());
        Assertions.assertEquals("S", result.get(1).getComprada());
        Assertions.assertEquals("S", result.get(2).getComprada());

    }

    @Test
    @Disabled
    void testGetPiecesValuationConcessionaireQuoted() {

        List<PiecesValuationDTO> result = productQuotationRepository.getPiecesValuationConcessionaireQuoted(492345);

        log.info(String.valueOf(result.size()));

        Assertions.assertEquals(4, result.size());
        Assertions.assertEquals("N", result.get(0).getComprada());
        Assertions.assertEquals("N", result.get(1).getComprada());
        Assertions.assertEquals("N", result.get(2).getComprada());
        Assertions.assertEquals("N", result.get(3).getComprada());

    }

    @Test
    @Disabled
    void testGetPiecesValuationMultiBrandBought() {

        List<PiecesValuationDTO> result = productQuotationRepository.getPiecesValuationMultibrandBought(484639);

        log.info(String.valueOf(result.size()));

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("S", result.get(0).getComprada());


    }


    @Test
    void testUpdateValuationPurchaseInProductQuotation_ReturnAmountOfUpdatedEntities() {

        List<ProductQuotationModel> productQuotationSaved = productQuotationRepository.saveAll(lsProductQuotation);

        List<Long> lsIdsProductQuotation = new ArrayList<>();
        productQuotationSaved.forEach(productQuotation -> {
            lsIdsProductQuotation.add(productQuotation.getId());

        });

        Integer productQuotationUpdated = productQuotationRepository.updateValuationPurchaseInProductQuotation(lsIdsProductQuotation, true);

        Assertions.assertEquals(2, productQuotationUpdated);

        List<ProductQuotationModel> result = productQuotationRepository.findAllById(lsIdsProductQuotation);

        Assertions.assertEquals(lsProductQuotation.get(0).getSendValuationPurchase(), result.get(0).getSendValuationPurchase());
        Assertions.assertEquals(lsProductQuotation.get(1).getSendValuationPurchase(), result.get(1).getSendValuationPurchase());

    }

    @Test
    void testUpdateValuationQuotationInProductQuotation_ReturnAmountOfUpdatedEntities() {

        List<ProductQuotationModel> productQuotationSaved = productQuotationRepository.saveAll(lsProductQuotation);

        List<Long> lsIdsProductQuotation = new ArrayList<>();
        productQuotationSaved.forEach(productQuotation -> {
            lsIdsProductQuotation.add(productQuotation.getId());

        });

        Integer productQuotationUpdated = productQuotationRepository.updateValuationQuotationInProductQuotation(lsIdsProductQuotation, true);

        Assertions.assertEquals(2, productQuotationUpdated);

        List<ProductQuotationModel> result = productQuotationRepository.findAllById(lsIdsProductQuotation);

        Assertions.assertEquals(lsProductQuotation.get(0).getSendValuationPurchase(), result.get(0).getSendValuationPurchase());
        Assertions.assertEquals(lsProductQuotation.get(1).getSendValuationPurchase(), result.get(1).getSendValuationPurchase());

    }


    @Test
    void testFindPiecesByIdAndOverTimeOverCost_ReturnRow() {

        List<ProductQuotationModel> productQuotationSaved = productQuotationRepository.saveAll(lsProductQuotationOverUnCost);

        List<Long> lsIdsProductQuotation = new ArrayList<>();
        productQuotationSaved.forEach(productQuotation -> lsIdsProductQuotation.add(productQuotation.getId()));

        List<ProductQuotationModel> result = productQuotationRepository.findPiecesByIdAndOverTimeOverCost(lsIdsProductQuotation);

        Assertions.assertEquals(productQuotationSaved.size(), result.size());
        Assertions.assertEquals(productQuotationSaved.get(0).getDescription(), result.get(0).getDescription());
        Assertions.assertEquals(productQuotationSaved.get(1).getAmount(), result.get(1).getAmount());
        assertThat(result.stream().findAny()).isPresent().get().extracting("extraCost").isEqualTo(true);

    }

    @Test
    @Disabled
    void testFindPiecesByIdAndOverTime_ReturnRow() {

        List<Long> lsProductQuotation = productQuotationRepository.findIdsByQuotationIdAndAuthTrue(198723L);

        List<ProductQuotationModel> result = productQuotationRepository.findPiecesByIdAndOverTime(lsProductQuotation);

        log.info("size" + result.size());
        Assertions.assertEquals(lsProductQuotation.size(), result.size());
        Assertions.assertTrue(result.get(0).isAuth());
        Assertions.assertFalse(result.get(0).getPurchase());
        Assertions.assertEquals("quoted", result.get(0).getStatus());
        Assertions.assertFalse(result.get(0).getMaxDeliveryDays());
        Assertions.assertFalse(result.get(0).getExtraCost());
    }


    @Test
    void testUpdateStatusAndPurchaseById_ReturnAmountOfUpdatedEntities() {

        List<ProductQuotationModel> productQuotationSaved = productQuotationRepository.saveAll(lsProductQuotation);

        List<Long> lsIdsProductQuotation = new ArrayList<>();
        productQuotationSaved.forEach(productQuotation -> {
            lsIdsProductQuotation.add(productQuotation.getId());

        });

        Integer productQuotationUpdated = productQuotationRepository.updateStatusAndPurchaseById(lsIdsProductQuotation);

        Assertions.assertEquals(lsIdsProductQuotation.size(), productQuotationUpdated);

        entityManager.refresh(productQuotationSaved.get(0));
        entityManager.refresh(productQuotationSaved.get(1));

        Assertions.assertEquals("accepted", productQuotationSaved.get(0).getStatus());
        Assertions.assertEquals(true, productQuotationSaved.get(0).getPurchase());
        Assertions.assertEquals("accepted", productQuotationSaved.get(1).getStatus());
        Assertions.assertEquals(true, productQuotationSaved.get(1).getPurchase());

    }

    @Test
    void productQuotationRepository_updateAllAuthByIdInAndEventId_ReturnRowCount() {

        List<ProductQuotationModel> productQuotationSaved = productQuotationRepository.saveAll(lsProductQuotation);

        List<Integer> positions = productQuotationSaved.stream()
                .map(ProductQuotationModel::getPosition)
                .toList();

        int productQuotationUpdated = productQuotationRepository.updateAllAuthByIdInAndEventId(Boolean.TRUE, positions, quotationModel.getExternalEvent());

        assertThat(productQuotationUpdated).isPositive().isEqualTo(productQuotationSaved.size());

        entityManager.refresh(productQuotationSaved.get(0));
        entityManager.refresh(productQuotationSaved.get(1));

        Assertions.assertTrue(productQuotationSaved.get(0).isAuth());
        Assertions.assertTrue(productQuotationSaved.get(1).isAuth());

    }


    @Test
    void productQuotationRepository_findIdsByQuotationIdAndAuthTrue_ReturnIdsList() {
        lsProductQuotation.forEach(p -> p.setAuth(true));

        List<ProductQuotationModel> productQuotationSaved = productQuotationRepository.saveAll(lsProductQuotation);

        List<Long> idsSaved = productQuotationSaved.stream().map(ProductQuotationModel::getId).collect(Collectors.toList());

        List<Long> pqIds = productQuotationRepository.findIdsByQuotationIdAndAuthTrue(quotationModel.getId());

        assertThat(pqIds).isNotEmpty()
                .hasSize(idsSaved.size())
                .containsExactlyInAnyOrderElementsOf(idsSaved);
    }

    @Test
    void productQuotationRepository_findAllWinnersByNoticeId_ReturnEntities() {

        quotationModel.setFlowType("Manual");

        lsProductQuotation.forEach(p -> p.setAuth(Boolean.TRUE).setWinner(Boolean.TRUE));

        List<ProductQuotationModel> productQuotationSaved = productQuotationRepository.saveAll(lsProductQuotation);

        List<ProductQuotationModel> productQuotationResults = productQuotationRepository.findAllWinnersByNoticeId(quotationModel.getNoticeId());

        assertThat(productQuotationResults).isNotEmpty()
                .hasSize(productQuotationSaved.size())
                .containsExactlyInAnyOrderElementsOf(productQuotationSaved);

    }

    @Test
    void productQuotationRepository_updatePurchaseSubsidiaryByExternalEventAndPositionIn_ReturnRowCount() {

        List<ProductQuotationModel> productQuotationSaved = productQuotationRepository.saveAll(lsProductQuotation);

        List<Integer> positions = productQuotationSaved.stream()
                .map(ProductQuotationModel::getPosition)
                .toList();

        int productQuotationUpdated = productQuotationRepository.updatePurchaseSubsidiaryByExternalEventAndPositionIn(quotationModel.getExternalEvent(),
                positions, Boolean.TRUE);

        assertThat(productQuotationUpdated).isPositive().isEqualTo(productQuotationSaved.size());

        entityManager.refresh(productQuotationSaved.get(0));
        entityManager.refresh(productQuotationSaved.get(1));

        Assertions.assertTrue(productQuotationSaved.get(0).isPurchaseSubsidiary());
        Assertions.assertTrue(productQuotationSaved.get(1).isPurchaseSubsidiary());

    }

}
