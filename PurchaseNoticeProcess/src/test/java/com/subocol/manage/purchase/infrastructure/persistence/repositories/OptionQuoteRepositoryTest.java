package com.subocol.manage.purchase.infrastructure.persistence.repositories;


import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.OptionQuoteModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OptionQuoteRepositoryTest {
    @Autowired
    private OptionQuoteRepository optionQuoteRepository;
    private OptionQuoteModel optionQuoteModel;
    @BeforeEach
    void setup(){
        optionQuoteModel = OptionQuoteModel.builder().id(987654321L)
                .event("987654321").irs("2654823sdas").noticeId(1L)
                .piece("bomper delantero").breach(0D).deliveryTime(3)
                .positionPiece(1).priority(1).price(1000D).reference("reference")
                .subsidiaryId(1L).date(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }



    @Test
    void saveOptionQuoteModel() {
        //Given-preconditions

        //when-action to do
        OptionQuoteModel result= optionQuoteRepository.save(optionQuoteModel);
        //then-verify result
        assertThat(result.getId()).isPositive();
        Assertions.assertEquals(optionQuoteModel.getPriority(), result.getPriority());
        Assertions.assertEquals(optionQuoteModel.getReference(), result.getReference());
        Assertions.assertEquals(optionQuoteModel.getEvent(), result.getEvent());
        Assertions.assertEquals(optionQuoteModel.getPiece(), result.getPiece());
        Assertions.assertEquals(optionQuoteModel.getNoticeId(), result.getNoticeId());

    }

    @Test
    void findOptionQuoteById() {
        //Given-preconditions

        OptionQuoteModel optionQuotedModelSave= optionQuoteRepository.save(optionQuoteModel);

        //when-action to do
        Optional<OptionQuoteModel> result= optionQuoteRepository.findById(optionQuotedModelSave.getId());

        //then-verify result
        assertThat(result).isPresent();
        assertThat(result.get()).isExactlyInstanceOf(OptionQuoteModel.class);
        Assertions.assertEquals(optionQuotedModelSave, result.get());

    }

    @Test
    void updateOptionQuote() {
        //Given-preconditions
        OptionQuoteModel optionQuoteModelSave= optionQuoteRepository.save(optionQuoteModel);
        optionQuoteModelSave.setReference("ReferenceUpdate").setPriority(5).setBreach(10D);
        //when-action to do
        OptionQuoteModel optionQuoteModelUpdate= optionQuoteRepository.save(optionQuoteModelSave);

        //then-verify result
        Assertions.assertEquals(optionQuoteModelSave.getReference(), optionQuoteModelUpdate.getReference());
        Assertions.assertEquals(optionQuoteModelSave.getPriority(), optionQuoteModelUpdate.getPriority());
        Assertions.assertEquals(optionQuoteModelSave.getBreach(), optionQuoteModelUpdate.getBreach());

    }
    @Test
    void deleteOptionQuote() {
        //Given-preconditions
        OptionQuoteModel optionQuotedModelSave= optionQuoteRepository.save(optionQuoteModel);

        //when-action to do
        optionQuoteRepository.deleteById(optionQuotedModelSave.getId());

        Optional<OptionQuoteModel> result= optionQuoteRepository.findById(optionQuotedModelSave.getId());
        //then-verify result
        assertThat(optionQuotedModelSave.getId()).isPositive();
        assertThat(result).isNotPresent();
    }

    @Test
    void findPiecesValuationMultibrandAccepted() {
        //Given-preconditions
        Integer externalEvent=484617;

        //when-action to do
        List<PiecesValuationDTO> result= optionQuoteRepository.getPiecesValuationMultibrandAccepted(
                externalEvent);

        log.info(String.valueOf(result.size()));

        //then-verify result

        assertThat(result).hasSize(5).asList().hasOnlyElementsOfType(PiecesValuationDTO.class);
        assertThat(result.stream().findAny()).isPresent().get().extracting("comprada").isEqualTo("S");
        assertThat(result.stream().findAny()).isPresent().get().extracting("calidadRepuesto").isEqualTo("Original");

    }

    @Test
    void findPiecesValuationMultibrandQuoted() {
        //Given-preconditions
        Integer externalEvent=484620;

        //when-action to do
        List<PiecesValuationDTO> result= optionQuoteRepository.getPiecesValuationMultibrandQuoted(
                externalEvent);

        log.info(String.valueOf(result.size()));

        //then-verify result

        assertThat(result).hasSize(4).asList().hasOnlyElementsOfType(PiecesValuationDTO.class);
        assertThat(result.stream().findAny()).isPresent().get().extracting("comprada").isEqualTo("N");
        assertThat(result.stream().findAny()).isPresent().get().extracting("calidadRepuesto").isEqualTo("Original");

    }
/////////////////////////////////////
    @Test
    @Disabled
    void testGetPiecesValuationMultiBrandAccepted() {

        List<PiecesValuationDTO> result= optionQuoteRepository.getPiecesValuationMultibrandAccepted(94324651);

        log.info(String.valueOf(result.size()));

        Assertions.assertEquals(5, result.size());
        Assertions.assertEquals("S", result.get(0).getComprada());
        Assertions.assertEquals("S", result.get(1).getComprada());
        Assertions.assertEquals("S", result.get(2).getComprada());
        Assertions.assertEquals("S", result.get(3).getComprada());
        Assertions.assertEquals("S", result.get(4).getComprada());

    }

    @Test
    @Disabled
    void testGetPiecesValuationMultiBrandQuoted() {

        List<PiecesValuationDTO> result= optionQuoteRepository.getPiecesValuationMultibrandQuoted(94324654);

        log.info(String.valueOf(result.size()));

        Assertions.assertEquals(5, result.size());
        Assertions.assertEquals("N", result.get(0).getComprada());
        Assertions.assertEquals("N", result.get(1).getComprada());
        Assertions.assertEquals("N", result.get(2).getComprada());
        Assertions.assertEquals("N", result.get(3).getComprada());
        Assertions.assertEquals("N", result.get(4).getComprada());

    }

}
