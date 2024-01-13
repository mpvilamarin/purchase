package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.common.TestConstants;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOrderModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductOrderRepositoryTest {
    @Autowired
    private ProductOrderRepository productOrderRepository;
    private ProductOrderModel productOrderModel;
    @BeforeEach
    void setup(){
        productOrderModel = ProductOrderModel.builder()
                .amount(1).grossPrice(100D).guide("this is a guide")
                .importer(false).comment("this is a commet")
                .order(new OrderModel().setId(1L)).status(ManageNoticeConstant.SENT)
                .description("bomber delantero")
                .quality("ORIGINAL")
                .build();
    }



    @Test
    void saveProductOrderModel() {
        //Given-preconditions

        //when-action to do
        ProductOrderModel result= productOrderRepository.save(productOrderModel);
        //then-verify result
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(productOrderModel.getComment(), result.getComment());
        Assertions.assertEquals(productOrderModel.getDescription(), result.getDescription());
        Assertions.assertEquals(productOrderModel.getImporter(), result.getImporter());
        Assertions.assertEquals(productOrderModel.getStatus(), result.getStatus());
        Assertions.assertEquals(productOrderModel.getAmount(), result.getAmount());

    }

    @Test
    void findProductOrderById() {
        //Given-preconditions

        ProductOrderModel productOrderModelSave= productOrderRepository.save(productOrderModel);

        //when-action to do
        Optional<ProductOrderModel> result= productOrderRepository.findById(productOrderModelSave.getId());

        //then-verify result
        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(ProductOrderModel.class);
        Assertions.assertEquals(productOrderModelSave, result.get());

    }

    @Test
    void updateProductOrder() {
        //Given-preconditions
        ProductOrderModel productOrderModelSave= productOrderRepository.save(productOrderModel);
        productOrderModelSave.setQuality("USED").setStatus(ManageNoticeConstant.ACCEPTED).setDescription("Bomper delantero rojo");
        //when-action to do
        ProductOrderModel productOrderModelUpdate= productOrderRepository.save(productOrderModelSave);

        //then-verify result
        Assertions.assertEquals(productOrderModelSave.getQuality(), productOrderModelUpdate.getQuality());
        Assertions.assertEquals(productOrderModelSave.getStatus(), productOrderModelUpdate.getStatus());
        Assertions.assertEquals(productOrderModelSave.getDescription(), productOrderModelUpdate.getDescription());

    }
    @Test
    void deleteProductOrder() {
        //Given-preconditions
        ProductOrderModel productOrderModelSave= productOrderRepository.save(productOrderModel);

        //when-action to do
        productOrderRepository.deleteById(productOrderModelSave.getId());

        Optional<ProductOrderModel> result= productOrderRepository.findById(productOrderModelSave.getId());
        //then-verify result
        assertThat(productOrderModelSave.getId()).isPositive();
        assertThat(result).isNotPresent();

    }

    @Test
    @Disabled(TestConstants.SKIP_DATABASE_DEPENDENCE)
    void testCountProductOrderByExternalEventAndPosition_ReturnCount() {

        int externalEvent = 435800;
        List<Integer> positions = Arrays.asList(1, 2, 3);

        Long result= productOrderRepository.countProductOrderByExternalEventAndPosition(externalEvent,positions);

        assertNotNull(result);
        assertThat(result).isPositive().isEqualTo(3);

    }

}
