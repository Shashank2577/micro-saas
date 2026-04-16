package com.crosscutting.starter.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        MDC.clear();
    }

    @Test
    void handleCcException_mapsStatusAndCode() {
        CcException ex = CcErrorCodes.resourceNotFound("Item 42 not found");

        ResponseEntity<ErrorResponse> response = handler.handleCcException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.error()).isEqualTo(CcErrorCodes.RESOURCE_NOT_FOUND);
        assertThat(body.message()).isEqualTo("Item 42 not found");
        assertThat(body.correlationId()).isNotBlank();
        assertThat(body.timestamp()).isNotNull();
    }

    @Test
    void handleCcException_usesCorrelationIdFromMdc() {
        MDC.put("correlationId", "test-corr-123");
        CcException ex = CcErrorCodes.forbidden("nope");

        ResponseEntity<ErrorResponse> response = handler.handleCcException(ex);

        assertThat(Objects.requireNonNull(response.getBody()).correlationId()).isEqualTo("test-corr-123");
    }

    @Test
    void handleCcException_serverError_returns500() {
        CcException ex = CcErrorCodes.internalError("boom", new RuntimeException("root cause"));

        ResponseEntity<ErrorResponse> response = handler.handleCcException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(Objects.requireNonNull(response.getBody()).error()).isEqualTo(CcErrorCodes.INTERNAL_ERROR);
    }

    @Test
    void handleValidation_returns400WithFieldErrors() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "email", "must not be blank"));
        bindingResult.addError(new FieldError("request", "name", "size must be between 1 and 100"));

        MethodParameter param = new MethodParameter(
                Objects.class.getDeclaredMethods()[0], -1);
        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(param, bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.error()).isEqualTo(CcErrorCodes.VALIDATION_ERROR);
        assertThat(body.details()).containsEntry("email", "must not be blank");
        assertThat(body.details()).containsEntry("name", "size must be between 1 and 100");
    }

    @Test
    void handleResponseStatus_preservesStatusCode() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid webhook signature");

        ResponseEntity<ErrorResponse> response = handler.handleResponseStatus(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.error()).isEqualTo(CcErrorCodes.UNAUTHORIZED);
        assertThat(body.message()).isEqualTo("Invalid webhook signature");
    }

    @Test
    void handleResponseStatus_mapsNotFoundCorrectly() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");

        ResponseEntity<ErrorResponse> response = handler.handleResponseStatus(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(response.getBody()).error()).isEqualTo(CcErrorCodes.RESOURCE_NOT_FOUND);
    }

    @Test
    void handleResponseStatus_mapsTooManyRequests() {
        ResponseStatusException ex = new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Rate limited");

        ResponseEntity<ErrorResponse> response = handler.handleResponseStatus(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        assertThat(Objects.requireNonNull(response.getBody()).error()).isEqualTo(CcErrorCodes.RATE_LIMITED);
    }

    @Test
    void handleAll_returns500WithCorrelationId() {
        MDC.put("correlationId", "corr-999");
        Exception ex = new RuntimeException("unexpected");

        ResponseEntity<ErrorResponse> response = handler.handleAll(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.error()).isEqualTo(CcErrorCodes.INTERNAL_ERROR);
        assertThat(body.correlationId()).isEqualTo("corr-999");
        assertThat(body.message()).contains("corr-999");
    }

    @Test
    void handleAll_generatesCorrelationIdWhenMdcEmpty() {
        Exception ex = new RuntimeException("unexpected");

        ResponseEntity<ErrorResponse> response = handler.handleAll(ex);

        ErrorResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.correlationId()).isNotBlank();
        // generated UUID format
        assertThat(body.correlationId()).containsPattern(
                "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }
}
