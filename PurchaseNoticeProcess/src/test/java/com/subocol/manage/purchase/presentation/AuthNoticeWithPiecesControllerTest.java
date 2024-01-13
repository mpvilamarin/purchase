package com.subocol.manage.purchase.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subocol.manage.purchase.application.constants.MessageResponse;
import com.subocol.manage.purchase.application.dtos.ResponseDTO;
import com.subocol.manage.purchase.application.main.AuthNoticeWithPiecesMainController;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.AuthNoticePiecesDTO;
import com.subocol.manage.purchase.domain.servicesimpl.dtos.PiecesQuoteAuthDTO;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthNoticeWithPiecesController.class)
class AuthNoticeWithPiecesControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    AuthNoticeWithPiecesMainController authNoticeWithPiecesMainController;

    private ResponseDTO response;
    private AuthNoticePiecesDTO authNoticePiecesDTO;

    @BeforeEach
    void setup() {
        response = new ResponseDTO();
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
    void testAuthPiecesSuccess() throws Exception {

        response.setStatus(HttpStatus.OK.value()).setDate(LocalDateTime.now().toString())
                .setMessage("Se autorizo aviso 999999999 NoticeId 999999999").setSuccess(true);

        when(authNoticeWithPiecesMainController.authNotice(any(AuthNoticePiecesDTO.class)))
                .thenReturn(response);

        ResultActions responseRequest = mockMvc.perform(post("/api/auth_notice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authNoticePiecesDTO)));

        responseRequest.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message").isString());

    }


    @Test
    void testAuthPiecesNotSuccess() throws Exception {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setDate(LocalDateTime.now().toString())
                .setMessage(MessageResponse.REQUEST_NOT_SUCCESS).setSuccess(false);

        when(authNoticeWithPiecesMainController.authNotice(any(AuthNoticePiecesDTO.class)))
                .thenReturn(response);

        ResultActions responseRequest = mockMvc.perform(post("/api/auth_notice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authNoticePiecesDTO)));

        responseRequest.andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())));

    }

    @Test
    void testAuthPiecesDTOValidateExternalEvent() throws Exception {
        response.setStatus(HttpStatus.BAD_REQUEST.value()).setDate(LocalDateTime.now().toString())
                .setMessage("El campo 'idAviso' no puede ser nulo o estar vacio.").setSuccess(false);

        when(authNoticeWithPiecesMainController.authNotice(any(AuthNoticePiecesDTO.class)))
                .thenReturn(response);
        authNoticePiecesDTO.setExternalEvent(null);
        ResultActions responseRequest = mockMvc.perform(post("/api/auth_notice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authNoticePiecesDTO)));

        responseRequest.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())));
    }

    @Test
    void testAuthPiecesDTOValidateClaimNumber() throws Exception {
        response.setStatus(HttpStatus.BAD_REQUEST.value()).setDate(LocalDateTime.now().toString())
                .setMessage("El campo 'noSiniestro' no puede ser nulo o estar vacio.").setSuccess(false);

        when(authNoticeWithPiecesMainController.authNotice(any(AuthNoticePiecesDTO.class)))
                .thenReturn(response);
        authNoticePiecesDTO.setClaimNumber(null);
        ResultActions responseRequest = mockMvc.perform(post("/api/auth_notice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authNoticePiecesDTO)));

        responseRequest.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())));
    }

    @Test
    void testAuthPiecesDTOValidateNullOrEmptySpareParts() throws Exception {
        response.setStatus(HttpStatus.BAD_REQUEST.value()).setDate(LocalDateTime.now().toString())
                .setMessage("El campo 'repuestos' no puede ser nulo. El campo 'repuestos' no puede estar vacio.").setSuccess(false);

        when(authNoticeWithPiecesMainController.authNotice(any(AuthNoticePiecesDTO.class)))
                .thenReturn(response);
        authNoticePiecesDTO.setSpareParts(null);
        ResultActions responseRequest = mockMvc.perform(post("/api/auth_notice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authNoticePiecesDTO)));

        responseRequest.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())));
    }

    @Test
    void testAuthPiecesDTOValidateEmptySpareParts() throws Exception {
        response.setStatus(HttpStatus.BAD_REQUEST.value()).setDate(LocalDateTime.now().toString())
                .setMessage("El campo 'repuestos' no puede estar vacio.").setSuccess(false);

        when(authNoticeWithPiecesMainController.authNotice(any(AuthNoticePiecesDTO.class)))
                .thenReturn(response);
        authNoticePiecesDTO.setSpareParts(new ArrayList<>());
        ResultActions responseRequest = mockMvc.perform(post("/api/auth_notice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authNoticePiecesDTO)));

        responseRequest.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.status", is(response.getStatus())))
                .andExpect(jsonPath("$.success", is(response.isSuccess())))
                .andExpect(jsonPath("$.message", is(response.getMessage())));
    }


}
