package dev.luanfernandes.hub.service;

import static dev.luanfernandes.hub.constants.TestConstants.ANY_ID;
import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getAtendente;
import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getAtendenteList;
import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getAtendenteRequest;
import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getAtendenteResponse;
import static dev.luanfernandes.hub.domain.SolicitacaoTestUtils.getSolicitacao;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import dev.luanfernandes.hub.domain.entity.Atendente;
import dev.luanfernandes.hub.domain.entity.Solicitacao;
import dev.luanfernandes.hub.domain.enums.TipoSolicitacaoEnum;
import dev.luanfernandes.hub.domain.event.AtendenteCriadoEvent;
import dev.luanfernandes.hub.domain.mapper.AtendenteMapper;
import dev.luanfernandes.hub.domain.request.AtendenteRequest;
import dev.luanfernandes.hub.domain.response.AtendenteResponse;
import dev.luanfernandes.hub.repository.AtendenteRepository;
import dev.luanfernandes.hub.service.impl.AtendenteServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AtendenteServiceTest {

    @Mock
    private AtendenteRepository atendenteRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Spy
    private AtendenteMapper atendenteMapper;

    @InjectMocks
    private AtendenteServiceImpl atendenteService;

    @Test
    void whenAlocarSolicitacao_thenShouldAddSolicitacaoToAtendente() {
        UUID atendenteId = ANY_ID;
        Solicitacao solicitacao = getSolicitacao();
        Atendente atendente = getAtendente();
        when(atendenteRepository.findById(atendenteId)).thenReturn(Optional.of(atendente));
        atendenteService.alocarSolicitacao(atendenteId, solicitacao);
        assertTrue(atendente.getSolicitacoes().contains(solicitacao));
        verify(atendenteRepository).save(atendente);
    }

    @Test
    void whenCriarAtendente_thenShouldCreateAtendente() {
        AtendenteRequest atendenteRequest = getAtendenteRequest();
        Atendente atendenteMock = new Atendente();
        when(atendenteMapper.atendenteRequestToAtendente(atendenteRequest)).thenReturn(atendenteMock);
        when(atendenteRepository.existsByEmail(atendenteRequest.email())).thenReturn(false);
        atendenteService.criarAtendente(atendenteRequest);
        verify(atendenteRepository).save(atendenteMock);
        verify(eventPublisher).publishEvent(any(AtendenteCriadoEvent.class));
    }

    @Test
    void whenListarAtendentes_thenShouldReturnList() {
        List<Atendente> expectedList = getAtendenteList();
        when(atendenteRepository.findAll()).thenReturn(expectedList);
        List<AtendenteResponse> result = atendenteService.listarAtendentes();
        assertEquals(expectedList.size(), result.size());
    }

    @Test
    void whenAlocarSolicitacaoAndAtendenteNotFound_thenShouldThrowEntityNotFoundException() {
        UUID atendenteId = ANY_ID;
        Solicitacao solicitacao = getSolicitacao();
        when(atendenteRepository.findById(atendenteId)).thenReturn(empty());
        assertThrows(EntityNotFoundException.class, () -> atendenteService.alocarSolicitacao(atendenteId, solicitacao));
    }

    @Test
    void whenCriarAtendenteAndAtendenteExists_thenShouldThrowResponseStatusException() {
        AtendenteRequest atendenteRequest = getAtendenteRequest();
        when(atendenteRepository.existsByEmail(atendenteRequest.email())).thenReturn(true);
        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> atendenteService.criarAtendente(atendenteRequest));
        assertEquals(UNPROCESSABLE_ENTITY, exception.getStatusCode());
    }

    @Test
    void whenBuscarAtendentePorIdWithValidId_thenShouldReturnAtendente() {
        UUID atendenteId = ANY_ID;
        Atendente atendenteMock = new Atendente();
        AtendenteResponse atendenteResponseMock = getAtendenteResponse();

        when(atendenteRepository.findById(atendenteId)).thenReturn(Optional.of(atendenteMock));
        when(atendenteMapper.atendenteToAtendenteResponse(atendenteMock)).thenReturn(atendenteResponseMock);

        AtendenteResponse result = atendenteService.buscarAtendentePorId(atendenteId);

        assertEquals(atendenteResponseMock, result);
        verify(atendenteRepository).findById(atendenteId);
        verify(atendenteMapper).atendenteToAtendenteResponse(atendenteMock);
    }

    @Test
    void whenBuscarAtendentePorIdWithInvalidId_thenShouldThrowEntityNotFoundException() {
        UUID atendenteId = ANY_ID;
        when(atendenteRepository.findById(atendenteId)).thenReturn(empty());
        assertThrows(EntityNotFoundException.class, () -> atendenteService.buscarAtendentePorId(atendenteId));
        verify(atendenteRepository).findById(atendenteId);
    }

    @Test
    void whenObterAtendentesDisponiveis_thenShouldReturnCorrectList() {
        List<Atendente> atendentesTeste = getAtendenteList();
        TipoSolicitacaoEnum tipoTeste = TipoSolicitacaoEnum.PROBLEMA_CARTAO;
        when(atendenteRepository.findAtendentesDisponiveis(tipoTeste.getTipoEquipe()))
                .thenReturn(atendentesTeste);
        List<Atendente> resultado = atendenteService.obterAtendentesDisponiveis(tipoTeste);
        assertEquals(atendentesTeste, resultado);
    }
}
