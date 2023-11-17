package dev.luanfernandes.hub.domain;

import static dev.luanfernandes.hub.constants.TestConstants.ANY_ID;
import static dev.luanfernandes.hub.constants.TestConstants.ANY_STRING;
import static dev.luanfernandes.hub.constants.TestConstants.VALID_EMAIL;
import static dev.luanfernandes.hub.domain.enums.TipoEquipeEnum.CARTOES;
import static java.util.List.of;

import dev.luanfernandes.hub.domain.entity.Atendente;
import dev.luanfernandes.hub.domain.request.AtendenteRequest;
import dev.luanfernandes.hub.domain.response.AtendenteResponse;
import dev.luanfernandes.hub.domain.response.ChamadosResponse;
import java.util.ArrayList;
import java.util.List;

public class AtendenteTestUtils {

    public static AtendenteRequest getAtendenteRequest() {
        return new AtendenteRequest(VALID_EMAIL, ANY_STRING, CARTOES);
    }

    public static AtendenteRequest getInvalidAtendenteRequest() {
        return new AtendenteRequest(ANY_STRING, ANY_STRING, CARTOES);
    }

    public static Atendente getAtendente() {
        return new Atendente(ANY_ID, ANY_STRING, VALID_EMAIL, CARTOES, new ArrayList<>());
    }

    public static List<Atendente> getAtendenteList() {
        return of(getAtendente());
    }

    public static List<AtendenteResponse> getAtendenteResponseList() {
        return of(getAtendenteResponse());
    }

    public static AtendenteResponse getAtendenteResponse() {
        var atendente = getAtendente();
        return new AtendenteResponse(
                atendente.getId(),
                atendente.getNome(),
                atendente.getEmail(),
                atendente.getEquipe(),
                new ChamadosResponse(0L, 0L));
    }
}
