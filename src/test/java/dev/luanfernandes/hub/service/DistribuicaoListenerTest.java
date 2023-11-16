package dev.luanfernandes.hub.service;

import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getAtendente;
import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getAtendenteList;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacao;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacaoList;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacaoListMoreThanThree;
import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.EM_ATENDIMENTO;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.luanfernandes.hub.domain.entity.Atendente;
import dev.luanfernandes.hub.domain.entity.Solicitacao;
import dev.luanfernandes.hub.domain.event.AtendenteCriadoEvent;
import dev.luanfernandes.hub.domain.event.SolicitacaoCriadaEvent;
import dev.luanfernandes.hub.domain.event.SolicitacaoEncerradaEvent;
import dev.luanfernandes.hub.service.impl.AtendenteServiceImpl;
import dev.luanfernandes.hub.service.impl.DistribuicaoListenerImpl;
import dev.luanfernandes.hub.service.impl.SolicitacaoServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DistribuicaoListenerTest {

    @Mock
    private AtendenteServiceImpl atendenteService;

    @Mock
    private SolicitacaoServiceImpl solicitacaoService;

    @InjectMocks
    private DistribuicaoListenerImpl distribuicaoListener;

    @Test
    void whenOnSolicitacaoCriada_thenShouldAllocateToAtendente() {
        Solicitacao solicitacao = getSolicitacao();
        SolicitacaoCriadaEvent event = new SolicitacaoCriadaEvent(solicitacao);
        List<Atendente> atendentesDisponiveis = getAtendenteList();
        when(atendenteService.obterAtendentesDisponiveis(any())).thenReturn(atendentesDisponiveis);
        distribuicaoListener.onSolicitacaoCriada(event);
        verify(atendenteService).obterAtendentesDisponiveis(any());
        verify(atendenteService).alocarSolicitacao(any(), any());
    }

    @Test
    void whenOnSolicitacaoEncerrada_thenShouldAllocateNextSolicitacao() {
        Solicitacao solicitacao = getSolicitacao();
        SolicitacaoEncerradaEvent event = new SolicitacaoEncerradaEvent(solicitacao);
        when(solicitacaoService.obterProximaSolicitacaoPendente()).thenReturn(solicitacao);
        distribuicaoListener.onSolicitacaoEncerrada(event);
        verify(solicitacaoService).obterProximaSolicitacaoPendente();
        verify(atendenteService).alocarSolicitacao(any(), any());
    }

    /////

    @Test
    void whenAtendentesDisponiveisNotEmpty_thenShouldAllocateSolicitacao() {
        SolicitacaoCriadaEvent event = new SolicitacaoCriadaEvent(getSolicitacao());
        List<Atendente> atendentesDisponiveis = getAtendenteList();
        when(atendenteService.obterAtendentesDisponiveis(any())).thenReturn(atendentesDisponiveis);
        distribuicaoListener.onSolicitacaoCriada(event);
        verify(atendenteService).alocarSolicitacao(any(), any());
    }

    @Test
    void whenAtendentesDisponiveisEmpty_thenShouldNotAllocateSolicitacao() {
        SolicitacaoCriadaEvent event = new SolicitacaoCriadaEvent(getSolicitacao());
        when(atendenteService.obterAtendentesDisponiveis(any())).thenReturn(emptyList());
        distribuicaoListener.onSolicitacaoCriada(event);
        verify(atendenteService, never()).alocarSolicitacao(any(), any());
    }

    @Test
    void whenOnSolicitacaoEncerradaAndAtendenteIsNull_thenShouldNotAllocateSolicitacao() {
        SolicitacaoEncerradaEvent event = new SolicitacaoEncerradaEvent(getSolicitacao());
        event.solicitacao().setAtendente(null);
        distribuicaoListener.onSolicitacaoEncerrada(event);
        verify(solicitacaoService, never()).obterProximaSolicitacaoPendente();
        verify(atendenteService, never()).alocarSolicitacao(any(), any());
    }

    @Test
    void whenOnSolicitacaoEncerradaAndAtendenteIsNotNull_thenShouldAllocateSolicitacao() {
        Atendente atendente = getAtendente();
        Solicitacao solicitacaoEncerrada = getSolicitacao();
        solicitacaoEncerrada.setAtendente(atendente);
        SolicitacaoEncerradaEvent event = new SolicitacaoEncerradaEvent(solicitacaoEncerrada);
        Solicitacao proximaSolicitacao = getSolicitacao();
        when(solicitacaoService.obterProximaSolicitacaoPendente()).thenReturn(proximaSolicitacao);
        distribuicaoListener.onSolicitacaoEncerrada(event);
        verify(solicitacaoService).obterProximaSolicitacaoPendente();
        verify(atendenteService).alocarSolicitacao(atendente.getId(), proximaSolicitacao);
    }

    @Test
    void whenOnSolicitacaoEncerradaAndProximaSolicitacaoIsNull_thenShouldNotAllocateSolicitacao() {
        Atendente atendente = getAtendente();
        Solicitacao solicitacaoEncerrada = getSolicitacao();
        solicitacaoEncerrada.setAtendente(atendente);
        SolicitacaoEncerradaEvent event = new SolicitacaoEncerradaEvent(solicitacaoEncerrada);

        when(solicitacaoService.obterProximaSolicitacaoPendente()).thenReturn(null);

        distribuicaoListener.onSolicitacaoEncerrada(event);

        verify(solicitacaoService).obterProximaSolicitacaoPendente();
        verify(atendenteService, never()).alocarSolicitacao(any(), any());
    }

    @Test
    void whenOnSolicitacaoEncerradaAndProximaSolicitacaoIsNotNull_thenShouldAllocateSolicitacao() {
        Atendente atendente = getAtendente();
        Solicitacao solicitacaoEncerrada = getSolicitacao();
        solicitacaoEncerrada.setAtendente(atendente);
        SolicitacaoEncerradaEvent event = new SolicitacaoEncerradaEvent(solicitacaoEncerrada);

        Solicitacao proximaSolicitacao = getSolicitacao();
        when(solicitacaoService.obterProximaSolicitacaoPendente()).thenReturn(proximaSolicitacao);

        distribuicaoListener.onSolicitacaoEncerrada(event);

        verify(solicitacaoService).obterProximaSolicitacaoPendente();
        verify(atendenteService).alocarSolicitacao(atendente.getId(), proximaSolicitacao);
    }

    @Test
    void whenOnAtendenteCriado_thenShouldAllocateUpToThreeSolicitacoes() {
        Atendente novoAtendente = getAtendente();
        List<Solicitacao> solicitacoesTeste = getSolicitacaoListMoreThanThree();
        AtendenteCriadoEvent eventoMock = new AtendenteCriadoEvent(novoAtendente);
        when(solicitacaoService.obterSolicitacoesPendentesPorEquipe(any())).thenReturn(solicitacoesTeste);
        distribuicaoListener.onAtendenteCriado(eventoMock);
        verify(solicitacaoService, times(1)).obterSolicitacoesPendentesPorEquipe(any());
        verify(atendenteService, times(3)).alocarSolicitacao(eq(novoAtendente.getId()), any(Solicitacao.class));
        for (int i = 0; i < 3; i++) {
            Solicitacao solicitacao = solicitacoesTeste.get(i);
            assertEquals(novoAtendente, solicitacao.getAtendente());
            assertEquals(EM_ATENDIMENTO, solicitacao.getStatus());
            assertNotNull(solicitacao.getDataAtualizacao());
        }
    }

    @Test
    void whenOnAtendenteCriadoWithLessThanThreeSolicitacoes_thenShouldAllocateAll() {
        Atendente novoAtendente = getAtendente();
        List<Solicitacao> solicitacoesTeste = getSolicitacaoList();
        AtendenteCriadoEvent eventoMock = new AtendenteCriadoEvent(novoAtendente);
        when(solicitacaoService.obterSolicitacoesPendentesPorEquipe(any())).thenReturn(solicitacoesTeste);
        distribuicaoListener.onAtendenteCriado(eventoMock);
        verify(solicitacaoService, times(1)).obterSolicitacoesPendentesPorEquipe(any());
        for (Solicitacao solicitacao : solicitacoesTeste) {
            assertEquals(novoAtendente, solicitacao.getAtendente());
            assertEquals(EM_ATENDIMENTO, solicitacao.getStatus());
            assertNotNull(solicitacao.getDataAtualizacao());
            verify(atendenteService).alocarSolicitacao(novoAtendente.getId(), solicitacao);
        }
    }
}
