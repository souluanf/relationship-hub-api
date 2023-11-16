package dev.luanfernandes.hub.common.exception;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerAdviceTest {
    @InjectMocks
    private ExceptionHandlerAdvice exceptionHandlerAdvice;

    @Test
    @DisplayName("Should return a bad request when handle a MethodArgumentNotValidException")
    void handleMethodArgumentNotValidException() {
        MethodParameter methodParameter = new MethodParameter(this.getClass().getDeclaredMethods()[0], -1);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(new FieldError("objectName", "field", "defaultMessage"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ProblemDetail problemDetail = exceptionHandlerAdvice.handleMethodArgumentNotValidException(exception);

        assertNotNull(problemDetail);
        assertEquals(BAD_REQUEST.value(), problemDetail.getStatus());
    }

    @Test
    @DisplayName("Should return a bad request when handle a ConstraintViolationException")
    void handleConstraintViolationException() {
        var exception = new ConstraintViolationException("message", null);

        var responseEntity = exceptionHandlerAdvice.handleConstraintViolationException(exception);

        var body = responseEntity.getBody();

        assertEquals(requireNonNull(body).getStatus(), BAD_REQUEST.value());
        assertNotNull(requireNonNull(body).getProperties());
    }

    @Test
    @DisplayName("Should return the correct status and detail when handling a ResponseStatusException")
    void handleResponseStatusException() {
        var status = NOT_FOUND;
        var reason = "Resource not found";
        var exception = new ResponseStatusException(status, reason);

        var responseEntity = exceptionHandlerAdvice.handleResponseStatusException(exception);

        assertNotNull(responseEntity);
        assertEquals(status, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(reason, responseEntity.getBody().getDetail());
    }

    @Test
    @DisplayName("Should return not found status when handling EntityNotFoundException")
    void handleEntityNotFoundException() {
        var message = "Entity not found";
        var exception = new EntityNotFoundException(message);

        var problemDetail = exceptionHandlerAdvice.handleEntityNotFoundException(exception);

        assertNotNull(problemDetail);
        assertEquals(NOT_FOUND.value(), problemDetail.getStatus());
        assertEquals(message, problemDetail.getDetail());
    }
}
