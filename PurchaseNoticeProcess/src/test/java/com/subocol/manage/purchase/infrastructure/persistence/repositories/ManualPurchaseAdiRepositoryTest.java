package com.subocol.manage.purchase.infrastructure.persistence.repositories;



import com.subocol.manage.purchase.infrastructure.persistence.entities.InsurerModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.ManualPurchaseAdiModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ManualPurchaseAdiRepositoryTest {
    @Autowired
    private ManualPurchaseAdiRepository manualPurchaseAdiRepository;

    @Test
    void getAllByEventFilterManualPurchaseForAuth() {
        //Given-preconditions
        Long externalEvent=659542227L;
        Long eventId=31250L;
        Long noticeId=28214L;
        String brand="TOYOTA";
        boolean totParameter=true;
        //when-action to do
        List<ManualPurchaseAdiModel> result= manualPurchaseAdiRepository.getAllByEventFilterManualPurchaseForAuth(
                externalEvent, eventId, noticeId, totParameter);
        //then-verify result
        assertThat(result).hasSize(86).asList().hasOnlyElementsOfType(ManualPurchaseAdiModel.class);
        assertThat(result.stream().findAny()).isPresent().get().extracting("externalEvent").isEqualTo(externalEvent);
        assertThat(result.stream().findAny()).isPresent().get().extracting("eventId").isEqualTo(eventId);
        assertThat(result.stream().findAny()).isPresent().get().extracting("brand").isEqualTo(brand);
    }

    @Test
    void getAllByEventFilterManualPurchase() {
        //Given-preconditions
        Long externalEvent=659542227L;
        Long eventId=31250L;
        String brand="TOYOTA";
        boolean totParameter=true;
        //when-action to do
        List<ManualPurchaseAdiModel> result= manualPurchaseAdiRepository.getAllByEventFilterManualPurchase(
                externalEvent, eventId,  totParameter);
        //then-verify result
        assertThat(result).hasSize(86).asList().hasOnlyElementsOfType(ManualPurchaseAdiModel.class);
        assertThat(result.stream().findAny()).isPresent().get().extracting("externalEvent").isEqualTo(externalEvent);
        assertThat(result.stream().findAny()).isPresent().get().extracting("eventId").isEqualTo(eventId);
        assertThat(result.stream().findAny()).isPresent().get().extracting("brand").isEqualTo(brand);
    }
}