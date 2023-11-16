package dev.luanfernandes.hub.domain.enums;

import static dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum.PROBLEMA_CARTAO;
import static dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum.PROBLEMA_EMPRESTIMO;
import static dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum.PROBLEMA_OUTROS;

import lombok.Getter;

@Getter
public enum TipoEquipeEnum {
    CARTOES(PROBLEMA_CARTAO),
    EMPRESTIMOS(PROBLEMA_EMPRESTIMO),
    OUTROS(PROBLEMA_OUTROS);

    private final TipoSolicitacaoEnum solicitacaoCorrespondente;

    TipoEquipeEnum(TipoSolicitacaoEnum equipe) {
        this.solicitacaoCorrespondente = equipe;
    }
}
