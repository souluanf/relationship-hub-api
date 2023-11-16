package dev.luanfernandes.hub.service;

import static dev.luanfernandes.hub.constants.TestConstants.ANY_ID_STRING;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacao;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacaoList;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacaoRequest;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacaoResponse;
import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.ENCERRADA;
import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.FILA;
import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.hub.domain.entity.Solicitacao;
import dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum;
import dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum;
import dev.luanfernandes.hub.domain.event.SolicitacaoCriadaEvent;
import dev.luanfernandes.hub.domain.mapper.SolicitacaoMapper;
import dev.luanfernandes.hub.domain.request.SolicitacaoRequest;
import dev.luanfernandes.hub.domain.response.SolicitacaoResponse;
import dev.luanfernandes.hub.repository.SolicitacaoRepository;
import dev.luanfernandes.hub.service.impl.SolicitacaoServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class SolicitacaoServiceTest {

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Spy
    private SolicitacaoMapper solicitacaoMapper;

    @InjectMocks
    private SolicitacaoServiceImpl solicitacaoService;

    @Test
    void whenEncerrarSolicitacao_thenShouldUpdateSolicitacao() {
        String solicitacaoId = ANY_ID_STRING;
        Solicitacao solicitacao = getSolicitacao();
        when(solicitacaoRepository.findById(fromString(solicitacaoId))).thenReturn(Optional.of(solicitacao));
        solicitacaoService.encerrarSolicitacao(solicitacaoId);
        verify(solicitacaoRepository).save(solicitacao);
    }

    @Test
    void whenBuscarPorIdAndSolicitacaoNotFound_thenShouldThrowEntityNotFoundException() {
        String solicitacaoId = ANY_ID_STRING;
        when(solicitacaoRepository.findById(fromString(solicitacaoId))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> solicitacaoService.buscarPorId(solicitacaoId));
    }

    @Test
    void whenEncerrarSolicitacaoAndSolicitacaoNotFound_thenShouldThrowEntityNotFoundException() {
        String solicitacaoId = ANY_ID_STRING;
        when(solicitacaoRepository.findById(fromString(solicitacaoId))).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> solicitacaoService.encerrarSolicitacao(solicitacaoId));
    }

    @Test
    void whenListarPorStatusWithResults_thenShouldReturnSolicitacoes() {
        StatusSolicitacaoEnum status = FILA;
        List<Solicitacao> solicitacoesMock = List.of(new Solicitacao(), new Solicitacao());
        when(solicitacaoRepository.findByStatus(status)).thenReturn(solicitacoesMock);

        List<SolicitacaoResponse> result = solicitacaoService.listarPorStatus(status);

        assertEquals(solicitacoesMock.size(), result.size());
        verify(solicitacaoRepository).findByStatus(status);
    }

    @Test
    void whenListarPorStatusWithNoResults_thenShouldReturnEmptyList() {
        StatusSolicitacaoEnum status = FILA;
        when(solicitacaoRepository.findByStatus(status)).thenReturn(Collections.emptyList());
        List<SolicitacaoResponse> result = solicitacaoService.listarPorStatus(status);
        assertTrue(result.isEmpty());
        verify(solicitacaoRepository).findByStatus(status);
    }

    @Test
    void whenObterProximaSolicitacaoPendenteWithResult_thenShouldReturnSolicitacao() {
        Solicitacao solicitacaoMock = new Solicitacao();
        when(solicitacaoRepository.findByStatusIsNotAndAtendenteIsNullOrderByDataCriacaoAsc(ENCERRADA))
                .thenReturn(List.of(solicitacaoMock));
        Solicitacao result = solicitacaoService.obterProximaSolicitacaoPendente();
        assertEquals(solicitacaoMock, result);
    }

    @Test
    void whenObterProximaSolicitacaoPendenteWithNoResult_thenShouldReturnNull() {
        when(solicitacaoRepository.findByStatusIsNotAndAtendenteIsNullOrderByDataCriacaoAsc(ENCERRADA))
                .thenReturn(Collections.emptyList());
        Solicitacao result = solicitacaoService.obterProximaSolicitacaoPendente();
        assertNull(result);
    }

    @Test
    void whenBuscarPorIdWithResult_thenShouldReturnSolicitacao() {
        String solicitacaoId = ANY_ID_STRING;
        Solicitacao solicitacaoMock = getSolicitacao();
        UUID uuidSolicitacaoId = fromString(solicitacaoId);
        when(solicitacaoRepository.findById(uuidSolicitacaoId)).thenReturn(Optional.of(solicitacaoMock));
        when(solicitacaoMapper.solicitacaoToSolicitacaoResponse(solicitacaoMock))
                .thenReturn(getSolicitacaoResponse());
        SolicitacaoResponse result = solicitacaoService.buscarPorId(solicitacaoId);
        assertNotNull(result);
        assertEquals(solicitacaoMock.getId(), uuidSolicitacaoId);
        verify(solicitacaoRepository).findById(uuidSolicitacaoId);
        verify(solicitacaoMapper).solicitacaoToSolicitacaoResponse(solicitacaoMock);
    }

    @Test
    void whenCriarSolicitacao_thenShouldCreateSolicitacaoAndPublishEvent() {
        SolicitacaoRequest request = getSolicitacaoRequest();
        Solicitacao solicitacaoMock = new Solicitacao();
        SolicitacaoResponse solicitacaoResponseMock = getSolicitacaoResponse();
        when(solicitacaoMapper.solicitacaoRequestToSolicitacao(request)).thenReturn(solicitacaoMock);
        when(solicitacaoMapper.solicitacaoToSolicitacaoResponse(solicitacaoMock))
                .thenReturn(solicitacaoResponseMock);
        SolicitacaoResponse result = solicitacaoService.criarSolicitacao(request);
        assertNotNull(result);
        verify(solicitacaoRepository).save(solicitacaoMock);
        verify(solicitacaoMapper).solicitacaoToSolicitacaoResponse(solicitacaoMock);
        ArgumentCaptor<SolicitacaoCriadaEvent> eventCaptor = ArgumentCaptor.forClass(SolicitacaoCriadaEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        SolicitacaoCriadaEvent capturedEvent = eventCaptor.getValue();
        assertNotNull(capturedEvent);
        assertEquals(solicitacaoMock, capturedEvent.solicitacao());
    }

    @Test
    void whenObterSolicitacoesPendentesPorEquipe_thenShouldReturnCorrectList() {
        List<Solicitacao> solicitacoesTeste = getSolicitacaoList();
        TipoSolicitacaoEnum tipoTeste = TipoSolicitacaoEnum.PROBLEMA_CARTAO;
        when(solicitacaoRepository.findByStatusAndAtendenteIsNullAndTipoOrderByDataCriacaoAsc(FILA, tipoTeste))
                .thenReturn(solicitacoesTeste);
        List<Solicitacao> resultado = solicitacaoService.obterSolicitacoesPendentesPorEquipe(tipoTeste);
        assertEquals(solicitacoesTeste, resultado);
    }
}
