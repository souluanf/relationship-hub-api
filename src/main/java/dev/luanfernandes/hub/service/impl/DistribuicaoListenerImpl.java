package dev.luanfernandes.hub.service.impl;

import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.EM_ATENDIMENTO;
import static java.time.LocalDateTime.now;

import dev.luanfernandes.hub.domain.entity.Atendente;
import dev.luanfernandes.hub.domain.entity.Solicitacao;
import dev.luanfernandes.hub.domain.enums.TipoEquipeEnum;
import dev.luanfernandes.hub.domain.event.AtendenteCriadoEvent;
import dev.luanfernandes.hub.domain.event.SolicitacaoCriadaEvent;
import dev.luanfernandes.hub.domain.event.SolicitacaoEncerradaEvent;
import dev.luanfernandes.hub.service.DistribuicaoListener;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DistribuicaoListenerImpl implements DistribuicaoListener {

    private final AtendenteServiceImpl atendenteService;
    private final SolicitacaoServiceImpl solicitacaoService;

    @Override
    @EventListener
    public void onAtendenteCriado(AtendenteCriadoEvent event) {
        Atendente novoAtendente = event.atendente();
        TipoEquipeEnum equipeDesejada = novoAtendente.getEquipe();
        List<Solicitacao> solicitacoesPendentes =
                solicitacaoService.obterSolicitacoesPendentesPorEquipe(equipeDesejada.getSolicitacaoCorrespondente());
        int alocacoesRealizadas = 0;
        for (Solicitacao proximaSolicitacao : solicitacoesPendentes) {
            if (alocacoesRealizadas >= 3) break;
            proximaSolicitacao.setAtendente(novoAtendente);
            proximaSolicitacao.setDataAtualizacao(now());
            proximaSolicitacao.setStatus(EM_ATENDIMENTO);
            atendenteService.alocarSolicitacao(novoAtendente.getId(), proximaSolicitacao);
            alocacoesRealizadas++;
        }
    }

    @Override
    @EventListener
    public void onSolicitacaoCriada(SolicitacaoCriadaEvent event) {
        Solicitacao solicitacao = event.solicitacao();
        List<Atendente> atendentesDisponiveis = atendenteService.obterAtendentesDisponiveis(solicitacao.getTipo());
        if (!atendentesDisponiveis.isEmpty()) {
            Atendente atendente = atendentesDisponiveis.get(0);
            solicitacao.setAtendente(atendente);
            solicitacao.setStatus(EM_ATENDIMENTO);
            solicitacao.setDataAtualizacao(now());
            atendenteService.alocarSolicitacao(atendente.getId(), solicitacao);
        }
    }

    @Override
    @EventListener
    public void onSolicitacaoEncerrada(SolicitacaoEncerradaEvent solicitacaoEncerrada) {
        Atendente atendente = solicitacaoEncerrada.solicitacao().getAtendente();
        if (atendente != null) {
            Solicitacao proximaSolicitacao = solicitacaoService.obterProximaSolicitacaoPendente();
            if (proximaSolicitacao != null) {
                proximaSolicitacao.setAtendente(atendente);
                proximaSolicitacao.setDataAtualizacao(now());
                proximaSolicitacao.setStatus(EM_ATENDIMENTO);
                atendenteService.alocarSolicitacao(atendente.getId(), proximaSolicitacao);
            }
        }
    }
}
