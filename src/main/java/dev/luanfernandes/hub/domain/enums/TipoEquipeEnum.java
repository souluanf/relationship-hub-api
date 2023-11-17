package dev.luanfernandes.hub.domain.enums;

import lombok.Getter;

@Getter
public enum TipoEquipeEnum {
    CARTOES,
    EMPRESTIMOS,
    OUTROS_ASSUNTOS;

    public TipoSolicitacaoEnum getTipoSolicitacao() {
        return switch (this) {
            case CARTOES -> TipoSolicitacaoEnum.PROBLEMA_CARTAO;
            case EMPRESTIMOS -> TipoSolicitacaoEnum.SOLICITACAO_EMPRESTIMO;
            case OUTROS_ASSUNTOS -> TipoSolicitacaoEnum.OUTROS;
        };
    }
}
