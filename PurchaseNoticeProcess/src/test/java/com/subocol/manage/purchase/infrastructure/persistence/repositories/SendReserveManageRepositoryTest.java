package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.common.AttributeAssertions;
import com.subocol.manage.purchase.domain.models.SendReserveManage;
import com.subocol.manage.purchase.infrastructure.persistence.entities.PieceModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SendReserveManageModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SendReserveManageRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private SendReserveManageRepository sendReserveManageRepository;

    SendReserveManageModel sendReserveManage;
    SendReserveManageModel sendReserveManageSaved;


    @BeforeEach
    void setup() {

        sendReserveManage = SendReserveManageModel.builder().date(new Timestamp(1692037973)).initCarSended(true).externalEvent(943562164).build();
        sendReserveManageSaved = SendReserveManageModel.builder().date(new Timestamp(1692037973)).initCarSended(true).externalEvent(943562165).build();
    }


    @Test
    void testFindByExternalEvent_ReturnSendReserveManage() {

        SendReserveManageModel sendReserveManageModel = sendReserveManageRepository.save(sendReserveManage);

        Optional<SendReserveManageModel> result = sendReserveManageRepository.findByExternalEvent(sendReserveManageModel.getExternalEvent());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(SendReserveManageModel.class);
        Assertions.assertEquals(sendReserveManageModel, result.get());

    }

    @Test
    void saveSendReserveManageModel() {

        SendReserveManageModel result = sendReserveManageRepository.save(sendReserveManageSaved);

        try {
            AttributeAssertions.assertAttributesEqual(sendReserveManageSaved, result);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            System.out.println("Error cause: " + e.getMessage());
        }

    }
}
