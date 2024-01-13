package com.subocol.manage.purchase.infrastructure.persistence.repositories;

import com.subocol.manage.purchase.infrastructure.persistence.entities.DataEventModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.PieceModel;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SuggestedReferenceModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SuggestedReferenceRepositoryTest {

    private final SuggestedReferenceRepository repository;

    private List<SuggestedReferenceModel> suggestedReferenceModelList;

    @Autowired
    public SuggestedReferenceRepositoryTest(SuggestedReferenceRepository repository) {
        this.repository = repository;
    }

    @BeforeEach
    void setup() {
        suggestedReferenceModelList = new ArrayList<>();

        DataEventModel dataEventModel = DataEventModel.builder()
                .id(1L)
                .build();

        PieceModel pieceModel = PieceModel.builder()
                .id(1)
                .position(1)
                .dataEvent(dataEventModel)
                .build();

        SuggestedReferenceModel firstSRM = SuggestedReferenceModel.builder()
                .reference("Ref 1")
                .price(2956.00)
                .detail("Detail test")
                .piece(pieceModel)
                .build();

        SuggestedReferenceModel secondSRM = SuggestedReferenceModel.builder()
                .reference("Ref 2")
                .price(1666.00)
                .detail("Detail test")
                .piece(pieceModel)
                .build();

        SuggestedReferenceModel thirdSRM = SuggestedReferenceModel.builder()
                .reference("Ref 3")
                .price(1836.00)
                .detail("Detail test")
                .piece(pieceModel)
                .build();

        suggestedReferenceModelList.add(firstSRM);
        suggestedReferenceModelList.add(secondSRM);
        suggestedReferenceModelList.add(thirdSRM);

    }

    @Test
    void SuggestedReferenceRepository_save_ReturnSavedSuggestedReference() {
        SuggestedReferenceModel firstSRM = suggestedReferenceModelList.get(0);

        SuggestedReferenceModel suggestedReferenceSaved = repository.save(firstSRM);

        assertNotNull(suggestedReferenceSaved);
        assertThat(suggestedReferenceSaved.getId()).isPositive();
        Assertions.assertEquals(firstSRM.getId(), suggestedReferenceSaved.getId());
        Assertions.assertEquals(firstSRM.getReference(), suggestedReferenceSaved.getReference());
        Assertions.assertEquals(firstSRM.getPrice(), suggestedReferenceSaved.getPrice());
        Assertions.assertEquals(firstSRM.getDetail(), suggestedReferenceSaved.getDetail());

        assertNotNull(suggestedReferenceSaved.getPiece());
        Assertions.assertEquals(firstSRM.getPiece().getId(), suggestedReferenceSaved.getPiece().getId());

        assertNotNull(suggestedReferenceSaved.getPiece().getDataEvent());
        Assertions.assertEquals(firstSRM.getPiece().getDataEvent().getId(),
                suggestedReferenceSaved.getPiece().getDataEvent().getId());
    }

    @Test
    void SuggestedReferenceRepository_findById_ReturnSuggestedReference() {

        SuggestedReferenceModel suggestedReferenceSaved = repository.save(suggestedReferenceModelList.get(0));
        Optional<SuggestedReferenceModel> result = repository.findById(suggestedReferenceSaved.getId());

        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(SuggestedReferenceModel.class);
        Assertions.assertEquals(suggestedReferenceSaved, result.get());

    }

    @Test
    void SuggestedReferenceRepository_update_ReturnUpdatedSuggestedReference() {

        SuggestedReferenceModel suggestedReferenceSaved = repository.save(suggestedReferenceModelList.get(0));

        suggestedReferenceSaved.setReference("Updated Reference")
                .setDetail("Updated Detail");

        SuggestedReferenceModel suggestedReferenceUpdated = repository.save(suggestedReferenceSaved);

        Assertions.assertEquals(suggestedReferenceSaved.getId(), suggestedReferenceUpdated.getId());
        Assertions.assertEquals(suggestedReferenceSaved.getReference(), suggestedReferenceUpdated.getReference());
        Assertions.assertEquals(suggestedReferenceSaved.getDetail(), suggestedReferenceUpdated.getDetail());
    }

    @Test
    void SuggestedReferenceRepository_delete() {

        SuggestedReferenceModel suggestedReferenceSaved = repository.save(suggestedReferenceModelList.get(0));

        repository.deleteById(suggestedReferenceSaved.getId());

        Optional<SuggestedReferenceModel> result = repository.findById(suggestedReferenceSaved.getId());

        assertThat(suggestedReferenceSaved.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

    @Test
    void SuggestedReferenceRepository_getAllReferenceByPiece_PositionAndDataEvent_ID_ReturnOrderedList() {

        SuggestedReferenceModel SRMSaved1 = repository.save(suggestedReferenceModelList.get(0));
        SuggestedReferenceModel SRMSaved2 = repository.save(suggestedReferenceModelList.get(1));
        SuggestedReferenceModel SRMSaved3 = repository.save(suggestedReferenceModelList.get(2));

        int cantRefToShow = 3;

        Pageable pageable = PageRequest.of(0, cantRefToShow);

        List<String> referenceList = repository.getAllReferenceByPiece_PositionAndDataEvent_ID(1L, 1, pageable);

        assertThat(referenceList).isNotEmpty().hasSize(3);

        assertThat(referenceList.get(0)).isEqualTo(SRMSaved3.getReference());
        assertThat(referenceList.get(1)).isEqualTo(SRMSaved2.getReference());
        assertThat(referenceList.get(2)).isEqualTo(SRMSaved1.getReference());

    }

    @Test
    void SuggestedReferenceRepository_getAllReferenceByPiece_PositionAndDataEvent_ID_ReturnEmptyList() {

        int cantRefToShow = 3;
        Pageable pageable = PageRequest.of(0, cantRefToShow);
        List<String> referenceList = repository.getAllReferenceByPiece_PositionAndDataEvent_ID(1L, 1, pageable);

        assertThat(referenceList).isEmpty();

    }

    @Test
    void SuggestedReferenceRepository_getAllReferenceByPiece_PositionAndDataEvent_ID_ReturnPageableList() {

        repository.save(suggestedReferenceModelList.get(0));
        repository.save(suggestedReferenceModelList.get(1));

        int cantRefToShow = 3;
        Pageable pageable = PageRequest.of(0, cantRefToShow);
        List<String> referenceList = repository.getAllReferenceByPiece_PositionAndDataEvent_ID(1L, 1, pageable);

        assertThat(referenceList).isNotEmpty().hasSize(2);

        cantRefToShow = 2;
        pageable = PageRequest.of(0, cantRefToShow);
        referenceList = repository.getAllReferenceByPiece_PositionAndDataEvent_ID(1L, 1, pageable);

        assertThat(referenceList).isNotEmpty().hasSize(cantRefToShow);

        cantRefToShow = 1;
        pageable = PageRequest.of(0, cantRefToShow);
        referenceList = repository.getAllReferenceByPiece_PositionAndDataEvent_ID(1L, 1, pageable);

        assertThat(referenceList).isNotEmpty().hasSize(cantRefToShow);

    }
}