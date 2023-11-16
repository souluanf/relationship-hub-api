package dev.luanfernandes.hub.domain.response;

import java.util.List;

public record SolicitacaoDataResponse(
        List<SolicitacaoResponse> fila,
        List<SolicitacaoResponse> emAtendimento,
        List<SolicitacaoResponse> encerradas) {}
