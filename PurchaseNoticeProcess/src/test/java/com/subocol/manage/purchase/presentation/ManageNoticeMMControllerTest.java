package com.subocol.manage.purchase.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.application.dtos.ManageNoticeMMDTO;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.ManageNoticeMMMainController;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManageNoticeMMController.class)
class ManageNoticeMMControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
     ManageNoticeMMMainController manageNoticeMMMainController;

    private ResponseDTO response;
    private ManageNoticeMMDTO manageNoticeMMDTO;
    @BeforeEach
    void setup(){
        response=new ResponseDTO();
        manageNoticeMMDTO= ManageNoticeMMDTO.builder().noticeId(123L).build();
    }

    @Test
    void manageNoticeMM_Success() throws Exception {
        //Given-preconditions

        response.setStatus(HttpStatus.OK.value()).setDate(LocalDateTime.now().toString())
                .setMessage(MessageResponse.REQUEST_IS_SUCCESS).setSuccess(true);

        when(manageNoticeMMMainController.manageNoticeMM(anyLong()))
                .thenReturn(response);

        //when-action to do
        ResultActions responseRequest = mockMvc.perform(post("/api/managenotice/mm")
                . contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manageNoticeMMDTO)));


        //then-verify result
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
        //Given-preconditions

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setDate(LocalDateTime.now().toString())
                .setMessage(MessageResponse.REQUEST_NOT_SUCCESS).setSuccess(false);

        when(manageNoticeMMMainController.manageNoticeMM(anyLong()))
                .thenReturn(response);

        //when-action to do

        ResultActions responseRequest = mockMvc.perform(post("/api/managenotice/mm")
                . contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(manageNoticeMMDTO)));

        //then-verify result
        responseRequest.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success",is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())))
                .andExpect(jsonPath("$.data").isEmpty());

    }
}
