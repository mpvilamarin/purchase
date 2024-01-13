package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOverrunCostModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductQuotationModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.QuotationModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductOverrunCostRepositoryTest {
    @Autowired
    private ProductOverrunCostRepository productOverrunCostRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private QuotationRepository quotationRepository;
    ProductOverrunCostModel productOverrunCostModel1;
    ProductOverrunCostModel productOverrunCostModel2;
    ProductQuotationModel productQuotation1;
    ProductQuotationModel productQuotation2;
    QuotationModel quotationModel;
    List<ProductOverrunCostModel> lsProductOverrunCostModel = new ArrayList<>();

    @BeforeEach
    void setup(){

        this.quotationModel = QuotationModel.builder().providerName("Provider Name").nit("123456789").quotationSubsidiaryName("Subsidiary Name")
                .replacementReference("Reference").unities(10).price(99.99).quality("High").importation(false).timeDelivery(7)
                .observations("Some observations").brand("Brand").line("Product Line").city("City").status("Status").externalEvent("External Event")
                .time(TimeZoneUtil.getTimestampByDefaultZone()).flowType("Autosuministro").noticeId(1L).unforeseen(true).repairOrder(BigDecimal.valueOf(123.45))
                .adiUpdated(true).dateUpdateQuotation(TimeZoneUtil.getTimestampByDefaultZone()).quotationManaged(false).quotationWinner(false).build();

        QuotationModel quotationModelSaved = quotationRepository.save(quotationModel);

        productOverrunCostModel1 = ProductOverrunCostModel.builder()
                .grossPrice(100D).brand("FORD").externalEvent("987654321")
                .importer(false).comment("")
                .status(ManageNoticeConstant.SENT).extraCost(false)
                .description("bomber delantero").plate("OWN999")
                .quality("ORIGINAL").quotation(quotationModelSaved)
                .pieceId(2L)
                .maxDeliveryDays(false)
                .build();

        productOverrunCostModel2 = ProductOverrunCostModel.builder()
                .grossPrice(100D).brand("FORD").externalEvent("987654321")
                .importer(false).comment("")
                .status(ManageNoticeConstant.SENT).extraCost(false)
                .description("tapa motor").plate("OWN999")
                .quality("ORIGINAL").quotation(quotationModelSaved)
                .pieceId(1L)
                .maxDeliveryDays(false)
                .build();


        lsProductOverrunCostModel.add(productOverrunCostModel1);
        lsProductOverrunCostModel.add(productOverrunCostModel2);
    }



    @Test
    void saveProductOverrunCostModel() {
        //Given-preconditions

        //when-action to do
        ProductOverrunCostModel result= productOverrunCostRepository.save(productOverrunCostModel1);
        //then-verify result
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(productOverrunCostModel1.getComment(), result.getComment());
        Assertions.assertEquals(productOverrunCostModel1.getDescription(), result.getDescription());
        Assertions.assertEquals(productOverrunCostModel1.getExternalEvent(), result.getExternalEvent());
        Assertions.assertEquals(productOverrunCostModel1.getStatus(), result.getStatus());
        Assertions.assertEquals(productOverrunCostModel1.getExtraCost(), result.getExtraCost());

    }

    @Test
    void findProductOverrunCostById() {
        //Given-preconditions

        ProductOverrunCostModel productOverrunCostModelSave= productOverrunCostRepository.save(productOverrunCostModel1);

        //when-action to do
        Optional<ProductOverrunCostModel> result= productOverrunCostRepository.findById(productOverrunCostModelSave.getId());

        //then-verify result
        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(ProductOverrunCostModel.class);
        Assertions.assertEquals(productOverrunCostModelSave, result.get());

    }

    @Test
    void updateProductOverrunCost() {
        //Given-preconditions
        ProductOverrunCostModel productOverrunCostModelSave= productOverrunCostRepository.save(productOverrunCostModel1);
        productOverrunCostModelSave.setQuality("USED").setStatus(ManageNoticeConstant.ACCEPTED).setDescription("Bomper delantero rojo");
        //when-action to do
        ProductOverrunCostModel productOverrunCostModelUpdate= productOverrunCostRepository.save(productOverrunCostModelSave);

        //then-verify result
        Assertions.assertEquals(productOverrunCostModelSave.getQuality(), productOverrunCostModelUpdate.getQuality());
        Assertions.assertEquals(productOverrunCostModelSave.getStatus(), productOverrunCostModelUpdate.getStatus());
        Assertions.assertEquals(productOverrunCostModelSave.getDescription(), productOverrunCostModelUpdate.getDescription());

    }
    @Test
    void deleteProductOverrunCost() {
        //Given-preconditions
        ProductOverrunCostModel productOverrunCostModelSave= productOverrunCostRepository.save(productOverrunCostModel1);

        //when-action to do
        productOverrunCostRepository.deleteById(productOverrunCostModelSave.getId());

        Optional<ProductOverrunCostModel> result= productOverrunCostRepository.findById(productOverrunCostModelSave.getId());
        //then-verify result
        assertThat(productOverrunCostModelSave.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

    @Test
    void findAllByPieceId() {

        ProductOverrunCostModel productOverrunCostModelSave= productOverrunCostRepository.save(productOverrunCostModel1);

        productOverrunCostRepository.findAllByPieceId(productOverrunCostModelSave.getId());

        Optional<ProductOverrunCostModel> result= productOverrunCostRepository.findById(productOverrunCostModelSave.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(ProductOverrunCostModel.class);
        Assertions.assertEquals(productOverrunCostModelSave, result.get());


    }

    @Test
    void testUpdatePurchaseByStatusAndIdIn_ReturnAmountOfUpdatedEntities() {

        List<ProductOverrunCostModel> productOverrunCostModelSaved = productOverrunCostRepository.saveAll(lsProductOverrunCostModel);

        List<Long> lsIdsProductOverrunCost = new ArrayList<>();
        productOverrunCostModelSaved.forEach(productOverrunCost -> {
            lsIdsProductOverrunCost.add(productOverrunCost.getPieceId());

        });

        Integer ProductOverrunCostUpdated = productOverrunCostRepository.updatePurchaseByStatusAndIdIn(lsIdsProductOverrunCost);

        Assertions.assertEquals(lsIdsProductOverrunCost.size(), ProductOverrunCostUpdated);

        entityManager.refresh(productOverrunCostModelSaved.get(0));
        entityManager.refresh(productOverrunCostModelSaved.get(1));

        Assertions.assertEquals("accepted", productOverrunCostModelSaved.get(0).getStatus());
        Assertions.assertEquals("accepted", productOverrunCostModelSaved.get(1).getStatus());

    }

}
