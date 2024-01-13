package com.subocol.manage.purchase.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.application.dtos.ManageNoticeCCDTO;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.ManageNoticeCCMainController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManageNoticeCCController.class)
class ManageNoticeCCControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    ManageNoticeCCMainController manageNoticeCCMainController;

    private ResponseDTO response;
    private ManageNoticeCCDTO manageNoticeCCDTO;
    @BeforeEach
    void setup(){
        response=new ResponseDTO();
        manageNoticeCCDTO= ManageNoticeCCDTO.builder().noticeId(123L).quotationId(54541L).serviceIntegration(true).listProductQuotationIds(List.of(54511L,55145L)).build();
    }

    @Test
    void manageNoticeCC_Success() throws Exception {

        response.setStatus(HttpStatus.OK.value()).setDate(LocalDateTime.now().toString())
                .setMessage(MessageResponse.REQUEST_IS_SUCCESS).setSuccess(true);

        when(manageNoticeCCMainController.manageNoticeCC(any(ManageNoticeCCDTO.class)))
                .thenReturn(response);

        ResultActions responseRequest = mockMvc.perform(post("/api/managenotice/cc")
                . contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manageNoticeCCDTO)));

        responseRequest.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success",is(response.isSuccess())))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isEmpty());

    }


    @Test
    void manageNoticeMM_NotSuccess() throws Exception {

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setDate(LocalDateTime.now().toString())
                .setMessage(MessageResponse.REQUEST_NOT_SUCCESS).setSuccess(false);

        when(manageNoticeCCMainController.manageNoticeCC(any(ManageNoticeCCDTO.class)))
                .thenReturn(response);

        ResultActions responseRequest = mockMvc.perform(post("/api/managenotice/cc")
                . contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manageNoticeCCDTO)));

        responseRequest.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success",is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())))
                .andExpect(jsonPath("$.data").isEmpty());

    }
}
