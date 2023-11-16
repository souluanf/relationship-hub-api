package dev.luanfernandes.hub.controller;

import static dev.luanfernandes.hub.constants.TestConstants.ANY_ID_STRING;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacaoRequest;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacaoResponse;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacaoResponseList;
import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.FILA;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import dev.luanfernandes.hub.controller.impl.SolicitacaoControllerImpl;
import dev.luanfernandes.hub.domain.request.SolicitacaoRequest;
import dev.luanfernandes.hub.domain.response.SolicitacaoResponse;
import dev.luanfernandes.hub.service.impl.SolicitacaoServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SolicitacaoControllerTest {

    @Mock
    private SolicitacaoServiceImpl solicitacaoService;

    @InjectMocks
    private SolicitacaoControllerImpl atendenteController;

    @Test
    void whenSolicitarWithValidRequest_thenShouldCreateSolicitacao() {
        SolicitacaoRequest request = getSolicitacaoRequest();
        SolicitacaoResponse expectedSolicitacao = getSolicitacaoResponse();
        when(solicitacaoService.criarSolicitacao(request)).thenReturn(expectedSolicitacao);
        ResponseEntity<SolicitacaoResponse> response = atendenteController.solicitar(request);
        assertEquals(ACCEPTED, response.getStatusCode());
        assertEquals(expectedSolicitacao, response.getBody());
    }

    @Test
    void whenBuscarPorIdWithValidId_thenShouldReturnSolicitacao() {
        String solicitacaoId = ANY_ID_STRING;
        SolicitacaoResponse expectedSolicitacao = getSolicitacaoResponse();
        when(solicitacaoService.buscarPorId(solicitacaoId)).thenReturn(expectedSolicitacao);
        ResponseEntity<SolicitacaoResponse> response = atendenteController.buscarPorId(solicitacaoId);
        assertEquals(OK, response.getStatusCode());
        assertEquals(expectedSolicitacao, response.getBody());
    }

    @Test
    void whenListarNaFila_thenShouldReturnSolicitacoesList() {
        List<SolicitacaoResponse> expectedList = getSolicitacaoResponseList();
        when(solicitacaoService.listarPorStatus(FILA)).thenReturn(expectedList);
        ResponseEntity<List<SolicitacaoResponse>> response = atendenteController.listarPorStatus(FILA);
        assertEquals(requireNonNull(OK), response.getStatusCode());
        assertEquals(expectedList, response.getBody());
    }

    @Test
    void whenEncerrarWithValidId_thenShouldEncerrarSolicitacao() {
        String solicitacaoId = ANY_ID_STRING;
        doNothing().when(solicitacaoService).encerrarSolicitacao(solicitacaoId);
        ResponseEntity<Void> response = atendenteController.encerrar(solicitacaoId);
        assertEquals(NO_CONTENT, response.getStatusCode());
        verify(solicitacaoService).encerrarSolicitacao(solicitacaoId);
    }
}
