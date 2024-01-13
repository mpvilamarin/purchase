package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.common.TestConstants;
import com.subocol.manage.purchase.domain.constant.ManageNoticeConstant;
import com.subocol.manage.purchase.domain.models.ProductOrdersPiecesNotice;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ProductOrdersPiecesNoticeModel;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.repository.query.Param;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductOrdersPiecesNoticeRepositoryTest {

    @Autowired
    private ProductOrdersPiecesNoticeRepository productOrdersPiecesNoticeRepository;
    private ProductOrdersPiecesNoticeModel productOrdersPiecesNoticeModel;

    @BeforeEach
    void setup(){
        productOrdersPiecesNoticeModel = ProductOrdersPiecesNoticeModel.builder().price(10.0).amount(5).reference("ProductReference").description("ProductDescription")
                .grossPrice(20.0).totalDiscount(2.0).promiseDelivery(new Timestamp(1664579257))
                .comment("ProductComment").positionPiece(1).status("Accepted").build();
    }


    @Test
    @Disabled(TestConstants.SKIP_DATABASE_DEPENDENCE)
    void testTotalPriceOrdersByExternalEventAndEventId_ReturnDouble() {

        Integer externalEvent = 435713;
        boolean type= true ;
        List<Integer> positionPiece = List.of(1,2,3);
        boolean unforeseen = false ;

        Double result= productOrdersPiecesNoticeRepository.totalPriceOrdersByExternalEventAndEventId(externalEvent,type,positionPiece,unforeseen);

        assertNotNull(result);
        assertEquals(20.79, result);

    }
    @Test
    @Disabled(TestConstants.SKIP_DATABASE_DEPENDENCE)
    void testTotalGrossPriceOrdersByExternalEventAndEventId_ReturnReserveCalculationTotalSuraDTO() {

        double total=21D;
        double totalIva=1.4600000000000002D;
        double totalDescuento=0.21000000000000085D;
        Integer externalEvent = 435713;

        List<Integer> positionPiece = List.of(1,2,3);
        boolean unforeseen = false ;

        Tuple result= productOrdersPiecesNoticeRepository.totalGrossPriceOrdersByExternalEventAndEventIdTuple(externalEvent, positionPiece, unforeseen);

        assertNotNull(result);
        assertEquals(total, result.get("total", Double.class));
        assertEquals(totalIva, result.get("totalIva", Double.class));
        assertEquals(totalDescuento, result.get("totalDescuento", Double.class));
    }

    @Test
    @Disabled(TestConstants.SKIP_DATABASE_DEPENDENCE)
    void testFindPiecesOrdersByExternalEvent_ReturnListReserveRepuestosSuraDTO() {

        ReserveRepuestosSuraDTO reserveRepuestosSuraDTO1=new ReserveRepuestosSuraDTO("1164",1, "lona cubre pick up", "Sin Referencia",
               1, 1D, 0.06952380952380953D, 7.022607022607023D, "A", false, 1D, "asd", 10D);
        ReserveRepuestosSuraDTO reserveRepuestosSuraDTO2=new ReserveRepuestosSuraDTO("1962",2, "refuerzo tapabarros del. der.", "Sin Referencia",
                1, 10D, 0.6952380952380953D, 7.022607022607023D, "A", false, 1D, "asd", 10D);

        Integer externalEvent = 435713;

        List<Integer> positionPiece = List.of(1,2);
        boolean unforeseen = false ;

        List<ReserveRepuestosSuraDTO>  result= productOrdersPiecesNoticeRepository.findPiecesOrdersByExternalEvent(externalEvent,ManageNoticeConstant.LIST_SPARE,positionPiece);
        assertThat(result).hasSize(2);
        try {
            //Assertions.assertEquals for all entity attributes
            AttributeAssertions.assertAttributesEqual(reserveRepuestosSuraDTO1, result.get(0));
            AttributeAssertions.assertAttributesEqual(reserveRepuestosSuraDTO2, result.get(1));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }
    }
}
