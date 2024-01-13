package com.subocol.manage.purchase.application;

import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.IntegrationServicesMainController;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ExternalIntegrationDTO;
import com.subocol.manage.purchase.domain.servicesimpl.integrations.Integrations;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IntegrationServicesMainControllerTest {

    @Mock
    private Integrations integrations;
    @InjectMocks
    private IntegrationServicesMainController integrationServicesMainController;
    private ExternalIntegrationDTO externalIntegrationDTO;

    @BeforeEach
    void setup() {
        externalIntegrationDTO = ExternalIntegrationDTO.builder().insurerId(500000002L).noticeId(642345L).orderIds(List.of(6432L,6433L,6434L)).build();
    }

    @Test
    void testIntegrationServicesSuccess() {
        ResponseDTO response = new ResponseDTO();
        response.setMessage("Se proceso correctamente el aviso: 642345 NoticeId  642345")
                .setStatus(200)
                .setSuccess(true);

        when(integrations.integrationServicesExposed(anyLong(),anyLong(),anyList())).thenReturn(response);

        ResponseDTO actualResponse = integrationServicesMainController.integrationServices(externalIntegrationDTO);

        Assertions.assertEquals(HttpStatus.OK.value(), actualResponse.getStatus());
        Assertions.assertEquals("Se proceso correctamente el aviso: 642345 NoticeId  642345", actualResponse.getMessage());
        Assertions.assertTrue(actualResponse.isSuccess());
    }

    @Test
    void testIntegrationServicesFailure() {

        when(integrations.integrationServicesExposed(anyLong(),anyLong(),anyList())).thenThrow(new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), MessageResponse.REQUEST_NOT_SUCCESS));

        ResponseDTO actualResponse = integrationServicesMainController.integrationServices(externalIntegrationDTO);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), actualResponse.getStatus());
        Assertions.assertEquals(MessageResponse.REQUEST_NOT_SUCCESS, actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertNull(actualResponse.getData());

    }

    @Test
    void testAuthNoticeWithPiecesGenericException() throws ExceptionUtil {

        RuntimeException exception = new RuntimeException("Generic Exception");
        exception.initCause(new Exception("Cause of the exception"));

        when(integrations.integrationServicesExposed(anyLong(),anyLong(),anyList())).thenThrow(exception);

        ResponseDTO actualResponse = integrationServicesMainController.integrationServices(externalIntegrationDTO);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actualResponse.getStatus());
        Assertions.assertEquals("Generic Exception", actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertEquals(exception.getMessage(), actualResponse.getData());
    }
}
