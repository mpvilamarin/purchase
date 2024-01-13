package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.amazonaws.auth.*;
import com.subocol.manage.purchase.infrastructure.persistence.entities.PieceModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.PieceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PieceAdapterTest {

    @Mock
    private PieceRepository pieceRepository;

    @InjectMocks
    private PieceAdapter pieceAdapter;

    @Test
    void PieceAdapter_countPiecesByExternalEventAndPositions_ReturnsCountPieces() {

        String externalEvent = "11198";
        List<Integer> positions = Arrays.asList(1, 2, 3, 4);

        when(pieceRepository.countPiecesByExternalEventAndPositions(anyInt(), anyList())).thenReturn(3);

        int count = pieceAdapter.countPiecesByExternalEventAndPositions(externalEvent, positions);

        assertThat(count).isPositive().isEqualTo(3);
        verify(pieceRepository, times(1)).countPiecesByExternalEventAndPositions(Integer.valueOf(externalEvent), positions);

    }

    @Test
    void PieceAdapter_countPiecesByExternalEventAndPositions_ReturnsZeroCountPieces() {

        String externalEvent = "11198";
        List<Integer> positions = Arrays.asList(4, 5, 6);

        when(pieceRepository.countPiecesByExternalEventAndPositions(anyInt(), anyList())).thenReturn(0);

        int count = pieceAdapter.countPiecesByExternalEventAndPositions(externalEvent, positions);

        assertThat(count).isZero();
        verify(pieceRepository, times(1)).countPiecesByExternalEventAndPositions(Integer.valueOf(externalEvent), positions);
    }


    @Test
    void testFindInitialPiecesByExternalEvent_ReturnsCountPieces() {

        Integer externalEvent = 11198;
        boolean singlePackage = true;
        boolean unforeseen = true;
        List<Integer> positions = Arrays.asList(1, 2, 3, 4);

        when(pieceRepository.findInitialPiecesByExternalEventBolivarConditionTrue(anyInt(), anyBoolean())).thenReturn(positions);

        List<Integer> count = pieceAdapter.findInitialPiecesByExternalEventBolivarConditionTrue(externalEvent, unforeseen);

        assertNotNull(count);
        assertEquals(count.size(), positions.size());
        verify(pieceRepository, times(1)).findInitialPiecesByExternalEventBolivarConditionTrue(anyInt(), anyBoolean());
        verifyNoMoreInteractions(pieceRepository);
    }

    @Test
    void testFindInitialPiecesByExternalWithOrders_ReturnsCountPieces() {

        Integer externalEvent = 11198;
        boolean singlePackage = true;
        boolean unforeseen = true;
        List<Integer> positions = Arrays.asList(1, 2, 3, 4, 5, 6);

        when(pieceRepository.findInitialPiecesByExternalEventWithOrders(anyInt(), anyBoolean())).thenReturn(positions);

        List<Integer> count = pieceAdapter.findInitialPiecesByExternalWithOrders(externalEvent, unforeseen);

        assertNotNull(count);
        assertEquals(count.size(), positions.size());
        verify(pieceRepository, times(1)).findInitialPiecesByExternalEventWithOrders(anyInt(), anyBoolean());
        verifyNoMoreInteractions(pieceRepository);
    }

    @Test
    void testFindInitialPiecesByExternalEventSura_ReturnsCountPieces() {

        Integer externalEvent = 11198;
        boolean singlePackage = true;
        boolean unforeseen = true;
        List<Integer> positions = Arrays.asList(1, 2, 3, 4);

        when(pieceRepository.findInitialPiecesByExternalEventSuraConditionTrue(anyInt(), anyBoolean())).thenReturn(positions);

        List<Integer> count = pieceAdapter.findInitialPiecesByExternalEventSuraConditionTrue(externalEvent, unforeseen);

        assertNotNull(count);
        assertEquals(count.size(), positions.size());
        verify(pieceRepository, times(1)).findInitialPiecesByExternalEventSuraConditionTrue(anyInt(), anyBoolean());
        verifyNoMoreInteractions(pieceRepository);
    }

    @Test
    void testFindInitialPiecesByExternalWithOrdersSura_ReturnsCountPieces() {

        Integer externalEvent = 11198;
        boolean singlePackage = true;
        boolean unforeseen = true;
        List<Integer> positions = Arrays.asList(1, 2, 3, 4, 5, 6);

        when(pieceRepository.findInitialPiecesByExternalEventWithOrdersSura(anyInt(), anyBoolean())).thenReturn(positions);

        List<Integer> count = pieceAdapter.findInitialPiecesByExternalWithOrdersSura(externalEvent, unforeseen);

        assertNotNull(count);
        assertEquals(count.size(), positions.size());
        verify(pieceRepository, times(1)).findInitialPiecesByExternalEventWithOrdersSura(anyInt(), anyBoolean());
        verifyNoMoreInteractions(pieceRepository);

    }

}
