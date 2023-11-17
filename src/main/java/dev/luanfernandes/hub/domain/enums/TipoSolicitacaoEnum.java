package dev.luanfernandes.hub.domain.enums;

import lombok.Getter;

@Getter
public enum TipoSolicitacaoEnum {
    PROBLEMA_CARTAO,
    SOLICITACAO_EMPRESTIMO,
    OUTROS;

    public TipoEquipeEnum getTipoEquipe() {
        return switch (this) {
            case PROBLEMA_CARTAO -> TipoEquipeEnum.CARTOES;
            case SOLICITACAO_EMPRESTIMO -> TipoEquipeEnum.EMPRESTIMOS;
            case OUTROS -> TipoEquipeEnum.OUTROS_ASSUNTOS;
        };
    }
}
