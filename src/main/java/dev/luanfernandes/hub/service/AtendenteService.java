package dev.luanfernandes.hub.service;

import dev.luanfernandes.hub.domain.entity.Atendente;
import dev.luanfernandes.hub.domain.entity.Solicitacao;
import dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum;
import dev.luanfernandes.hub.domain.request.AtendenteRequest;
import dev.luanfernandes.hub.domain.response.AtendenteResponse;
import java.util.List;
import java.util.UUID;

public interface AtendenteService {
    List<Atendente> obterAtendentesDisponiveis(TipoSolicitacaoEnum tipoSolicitacao);

    void alocarSolicitacao(UUID atendenteId, Solicitacao solicitacao);

    void criarAtendente(AtendenteRequest atendenteRequest);

    List<AtendenteResponse> listarAtendentes();

    AtendenteResponse buscarAtendentePorId(UUID id);
}
