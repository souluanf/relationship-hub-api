package dev.luanfernandes.hub.domain.enums;

import static dev.luanfernandes.hub.domain.enums.TipoEquipeEnum.CARTOES;
import static dev.luanfernandes.hub.domain.enums.TipoEquipeEnum.EMPRESTIMOS;
import static dev.luanfernandes.hub.domain.enums.TipoEquipeEnum.OUTROS;

import lombok.Getter;

@Getter
public enum TipoSolicitacaoEnum {
    PROBLEMA_CARTAO(CARTOES),
    PROBLEMA_EMPRESTIMO(EMPRESTIMOS),
    PROBLEMA_OUTROS(OUTROS);

    private final TipoEquipeEnum equipeCorrespondente;

    TipoSolicitacaoEnum(TipoEquipeEnum equipe) {
        this.equipeCorrespondente = equipe;
    }
}
