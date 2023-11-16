package dev.luanfernandes.hub.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathConstants {
    private static final String API = "/api";
    public static final String ATENDENTES_V1 = API + "/v1/atendentes";
    public static final String SOLICITACOES_V1 = API + "/v1/solicitacoes";
    public static final String SOLICITACOES_STATUS = SOLICITACOES_V1 + "/status";
}
