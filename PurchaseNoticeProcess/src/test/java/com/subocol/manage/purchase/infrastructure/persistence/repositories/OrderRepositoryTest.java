package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.infrastructure.persistence.entities.NoticeModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SubsidiaryModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    private OrderModel orderModel;
    @BeforeEach
    void setup(){
        orderModel = OrderModel.builder().orderIdDms(2323).billingServiceId(2323)
                .comment("this is a order").documentUrl("myOrder.pdf").priority(1)
                .reference("TODAYORDER").status(ManageNoticeConstant.SENT)
                .date(Timestamp.valueOf(LocalDateTime.now()))
                .workshop("taller panama").notice(new NoticeModel().setId(1L))
                .subsidiary(new SubsidiaryModel().setId(1L)).build();
    }



    @Test
    void saveOrderModel() {
        //Given-preconditions

        //when-action to do
        OrderModel result= orderRepository.save(orderModel);
        //then-verify result
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(orderModel.getReference(), result.getReference());
        Assertions.assertEquals(orderModel.getOrderIdDms(), result.getOrderIdDms());
        Assertions.assertEquals(orderModel.getComment(), result.getComment());
        Assertions.assertEquals(orderModel.getDocumentUrl(), result.getDocumentUrl());
        Assertions.assertEquals(orderModel.getStatus(), result.getStatus());

    }

    @Test
    void findOrderById() {
        //Given-preconditions

        OrderModel orderModelSave= orderRepository.save(orderModel);

        //when-action to do
        Optional<OrderModel> result= orderRepository.findById(orderModelSave.getId());

        //then-verify result
        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(OrderModel.class);
        Assertions.assertEquals(orderModelSave, result.get());

    }

    @Test
    void updateOrder() {
        //Given-preconditions
        OrderModel orderModelSave= orderRepository.save(orderModel);
        orderModelSave.setComment("This is a order update").setDocumentUrl("upodate.pdf").setPriority(5);
        //when-action to do
        OrderModel orderModelUpdate= orderRepository.save(orderModelSave);

        //then-verify result
        Assertions.assertEquals(orderModelSave.getComment(), orderModelUpdate.getComment());
        Assertions.assertEquals(orderModelSave.getDocumentUrl(), orderModelUpdate.getDocumentUrl());
        Assertions.assertEquals(orderModelSave.getPriority(), orderModelUpdate.getPriority());

    }
    @Test
    void deleteOrder() {
        //Given-preconditions
        OrderModel orderModelSave= orderRepository.save(orderModel);

        //when-action to do
        orderRepository.deleteById(orderModelSave.getId());

        Optional<OrderModel> result= orderRepository.findById(orderModelSave.getId());
        //then-verify result
        assertThat(orderModelSave.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

}
