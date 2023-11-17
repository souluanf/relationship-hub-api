package dev.luanfernandes.hub.service.impl;

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import dev.luanfernandes.hub.domain.entity.Atendente;
import dev.luanfernandes.hub.domain.entity.Solicitacao;
import dev.luanfernandes.hub.domain.enums.TipoEquipeEnum;
import dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum;
import dev.luanfernandes.hub.domain.event.AtendenteCriadoEvent;
import dev.luanfernandes.hub.domain.mapper.AtendenteMapper;
import dev.luanfernandes.hub.domain.request.AtendenteRequest;
import dev.luanfernandes.hub.domain.response.AtendenteResponse;
import dev.luanfernandes.hub.repository.AtendenteRepository;
import dev.luanfernandes.hub.service.AtendenteService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AtendenteServiceImpl implements AtendenteService {

    private final AtendenteRepository atendenteRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AtendenteMapper atendenteMapper;

    @Override
    public List<Atendente> obterAtendentesDisponiveis(TipoSolicitacaoEnum tipoSolicitacao) {
        TipoEquipeEnum equipeDesejada = tipoSolicitacao.getTipoEquipe();
        return atendenteRepository.findAtendentesDisponiveis(equipeDesejada);
    }

    @Override
    public void alocarSolicitacao(UUID atendenteId, Solicitacao solicitacao) {
        Atendente atendente = atendenteRepository.findById(atendenteId).orElseThrow(EntityNotFoundException::new);
        atendente.getSolicitacoes().add(solicitacao);
        atendenteRepository.save(atendente);
    }

    @Override
    public void criarAtendente(AtendenteRequest atendenteRequest) {
        if (atendenteRepository.existsByEmail(atendenteRequest.email())) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY);
        }
        Atendente novoAtendente = atendenteMapper.atendenteRequestToAtendente(atendenteRequest);
        atendenteRepository.save(novoAtendente);
        AtendenteCriadoEvent evento = new AtendenteCriadoEvent(novoAtendente);
        eventPublisher.publishEvent(evento);
    }

    @Override
    public List<AtendenteResponse> listarAtendentes() {
        return atendenteRepository.findAll().stream()
                .map(atendenteMapper::atendenteToAtendenteResponse)
                .toList();
    }

    @Override
    public AtendenteResponse buscarAtendentePorId(UUID id) {
        Atendente atendente = atendenteRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return atendenteMapper.atendenteToAtendenteResponse(atendente);
    }
}
