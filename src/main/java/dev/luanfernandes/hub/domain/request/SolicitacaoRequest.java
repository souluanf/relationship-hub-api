package dev.luanfernandes.hub.domain.request;

import dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SolicitacaoRequest(@NotNull TipoSolicitacaoEnum tipo, @NotBlank @Size(max = 255) String descricao) {}
