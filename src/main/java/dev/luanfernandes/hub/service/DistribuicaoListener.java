package dev.luanfernandes.hub.service;

import dev.luanfernandes.hub.domain.event.AtendenteCriadoEvent;
import dev.luanfernandes.hub.domain.event.SolicitacaoCriadaEvent;
import dev.luanfernandes.hub.domain.event.SolicitacaoEncerradaEvent;
import org.springframework.context.event.EventListener;

public interface DistribuicaoListener {
    @EventListener
    void onAtendenteCriado(AtendenteCriadoEvent event);

    @EventListener
    void onSolicitacaoCriada(SolicitacaoCriadaEvent event);

    @EventListener
    void onSolicitacaoEncerrada(SolicitacaoEncerradaEvent solicitacaoEncerrada);
}
