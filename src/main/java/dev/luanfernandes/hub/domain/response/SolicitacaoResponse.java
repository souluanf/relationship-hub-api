package dev.luanfernandes.hub.domain.response;

import dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum;
import dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum;
import java.time.LocalDateTime;
import java.util.UUID;

public record SolicitacaoResponse(
        UUID id,
        TipoSolicitacaoEnum tipo,
        StatusSolicitacaoEnum status,
        String descricao,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao,
        UUID atendenteId) {}
