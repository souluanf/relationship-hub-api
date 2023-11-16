package dev.luanfernandes.hub.service;

import dev.luanfernandes.hub.domain.entity.Solicitacao;
import dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum;
import dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum;
import dev.luanfernandes.hub.domain.request.SolicitacaoRequest;
import dev.luanfernandes.hub.domain.response.SolicitacaoResponse;
import java.util.List;

public interface SolicitacaoService {

    SolicitacaoResponse criarSolicitacao(SolicitacaoRequest request);

    Solicitacao obterProximaSolicitacaoPendente();

    List<Solicitacao> obterSolicitacoesPendentesPorEquipe(TipoSolicitacaoEnum tipoSolicitacao);

    void encerrarSolicitacao(String solicitacaoId);

    SolicitacaoResponse buscarPorId(String solicitacaoId);

    List<SolicitacaoResponse> listarPorStatus(StatusSolicitacaoEnum status);
}
