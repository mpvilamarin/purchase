package com.subocol.manage.purchase.application;

import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.application.dtos.ManageNoticeCCDTO;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.ManageNoticeCCMainController;
import com.subocol.manage.purchase.common.utils.ExceptionUtil;
import com.subocol.manage.purchase.domain.servicesimpl.ManageNoticeCC;
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
class ManageNoticeCCMainControllerTest {

    @Mock
    private ManageNoticeCC manageNoticeCC;

    @InjectMocks
    private ManageNoticeCCMainController manageNoticeCCMainController;

    private ManageNoticeCCDTO manageNoticeCCDTO;

    @BeforeEach
    void setup(){
        manageNoticeCCDTO= ManageNoticeCCDTO.builder().noticeId(123L).quotationId(54541L).serviceIntegration(true).listProductQuotationIds(List.of(54511L,55145L)).build();
    }

    @Test
    void testManageNoticeMM_Success() throws ExceptionUtil {

        when(manageNoticeCC.manageNoticeCC(anyLong(), anyLong(), anyList(), anyBoolean())).thenReturn(true);

        ResponseDTO actualResponse = manageNoticeCCMainController.manageNoticeCC(manageNoticeCCDTO);

        Assertions.assertEquals(HttpStatus.OK.value(), actualResponse.getStatus());
        Assertions.assertEquals(MessageResponse.REQUEST_IS_SUCCESS, actualResponse.getMessage());
        Assertions.assertTrue(actualResponse.isSuccess());
        Assertions.assertNull(actualResponse.getData());
    }

    @Test
    void testManageNoticeMM_Failure() throws ExceptionUtil {

        when(manageNoticeCC.manageNoticeCC(anyLong(), anyLong(), anyList(), anyBoolean())).thenReturn(false);

        ResponseDTO actualResponse = manageNoticeCCMainController.manageNoticeCC(manageNoticeCCDTO);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), actualResponse.getStatus());
        Assertions.assertEquals(MessageResponse.REQUEST_NOT_SUCCESS, actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertNull(actualResponse.getData());
    }

    @Test
    void testManageNoticeMM_Exception() throws ExceptionUtil {

        when(manageNoticeCC.manageNoticeCC(anyLong(), anyLong(), anyList(), anyBoolean())).thenThrow(new ExceptionUtil(HttpStatus.BAD_REQUEST.value(), MessageResponse.REQUEST_NOT_SUCCESS));

        ResponseDTO actualResponse = manageNoticeCCMainController.manageNoticeCC(manageNoticeCCDTO);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), actualResponse.getStatus());
        Assertions.assertEquals(MessageResponse.REQUEST_NOT_SUCCESS, actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertNull(actualResponse.getData());
    }

    @Test
    void testManageNoticeMM_GenericException() throws ExceptionUtil {

        RuntimeException exception = new RuntimeException("Generic Exception");
        exception.initCause(new Exception("Cause of the exception"));

        when(manageNoticeCC.manageNoticeCC(anyLong(), anyLong(), anyList(), anyBoolean())).thenThrow(exception);

        ResponseDTO actualResponse = manageNoticeCCMainController.manageNoticeCC(manageNoticeCCDTO);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), actualResponse.getStatus());
        Assertions.assertEquals("Generic Exception", actualResponse.getMessage());
        Assertions.assertFalse(actualResponse.isSuccess());
        Assertions.assertEquals(exception.getMessage(), actualResponse.getData());
    }
}
