package dev.luanfernandes.hub.domain.response;

import dev.luanfernandes.hub.domain.enums.TipoEquipeEnum;
import java.util.UUID;

public record AtendenteResponse(UUID id, String nome, String email, TipoEquipeEnum equipe, ChamadosResponse chamados) {}
