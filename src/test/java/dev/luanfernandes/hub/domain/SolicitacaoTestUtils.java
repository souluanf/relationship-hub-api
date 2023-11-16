package dev.luanfernandes.hub.domain;

import static dev.luanfernandes.hub.constants.TestConstants.ANY_ID;
import static dev.luanfernandes.hub.constants.TestConstants.ANY_LOCAL_DATE_TIME;
import static dev.luanfernandes.hub.constants.TestConstants.ANY_STRING;
import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getAtendente;
import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.FILA;
import static dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum.PROBLEMA_CARTAO;
import static java.util.List.of;

import dev.luanfernandes.hub.domain.entity.Solicitacao;
import dev.luanfernandes.hub.domain.request.SolicitacaoRequest;
import dev.luanfernandes.hub.domain.response.SolicitacaoResponse;
import java.util.List;

public class SolicitacaoTestUtils {

    public static SolicitacaoRequest getSolicitacaoRequest() {
        return new SolicitacaoRequest(PROBLEMA_CARTAO, ANY_STRING);
    }

    public static Solicitacao getSolicitacao() {
        return new Solicitacao(
                ANY_ID, PROBLEMA_CARTAO, FILA, ANY_STRING, ANY_LOCAL_DATE_TIME, ANY_LOCAL_DATE_TIME, getAtendente());
    }

    public static List<Solicitacao> getSolicitacaoList() {
        return of(getSolicitacao(), getSolicitacao());
    }

    public static List<Solicitacao> getSolicitacaoListMoreThanThree() {
        return of(getSolicitacao(), getSolicitacao(), getSolicitacao(), getSolicitacao());
    }

    public static SolicitacaoResponse getSolicitacaoResponse() {
        var solicitacao = getSolicitacao();
        return new SolicitacaoResponse(
                solicitacao.getId(),
                solicitacao.getTipo(),
                solicitacao.getStatus(),
                solicitacao.getDescricao(),
                solicitacao.getDataCriacao(),
                solicitacao.getDataAtualizacao(),
                solicitacao.getAtendente().getId());
    }

    public static List<SolicitacaoResponse> getSolicitacaoResponseList() {
        return of(getSolicitacaoResponse());
    }
}
