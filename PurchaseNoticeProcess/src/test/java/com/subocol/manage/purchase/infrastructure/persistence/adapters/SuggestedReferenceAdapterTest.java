package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.infrastructure.persistence.repositories.SuggestedReferenceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuggestedReferenceAdapterTest {

    @Mock
    private SuggestedReferenceRepository repository;

    @InjectMocks
    private SuggestedReferenceAdapter suggestedReferenceAdapter;

    @Test
    void SuggestedReferenceAdapter_findListSuggestedReferenceByEventAndPosition_ReturnListOfStrings() {

        List<String> referencesList = Arrays.asList("Ref 3", "Ref 2", "Ref 1");

        when(repository.getAllReferenceByPiece_PositionAndDataEvent_ID(anyLong(), anyInt(), Mockito.any(Pageable.class)))
                .thenReturn(referencesList);

        List<String> resultList = suggestedReferenceAdapter
                .findListSuggestedReferenceByEventAndPosition(1L, 1, 3);

        verify(repository, times(1)).getAllReferenceByPiece_PositionAndDataEvent_ID(1L, 1, Pageable.ofSize(3));
        Assertions.assertThat(resultList).isNotEmpty().hasSize(3);

    }

    @Test
    void SuggestedReferenceAdapter_getAllReferenceByPiece_PositionAndDataEvent_ID_ReturnEmptyList() {

        List<String> referencesList = Collections.emptyList();

        when(repository.getAllReferenceByPiece_PositionAndDataEvent_ID(anyLong(), anyInt(), Mockito.any(Pageable.class)))
                .thenReturn(referencesList);

        List<String> resultList = suggestedReferenceAdapter
                .findListSuggestedReferenceByEventAndPosition(1L, 1, 5);

        verify(repository, times(1)).getAllReferenceByPiece_PositionAndDataEvent_ID(1L, 1, Pageable.ofSize(5));
        Assertions.assertThat(resultList).isEmpty();

    }

    @Test
    void SuggestedReferenceAdapter_getAllReferenceByPiece_PositionAndDataEvent_ID_ReturnPageableList() {

        List<String> referencesList = Arrays.asList("Ref 3", "Ref 2", "Ref 1");
        when(repository.getAllReferenceByPiece_PositionAndDataEvent_ID(anyLong(), anyInt(), Mockito.any(Pageable.class)))
                .thenReturn(referencesList);
        List<String> resultList = suggestedReferenceAdapter
                .findListSuggestedReferenceByEventAndPosition(1L, 1, 3);

        Assertions.assertThat(resultList).isNotEmpty().hasSize(3);

        verify(repository, times(1)).getAllReferenceByPiece_PositionAndDataEvent_ID(1L, 1, Pageable.ofSize(3));

        referencesList = Arrays.asList("Ref 2", "Ref 1");

        when(repository.getAllReferenceByPiece_PositionAndDataEvent_ID(anyLong(), anyInt(), Mockito.any(Pageable.class)))
                .thenReturn(referencesList);

        resultList = suggestedReferenceAdapter
                .findListSuggestedReferenceByEventAndPosition(1L, 1, 2);

        verify(repository, times(1)).getAllReferenceByPiece_PositionAndDataEvent_ID(1L, 1, Pageable.ofSize(2));

        Assertions.assertThat(resultList).isNotEmpty().hasSize(2);

    }
}