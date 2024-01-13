package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeClaimNumberDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.NoticeModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.spi.NativeQueryTupleTransformer;
import org.hibernate.sql.results.internal.TupleImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NoticeRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    private final NoticeRepository noticeRepository;

    private NoticeModel noticeModel;

    private NoticeModel secondNoticeModel;

    @Autowired
    NoticeRepositoryTest(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @BeforeEach
    void setup() {
        noticeModel = NoticeModel.builder()
                .externalEvent(5432)
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
                .unforeseen(true)
                .build();

        secondNoticeModel = NoticeModel.builder()
                .externalEvent(5432)
                .brand("Toyota")
                .auth(true)
                .model("2021")
                .claimNumber("123456753")
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
    }


    @Test
    void saveNoticeModel() {
        //Given-preconditions

        //when-action to do
        NoticeModel result = noticeRepository.save(noticeModel);
        //then-verify result
        assertThat(result.getId()).isPositive();
        try {
            AttributeAssertions.assertAttributesEqual(noticeModel, result);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }

    @Test
    void findNoticeById() {
        //Given-preconditions

        NoticeModel noticeModelSave = noticeRepository.save(noticeModel);

        //when-action to do
        Optional<NoticeModel> result = noticeRepository.findById(noticeModelSave.getId());

        //then-verify result
        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(NoticeModel.class);
        Assertions.assertEquals(noticeModelSave, result.get());

    }

    @Test
    void updateNotice() {
        //Given-preconditions
        NoticeModel noticeModelSave = noticeRepository.save(noticeModel);
        noticeModelSave.setBrand("CHEVROLET").setModel("2010").setLine("SAIL");
        //when-action to do
        NoticeModel noticeModelUpdate = noticeRepository.save(noticeModelSave);

        //then-verify result
        Assertions.assertEquals(noticeModelSave.getBrand(), noticeModelUpdate.getBrand());
        Assertions.assertEquals(noticeModelSave.getModel(), noticeModelUpdate.getModel());
        Assertions.assertEquals(noticeModelSave.getLine(), noticeModelUpdate.getLine());

    }

    @Test
    void deleteNotice() {
        //Given-preconditions
        NoticeModel noticeModelSave = noticeRepository.save(noticeModel);

        //when-action to do
        noticeRepository.deleteById(noticeModelSave.getId());

        Optional<NoticeModel> result = noticeRepository.findById(noticeModelSave.getId());
        //then-verify result
        assertThat(noticeModelSave.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

    @Test
    void noticeRepository_findByExternalEventAndAuth_ReturnNotice() {

        NoticeModel noticeModelSaved = noticeRepository.save(noticeModel);

        List<NoticeModel> noticeModels = noticeRepository.findByExternalEventAndAuth(noticeModelSaved.getExternalEvent(), noticeModelSaved.isAuth());

        assertThat(noticeModels)
                .isNotEmpty()
                .hasSize(1)
                .contains(noticeModelSaved);

    }

    @Test
    void noticeRepository_findByExternalEventAndAuth_ReturnNotices() {

        NoticeModel noticeModelSaved = noticeRepository.save(noticeModel);
        NoticeModel secondNoticeModelSaved = noticeRepository.save(secondNoticeModel);

        List<NoticeModel> noticeModels = noticeRepository.findByExternalEventAndAuth(noticeModelSaved.getExternalEvent(), noticeModelSaved.isAuth());

        assertThat(noticeModels)
                .isNotEmpty()
                .hasSize(2)
                .contains(noticeModelSaved)
                .contains(secondNoticeModelSaved);

    }

    @Test
    void noticeRepository_updateAuthByExternalEvent_ReturnCount() {

        NoticeModel noticeModelSaved = noticeRepository.save(noticeModel);
        NoticeModel secondNoticeModelSaved = noticeRepository.save(secondNoticeModel);

        int count = noticeRepository.updateAuthByExternalEvent(noticeModelSaved.getExternalEvent(), false);

        entityManager.refresh(noticeModelSaved);
        entityManager.refresh(secondNoticeModelSaved);

        assertThat(count).isPositive().isEqualTo(2);
        assertThat(noticeModelSaved.isAuth()).isFalse();
        assertThat(secondNoticeModelSaved.isAuth()).isFalse();

    }

    @Test
    void noticeRepository_findAllByExternalEvent_ReturnNotices() {

        NoticeModel noticeModelSaved = noticeRepository.save(noticeModel);
        NoticeModel secondNoticeModelSaved = noticeRepository.save(secondNoticeModel);

        List<NoticeModel> noticeModels = noticeRepository.findAllByExternalEvent(noticeModelSaved.getExternalEvent());

        assertThat(noticeModels)
                .isNotEmpty()
                .hasSize(2)
                .contains(noticeModelSaved)
                .contains(secondNoticeModelSaved);

    }

    @Test
    void noticeRepository_findDistinctClaimNumberByExternalEvent_ReturnNoticeClaimNumberDTO() {

        NoticeModel noticeModelSaved = noticeRepository.save(noticeModel);
        noticeRepository.save(secondNoticeModel);

        NoticeClaimNumberDTO noticeDto = noticeRepository.findDistinctClaimNumberByExternalEvent(noticeModelSaved.getExternalEvent(), noticeModelSaved.getClaimNumber());

        assertThat(noticeDto).isNotNull();
        Assertions.assertEquals(noticeDto.getExternalEvent(), noticeModelSaved.getExternalEvent());
        Assertions.assertEquals(2, noticeDto.getCountPackages());
        Assertions.assertEquals(1, noticeDto.getCountClaimEquals());

    }

    @Test
    void noticeRepository_updateClaimNumberByExternalEvent_ReturnCount() {

        String updatedClaimNumber = "1234R2";

        NoticeModel noticeModelSaved = noticeRepository.save(noticeModel);
        NoticeModel secondNoticeModelSaved = noticeRepository.save(secondNoticeModel);

        int count = noticeRepository.updateClaimNumberByExternalEvent(noticeModelSaved.getExternalEvent(), updatedClaimNumber);

        entityManager.refresh(noticeModelSaved);
        entityManager.refresh(secondNoticeModelSaved);

        assertThat(count).isPositive().isEqualTo(2);
        Assertions.assertEquals(updatedClaimNumber, noticeModelSaved.getClaimNumber());
        Assertions.assertEquals(updatedClaimNumber, secondNoticeModelSaved.getClaimNumber());

    }

    @Test
    @Disabled("This test use real data from BD")
    void findSpareToFollowUpByExternalEventAndPositionsNoAuth() {

        long externalEvent=50L;

        List<Tuple> result= noticeRepository.findSpareToFollowUpByExternalEventAndPositionsNoAuth(externalEvent);
        log.info(result.get(0).getClass().getName());
        log.info(result.get(0).getClass().getSimpleName());

        assertThat(result).isNotEmpty();
        Tuple tuple1=result.get(0);
        Tuple tuple2=result.get(1);

        Assertions.assertEquals(1, tuple1.get("position_piece", Integer.class));
        Assertions.assertEquals(1, tuple1.get("quantity", BigDecimal.class).intValue());
        Assertions.assertEquals(false, tuple1.get("esCotizado", Boolean.class));
        Assertions.assertEquals(false, tuple1.get("deleted", Boolean.class));

        Assertions.assertEquals(3, tuple2.get("position_piece", Integer.class));
        Assertions.assertEquals(1, tuple2.get("quantity", BigDecimal.class).intValue());
        Assertions.assertEquals(false, tuple2.get("esCotizado", Boolean.class));
        Assertions.assertEquals(false, tuple2.get("deleted", Boolean.class));
    }

    @Test
    @Disabled("This test use real data from BD")
    void testFindSpareToFollowUpByExternalEventAndPositionsReturnListSpareDetailToFollowUpDTO() {

        List<Integer> positions=List.of(1,2);
        Long externalEvent=7065L;

        List<SpareDetailToFollowUpDTO> result= noticeRepository.findSpareToFollowUpByExternalEventAndPositions(
        externalEvent,  positions, true, true);
        assertThat(result).isNotEmpty().hasSize(2);

        SpareDetailToFollowUpDTO dto1=result.get(0);
        SpareDetailToFollowUpDTO dto2=result.get(1);

        Assertions.assertEquals(1,dto1.getPosicion());
        Assertions.assertEquals(1,dto1.getCantidad());
        Assertions.assertTrue(dto1.isDeleted());
        Assertions.assertTrue(dto1.isEsCotizado());
        Assertions.assertEquals(2,dto2.getPosicion());
        Assertions.assertEquals(16,dto2.getCantidad());
        Assertions.assertTrue(dto2.isDeleted());
        Assertions.assertTrue(dto2.isEsCotizado());
    }

    @Test
    void testCountAllByExternalEventAndUnforeseen_ReturnCount() {

        NoticeModel noticeModelSaved = noticeRepository.save(noticeModel);

        Long count = noticeRepository.countAllByExternalEventAndUnforeseen(noticeModelSaved.getExternalEvent(), true);

        entityManager.refresh(noticeModelSaved);

        assertThat(count).isPositive().isEqualTo(1);
        Assertions.assertTrue(noticeModelSaved.isUnforeseen());

    }
}
