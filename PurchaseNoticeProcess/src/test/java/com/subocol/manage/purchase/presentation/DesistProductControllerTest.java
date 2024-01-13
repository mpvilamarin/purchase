package com.subocol.manage.purchase.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.DesistProductMainController;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.DesistDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

@WebMvcTest(DesistProductController.class)
class DesistProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    DesistProductMainController desistProductMainController;

    private ResponseDTO response;
    private DesistDTO desistDTO;

    @BeforeEach
    void setup() {
        response = new ResponseDTO();
        desistDTO = DesistDTO.builder().userName("user").observation("").causal("precio mal cotizado").ids(List.of(54454L,44544L)).build();
    }

    @Test
    void testDesistProductSuccess() throws Exception {

        response.setStatus(HttpStatus.OK.value()).setDate(LocalDateTime.now().toString())
                .setMessage("Se desistio correctamente los productos con ids : [54454, 44544]").setSuccess(true);

        when(desistProductMainController.desistProduct(any(DesistDTO.class)))
                .thenReturn(response);

        ResultActions responseRequest = mockMvc.perform(post("/api/desist_product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(desistDTO)));

        responseRequest.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message").isString());

    }

    @Test
    void testDesistProductNotSuccess() throws Exception {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setDate(LocalDateTime.now().toString())
                .setMessage(MessageResponse.REQUEST_NOT_SUCCESS).setSuccess(false);

        when(desistProductMainController.desistProduct(any(DesistDTO.class)))
                .thenReturn(response);

        ResultActions responseRequest = mockMvc.perform(post("/api/desist_product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(desistDTO)));

        responseRequest.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())));

    }

    @Test
    @Disabled
    void testDesistDTOValidateIds() throws Exception {
        response.setStatus(HttpStatus.BAD_REQUEST.value()).setDate(LocalDateTime.now().toString())
                .setMessage("El campo 'ids' no puede estar vacio. El campo 'ids' no puede ser nulo.").setSuccess(false);

        when(desistProductMainController.desistProduct(any(DesistDTO.class)))
                .thenReturn(response);
        desistDTO.setIds(null);

        ResultActions responseRequest = mockMvc.perform(post("/api/desist_product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(desistDTO)));

        responseRequest.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())));
    }

    @Test
    void testDesistDTOValidateCausal() throws Exception {
        response.setStatus(HttpStatus.BAD_REQUEST.value()).setDate(LocalDateTime.now().toString())
                .setMessage("El campo 'causal' no puede ser nulo o estar vacio.").setSuccess(false);

        when(desistProductMainController.desistProduct(any(DesistDTO.class)))
                .thenReturn(response);
        desistDTO.setCausal(null);

        ResultActions responseRequest = mockMvc.perform(post("/api/desist_product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(desistDTO)));

        responseRequest.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())));
    }

    @Test
    void testDesistDTOValidateUserName() throws Exception {
        response.setStatus(HttpStatus.BAD_REQUEST.value()).setDate(LocalDateTime.now().toString())
                .setMessage("El campo 'userName' no puede ser nulo o estar vacio.").setSuccess(false);

        when(desistProductMainController.desistProduct(any(DesistDTO.class)))
                .thenReturn(response);
        desistDTO.setUserName(null);

        ResultActions responseRequest = mockMvc.perform(post("/api/desist_product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(desistDTO)));

        responseRequest.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())));
    }
}
