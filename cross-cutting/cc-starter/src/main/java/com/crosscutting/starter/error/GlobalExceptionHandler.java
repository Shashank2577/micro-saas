package com.crosscutting.starter.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String CORRELATION_ID_KEY = "correlationId";

    @ExceptionHandler(CcException.class)
    public ResponseEntity<ErrorResponse> handleCcException(CcException ex) {
        String correlationId = resolveCorrelationId();
        int status = ex.getHttpStatus();

        if (status >= 500) {
            log.error("[{}] CcException: {} - {}", correlationId, ex.getErrorCode(), ex.getMessage(), ex);
        } else {
            log.warn("[{}] CcException: {} - {}", correlationId, ex.getErrorCode(), ex.getMessage());
        }

        ErrorResponse body = new ErrorResponse(ex.getErrorCode(), ex.getMessage(), correlationId);
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String correlationId = resolveCorrelationId();
        log.warn("[{}] Validation error: {}", correlationId, ex.getMessage());

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }

        ErrorResponse body = new ErrorResponse(
                CcErrorCodes.VALIDATION_ERROR,
                "Validation failed",
                correlationId,
                fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        String correlationId = resolveCorrelationId();
        log.warn("[{}] Access denied: {}", correlationId, ex.getMessage());

        ErrorResponse body = new ErrorResponse(CcErrorCodes.FORBIDDEN, "Access denied", correlationId);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
        String correlationId = resolveCorrelationId();
        log.warn("[{}] Malformed request body: {}", correlationId, ex.getMessage());

        ErrorResponse body = new ErrorResponse(
                CcErrorCodes.VALIDATION_ERROR,
                "Malformed request body",
                correlationId
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        String correlationId = resolveCorrelationId();
        int status = ex.getStatusCode().value();

        if (status >= 500) {
            log.error("[{}] ResponseStatusException: {} - {}", correlationId, status, ex.getReason(), ex);
        } else {
            log.warn("[{}] ResponseStatusException: {} - {}", correlationId, status, ex.getReason());
        }

        ErrorResponse body = new ErrorResponse(
                deriveErrorCode(status),
                ex.getReason() != null ? ex.getReason() : "Request failed",
                correlationId
        );
        return ResponseEntity.status(ex.getStatusCode()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        String correlationId = resolveCorrelationId();
        log.error("[{}] Unhandled exception: {}", correlationId, ex.getMessage(), ex);

        ErrorResponse body = new ErrorResponse(
                CcErrorCodes.INTERNAL_ERROR,
                "An unexpected error occurred. Correlation ID: " + correlationId,
                correlationId
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private static String deriveErrorCode(int status) {
        return switch (status) {
            case 400 -> CcErrorCodes.VALIDATION_ERROR;
            case 401 -> CcErrorCodes.UNAUTHORIZED;
            case 403 -> CcErrorCodes.FORBIDDEN;
            case 404 -> CcErrorCodes.RESOURCE_NOT_FOUND;
            case 409 -> CcErrorCodes.RESOURCE_CONFLICT;
            case 429 -> CcErrorCodes.RATE_LIMITED;
            default -> CcErrorCodes.INTERNAL_ERROR;
        };
    }

    private String resolveCorrelationId() {
        String id = MDC.get(CORRELATION_ID_KEY);
        return id != null ? id : UUID.randomUUID().toString();
    }
}
