package dev.luanfernandes.hub.repository;

import dev.luanfernandes.hub.domain.entity.Atendente;
import dev.luanfernandes.hub.domain.enums.TipoEquipeEnum;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AtendenteRepository extends JpaRepository<Atendente, UUID> {
    @Query(
            """
            SELECT a FROM Atendente a LEFT JOIN a.solicitacoes s
            WHERE a.equipe = :equipe
            AND (SELECT COUNT(s) FROM a.solicitacoes s WHERE s.status = 'EM_ATENDIMENTO' ) < 3
            ORDER BY (SELECT COUNT(s) FROM a.solicitacoes s WHERE s.status = 'EM_ATENDIMENTO') ASC""")
    List<Atendente> findAtendentesDisponiveis(@Param("equipe") TipoEquipeEnum equipe);

    boolean existsByEmail(String email);
}
