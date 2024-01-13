package com.subocol.manage.purchase.application;

import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.AuthNoticeWithPiecesMainController;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.servicesimpl.AuthNoticeWithPieces;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.AuthNoticePiecesDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesQuoteAuthDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthNoticeWithPiecesMainControllerTest {

    @Mock
    private AuthNoticeWithPieces authNoticeWithPieces;

    @InjectMocks
    private AuthNoticeWithPiecesMainController authNoticeWithPiecesMainController;

    private AuthNoticePiecesDTO authNoticePiecesDTO;

    @BeforeEach
    void setup() {
        List<PiecesQuoteAuthDTO> spareParts = new ArrayList<>();
        spareParts.add(PiecesQuoteAuthDTO.builder()
                .position(1)
                .code("06030006nitn")
                .reference("REPU")
                .quantity(1)
                .description("llanta trasero izquierdo")
                .unitValue(0D)
                .sparePartQuality("ORIGINAL")
                .build());

        spareParts.add(PiecesQuoteAuthDTO.builder()
                .position(2)
                .code("06030006nitn")
                .reference("REPU")
                .quantity(1)
                .description("llanta trasero izquierdo")
                .unitValue(0D)
                .sparePartQuality("ORIGINAL")
                .build());

        this.authNoticePiecesDTO = AuthNoticePiecesDTO.builder()
                .externalEvent("999999999")
                .claimNumber("4899")
                .spareParts(spareParts)
                .build();
    }

    @Test
    void testAuthNoticeWithPiecesSuccess() {
        ResponseDTO response = new ResponseDTO();
        response.setMessage("Se autorizo aviso 999999999 NoticeId 999999999")
                .setStatus(200)
                .setSuccess(true);

        when(authNoticeWithPieces.auth(any(AuthNoticePiecesDTO.class))).thenReturn(response);

        ResponseDTO actualResponse = authNoticeWithPiecesMainController.authNotice(authNoticePiecesDTO);

        Assertions.assertEquals(HttpStatus.OK.value(), actualResponse.getStatus());
        Assertions.assertEquals("Se autorizo aviso 999999999 NoticeId 999999999", actualResponse.getMessage());
        Assertions.assertTrue(actualResponse.isSuccess());
    }

    @Test
    void testAuthNoticeWithPiecesFailure() {

        when(authNoticeWithPieces.auth(any(AuthNoticePiecesDTO.class))).thenThrow(new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), MessageResponse.REQUEST_NOT_SUCCESS));

        ResponseDTO actualResponse = authNoticeWithPiecesMainController.authNotice(authNoticePiecesDTO);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), actualResponse.getStatus());
        Assertions.assertEquals(MessageResponse.REQUEST_NOT_SUCCESS, actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertNull(actualResponse.getData());

    }

    @Test
    void testAuthNoticeWithPiecesGenericException() throws ExceptionUtil {

        RuntimeException exception = new RuntimeException("Generic Exception");
        exception.initCause(new Exception("Cause of the exception"));

        when(authNoticeWithPieces.auth(any(AuthNoticePiecesDTO.class))).thenThrow(exception);

        ResponseDTO actualResponse = authNoticeWithPiecesMainController.authNotice(authNoticePiecesDTO);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actualResponse.getStatus());
        Assertions.assertEquals("Generic Exception", actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertEquals(exception.getMessage(), actualResponse.getData());
    }
}
