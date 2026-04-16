package com.crosscutting.starter.error;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        String error,
        String message,
        String correlationId,
        Instant timestamp,
        Map<String, String> details
) {

    public ErrorResponse(String error, String message, String correlationId) {
        this(error, message, correlationId, Instant.now(), Map.of());
    }

    public ErrorResponse(String error, String message, String correlationId, Map<String, String> details) {
        this(error, message, correlationId, Instant.now(), details);
    }
}
