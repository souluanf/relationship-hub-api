package dev.luanfernandes.hub.domain.entity;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.AUTO;

import dev.luanfernandes.hub.domain.enums.TipoEquipeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "atendentes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Atendente {
    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(STRING)
    @Column(nullable = false)
    private TipoEquipeEnum equipe;

    @OneToMany(mappedBy = "atendente", cascade = ALL, orphanRemoval = true)
    private List<Solicitacao> solicitacoes = new ArrayList<>();
}
