package dev.luanfernandes.hub.domain.entity;

import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.FILA;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.AUTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum;
import dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "solicitacoes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;

    @Enumerated(STRING)
    @Column(nullable = false)
    private TipoSolicitacaoEnum tipo;

    @Enumerated(STRING)
    @Column(nullable = false)
    private StatusSolicitacaoEnum status = FILA;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "atendente_id")
    private Atendente atendente;
}
