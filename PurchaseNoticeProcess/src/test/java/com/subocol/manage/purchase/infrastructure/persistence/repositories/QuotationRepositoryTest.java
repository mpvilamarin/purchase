package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.common.TestConstants;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterStatusProductQuotation;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuotationRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ProductQuotationRepository productQuotationRepository;
    private QuotationRepository repository;

    private QuotationModel quotationModel;

    ProductQuotationModel productQuotation1;
    ProductQuotationModel productQuotation2;
    QuotationModel quotationModelSaved;
    List<ProductQuotationModel> lsProductQuotation = new ArrayList<>();
    @Autowired
    QuotationRepositoryTest(QuotationRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setup() {

        this.quotationModel = QuotationModel.builder().providerName("Provider Name").nit("123456789").quotationSubsidiaryName("Subsidiary Name")
                .replacementReference("Reference").unities(10).price(99.99).quality("High").importation(false).timeDelivery(7)
                .observations("Some observations").brand("Brand").line("Product Line").city("City").status("Status")
                .externalEvent("External Event").time(TimeZoneUtil.getTimestampByDefaultZone()).flowType("Autosuministro").noticeId(1L)
                .unforeseen(true).repairOrder(BigDecimal.valueOf(123.45)).adiUpdated(true).dateUpdateQuotation(TimeZoneUtil.getTimestampByDefaultZone())
                .quotationManaged(false).quotationWinner(false).build();

        quotationModelSaved = repository.save(quotationModel);

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

        lsProductQuotation.add(productQuotation1);
        lsProductQuotation.add(productQuotation2);
    }

    @Test
    void QuotationRepository_save_ReturnSavedQuotation() {

        QuotationModel quotationSaved = repository.save(quotationModel);

        assertNotNull(quotationSaved);
        assertThat(quotationSaved.getId()).isPositive();
        Assertions.assertEquals(quotationModel.getProviderName(), quotationSaved.getProviderName());
        Assertions.assertEquals(quotationModel.getNit(), quotationSaved.getNit());
        Assertions.assertEquals(quotationModel.getQuotationSubsidiaryName(), quotationSaved.getQuotationSubsidiaryName());
        Assertions.assertEquals(quotationModel.getReplacementReference(), quotationSaved.getReplacementReference());
        Assertions.assertEquals(quotationModel.getQuality(), quotationSaved.getQuality());
        Assertions.assertEquals(quotationModel.getObservations(), quotationSaved.getObservations());
        Assertions.assertEquals(quotationModel.getBrand(), quotationSaved.getBrand());
        Assertions.assertEquals(quotationModel.getLine(), quotationSaved.getLine());
        Assertions.assertEquals(quotationModel.getCity(), quotationSaved.getCity());
        Assertions.assertEquals(quotationModel.getStatus(), quotationSaved.getStatus());
        Assertions.assertEquals(quotationModel.getExternalEvent(), quotationSaved.getExternalEvent());
        Assertions.assertEquals(quotationModel.getFlowType(), quotationSaved.getFlowType());
        Assertions.assertEquals(quotationModel.getUnities(), quotationSaved.getUnities());
        Assertions.assertEquals(quotationModel.getTimeDelivery(), quotationSaved.getTimeDelivery());
        Assertions.assertEquals(quotationModel.getPrice(), quotationSaved.getPrice(), 0.01);
        Assertions.assertEquals(quotationModel.getImportation(), quotationSaved.getImportation());
        Assertions.assertEquals(quotationModel.getUnforeseen(), quotationSaved.getUnforeseen());
        Assertions.assertEquals(quotationModel.isAdiUpdated(), quotationSaved.isAdiUpdated());
        Assertions.assertEquals(quotationModel.isQuotationManaged(), quotationSaved.isQuotationManaged());
        Assertions.assertEquals(quotationModel.isQuotationWinner(), quotationSaved.isQuotationWinner());
        Assertions.assertEquals(quotationModel.getNoticeId(), quotationSaved.getNoticeId());
        Assertions.assertEquals(quotationModel.getRepairOrder(), quotationSaved.getRepairOrder());
        Assertions.assertEquals(quotationModel.getTime(), quotationSaved.getTime());
        Assertions.assertEquals(quotationModel.getDateUpdateQuotation(), quotationSaved.getDateUpdateQuotation());
        Assertions.assertEquals(quotationModel.getProductQuotations(), quotationSaved.getProductQuotations());
        Assertions.assertEquals(quotationModel.getProductOverrunCosts(), quotationSaved.getProductOverrunCosts());
        Assertions.assertEquals(quotationModel.getOrders(), quotationSaved.getOrders());
        Assertions.assertEquals(quotationModel.getSubsidiary(), quotationSaved.getSubsidiary());

    }


    @Test
    void QuotationRepository_findById_ReturnQuotation() {

        QuotationModel quotationSaved = repository.save(quotationModel);
        Optional<QuotationModel> result = repository.findById(quotationSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(QuotationModel.class);
        Assertions.assertEquals(quotationSaved, result.get());

    }

    @Test
    void QuotationRepository_update_ReturnUpdatedQuotation() {

        QuotationModel quotationSaved = repository.save(quotationModel);

        quotationSaved.setCity("Medellin")
                .setBrand("Mercedes");

        QuotationModel quotationUpdated = repository.save(quotationSaved);

        Assertions.assertEquals(quotationSaved.getId(), quotationUpdated.getId());
        Assertions.assertEquals(quotationSaved.getCity(), quotationUpdated.getCity());
        Assertions.assertEquals(quotationSaved.getBrand(), quotationUpdated.getBrand());
    }

    @Test
    void QuotationRepository_delete() {

        QuotationModel quotationSaved = repository.save(quotationModel);

        repository.deleteById(quotationSaved.getId());

        Optional<QuotationModel> result = repository.findById(quotationSaved.getId());

        assertThat(quotationSaved.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

    @Test
    void QuotationRepository_getCounterStatusProductQuotation_ReturnListOfCounterStatusProductQuotation() {
        Long noticeId = 22923L;

        List<CounterStatusProductQuotation> result = repository.getCounterStatusProductQuotation(noticeId);

        assertThat(result).isNotNull();
        assertThat(result.get(0)).isNotNull();
        assertThat(result.get(0).getId()).isPositive();
        assertThat(result.get(0).getTotalProducts()).isPositive();

        Optional<QuotationModel> quotationModelOptional = repository.findById(result.get(0).getId());
        assertThat(quotationModelOptional).isPresent();

    }

    @Test
    void QuotationRepository_updateQuotationStatusById_ReturnAmountOfUpdatedEntities() {

        QuotationModel quotationSaved = repository.save(quotationModel);
        quotationSaved.setStatus("bought");
        Integer quotationsUpdated = repository.updateQuotationStatusById("bought", quotationSaved.getId());

        Assertions.assertEquals(1, quotationsUpdated);

        Optional<QuotationModel> result = repository.findById(quotationSaved.getId());

        assertThat(quotationSaved.getId()).isPositive();
        assertThat(result).isPresent();
        Assertions.assertEquals(quotationModel.getStatus(), result.get().getStatus());

    }

    @Test
    void findQuotationWithAllProductManage() {
        //Given-preconditions
        Long quotationId = 197187L;
        //when-action to do
        List<Tuple> result = repository.findQuotationWithAllProductManage(quotationId);


        log.info(result.toString());
        //then-verify result

        //Assertions.assertEquals(4, result);
        //Assertions.assertTrue( listUpdated.get(0).getSendValuationPurchase());
        //Assertions.assertTrue( listUpdated.get(1).getSendValuationPurchase());
        //Assertions.assertTrue( listUpdated.get(2).getSendValuationPurchase());
        //Assertions.assertTrue( listUpdated.get(3).getSendValuationPurchase());

    }

    @Test
    void QuotationRepository_updateQuotationManaged_ReturnUpdateCountAndChangeQM() {

        QuotationModel quotationSaved = repository.save(quotationModel);

        int count = repository.updateQuotationManaged(Collections.singletonList(quotationSaved.getId()));

        assertThat(count).isEqualTo(1);

        entityManager.refresh(quotationSaved);

        assertThat(quotationSaved.getId()).isPositive();
        Assertions.assertEquals(Boolean.TRUE, quotationSaved.isQuotationManaged());

    }

    @Test
    @Disabled(TestConstants.SKIP_DATABASE_DEPENDENCE)
    void QuotationRepository_findQuotationManagedByExternalEvent_ReturnEntityId() {

        String externalEvent = "948933462";
        Long quotationId = 198407L;

        Optional<QuotationModel> optional = repository.findById(quotationId);

        assertThat(optional).isPresent();
        optional.get().setQuotationManaged(Boolean.FALSE);

        List<Tuple> result = repository.findQuotationManagedByExternalEvent(externalEvent);

        assertThat(result).hasSize(1).asList().hasOnlyElementsOfType(Tuple.class);

        Assertions.assertEquals(quotationId, result.get(0).get("id", Long.class));

    }

    @Test
    void QuotationRepository_findQuotationByNoticeIdAndFlowType_ReturnQuotation() {

        QuotationModel quotationSaved = repository.save(quotationModel);

        Optional<QuotationModel> result = repository.findQuotationByNoticeIdAndFlowType(quotationSaved.getNoticeId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(QuotationModel.class);
        Assertions.assertEquals(quotationSaved, result.get());
        Assertions.assertEquals(quotationSaved.getNoticeId(), result.get().getNoticeId());

    }

    @Test
    void testFindQuotationWithAllProductManage_ReturnTuple() {
        List<ProductQuotationModel> productQuotationSaved = productQuotationRepository.saveAll(lsProductQuotation);

        List<Tuple> result = repository.findQuotationWithAllProductManage(quotationModelSaved.getId());

        assertNotNull(result);
        assertThat(result).hasSize(1).asList().hasOnlyElementsOfType(Tuple.class);
        Assertions.assertEquals(quotationModel.getId(), result.get(0).get(0, Long.class));

    }
    @Test
    void testUpdateQuotationManaged_ReturnRowUpdated() {

        List<ProductQuotationModel> productQuotationSaved = productQuotationRepository.saveAll(lsProductQuotation);

        List<Long> lsIdsQuotation = new ArrayList<>();
        List.of(quotationModel).forEach(quotation -> {
            lsIdsQuotation.add(quotation.getId());

        });

        Integer quotationUpdated = repository.updateQuotationManaged(lsIdsQuotation);

        Assertions.assertEquals(lsIdsQuotation.size(), quotationUpdated);

        entityManager.refresh(quotationModelSaved);

        Assertions.assertTrue(quotationModel.isQuotationManaged());

    }

}