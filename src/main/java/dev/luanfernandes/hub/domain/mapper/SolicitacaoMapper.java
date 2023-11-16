package dev.luanfernandes.hub.domain.mapper;

import dev.luanfernandes.hub.domain.entity.Atendente;
import dev.luanfernandes.hub.domain.entity.Solicitacao;
import dev.luanfernandes.hub.domain.request.SolicitacaoRequest;
import dev.luanfernandes.hub.domain.response.SolicitacaoResponse;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SolicitacaoMapper {
    @Mapping(target = "atendenteId", source = "atendente", qualifiedByName = "atendenteToUuid")
    SolicitacaoResponse solicitacaoToSolicitacaoResponse(Solicitacao solicitacao);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "dataAtualizacao", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "atendente", ignore = true)
    Solicitacao solicitacaoRequestToSolicitacao(SolicitacaoRequest request);

    @Named("atendenteToUuid")
    default UUID atendenteToUuid(Atendente atendente) {
        return atendente != null ? atendente.getId() : null;
    }
}
