package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.NoticeClaimNumberDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.SpareDetailToFollowUpDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.NoticeModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.NoticeRepository;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.spi.NativeQueryTupleTransformer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class NoticeAdapterTest {

    @Mock
    private NoticeRepository repository;

    @InjectMocks
    private NoticeAdapter noticeAdapter;

    NoticeModel noticeModel;
    Notice notice;
    Long noticeId = 19867L;

    @BeforeEach
    void setup() {
        noticeModel = NoticeModel.builder()
                .id(19867L).externalEvent(465326411).plate("AJ7890").date(TimeZoneUtil.getTimestampByDefaultZone()).brand("SUBARU")
                .line("FORESTER").model("2016").version("1600CC AUTOMOVIL").workshop("SUBARU DEL CANAL")
                .city("PANAMA").vin("JF1BS9LC2JG155408").workshopEmail(null).phone(null).cellphone(null).coverage("RC").workshopAddress("cl98 54 78")
                .idCountry(2L).auth(true).insuranceNumber(200000002L).workshopType("Autosuministro").statusNotice(null).dateClose(null).claimNumber("9453168")
                .eventId(22879L).unforeseen(false).repairOrder(BigDecimal.valueOf(6546544)).quotationEstimatedDate(new Timestamp(1664582461)).closed(false)
                .lossIndicator(0.0).totalWorkforce(0.0).insuredValue(0.0).processedNotice(true).consultedReferenceIA(false).nextDateReferenceIa(null).workshopId(null)
                .build();

        notice = Notice.builder()
                .id(19867L).externalEvent(465326411).plate("AJ7890").date(TimeZoneUtil.getTimestampByDefaultZone()).brand("SUBARU")
                .line("FORESTER").model("2016").version("1600CC AUTOMOVIL").workshop("SUBARU DEL CANAL")
                .city("PANAMA").vin("JF1BS9LC2JG155408").workshopEmail(null).phone(null).cellphone(null).coverage("RC").workshopAddress("cl98 54 78")
                .idCountry(2L).auth(true).insuranceNumber(200000002L).workshopType("Autosuministro").statusNotice(null).dateClose(null).claimNumber("9453168")
                .eventId(22879L).unforeseen(false).repairOrder(BigDecimal.valueOf(6546544)).quotationEstimatedDate(new Timestamp(1664582461)).closed(false)
                .lossIndicator(0.0).totalWorkforce(0.0).insuredValue(0.0).processedNotice(true).consultedReferenceIA(false).nextDateReferenceIa(null).workshopId(null)
                .build();

    }


    @Test
    void noticeAdapter_findById_ReturnNoticeEntity() {

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(noticeModel));

        Optional<Notice> resultNotice = noticeAdapter.findById(noticeId);

        verify(repository, times(1)).findById(noticeId);
        assertThat(resultNotice).isPresent();
        assertThat(resultNotice.get()).isNotNull();
        assertThat(resultNotice.get()).isExactlyInstanceOf(Notice.class);

        Notice domainNotice = resultNotice.get();

        assertThat(domainNotice.getId()).isEqualTo(noticeId);
        assertThat(domainNotice).isNotNull();
        try {
            AttributeAssertions.assertAttributesEqual(noticeModel, domainNotice);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }

    @Test
    void noticeAdapter_findById_ReturnEmptyOptional() {

        Long noticeId = 1L;

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Notice> resultNotice = noticeAdapter.findById(noticeId);

        verify(repository, times(1)).findById(noticeId);
        assertThat(resultNotice).isNotPresent();

    }

    @Test
    void testSave() {

        //TODO: Improve this method

        when(repository.save(any(NoticeModel.class))).thenReturn(noticeModel);

        Notice savedNotice = noticeAdapter.save(notice);

        Assertions.assertEquals(notice.getId(), savedNotice.getId());
        Assertions.assertEquals(notice.getExternalEvent(), savedNotice.getExternalEvent());
        Assertions.assertEquals(notice.getPlate(), savedNotice.getPlate());
        Assertions.assertEquals(notice.getBrand(), savedNotice.getBrand());
        Assertions.assertEquals(notice.getLine(), savedNotice.getLine());
        Assertions.assertEquals(notice.getModel(), savedNotice.getModel());
        Assertions.assertEquals(notice.getVersion(), savedNotice.getVersion());
        Assertions.assertEquals(notice.getWorkshop(), savedNotice.getWorkshop());
        Assertions.assertEquals(notice.getCity(), savedNotice.getCity());
        Assertions.assertEquals(notice.getStatus(), savedNotice.getStatus());
        Assertions.assertEquals(notice.getVin(), savedNotice.getVin());
        Assertions.assertEquals(notice.getWorkshopEmail(), savedNotice.getWorkshopEmail());
        Assertions.assertEquals(notice.getPhone(), savedNotice.getPhone());
        Assertions.assertEquals(notice.getCellphone(), savedNotice.getCellphone());
        Assertions.assertEquals(notice.getCoverage(), savedNotice.getCoverage());
        Assertions.assertEquals(notice.getWorkshopAddress(), savedNotice.getWorkshopAddress());
        Assertions.assertEquals(notice.getIdCountry(), savedNotice.getIdCountry());
        Assertions.assertEquals(notice.isAuth(), savedNotice.isAuth());
        Assertions.assertEquals(notice.getInsuranceNumber(), savedNotice.getInsuranceNumber());
        Assertions.assertEquals(notice.getWorkshopType(), savedNotice.getWorkshopType());
        Assertions.assertEquals(notice.getStatusNotice(), savedNotice.getStatusNotice());
        Assertions.assertEquals(notice.getDateClose(), savedNotice.getDateClose());
        Assertions.assertEquals(notice.getClaimNumber(), savedNotice.getClaimNumber());
        Assertions.assertEquals(notice.getEventId(), savedNotice.getEventId());
        Assertions.assertEquals(notice.isUnforeseen(), savedNotice.isUnforeseen());
        Assertions.assertEquals(notice.getRepairOrder(), savedNotice.getRepairOrder());
        Assertions.assertEquals(notice.getQuotationEstimatedDate(), savedNotice.getQuotationEstimatedDate());
        Assertions.assertEquals(notice.isClosed(), savedNotice.isClosed());
        Assertions.assertEquals(notice.getLossIndicator(), savedNotice.getLossIndicator());
        Assertions.assertEquals(notice.getTotalWorkforce(), savedNotice.getTotalWorkforce());
        Assertions.assertEquals(notice.getInsuredValue(), savedNotice.getInsuredValue());
        Assertions.assertEquals(notice.isProcessedNotice(), savedNotice.isProcessedNotice());
        Assertions.assertEquals(notice.isConsultedReferenceIA(), savedNotice.isConsultedReferenceIA());
        Assertions.assertEquals(notice.getNextDateReferenceIa(), savedNotice.getNextDateReferenceIa());
        Assertions.assertEquals(notice.getWorkshopId(), savedNotice.getWorkshopId());
    }

    @Test
    void noticeAdapter_findAllByExternalEventAndAuth_ReturnNoticeEntities() {

        int externalEvent = noticeModel.getExternalEvent();
        boolean auth = noticeModel.isAuth();

        when(repository.findByExternalEventAndAuth(externalEvent, auth)).thenReturn(Collections.singletonList(noticeModel));

        List<Notice> resultNotices = noticeAdapter.findAllByExternalEventAndAuth(String.valueOf(externalEvent), auth);

        verify(repository, times(1)).findByExternalEventAndAuth(externalEvent, auth);
        assertThat(resultNotices).isNotEmpty().hasSize(1);

        Notice domainNotice = resultNotices.get(0);

        assertThat(domainNotice).isNotNull();

        try {
            AttributeAssertions.assertAttributesEqual(noticeModel, domainNotice);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }

    @Test
    void noticeAdapter_findAllByExternalEventAndAuth_ReturnEmpty() {

        int externalEvent = noticeModel.getExternalEvent();

        when(repository.findByExternalEventAndAuth(externalEvent, true)).thenReturn(Collections.emptyList());

        List<Notice> resultNotices = noticeAdapter.findAllByExternalEventAndAuth(String.valueOf(externalEvent), true);

        verify(repository, times(1)).findByExternalEventAndAuth(externalEvent, true);
        assertThat(resultNotices).isEmpty();

    }

    @Test
    void noticeAdapter_findAllByExternalEvent_ReturnNoticeEntities() {

        int externalEvent = noticeModel.getExternalEvent();

        when(repository.findAllByExternalEvent(externalEvent)).thenReturn(Collections.singletonList(noticeModel));

        List<Notice> resultNotices = noticeAdapter.findAllByExternalEvent(String.valueOf(externalEvent));

        verify(repository, times(1)).findAllByExternalEvent(externalEvent);
        assertThat(resultNotices).isNotEmpty().hasSize(1);

        Notice domainNotice = resultNotices.get(0);

        assertThat(domainNotice).isNotNull();

        try {
            AttributeAssertions.assertAttributesEqual(noticeModel, domainNotice);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }

    @Test
    void noticeAdapter_findAllByExternalEvent_ReturnEmpty() {

        int externalEvent = noticeModel.getExternalEvent();

        when(repository.findAllByExternalEvent(externalEvent)).thenReturn(Collections.emptyList());

        List<Notice> resultNotices = noticeAdapter.findAllByExternalEvent(String.valueOf(externalEvent));

        verify(repository, times(1)).findAllByExternalEvent(externalEvent);
        assertThat(resultNotices).isEmpty();

    }

    @Test
    void noticeAdapter_updateAuthByExternalEvent_ReturnsUpdatedRowCount() {
        int externalEvent = noticeModel.getExternalEvent();
        int updatedRowCount = 1;

        when(repository.updateAuthByExternalEvent(externalEvent, Boolean.TRUE)).thenReturn(updatedRowCount);

        int result = noticeAdapter.updateAuthByExternalEvent(String.valueOf(externalEvent), Boolean.TRUE);

        verify(repository, times(1)).updateAuthByExternalEvent(externalEvent, Boolean.TRUE);
        assertEquals(updatedRowCount, result);

    }

    @Test
    void noticeAdapter_updateClaimNumberByExternalEvent_ReturnsUpdatedRowCount() {

        int externalEvent = noticeModel.getExternalEvent();
        String updatedClaimNumber = "123445";

        int updatedRowCount = 1;

        when(repository.updateClaimNumberByExternalEvent(externalEvent, updatedClaimNumber)).thenReturn(updatedRowCount);

        int result = noticeAdapter.updateClaimNumberByExternalEvent(String.valueOf(externalEvent), updatedClaimNumber);

        verify(repository, times(1)).updateClaimNumberByExternalEvent(externalEvent, updatedClaimNumber);
        assertEquals(updatedRowCount, result);

    }

    @Test
    void noticeAdapter_findDistinctClaimNumberByExternalEvent_ReturnNoticeClaimNumberDto() {

        int externalEvent = noticeModel.getExternalEvent();
        String claimNumber = noticeModel.getClaimNumber();

        NoticeClaimNumberDTO noticeClaimNumberDTO = new NoticeClaimNumberDTO(externalEvent, 1L, 1L);

        when(repository.findDistinctClaimNumberByExternalEvent(externalEvent, claimNumber)).thenReturn(noticeClaimNumberDTO);

        NoticeClaimNumberDTO resultDto = noticeAdapter.findDistinctClaimNumberByExternalEvent(String.valueOf(externalEvent), claimNumber);

        verify(repository, times(1)).findDistinctClaimNumberByExternalEvent(externalEvent, claimNumber);
        assertThat(resultDto).isNotNull();

        try {
            AttributeAssertions.assertAttributesEqual(noticeClaimNumberDTO, resultDto);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }

    @Test
    void findSpareToFollowUpByExternalEventAndPositionsReturnListSpareDetailToFollowUpDTO(){
        SpareDetailToFollowUpDTO spareDetailToFollowUpDTO1=SpareDetailToFollowUpDTO.builder()
                .idStatusParts(12345L).deleted(false).cantidad(1).posicion(1).build();
        SpareDetailToFollowUpDTO spareDetailToFollowUpDTO2=SpareDetailToFollowUpDTO.builder()
                .idStatusParts(12346L).deleted(false).cantidad(1).posicion(2).build();
        List<Integer> positions=List.of(1,2);
        when(repository.findSpareToFollowUpByExternalEventAndPositions(noticeModel.getExternalEvent().longValue(), positions, false, true)).thenReturn(List.of(spareDetailToFollowUpDTO1, spareDetailToFollowUpDTO2));

        List<SpareDetailToFollowUpDTO> result=noticeAdapter.findSpareToFollowUpByExternalEventAndPositions(noticeModel.getExternalEvent().longValue(), positions, false, true);

        try {
            //Assertions.assertEquals for all entity attributes
            AttributeAssertions.assertAttributesEqual(result.get(0), spareDetailToFollowUpDTO1);
            AttributeAssertions.assertAttributesEqual(result.get(1), spareDetailToFollowUpDTO2);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }

    @Test
    void testFindSpareToFollowUpByExternalEventAndPositionsNoAuthReturnListSpareDetailToFollowUpDTO() {

    Long externalEvent = 123L;

    NativeQueryTupleTransformer nativeQueryTupleTransformer=new NativeQueryTupleTransformer();
    Object[] objectsForTuple1={1, new BigDecimal(1), false, false};
    String[] aliasForTuple={"position_piece", "quantity", "escotizado", "deleted"};
    Object[] objectsForTuple2={2, new BigDecimal(4), true, false};

    Tuple tuple1= nativeQueryTupleTransformer.transformTuple(objectsForTuple1, aliasForTuple);
    Tuple tuple2= nativeQueryTupleTransformer.transformTuple(objectsForTuple2, aliasForTuple);

    when(repository.findSpareToFollowUpByExternalEventAndPositionsNoAuth(externalEvent)).thenReturn(List.of(tuple1, tuple2));

    List<SpareDetailToFollowUpDTO> result = noticeAdapter.findSpareToFollowUpByExternalEventAndPositionsNoAuth(externalEvent);

    log.info(result.toString());

    assertEquals(2, result.size());

    SpareDetailToFollowUpDTO dto1 = result.get(0);
    assertEquals(1, dto1.getPosicion());
    assertEquals(1, dto1.getCantidad());
    assertFalse(dto1.isEsCotizado());
    assertFalse(dto1.isDeleted());

    SpareDetailToFollowUpDTO dto2 = result.get(1);
    assertEquals(2, dto2.getPosicion());
    assertEquals(4, dto2.getCantidad());
    assertTrue(dto2.isEsCotizado());
    assertFalse(dto2.isDeleted());
    }
    @Test
    void testCountAllByExternalEventAndUnforeseen() {

        long externalEvent = 123L;

        when(repository.countAllByExternalEventAndUnforeseen((int) externalEvent, true)).thenReturn(2L);

        Long result = noticeAdapter.countAllByExternalEventAndUnforeseen((int) externalEvent, true);

        assertEquals(2, result);

    }
}

