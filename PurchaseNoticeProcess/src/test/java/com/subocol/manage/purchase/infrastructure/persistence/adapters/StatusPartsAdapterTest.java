package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.models.SellOrder;
import com.subocol.manage.purchase.domain.models.SellOrderDetail;
import com.subocol.manage.purchase.domain.models.StatusParts;
import com.subocol.manage.purchase.domain.models.StatusReplacement;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SellOrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.StatusReplacementModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.SellOrderRepository;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.StatusPartsRepository;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.StatusReplacementRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StatusPartsAdapterTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private StatusPartsRepository statusPartsRepository;
    @Autowired
    private StatusReplacementRepository statusReplacementRepository;


    private StatusPartsAdapter statusPartsAdapter;

    private OrderModel orderModel;
    private StatusReplacementModel statusReplacementModel;



    @BeforeEach
    public void setUp() {
        statusReplacementModel = StatusReplacementModel.builder()
                .externalEvent("11200")
                .seller("Orbika")
                .dateOrder(TimeZoneUtil.getTimestampByDefaultZone())
                .provider("COMPANIA CHIRICANA DE AUTOMOVILES")
                .email("gubarahonaTest@excelautomotriz.com")
                .phone("4567738")
                .approvedDate(TimeZoneUtil.getTimestampByDefaultZone())
                .providerObservation("Provider Observations")
                .quantityParts(2)
                .subsidiary("COMPANIA CHIRICANA DE AUTOMOVILES CDA PANAMA")
                .emailSubsidiary("dapertuzTest@excelautomotriz.com")
                .phoneSubsidiary("3542160")
                .statusParts(new HashSet<>())
                .build();
        statusPartsAdapter=new StatusPartsAdapter(entityManager,  statusPartsRepository);
    }

    @Test
    void testSaveAllNative() {

        List<StatusParts> statusParts = new ArrayList<>();
        StatusReplacementModel statusReplacementModelSaved=statusReplacementRepository.save(statusReplacementModel);

        StatusParts statusParts1=StatusParts.builder().status("this is a test!")
                .importPart(false).namePart("Bomper delantero").statusReplacement(new StatusReplacement().setId(statusReplacementModelSaved.getId()))
                .totalParts(1)
                .approvedOrderDate(Timestamp.valueOf(LocalDateTime.now()))
                .estimateDeliveryDate(Timestamp.valueOf(LocalDateTime.now()))
                .idProductOrder(1L).reference("This is a test!").idOrder(123L)
                .completedDate(Timestamp.valueOf(LocalDateTime.now())).lateDate(Timestamp.valueOf(LocalDateTime.now()))
                .devolutionDate(Timestamp.valueOf(LocalDateTime.now())).build();

        StatusParts statusParts2=StatusParts.builder().status("this is a test!")
                .importPart(false).namePart("parabrisas").statusReplacement(new StatusReplacement().setId(statusReplacementModelSaved.getId()))
                .totalParts(1)
                .approvedOrderDate(Timestamp.valueOf(LocalDateTime.now()))
                .estimateDeliveryDate(Timestamp.valueOf(LocalDateTime.now()))
                .idProductOrder(2L).reference("This is a test!").idOrder(123L)
                .completedDate(Timestamp.valueOf(LocalDateTime.now())).lateDate(Timestamp.valueOf(LocalDateTime.now()))
                .devolutionDate(Timestamp.valueOf(LocalDateTime.now())).build();


        statusParts.add(statusParts1);
        statusParts.add(statusParts2);

        int expectedSavedItems = 2;

        int savedItems = statusPartsAdapter.saveAllNative(statusParts);


        // Verify interactions and assertions
        assertEquals(expectedSavedItems, savedItems);
    }
}
