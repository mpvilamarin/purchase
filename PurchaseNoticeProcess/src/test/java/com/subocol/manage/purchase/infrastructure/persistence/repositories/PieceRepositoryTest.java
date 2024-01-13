package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.common.TestConstants;
import com.subocol.manage.purchase.infrastructure.persistence.entities.DataEventModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.PieceModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Slf4j
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PieceRepositoryTest {
    @Autowired
    private PieceRepository pieceRepository;
    private PieceModel pieceModel;

    @BeforeEach
    void setup() {
        pieceModel = PieceModel.builder()
                .dataEvent(new DataEventModel()
                        .setId(1L)
                        .setExternalEvent(1234))
                .code("code_piece")
                .description("bomber delantero")
                .position(1)
                .quality("ORIGINAL")
                .quantity(5)
                .type("REPU")
                .unitValue(0D)
                .reQuote(false)
                .groupName("GNM1")
                .observations("Sample observations")
                .build();

    }


    @Test
    void savePieceModel() {
        //Given-preconditions

        //when-action to do
        PieceModel result = pieceRepository.save(pieceModel);
        //then-verify result
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(pieceModel.getDataEvent(), result.getDataEvent());
        Assertions.assertEquals(pieceModel.getPosition(), result.getPosition());
        Assertions.assertEquals(pieceModel.getQuality(), result.getQuality());
        Assertions.assertEquals(pieceModel.getQuantity(), result.getQuantity());
        Assertions.assertEquals(pieceModel.getType(), result.getType());

    }

    @Test
    void findPieceById() {
        //Given-preconditions

        PieceModel orderModelSave = pieceRepository.save(pieceModel);

        //when-action to do
        Optional<PieceModel> result = pieceRepository.findById(orderModelSave.getId());

        //then-verify result
        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(PieceModel.class);
        Assertions.assertEquals(orderModelSave, result.get());

    }

    @Test
    void updatePiece() {
        //Given-preconditions
        PieceModel pieceModelSave = pieceRepository.save(pieceModel);
        pieceModelSave.setQuality("USED").setQuantity(10).setDescription("Bomper delantero rojo");
        //when-action to do
        PieceModel pieceModelUpdate = pieceRepository.save(pieceModelSave);

        //then-verify result
        Assertions.assertEquals(pieceModelSave.getQuality(), pieceModelUpdate.getQuality());
        Assertions.assertEquals(pieceModelSave.getQuantity(), pieceModelUpdate.getQuantity());
        Assertions.assertEquals(pieceModelSave.getDescription(), pieceModelUpdate.getDescription());

    }

    @Test
    void deletePiece() {
        //Given-preconditions
        PieceModel pieceModelSave = pieceRepository.save(pieceModel);

        //when-action to do
        pieceRepository.deleteById(pieceModelSave.getId());

        Optional<PieceModel> result = pieceRepository.findById(pieceModelSave.getId());
        //then-verify result
        assertThat(pieceModelSave.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

    @Test
    void PieceRepository_countPiecesByExternalEventAndPositions_ReturnZeroCount() {
        PieceModel pieceModelSave = pieceRepository.save(pieceModel);

        int count = pieceRepository.countPiecesByExternalEventAndPositions(
                pieceModelSave.getDataEvent().getExternalEvent(),
                Collections.singletonList(pieceModelSave.getPosition()));

        assertThat(count).isZero();
    }

    @Test
    @Disabled(TestConstants.SKIP_DATABASE_DEPENDENCE)
    void PieceRepository_countPiecesByExternalEventAndPositions_ReturnCount() {
        //DataEventId = 1L, 3 PiecesIds(1L, 2L, 3L)
        int externalEvent = 11198;
        List<Integer> positions = Arrays.asList(1, 2, 3, 4);

        int count = pieceRepository.countPiecesByExternalEventAndPositions(externalEvent, positions);

        assertThat(count).isPositive().isEqualTo(3);
    }

    @Test
    @Disabled(TestConstants.SKIP_DATABASE_DEPENDENCE)
    void testFindInitialPiecesByExternalEvent_ReturnCount() {

        int externalEvent = 435712;
        boolean singlePackage = true;
        boolean unforeseen = true;
        int expectedCount = 3;

        List<Integer> count = pieceRepository.findInitialPiecesByExternalEventBolivarConditionTrue(externalEvent, unforeseen);

        assertNotNull(count);
        assertFalse(count.isEmpty());
        assertEquals(expectedCount, count.size());

    }

    @Test
    @Disabled(TestConstants.SKIP_DATABASE_DEPENDENCE)
    void testFindInitialPiecesByExternalWithOrders_ReturnCount() {

        int externalEvent = 435802;
        boolean singlePackage = true;
        boolean unforeseen = false;
        int expectedCount = 2;

        List<Integer> count = pieceRepository.findInitialPiecesByExternalEventWithOrders(externalEvent, unforeseen);

        assertNotNull(count);
        assertFalse(count.isEmpty());
        assertEquals(expectedCount, count.size());

    }

}
