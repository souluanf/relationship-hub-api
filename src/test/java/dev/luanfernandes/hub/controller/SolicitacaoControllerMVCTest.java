package dev.luanfernandes.hub.controller;

import static dev.luanfernandes.hub.constants.PathConstants.SOLICITACOES_STATUS;
import static dev.luanfernandes.hub.constants.PathConstants.SOLICITACOES_V1;
import static dev.luanfernandes.hub.constants.TestConstants.ANY_ID_STRING;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacaoRequest;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacaoResponse;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacaoResponseList;
import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.EM_ATENDIMENTO;
import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.ENCERRADA;
import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.FILA;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.luanfernandes.hub.common.exception.ExceptionHandlerAdvice;
import dev.luanfernandes.hub.controller.impl.SolicitacaoControllerImpl;
import dev.luanfernandes.hub.domain.request.SolicitacaoRequest;
import dev.luanfernandes.hub.domain.response.SolicitacaoResponse;
import dev.luanfernandes.hub.service.impl.SolicitacaoServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest
@ContextConfiguration(
        classes = {
            SolicitacaoControllerImpl.class,
            ExceptionHandlerAdvice.class,
        })
class SolicitacaoControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SolicitacaoServiceImpl service;

    @Test
    void whenSolicitarWithValidRequest_thenShouldReturnAcceptedStatus() throws Exception {
        SolicitacaoRequest request = getSolicitacaoRequest();
        SolicitacaoResponse expectedResponse = getSolicitacaoResponse();
        when(service.criarSolicitacao(any(SolicitacaoRequest.class))).thenReturn(expectedResponse);
        mockMvc.perform(post(SOLICITACOES_V1)
                        .contentType(APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(expectedResponse.id().toString()));
    }

    @Test
    void whenBuscarPorIdWithValidId_thenShouldReturnOkStatus() throws Exception {
        String id = ANY_ID_STRING;
        SolicitacaoResponse expectedResponse = getSolicitacaoResponse();
        when(service.buscarPorId(id)).thenReturn(expectedResponse);

        mockMvc.perform(get(SOLICITACOES_V1 + "/{id}", id).contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedResponse.id().toString()));
    }

    @Test
    void whenListarNaFila_thenShouldReturnOkStatus() throws Exception {
        List<SolicitacaoResponse> solicitacoes = getSolicitacaoResponseList();
        when(service.listarPorStatus(FILA)).thenReturn(solicitacoes);
        mockMvc.perform(get(SOLICITACOES_STATUS)
                        .param("status", FILA.toString())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(solicitacoes.size())));
    }

    @Test
    void whenEncerrarWithValidId_thenShouldReturnNoContentStatus() throws Exception {
        String id = ANY_ID_STRING;
        doNothing().when(service).encerrarSolicitacao(id);
        mockMvc.perform(put(SOLICITACOES_V1 + "/{id}", id).contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void whenListarEmAtendimento_thenShouldReturnOkStatusAndList() throws Exception {
        List<SolicitacaoResponse> solicitacoesEmAtendimento = getSolicitacaoResponseList();
        when(service.listarPorStatus(EM_ATENDIMENTO)).thenReturn(solicitacoesEmAtendimento);
        mockMvc.perform(get(SOLICITACOES_STATUS)
                        .param("status", EM_ATENDIMENTO.toString())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(solicitacoesEmAtendimento.size())));
    }

    @Test
    void whenListarEncerradas_thenShouldReturnOkStatusAndList() throws Exception {
        List<SolicitacaoResponse> solicitacoesEncerradas = getSolicitacaoResponseList();
        when(service.listarPorStatus(ENCERRADA)).thenReturn(solicitacoesEncerradas);
        mockMvc.perform(get(SOLICITACOES_STATUS)
                        .param("status", ENCERRADA.toString())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(solicitacoesEncerradas.size())));
    }
}
