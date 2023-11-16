package dev.luanfernandes.hub.controller;

import static dev.luanfernandes.hub.constants.TestConstants.ANY_ID;
import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getAtendenteRequest;
import static dev.luanfernandes.hub.domain.AtendenteTestUtils.getAtendenteResponse;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import dev.luanfernandes.hub.controller.impl.AtendenteControllerImpl;
import dev.luanfernandes.hub.domain.request.AtendenteRequest;
import dev.luanfernandes.hub.domain.response.AtendenteResponse;
import dev.luanfernandes.hub.service.impl.AtendenteServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AtendenteControllerTest {

    @Mock
    private AtendenteServiceImpl atendenteService;

    @InjectMocks
    private AtendenteControllerImpl atendenteController;

    @Test
    void whenCriarAtendenteWithValidRequest_thenShouldReturnCreatedStatus() {
        AtendenteRequest atendenteRequest = getAtendenteRequest();
        atendenteController.criarAtendente(atendenteRequest);
        verify(atendenteService).criarAtendente(atendenteRequest);
    }

    @Test
    void whenListarAtendentes_thenShouldReturnOkStatus() {
        ResponseEntity<List<AtendenteResponse>> response = atendenteController.listarAtendentes();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        verify(atendenteService).listarAtendentes();
    }

    @Test
    void whenListarAtendentesAndServiceThrowsException_thenShouldHandleException() {
        doThrow(new ResponseStatusException(INTERNAL_SERVER_ERROR))
                .when(atendenteService)
                .listarAtendentes();

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> atendenteController.listarAtendentes());
        assertEquals(INTERNAL_SERVER_ERROR, exception.getStatusCode());
    }

    @Test
    void whenListarAtendentesAndNoAtendentesAvailable_thenShouldReturnEmptyList() {
        when(atendenteService.listarAtendentes()).thenReturn(emptyList());
        ResponseEntity<List<AtendenteResponse>> response = atendenteController.listarAtendentes();
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(emptyList());
    }

    @Test
    void whenBuscarAtendentePorIdWithValidId_thenShouldReturnAtendenteResponse() {
        UUID atendenteId = ANY_ID;
        AtendenteResponse atendenteResponse = getAtendenteResponse();
        when(atendenteService.buscarAtendentePorId(atendenteId)).thenReturn(atendenteResponse);

        ResponseEntity<AtendenteResponse> response = atendenteController.buscarAtendentePorId(atendenteId);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(atendenteResponse);
        verify(atendenteService).buscarAtendentePorId(atendenteId);
    }

    @Test
    void whenBuscarAtendentePorIdWithInvalidId_thenShouldThrowException() {
        UUID atendenteId = ANY_ID;
        when(atendenteService.buscarAtendentePorId(atendenteId)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> atendenteController.buscarAtendentePorId(atendenteId));
    }
}
