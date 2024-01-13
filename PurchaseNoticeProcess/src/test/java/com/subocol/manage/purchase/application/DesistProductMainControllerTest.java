package com.subocol.manage.purchase.application;

import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.DesistProductMainController;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.servicesimpl.DesistProduct;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.DesistDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DesistProductMainControllerTest {

    @Mock
    private DesistProduct desistProduct;

    @InjectMocks
    private DesistProductMainController desistProductMainController;

    private DesistDTO desistDTO;

    @BeforeEach
    void setup() {
        desistDTO = DesistDTO.builder().userName("user").observation("").causal("precio mal cotizado").ids(List.of(54454L,44544L)).build();
    }

    @Test
    void testDesistProductSuccess() {
        ResponseDTO response = new ResponseDTO();
        response.setMessage("Se desistio correctamente los productos con ids : " + desistDTO.getIds())
        .setStatus(200)
        .setSuccess(true);

        when(desistProduct.desistProduct(any(DesistDTO.class))).thenReturn(response);

        ResponseDTO actualResponse = desistProductMainController.desistProduct(desistDTO);

        Assertions.assertEquals(HttpStatus.OK.value(), actualResponse.getStatus());
        Assertions.assertEquals("Se desistio correctamente los productos con ids : [54454, 44544]", actualResponse.getMessage());
        Assertions.assertTrue(actualResponse.isSuccess());
    }

    @Test
    void testDesistProductFailure() {

        when(desistProduct.desistProduct(any(DesistDTO.class))).thenThrow(new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), MessageResponse.REQUEST_NOT_SUCCESS));

        ResponseDTO actualResponse = desistProductMainController.desistProduct(desistDTO);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), actualResponse.getStatus());
        Assertions.assertEquals(MessageResponse.REQUEST_NOT_SUCCESS, actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertNull(actualResponse.getData());

    }

    @Test
    void testDesistProductGenericException() throws ExceptionUtil {

        RuntimeException exception = new RuntimeException("Generic Exception");
        exception.initCause(new Exception("Cause of the exception"));

        when(desistProduct.desistProduct(any(DesistDTO.class))).thenThrow(exception);

        ResponseDTO actualResponse = desistProductMainController.desistProduct(desistDTO);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actualResponse.getStatus());
        Assertions.assertEquals("Generic Exception", actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertEquals(exception.getMessage(), actualResponse.getData());
    }
}
