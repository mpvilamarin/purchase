package com.subocol.manage.purchase.infrastructure.persistence.adapters;

import com.subocol.manage.purchase.domain.models.SendReserveManage;
import com.subocol.manage.purchase.domain.ports.persistence.ProductOrdersPiecesNoticeRepositoryPort;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveCalculationTotalSuraDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ReserveRepuestosSuraDTO;
import com.subocol.manage.purchase.infrastructure.persistence.entities.SendReserveManageModel;
import com.subocol.manage.purchase.infrastructure.persistence.repositories.ProductOrdersPiecesNoticeRepository;
import jakarta.persistence.Tuple;
import org.hibernate.jpa.spi.NativeQueryTupleTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ProductOrdersPiecesNoticeAdapterTest {

    @Mock
    private ProductOrdersPiecesNoticeRepository repository;

    @InjectMocks
    private ProductOrdersPiecesNoticeAdapter productOrdersPiecesNoticeAdapter;

    @BeforeEach
    void setup() {

    }

    @Test
    void testCountAllByExternalEventAndUnforeseen() {

        int externalEvent = 123;
        List<Integer> positions = Arrays.asList(1, 2, 3, 4, 5, 6);

        when(repository.totalPriceOrdersByExternalEventAndEventId(anyInt(),anyBoolean(),anyList(),anyBoolean())).thenReturn(264.0);

        double result = productOrdersPiecesNoticeAdapter.totalPriceOrdersByExternalEventAndEventId(externalEvent, true, positions, true);

        assertEquals(264.0, result);
        verify(repository, times(1)).totalPriceOrdersByExternalEventAndEventId(anyInt(),anyBoolean(),anyList(),anyBoolean());
        verifyNoMoreInteractions(repository);

    }
    @Test
    public void testTotalGrossPriceOrdersByExternalEventAndEventId() {
        Integer externalEvent = 123;
        List<String> type = Arrays.asList("Type1", "Type2");
        List<Integer> positionPiece = Arrays.asList(1, 2);
        boolean unforeseen = false;

        NativeQueryTupleTransformer nativeQueryTupleTransformer=new NativeQueryTupleTransformer();
        Object[] objectsForTuple1={200D, 20D, 180D};
        String[] aliasForTuple={"total", "totalIva", "totalDescuento"};
        Tuple tuple1= nativeQueryTupleTransformer.transformTuple(objectsForTuple1, aliasForTuple);

        when(repository.totalGrossPriceOrdersByExternalEventAndEventIdTuple(
                externalEvent, positionPiece, unforeseen))
                .thenReturn(tuple1);

        ReserveCalculationTotalSuraDTO resultDTO = productOrdersPiecesNoticeAdapter.totalGrossPriceOrdersByExternalEventAndEventId(
                externalEvent, positionPiece, unforeseen);

        verify(repository).totalGrossPriceOrdersByExternalEventAndEventIdTuple(
                externalEvent, positionPiece, unforeseen);

        assertEquals(tuple1.get("total", Double.class), resultDTO.getTotalRepuestos());
        assertEquals(tuple1.get("totalIva", Double.class), resultDTO.getPrecioTotalIva());
        assertEquals(tuple1.get("totalDescuento", Double.class), resultDTO.getValorTotalDescuento());
    }
    @Test
    void testFindPiecesOrdersByExternalEvent() {
        Integer externalEvent = 123;
        List<String> type = Arrays.asList("Type1", "Type2");
        List<Integer> positionPiece = Arrays.asList(1, 2);

        List<ReserveRepuestosSuraDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(new ReserveRepuestosSuraDTO(
                "123", 1, "Pieza1", "REF1", 10, 50.0, 5.0, 10.0, "ORIGEN1", true, 15.0, "PROV1", 10D
        ));
        expectedDTOs.add(new ReserveRepuestosSuraDTO(
                "456", 2, "Pieza2", "REF2", 20, 30.0, 3.0, 5.0, "ORIGEN2", false, 10.0, "PROV2", 10D
        ));

        when(repository.findPiecesOrdersByExternalEvent(
                externalEvent, type, positionPiece))
                .thenReturn(expectedDTOs);

        List<ReserveRepuestosSuraDTO> resultDTOs = productOrdersPiecesNoticeAdapter.findPiecesOrdersByExternalEvent(
                externalEvent, type, positionPiece);

        verify(repository).findPiecesOrdersByExternalEvent(
                externalEvent, type, positionPiece);

        assertEquals(expectedDTOs.size(), resultDTOs.size());

        assertEquals(expectedDTOs.get(0).getCodigoPieza(), resultDTOs.get(0).getCodigoPieza());
        assertEquals(expectedDTOs.get(0).getPosicionRepuesto(), resultDTOs.get(0).getPosicionRepuesto());
        assertEquals(expectedDTOs.get(0).getNombrePieza(), resultDTOs.get(0).getNombrePieza());
        assertEquals(expectedDTOs.get(0).getCodReferenciaPieza(), resultDTOs.get(0).getCodReferenciaPieza());

        assertEquals(expectedDTOs.get(1).getCodigoPieza(), resultDTOs.get(1).getCodigoPieza());
        assertEquals(expectedDTOs.get(1).getPosicionRepuesto(), resultDTOs.get(1).getPosicionRepuesto());
        assertEquals(expectedDTOs.get(1).getNombrePieza(), resultDTOs.get(1).getNombrePieza());
        assertEquals(expectedDTOs.get(1).getCodReferenciaPieza(), resultDTOs.get(1).getCodReferenciaPieza());
    }
}
