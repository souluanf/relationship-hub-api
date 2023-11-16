package dev.luanfernandes.hub.controller;

import static dev.luanfernandes.hub.constants.PathConstants.ATENDENTES_V1;
import static dev.luanfernandes.hub.constants.TestConstants.ANY_ID;
import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getAtendenteRequest;
import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getAtendenteResponse;
import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getAtendenteResponseList;
import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getInvalidAtendenteRequest;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luanfernandes.hub.common.exception.ExceptionHandlerAdvice;
import dev.luanfernandes.hub.controller.impl.AtendenteControllerImpl;
import dev.luanfernandes.hub.domain.request.AtendenteRequest;
import dev.luanfernandes.hub.domain.response.AtendenteResponse;
import dev.luanfernandes.hub.service.impl.AtendenteServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

@ActiveProfiles("test")
@WebMvcTest
@ContextConfiguration(
        classes = {
            AtendenteControllerImpl.class,
            ExceptionHandlerAdvice.class,
        })
class AtendenteControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AtendenteServiceImpl service;

    @Test
    void whenCriarAtendenteWithValidRequest_thenShouldReturnCreatedStatus() throws Exception {
        AtendenteRequest request = getAtendenteRequest();
        mockMvc.perform(post(ATENDENTES_V1)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated());
        verify(service).criarAtendente(any(AtendenteRequest.class));
    }

    @Test
    void whenCriarAtendenteWithInvalidRequest_thenShouldReturnBadRequest() throws Exception {
        AtendenteRequest request = getInvalidAtendenteRequest();
        mockMvc.perform(post(ATENDENTES_V1)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenListarAtendentes_thenShouldReturnOkStatus() throws Exception {
        List<AtendenteResponse> atendentes = getAtendenteResponseList();
        when(service.listarAtendentes()).thenReturn(atendentes);
        mockMvc.perform(get(ATENDENTES_V1).contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(atendentes.size())));
    }

    @Test
    void whenListarAtendentesAndServiceThrows_thenShouldReturnInternalServerError() throws Exception {
        doThrow(new ResponseStatusException(INTERNAL_SERVER_ERROR))
                .when(service)
                .listarAtendentes();
        mockMvc.perform(get(ATENDENTES_V1).contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenBuscarAtendentePorIdWithValidId_thenShouldReturnOkStatus() throws Exception {
        UUID atendenteId = ANY_ID;
        AtendenteResponse atendenteResponse = getAtendenteResponse();
        when(service.buscarAtendentePorId(atendenteId)).thenReturn(atendenteResponse);
        mockMvc.perform(get(ATENDENTES_V1 + "/" + atendenteId)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(atendenteId.toString()));
    }

    @Test
    void whenBuscarAtendentePorIdWithInvalidId_thenShouldReturnNotFound() throws Exception {
        UUID atendenteId = UUID.randomUUID();
        when(service.buscarAtendentePorId(atendenteId)).thenThrow(EntityNotFoundException.class);
        mockMvc.perform(get(ATENDENTES_V1 + "/" + atendenteId)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
