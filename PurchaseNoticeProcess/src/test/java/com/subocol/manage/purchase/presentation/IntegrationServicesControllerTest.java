package com.subocol.manage.purchase.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.IntegrationServicesMainController;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.ExternalIntegrationDTO;
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

@WebMvcTest(IntegrationServicesController.class)
class IntegrationServicesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    IntegrationServicesMainController integrationServicesMainController;

    private ResponseDTO response;
    private ExternalIntegrationDTO externalIntegrationDTO;


    @BeforeEach
    void setup() {
        response = new ResponseDTO();
        externalIntegrationDTO = ExternalIntegrationDTO.builder().insurerId(500000002L).noticeId(642345L).orderIds(List.of(6432L,6433L,6434L)).build();
    }

    @Test
    void integrationServicesSuccess() throws Exception {

        response.setStatus(HttpStatus.OK.value()).setDate(LocalDateTime.now().toString())
                .setMessage("Se proceso correctamente el aviso: 642345 NoticeId 642345").setSuccess(true);

        when(integrationServicesMainController.integrationServices(any(ExternalIntegrationDTO.class)))
                .thenReturn(response);

        ResultActions responseRequest = mockMvc.perform(post("/api/integration_services")
                . contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(externalIntegrationDTO)));

        responseRequest.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success",is(response.isSuccess())))
                .andExpect(jsonPath("$.message").isString())
                .andExpect(jsonPath("$.data").isEmpty());

    }


    @Test
    void integrationServicesNotSuccess() throws Exception {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setDate(LocalDateTime.now().toString())
                .setMessage(MessageResponse.REQUEST_NOT_SUCCESS).setSuccess(false);

        when(integrationServicesMainController.integrationServices(any(ExternalIntegrationDTO.class)))
                .thenReturn(response);

        ResultActions responseRequest = mockMvc.perform(post("/api/integration_services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(externalIntegrationDTO)));

        responseRequest.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())));

    }

    @Test
    void testAuthPiecesDTOValidateNoticeId() throws Exception {
        response.setStatus(HttpStatus.BAD_REQUEST.value()).setDate(LocalDateTime.now().toString())
                .setMessage("El campo 'noticeId' no puede ser nulo o estar vacio.").setSuccess(false);

        when(integrationServicesMainController.integrationServices(any(ExternalIntegrationDTO.class)))
                .thenReturn(response);
        externalIntegrationDTO.setNoticeId(null);
        ResultActions responseRequest = mockMvc.perform(post("/api/integration_services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(externalIntegrationDTO)));

        responseRequest.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())));
    }

    @Test
    void testAuthPiecesDTOValidateInsurerId() throws Exception {
        response.setStatus(HttpStatus.BAD_REQUEST.value()).setDate(LocalDateTime.now().toString())
                .setMessage("El campo 'insurerId' no puede ser nulo o estar vacio.").setSuccess(false);

        when(integrationServicesMainController.integrationServices(any(ExternalIntegrationDTO.class)))
                .thenReturn(response);
        externalIntegrationDTO.setInsurerId(null);
        ResultActions responseRequest = mockMvc.perform(post("/api/integration_services")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(externalIntegrationDTO)));

        responseRequest.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())));
    }
}
