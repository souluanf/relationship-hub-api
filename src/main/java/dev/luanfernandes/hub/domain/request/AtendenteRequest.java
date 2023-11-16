package dev.luanfernandes.hub.domain.request;

import dev.luanfernandes.hub.domain.enums.TipoEquipeEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

@Validated
public record AtendenteRequest(
        @Email String email, @NotBlank @Size(max = 255) String nome, @NotNull TipoEquipeEnum equipe) {}
