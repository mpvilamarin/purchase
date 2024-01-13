package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.domain.models.SendReserveManage;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SendReserveManageModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.SendReserveManageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendReserveManageAdapterTest {

    @Mock
    private SendReserveManageRepository repository;

    @InjectMocks
    private SendReserveManageAdapter sendReserveManageAdapter;

    SendReserveManageModel sendReserveManage;
    SendReserveManage sendReserveManage2;
    SendReserveManageModel sendReserveManageSaved;

    @BeforeEach
    void setup() {

        sendReserveManage = SendReserveManageModel.builder().id(37L).date(new Timestamp(1692037973)).initCarSended(true).externalEvent(138070).build();
        sendReserveManageSaved = SendReserveManageModel.builder().id(38L).date(new Timestamp(1692037973)).initCarSended(true).externalEvent(138070).build();
        sendReserveManage2 = SendReserveManage.builder().id(38L).date(new Timestamp(1692037973)).initCarSended(true).externalEvent(138070).build();
    }

    @Test
    void testFindByExternalEvent_ReturnEmptyOptional() {

        Integer externalEvent = 164462;

        when(repository.findByExternalEvent(anyInt())).thenReturn(Optional.empty());

        Optional<SendReserveManage> resultSendReserveManage = sendReserveManageAdapter.findByExternalEvent(externalEvent);

        assertThat(resultSendReserveManage).isNotPresent();

    }

    @Test
    void testFindByExternalEvent_ReturnSendReserveManageEntity() {

        Integer sendReserveManageId = 37;

        when(repository.findByExternalEvent(anyInt())).thenReturn(Optional.of(sendReserveManage));

        Optional<SendReserveManage> resultSendReserveManage = sendReserveManageAdapter.findByExternalEvent(sendReserveManageId);

        assertThat(resultSendReserveManage).isPresent();
        assertThat(resultSendReserveManage.get()).isNotNull();
        assertThat(resultSendReserveManage.get()).isExactlyInstanceOf(SendReserveManage.class);

        try {
            AttributeAssertions.assertAttributesEqual(sendReserveManage, resultSendReserveManage);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }
    }

    @Test
    void testSave() {
        when(repository.save(any(SendReserveManageModel.class))).thenReturn(sendReserveManageSaved);

        SendReserveManage savedSendReserveManage = sendReserveManageAdapter.save(sendReserveManage2);

        try {
            AttributeAssertions.assertAttributesEqual(sendReserveManage2, savedSendReserveManage);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }
}
