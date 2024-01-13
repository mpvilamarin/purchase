package com.subocol.manage.purchase.application;

import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.ManageNoticeMMMainController;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.servicesimpl.ManageNoticeMM;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManageNoticeMMMainControllerTest {

    @Mock
    private ManageNoticeMM manageNoticeMM;

    @InjectMocks
    private ManageNoticeMMMainController manageNoticeMMMainController;

    @Test
    void testManageNoticeMM_Success() throws ExceptionUtil {
        //Given-preconditions
        when(manageNoticeMM.manageNoticeByNoticeId(anyLong(), anyBoolean())).thenReturn(true);

        // When-Action to do
        ResponseDTO actualResponse = manageNoticeMMMainController.manageNoticeMM(123L);

        // then- Validations
        Assertions.assertEquals(HttpStatus.OK.value(), actualResponse.getStatus());
        Assertions.assertEquals(MessageResponse.REQUEST_IS_SUCCESS, actualResponse.getMessage());
        Assertions.assertTrue(actualResponse.isSuccess());
        Assertions.assertNull(actualResponse.getData());
    }

    @Test
    void testManageNoticeMM_Failure() throws ExceptionUtil {
        // Given-precontidions
        when(manageNoticeMM.manageNoticeByNoticeId(anyLong(), anyBoolean())).thenReturn(false);
        // When-Action to do
        ResponseDTO actualResponse = manageNoticeMMMainController.manageNoticeMM(456L);

        // then- Validations
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), actualResponse.getStatus());
        Assertions.assertEquals(MessageResponse.REQUEST_NOT_SUCCESS, actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertNull(actualResponse.getData());
    }

    @Test
    void testManageNoticeMM_Exception() throws ExceptionUtil {
        // Given-precontidions
        when(manageNoticeMM.manageNoticeByNoticeId(anyLong(), anyBoolean())).thenThrow(new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), MessageResponse.REQUEST_NOT_SUCCESS));

        // When-Action to do
        ResponseDTO actualResponse = manageNoticeMMMainController.manageNoticeMM(789L);

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

        when(manageNoticeMM.manageNoticeByNoticeId(anyLong(), anyBoolean())).thenThrow(exception);


        // When- Action to do
        ResponseDTO actualResponse = manageNoticeMMMainController.manageNoticeMM(999L);

        // then-Validations
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actualResponse.getStatus());
        Assertions.assertEquals("Generic Exception", actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertEquals(exception.getMessage(), actualResponse.getData());
    }
}
