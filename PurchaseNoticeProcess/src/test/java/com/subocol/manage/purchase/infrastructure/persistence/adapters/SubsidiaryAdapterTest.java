package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.domain.models.Subsidiary;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OrderModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SubsidiaryModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.SubsidiaryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubsidiaryAdapterTest {

    @Mock
    private SubsidiaryRepository repository;

    @InjectMocks
    private SubsidiaryAdapter subsidiaryAdapter;

    @Test
    void SubsidiaryAdapter_findById_ReturnSubsidiaryEntity() {

        Long subsidiaryId = 1L;

        SubsidiaryModel subsidiaryModel = SubsidiaryModel.builder()
                .id(subsidiaryId)
                .alias("ADVANCE MOTORS SA CHIRIQI TEST")
                .email("sucpan101TEST@gmail.com")
                .name("ADVANCE MOTORS SA CHIRIQI TEST")
                .phone("7752509")
                .status(Boolean.TRUE)
                .locationExternalId(40L)
                .warehouseIdDms(null)
                .classification("Multimarca")
                .dmsCode(null)
                .idJob(null)
                .intermediation(null)
                .build();

        when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(subsidiaryModel));

        Optional<Subsidiary> resultSubsidiary = subsidiaryAdapter.findById(subsidiaryId);

        verify(repository, times(1)).findById(subsidiaryId);
        assertThat(resultSubsidiary).isPresent();
        assertThat(resultSubsidiary.get()).isNotNull();
        assertThat(resultSubsidiary.get()).isExactlyInstanceOf(Subsidiary.class);

        Subsidiary domainSubsidiary = resultSubsidiary.get();

        assertThat(domainSubsidiary.getId()).isEqualTo(subsidiaryId);
        assertThat(subsidiaryModel).isNotNull();
        Assertions.assertEquals(domainSubsidiary.getAlias(), subsidiaryModel.getAlias());
        Assertions.assertEquals(domainSubsidiary.getEmail(), subsidiaryModel.getEmail());
        Assertions.assertEquals(domainSubsidiary.getName(), subsidiaryModel.getName());
        Assertions.assertEquals(domainSubsidiary.getPhone(), subsidiaryModel.getPhone());
        Assertions.assertEquals(domainSubsidiary.getStatus(), subsidiaryModel.getStatus());
        Assertions.assertEquals(domainSubsidiary.getLocationExternalId(), subsidiaryModel.getLocationExternalId());
        Assertions.assertEquals(domainSubsidiary.getWarehouseIdDms(), subsidiaryModel.getWarehouseIdDms());
        Assertions.assertEquals(domainSubsidiary.getClassification(), subsidiaryModel.getClassification());
        Assertions.assertEquals(domainSubsidiary.getDmsCode(), subsidiaryModel.getDmsCode());
        Assertions.assertEquals(domainSubsidiary.getIdJob(), subsidiaryModel.getIdJob());
        Assertions.assertEquals(domainSubsidiary.getIntermediation(), subsidiaryModel.getIntermediation());
    }

    @Test
    void SubsidiaryAdapter_findById_ReturnEmptyOptional() {

        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Subsidiary> resultSubsidiary = subsidiaryAdapter.findById(anyLong());
        verify(repository, times(1)).findById(anyLong());
        assertThat(resultSubsidiary).isNotPresent();

    }

    @Test
    void SubsidiaryAdapter_findByOrderId_ReturnSubsidiaryEntity() {

        Long subsidiaryId = 1L;

        OrderModel orderModel = new OrderModel().setId(1L);
        Set<OrderModel> orderModels = new HashSet<>();
        orderModels.add(orderModel);

        SubsidiaryModel subsidiaryModel = SubsidiaryModel.builder()
                .id(subsidiaryId)
                .alias("ADVANCE MOTORS SA CHIRIQI TEST")
                .email("sucpan101TEST@gmail.com")
                .name("ADVANCE MOTORS SA CHIRIQI TEST")
                .phone("7752509")
                .status(Boolean.TRUE)
                .locationExternalId(40L)
                .warehouseIdDms(null)
                .classification("Multimarca")
                .dmsCode(null)
                .idJob(null)
                .intermediation(null)
                .orders(orderModels)
                .build();

        when(repository.findFirstByOrders_Id(anyLong())).thenReturn(Optional.ofNullable(subsidiaryModel));

        Optional<Subsidiary> resultSubsidiary = subsidiaryAdapter.findByOrderId(subsidiaryId);

        verify(repository, times(1)).findFirstByOrders_Id(subsidiaryId);
        assertThat(resultSubsidiary).isPresent();
        assertThat(resultSubsidiary.get()).isNotNull();
        assertThat(resultSubsidiary.get()).isExactlyInstanceOf(Subsidiary.class);

        Subsidiary domainSubsidiary = resultSubsidiary.get();

        assertThat(domainSubsidiary.getId()).isEqualTo(subsidiaryId);
        assertThat(subsidiaryModel).isNotNull();

        try {
            //Assertions.assertEquals for all entity attributes
            AttributeAssertions.assertAttributesEqual(subsidiaryModel, domainSubsidiary);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }
    }

    @Test
    void SubsidiaryAdapter_findByOrderId_ReturnEmptyOptional() {

        when(repository.findFirstByOrders_Id(anyLong())).thenReturn(Optional.empty());

        Optional<Subsidiary> resultSubsidiary = subsidiaryAdapter.findByOrderId(anyLong());

        verify(repository, times(1)).findFirstByOrders_Id(anyLong());
        assertThat(resultSubsidiary).isNotPresent();

    }

}