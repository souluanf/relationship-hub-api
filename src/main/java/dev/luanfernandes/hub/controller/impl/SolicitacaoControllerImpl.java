package dev.luanfernandes.hub.controller.impl;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

import dev.luanfernandes.hub.controller.SolicitacaoController;
import dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum;
import dev.luanfernandes.hub.domain.request.SolicitacaoRequest;
import dev.luanfernandes.hub.domain.response.SolicitacaoResponse;
import dev.luanfernandes.hub.service.SolicitacaoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SolicitacaoControllerImpl implements SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    @Override
    public ResponseEntity<SolicitacaoResponse> solicitar(SolicitacaoRequest request) {
        return status(ACCEPTED).body(solicitacaoService.criarSolicitacao(request));
    }

    @Override
    public ResponseEntity<SolicitacaoResponse> buscarPorId(String id) {
        return ok(solicitacaoService.buscarPorId(id));
    }

    @Override
    public ResponseEntity<List<SolicitacaoResponse>> listarPorStatus(StatusSolicitacaoEnum status) {
        return ok(solicitacaoService.listarPorStatus(status));
    }

    @Override
    public ResponseEntity<Void> encerrar(String id) {
        solicitacaoService.encerrarSolicitacao(id);
        return noContent().build();
    }
}
