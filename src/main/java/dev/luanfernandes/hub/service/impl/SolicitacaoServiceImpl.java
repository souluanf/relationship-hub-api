package dev.luanfernandes.hub.service.impl;

import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.ENCERRADA;
import static dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum.FILA;
import static java.time.LocalDateTime.now;
import static java.util.UUID.fromString;

import dev.luanfernandes.hub.domain.entity.Solicitacao;
import dev.luanfernandes.hub.domain.enums.StatusSolicitacaoEnum;
import dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum;
import dev.luanfernandes.hub.domain.event.SolicitacaoCriadaEvent;
import dev.luanfernandes.hub.domain.event.SolicitacaoEncerradaEvent;
import dev.luanfernandes.hub.domain.mapper.SolicitacaoMapper;
import dev.luanfernandes.hub.domain.request.SolicitacaoRequest;
import dev.luanfernandes.hub.domain.response.SolicitacaoResponse;
import dev.luanfernandes.hub.repository.SolicitacaoRepository;
import dev.luanfernandes.hub.service.SolicitacaoService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolicitacaoServiceImpl implements SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SolicitacaoMapper solicitacaoMapper;

    @Override
    public List<SolicitacaoResponse> listarPorStatus(StatusSolicitacaoEnum status) {
        return solicitacaoRepository.findByStatus(status).stream()
                .map(solicitacaoMapper::solicitacaoToSolicitacaoResponse)
                .toList();
    }

    @Override
    public SolicitacaoResponse criarSolicitacao(SolicitacaoRequest request) {
        Solicitacao solicitacao = solicitacaoMapper.solicitacaoRequestToSolicitacao(request);
        solicitacaoRepository.save(solicitacao);
        SolicitacaoCriadaEvent evento = new SolicitacaoCriadaEvent(solicitacao);
        eventPublisher.publishEvent(evento);
        return solicitacaoMapper.solicitacaoToSolicitacaoResponse(solicitacao);
    }

    @Override
    public Solicitacao obterProximaSolicitacaoPendente() {
        return solicitacaoRepository.findByStatusIsNotAndAtendenteIsNullOrderByDataCriacaoAsc(ENCERRADA).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Solicitacao> obterSolicitacoesPendentesPorEquipe(TipoSolicitacaoEnum tipoSolicitacao) {
        return solicitacaoRepository.findByStatusAndAtendenteIsNullAndTipoOrderByDataCriacaoAsc(FILA, tipoSolicitacao);
    }

    @Override
    public void encerrarSolicitacao(String solicitacaoId) {
        Solicitacao solicitacao = getSolicitacaoOrThrow(solicitacaoId);
        solicitacao.setStatus(ENCERRADA);
        solicitacao.setDataAtualizacao(now());
        solicitacaoRepository.save(solicitacao);
        eventPublisher.publishEvent(new SolicitacaoEncerradaEvent(solicitacao));
    }

    private Solicitacao getSolicitacaoOrThrow(String id) {
        return solicitacaoRepository.findById(fromString(id)).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public SolicitacaoResponse buscarPorId(String id) {
        return solicitacaoRepository
                .findById(fromString(id))
                .map(solicitacaoMapper::solicitacaoToSolicitacaoResponse)
                .orElseThrow(EntityNotFoundException::new);
    }
}
