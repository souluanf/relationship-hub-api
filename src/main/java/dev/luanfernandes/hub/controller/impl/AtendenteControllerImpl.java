package dev.luanfernandes.hub.controller.impl;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import dev.luanfernandes.hub.controller.AtendenteController;
import dev.luanfernandes.hub.domain.request.AtendenteRequest;
import dev.luanfernandes.hub.domain.response.AtendenteResponse;
import dev.luanfernandes.hub.service.impl.AtendenteServiceImpl;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AtendenteControllerImpl implements AtendenteController {

    private final AtendenteServiceImpl atendenteService;

    @Override
    public ResponseEntity<Void> criarAtendente(AtendenteRequest atendenteRequest) {
        atendenteService.criarAtendente(atendenteRequest);
        return status(CREATED).build();
    }

    @Override
    public ResponseEntity<List<AtendenteResponse>> listarAtendentes() {
        return ok(atendenteService.listarAtendentes());
    }

    @Override
    public ResponseEntity<AtendenteResponse> buscarAtendentePorId(UUID id) {
        return ok(atendenteService.buscarAtendentePorId(id));
    }
}
