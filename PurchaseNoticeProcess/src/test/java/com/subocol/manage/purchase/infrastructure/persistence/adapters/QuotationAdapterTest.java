package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.models.Quotation;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.CounterStatusProductQuotation;
import com.subocol.manage.purchase.infrastructure.persistence.entities.QuotationModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.QuotationRepository;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuotationAdapterTest {

    @Mock
    private QuotationRepository repository;

    @InjectMocks
    private QuotationAdapter quotationAdapter;

    Long noticeId = 123L;
    Long quotationId = 123L;
    String newStatus = "quoted";

    private QuotationModel quotationModel;

    @BeforeEach
    void setup() {

        this.quotationModel = QuotationModel.builder()
                .providerName("Provider Name")
                .nit("123456789")
                .quotationSubsidiaryName("Subsidiary Name")
                .replacementReference("Reference")
                .unities(10)
                .price(99.99)
                .quality("High")
                .importation(false)
                .timeDelivery(7)
                .observations("Some observations")
                .brand("Brand")
                .line("Product Line")
                .city("City")
                .status("Status")
                .externalEvent("External Event")
                .time(TimeZoneUtil.getTimestampByDefaultZone())
                .flowType("Autosuministro")
                .noticeId(1L)
                .unforeseen(true)
                .repairOrder(BigDecimal.valueOf(123.45))
                .adiUpdated(true)
                .dateUpdateQuotation(TimeZoneUtil.getTimestampByDefaultZone())
                .quotationManaged(false)
                .quotationWinner(false)
                .build();


    }

    @Test
    void countStatusProductQuotationMM_ReturnsCounterStatusProductQuotationList() {

        List<CounterStatusProductQuotation> mockedResult = new ArrayList<>();
        mockedResult.add(new CounterStatusProductQuotation(10L, 5L, 10L, 5L, 10L, 10L));
        mockedResult.add(new CounterStatusProductQuotation(10L, 5L, 10L, 5L, 10L, 10L));

        when(repository.getCounterStatusProductQuotation(anyLong())).thenReturn(mockedResult);

        List<CounterStatusProductQuotation> result = quotationAdapter.countStatusProductQuotationMM(noticeId);

        assertNotNull(result);
        assertEquals(mockedResult.size(), result.size());

        for (int i = 0; i < mockedResult.size(); i++) {
            CounterStatusProductQuotation mockedCounter = mockedResult.get(i);
            CounterStatusProductQuotation actualCounter = result.get(i);

            assertEquals(mockedCounter.getOmittedProducts(), actualCounter.getOmittedProducts());
            assertEquals(mockedCounter.getTotalProducts(), actualCounter.getTotalProducts());
            assertEquals(mockedCounter.getQuotedProducts(), actualCounter.getQuotedProducts());
            assertEquals(mockedCounter.getRejectedQuotedProducts(), actualCounter.getRejectedQuotedProducts());
            assertEquals(mockedCounter.getAcceptedProducts(), actualCounter.getAcceptedProducts());

        }
    }

    @Test
    void countStatusProductQuotationMM_ReturnsEmptyList() {

        List<CounterStatusProductQuotation> mockedResult = Collections.emptyList();

        when(repository.getCounterStatusProductQuotation(anyLong())).thenReturn(mockedResult);

        List<CounterStatusProductQuotation> result = quotationAdapter.countStatusProductQuotationMM(noticeId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void updateStatusQuotation_ReturnsUpdatedRowCount() {

        int updatedRowCount = 1;
        when(repository.updateQuotationStatusById(anyString(), anyLong())).thenReturn(updatedRowCount);

        int result = quotationAdapter.updateStatusQuotation(quotationId, newStatus);

        assertEquals(updatedRowCount, result);
    }

    @Test
    void testFindQuotationWithAllProductManage() {

        Long quotationId = 123L;
        List<Tuple> mockResult = new ArrayList<>();
        Tuple tuple1 = Mockito.mock(Tuple.class);
        Tuple tuple2 = Mockito.mock(Tuple.class);
        mockResult.add(tuple1);
        mockResult.add(tuple2);

        when(repository.findQuotationWithAllProductManage(quotationId)).thenReturn(mockResult);
        when(tuple1.get("id", Long.class)).thenReturn(1L);
        when(tuple2.get("id", Long.class)).thenReturn(2L);


        List<Long> result = quotationAdapter.findQuotationWithAllProductManage(quotationId);

        assertEquals(2, result.size());

        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));
    }

    @Test
    void quotationAdapter_updateQuotationManaged_ReturnsUpdatedRowCount() {

        int updatedRowCount = 3;
        List<Long> lsIdsQuotation = Arrays.asList(1L, 2L, 3L);
        when(repository.updateQuotationManaged(anyList())).thenReturn(updatedRowCount);

        int result = quotationAdapter.updateQuotationManaged(lsIdsQuotation);

        verify(repository, times(1)).updateQuotationManaged(lsIdsQuotation);
        assertEquals(updatedRowCount, result);

    }

    @Test
    void quotationAdapter_findQuotationManagedByExternalEvent_ReturnQuotationsIds() {

        String externalEvent = "948933462";
        Long quotationId = 198407L;

        Tuple mockTuple = mock(Tuple.class);
        when(mockTuple.get("id", Long.class)).thenReturn(quotationId);

        List<Tuple> mockResult = new ArrayList<>();
        mockResult.add(mockTuple);

        when(repository.findQuotationManagedByExternalEvent(externalEvent)).thenReturn(mockResult);

        List<Long> quotationIds = quotationAdapter.findQuotationManagedByExternalEvent(externalEvent);

        verify(repository, times(1)).findQuotationManagedByExternalEvent(externalEvent);

        assertThat(quotationIds)
                .isNotEmpty()
                .hasSize(1)
                .contains(quotationId);

    }

    @Test
    void quotationAdapter_findQuotationManagedByExternalEvent_ReturnEmptyList() {

        String externalEvent = "346723462";

        List<Tuple> mockResult = new ArrayList<>();

        when(repository.findQuotationManagedByExternalEvent(externalEvent)).thenReturn(mockResult);

        List<Long> quotationIds = quotationAdapter.findQuotationManagedByExternalEvent(externalEvent);

        verify(repository, times(1)).findQuotationManagedByExternalEvent(externalEvent);

        assertThat(quotationIds).isEmpty();

    }

    @Test
    void quotationAdapter_findQuotationByNoticeIdAndFlowType_ReturnQuotation() {

        Long noticeId = quotationModel.getNoticeId();

        when(repository.findQuotationByNoticeIdAndFlowType(noticeId)).thenReturn(Optional.ofNullable(quotationModel));

        Optional<Quotation> optional = quotationAdapter.findQuotationByNoticeIdAndFlowType(noticeId);

        verify(repository, times(1)).findQuotationByNoticeIdAndFlowType(noticeId);

        assertThat(optional).isPresent();
        assertThat(optional.get()).isExactlyInstanceOf(Quotation.class);

        Quotation quotationDomain = optional.get();

        try {
            AttributeAssertions.assertAttributesEqual(quotationModel, quotationDomain);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }

}
