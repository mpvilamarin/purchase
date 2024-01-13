package com.subocol.manage.purchase.application;

import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.application.dtos.DeletePiecesManualPurchaseDTO;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.DeletePiecesMainController;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.servicesimpl.DeletePieces;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeletePiecesMainControllerTest {

    @Mock
    private DeletePieces deletePieces;

    @InjectMocks
    private DeletePiecesMainController deletePiecesMainController;

    DeletePiecesManualPurchaseDTO deletePiecesManualPurchaseDTO;

    @BeforeEach
    void setup(){
        deletePiecesManualPurchaseDTO= DeletePiecesManualPurchaseDTO.builder()
                .positionPiece(1).userName("Jhon117").externalEvent("1234").deletedCause("This is a test!").build();
    }
    @Test
    void testManageNoticeMM_Success() throws ExceptionUtil {
        //Given-preconditions

        when(deletePieces.deletePieces(deletePiecesManualPurchaseDTO)).thenReturn(true);

        // When-Action to do
        ResponseDTO actualResponse = deletePiecesMainController.deletePieces(deletePiecesManualPurchaseDTO);

        // then- Validations
        Assertions.assertEquals(HttpStatus.OK.value(), actualResponse.getStatus());
        Assertions.assertEquals("Pieces deleted successfully", actualResponse.getMessage());
        Assertions.assertTrue(actualResponse.isSuccess());
        Assertions.assertNull(actualResponse.getData());
    }


    @Test
    void testManageNoticeMM_ExceptionUtil() throws ExceptionUtil {
        // Given-precontidions
        when(deletePieces.deletePieces(deletePiecesManualPurchaseDTO)).thenThrow(new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), MessageResponse.REQUEST_NOT_SUCCESS));

        // When-Action to do
        ResponseDTO actualResponse = deletePiecesMainController.deletePieces(deletePiecesManualPurchaseDTO);

        // then- Validations
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), actualResponse.getStatus());
        Assertions.assertEquals(MessageResponse.REQUEST_NOT_SUCCESS, actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertNull(actualResponse.getData());
    }

    @Test
    void testManageNoticeMM_GenericException() throws ExceptionUtil {
        // Given-Preconditions
        RuntimeException exception = new RuntimeException("Generic Exception");
        exception.initCause(new Exception("Cause of the exception"));

        when(deletePieces.deletePieces(deletePiecesManualPurchaseDTO)).thenThrow(exception);


        // When- Action to do
        ResponseDTO actualResponse = deletePiecesMainController.deletePieces(deletePiecesManualPurchaseDTO);

        // then-Validations
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actualResponse.getStatus());
        Assertions.assertEquals("Generic Exception", actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertEquals(exception.getMessage(), actualResponse.getData());
    }
}
