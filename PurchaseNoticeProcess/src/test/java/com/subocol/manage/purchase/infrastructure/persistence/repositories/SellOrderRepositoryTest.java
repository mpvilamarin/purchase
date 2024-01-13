package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.infrastructure.persistence.dtos.SellOrderValuesDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.NoticeModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SellOrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SubsidiaryModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SellOrderRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    private final SellOrderRepository repository;

    private final OrderRepository orderRepository;

    private SellOrderModel sellOrderModel;
    private OrderModel orderModel;

    @Autowired
    SellOrderRepositoryTest(SellOrderRepository repository, OrderRepository orderRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
    }

    @BeforeEach
    void setup() {

        this.sellOrderModel = SellOrderModel.builder()
                .creationDate(TimeZoneUtil.getTimestampByDefaultZone())
                .lastUpdateDate(TimeZoneUtil.getTimestampByDefaultZone())
                .subtotal(1780.0)
                .total(2064.8)
                .iva(16.0)
                .pdfUrl("Path PDF")
                .order(new OrderModel().setId(1L))
                .details(new HashSet<>())
                .build();

        orderModel = OrderModel.builder()
                .id(1L)
                .orderIdDms(2323)
                .billingServiceId(2323)
                .comment("this is a order")
                .documentUrl("myOrder.pdf")
                .priority(1)
                .reference("TODAYORDER")
                .status(ManageNoticeConstant.SENT)
                .date(Timestamp.valueOf(LocalDateTime.now()))
                .workshop("taller panama")
                .notice(new NoticeModel().setId(1L))
                .subsidiary(new SubsidiaryModel().setId(1L)).build();

    }

    @Test
    void SellOrderRepository_save_ReturnSavedSellOrder() {

        SellOrderModel sellOrderSaved = repository.save(sellOrderModel);

        assertNotNull(sellOrderSaved);
        assertThat(sellOrderSaved.getId()).isPositive();
        Assertions.assertEquals(sellOrderModel.getCreationDate(), sellOrderSaved.getCreationDate());
        Assertions.assertEquals(sellOrderModel.getLastUpdateDate(), sellOrderSaved.getLastUpdateDate());
        Assertions.assertEquals(sellOrderModel.getSubtotal(), sellOrderSaved.getSubtotal(), 0.01);
        Assertions.assertEquals(sellOrderModel.getTotal(), sellOrderSaved.getTotal(), 0.01);
        Assertions.assertEquals(sellOrderModel.getIva(), sellOrderSaved.getIva(), 0.01);
        Assertions.assertEquals(sellOrderModel.getPdfUrl(), sellOrderSaved.getPdfUrl());
        assertNotNull(sellOrderSaved.getOrder());
        Assertions.assertEquals(sellOrderModel.getOrder().getId(), sellOrderSaved.getOrder().getId());

    }

    @Test
    void SellOrderRepository_findById_ReturnSellOrder() {

        SellOrderModel sellOrderSaved = repository.save(sellOrderModel);
        Optional<SellOrderModel> result = repository.findById(sellOrderSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(SellOrderModel.class);
        Assertions.assertEquals(sellOrderSaved, result.get());

    }

    @Test
    void SellOrderRepository_update_ReturnUpdatedSellOrder() {

        SellOrderModel sellOrderSaved = repository.save(sellOrderModel);

        sellOrderSaved.setPdfUrl("Updated PDF  Path")
                .setLastUpdateDate(TimeZoneUtil.getTimestampByDefaultZone());

        SellOrderModel sellOrderUpdated = repository.save(sellOrderSaved);

        Assertions.assertEquals(sellOrderSaved.getId(), sellOrderUpdated.getId());
        Assertions.assertEquals(sellOrderSaved.getPdfUrl(), sellOrderUpdated.getPdfUrl());
        Assertions.assertEquals(sellOrderSaved.getLastUpdateDate(), sellOrderUpdated.getLastUpdateDate());
    }

    @Test
    void SellOrderRepository_delete() {

        SellOrderModel sellOrderSaved = repository.save(sellOrderModel);

        repository.deleteById(sellOrderSaved.getId());

        Optional<SellOrderModel> result = repository.findById(sellOrderSaved.getId());

        assertThat(sellOrderSaved.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

    @Test
    void SellOrderRepository_findByOrder_Id_ReturnSellOrder() {


        OrderModel orderSaved = orderRepository.save(orderModel);

        sellOrderModel.setOrder(orderSaved);

        SellOrderModel sellOrderSaved = repository.save(sellOrderModel);

        Optional<SellOrderModel> result = repository.findByOrder_Id(sellOrderModel.getOrder().getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(SellOrderModel.class);
        Assertions.assertEquals(sellOrderSaved, result.get());

    }
    @Test
    void findSellOrderValuesDTOByOrderId() {
        SellOrderModel sellOrderModel1=sellOrderModel;
        Long orderId=1234567L;
        sellOrderModel1.setOrder(new OrderModel().setId(orderId));

        repository.save(sellOrderModel1);

        SellOrderValuesDTO result=repository.findSellOrderValuesDTOByOrderId(orderId);

        assertNotNull(result);
        Assertions.assertEquals(sellOrderModel.getSubtotal(), result.getSubTotal(), 0.01);
        Assertions.assertEquals(sellOrderModel.getTotal(), result.getTotal(), 0.01);

    }
    @Test
    void findUpdateSubtotalAndTotalAndIvaById() {

        Double subTotal=80D;
        Double total=100D;
        Double iva=20D;

        sellOrderModel.setIva(0D).setSubtotal(0D).setTotal(0D);
        OrderModel orderSaved = orderRepository.save(orderModel);
        sellOrderModel.setOrder(orderSaved);
        sellOrderModel=repository.save(sellOrderModel);
        entityManager.detach(sellOrderModel);
        log.info(sellOrderModel.getId().toString());
        int result=repository.updateSubtotalAndTotalAndIvaById(sellOrderModel.getId(), subTotal, total, iva);

        Optional<SellOrderModel> sellOrderModelUpdate = repository.findByOrder_Id(sellOrderModel.getOrder().getId());

//        Optional<SellOrderModel> sellOrderModelUpdate=repository.findById(sellOrderModel.getId());
        assertThat(result).isPositive();
        assertThat(sellOrderModelUpdate).isPresent();
        Assertions.assertEquals(subTotal, sellOrderModelUpdate.get().getSubtotal());
        Assertions.assertEquals(total, sellOrderModelUpdate.get().getTotal());
        Assertions.assertEquals(iva, sellOrderModelUpdate.get().getIva());

    }
}