package dev.luanfernandes.hub.repository;

import dev.luanfernandes.hub.domain.entity.Solicitacao;
import dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum;
import dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, UUID> {
    List<Solicitacao> findByStatus(StatusSolicitacaoEnum status);

    List<Solicitacao> findByStatusIsNotAndAtendenteIsNullOrderByDataCriacaoAsc(StatusSolicitacaoEnum status);

    List<Solicitacao> findByStatusAndAtendenteIsNullAndTipoOrderByDataCriacaoAsc(
            StatusSolicitacaoEnum statusSolicitacaoEnum, TipoSolicitacaoEnum tipoSolicitacao);
}
