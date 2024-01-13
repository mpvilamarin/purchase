package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.common.utils.TimeZoneUtil;
import com.subocol.manage.purchase.domain.models.Notice;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesValuationDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.NoticeModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.OptionQuoteRepository;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OptionQuoteAdapterTest {

    @Mock
    private OptionQuoteRepository repository;
    @InjectMocks
    private OptionQuoteAdapter optionQuoteAdapter;

    Integer externalEvent = 46235;
    PiecesValuationDTO piecesQuoted1;
    PiecesValuationDTO piecesQuoted2;
    PiecesValuationDTO piecesAccepted1;
    PiecesValuationDTO piecesAccepted2;
    List<PiecesValuationDTO> piecesAccepted = new ArrayList<>();
    List<PiecesValuationDTO> piecesQuoted = new ArrayList<>();
    @BeforeEach
    void setup() {

        piecesQuoted1 = PiecesValuationDTO.builder().posicion(3).codigo("02100043nndn").referencia("Sin Referencia").cantidad(1)
                .valorUnitario(10.0).valorUnitarioConDescuento(2.3).comprada("N").tiempoEstimadoEntrega(1).calidadRepuesto("ORIGINAL")
                .descuento(77.0).nombreSucursalGanadora("PROVEEDOR AUTOSUM DEV").build();

        piecesQuoted2 = PiecesValuationDTO.builder().posicion(4).codigo("02100043nndn").referencia("Sin Referencia").cantidad(1)
                .valorUnitario(10.0).valorUnitarioConDescuento(2.3).comprada("N").tiempoEstimadoEntrega(1).calidadRepuesto("ORIGINAL")
                .descuento(77.0).nombreSucursalGanadora("PROVEEDOR AUTOSUM DEV").build();

        piecesAccepted1 = PiecesValuationDTO.builder().posicion(1).codigo("02100043nndn").referencia("Sin Referencia").cantidad(1)
                .valorUnitario(10.0).valorUnitarioConDescuento(2.3).comprada("S").tiempoEstimadoEntrega(1).calidadRepuesto("ORIGINAL")
                .descuento(77.0).nombreSucursalGanadora("PROVEEDOR AUTOSUM DEV").build();

        piecesAccepted2 = PiecesValuationDTO.builder().posicion(2).codigo("02100043nndn").referencia("Sin Referencia").cantidad(1)
                .valorUnitario(10.0).valorUnitarioConDescuento(2.3).comprada("S").tiempoEstimadoEntrega(1).calidadRepuesto("ORIGINAL")
                .descuento(77.0).nombreSucursalGanadora("PROVEEDOR AUTOSUM DEV").build();

        piecesAccepted = new ArrayList<>();
        piecesAccepted.add(piecesAccepted1);
        piecesAccepted.add(piecesAccepted2);

        piecesQuoted = new ArrayList<>();
        piecesQuoted.add(piecesQuoted1);
        piecesQuoted.add(piecesQuoted2);

    }

    @Test
    void findFindPiecesValuationMultibrandAccepted() {

        when(repository.getPiecesValuationMultibrandAccepted(anyInt())).thenReturn(piecesAccepted);

        List<PiecesValuationDTO> result = optionQuoteAdapter.findPiecesValuationMultibrandAccepted(externalEvent);

        assertNotNull(result);
        assertEquals(piecesAccepted.size(), result.size());
        assertEquals( "S",result.get(0).getComprada());
        assertEquals( "S",result.get(1).getComprada());
    }

    @Test
    void findFindPiecesValuationMultibrandQuoted() {

        when(repository.getPiecesValuationMultibrandQuoted(anyInt())).thenReturn(piecesQuoted);

        List<PiecesValuationDTO> result = optionQuoteAdapter.findPiecesValuationMultibrandQuoted(externalEvent);

        assertNotNull(result);
        assertEquals(piecesAccepted.size(), result.size());
        assertEquals( "N",result.get(0).getComprada());
        assertEquals( "N",result.get(1).getComprada());
    }
}
