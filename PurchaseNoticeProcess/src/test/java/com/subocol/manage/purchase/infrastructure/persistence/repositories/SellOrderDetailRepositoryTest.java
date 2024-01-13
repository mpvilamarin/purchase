package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SellOrderDetailModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SellOrderModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SellOrderDetailRepositoryTest {

    private final SellOrderDetailRepository repository;

    private SellOrderDetailModel sellOrderDetailModel;

    @Autowired
    SellOrderDetailRepositoryTest(SellOrderDetailRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setup() {

        this.sellOrderDetailModel = SellOrderDetailModel.builder()
                .reference("Sin Referencia")
                .description("Product Description")
                .unitPrice(10.99)
                .amount(5)
                .total(54.95)
                .grossPrice(50.0)
                .discount(4.95)
                .promiseDelivery(TimeZoneUtil.getTimestampByDefaultZone())
                .comment("Order comment")
                .positionPiece(1)
                .sellOrder(new SellOrderModel().setId(1L))
                .build();

    }

    @Test
    void SellOrderDetailRepository_save_ReturnSavedSellOrderDetail() {

        SellOrderDetailModel sellOrderDetailSaved = repository.save(sellOrderDetailModel);

        assertNotNull(sellOrderDetailSaved);
        assertThat(sellOrderDetailSaved.getId()).isPositive();
        Assertions.assertEquals(sellOrderDetailModel.getReference(), sellOrderDetailSaved.getReference());
        Assertions.assertEquals(sellOrderDetailModel.getDescription(), sellOrderDetailSaved.getDescription());
        Assertions.assertEquals(sellOrderDetailModel.getUnitPrice(), sellOrderDetailSaved.getUnitPrice(), 0.01);
        Assertions.assertEquals(sellOrderDetailModel.getAmount(), sellOrderDetailSaved.getAmount());
        Assertions.assertEquals(sellOrderDetailModel.getTotal(), sellOrderDetailSaved.getTotal(), 0.01);
        Assertions.assertEquals(sellOrderDetailModel.getGrossPrice(), sellOrderDetailSaved.getGrossPrice(), 0.01);
        Assertions.assertEquals(sellOrderDetailModel.getDiscount(), sellOrderDetailSaved.getDiscount(), 0.01);
        Assertions.assertNotNull(sellOrderDetailModel.getPromiseDelivery());
        Assertions.assertEquals(sellOrderDetailModel.getComment(), sellOrderDetailSaved.getComment());
        Assertions.assertEquals(sellOrderDetailModel.getPositionPiece(), sellOrderDetailSaved.getPositionPiece());
        assertNotNull(sellOrderDetailSaved.getSellOrder());
        Assertions.assertEquals(sellOrderDetailModel.getSellOrder().getId(), sellOrderDetailSaved.getSellOrder().getId());

    }

    @Test
    void SellOrderDetailRepository_findById_ReturnSellOrderDetail() {

        SellOrderDetailModel sellOrderDetailSaved = repository.save(sellOrderDetailModel);
        Optional<SellOrderDetailModel> result = repository.findById(sellOrderDetailSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(SellOrderDetailModel.class);
        Assertions.assertEquals(sellOrderDetailSaved, result.get());

    }

    @Test
    void SellOrderDetailRepository_update_ReturnUpdatedSellOrderDetail() {

        SellOrderDetailModel sellOrderDetailSaved = repository.save(sellOrderDetailModel);

        sellOrderDetailSaved.setAmount(1)
                .setComment("In Sell Order updated comment");

        SellOrderDetailModel sellOrderDetailUpdated = repository.save(sellOrderDetailSaved);

        Assertions.assertEquals(sellOrderDetailSaved.getId(), sellOrderDetailUpdated.getId());
        Assertions.assertEquals(sellOrderDetailSaved.getAmount(), sellOrderDetailUpdated.getAmount());
        Assertions.assertEquals(sellOrderDetailSaved.getComment(), sellOrderDetailUpdated.getComment());
    }

    @Test
    void SellOrderDetailRepository_delete() {

        SellOrderDetailModel sellOrderDetailSaved = repository.save(sellOrderDetailModel);

        repository.deleteById(sellOrderDetailSaved.getId());

        Optional<SellOrderDetailModel> result = repository.findById(sellOrderDetailSaved.getId());

        assertThat(sellOrderDetailSaved.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

}