package dev.luanfernandes.hub.domain.mapper;

import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.EM_ATENDIMENTO;
import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.ENCERRADA;

import dev.luanfernandes.hub.domain.entity.Atendente;
import dev.luanfernandes.hub.domain.request.AtendenteRequest;
import dev.luanfernandes.hub.domain.response.AtendenteResponse;
import dev.luanfernandes.hub.domain.response.ChamadosResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AtendenteMapper {

    @Mapping(target = "chamados", expression = "java(contarChamadosPorStatus(atendente))")
    AtendenteResponse atendenteToAtendenteResponse(Atendente atendente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "solicitacoes", ignore = true)
    Atendente atendenteRequestToAtendente(AtendenteRequest request);

    default ChamadosResponse contarChamadosPorStatus(Atendente atendente) {
        long emAtendimento = atendente.getSolicitacoes().stream()
                .filter(solicitacao -> solicitacao.getStatus().equals(EM_ATENDIMENTO))
                .count();
        long encerradas = atendente.getSolicitacoes().stream()
                .filter(solicitacao -> solicitacao.getStatus().equals(ENCERRADA))
                .count();
        return new ChamadosResponse(emAtendimento, encerradas);
    }
}
