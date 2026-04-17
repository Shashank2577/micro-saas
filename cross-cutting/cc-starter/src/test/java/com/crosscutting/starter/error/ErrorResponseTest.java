package com.crosscutting.starter.error;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void simpleConstructorSetsTimestampAndEmptyDetails() {
        ErrorResponse resp = new ErrorResponse("NOT_FOUND", "Not found", "corr-123");

        assertThat(resp.error()).isEqualTo("NOT_FOUND");
        assertThat(resp.message()).isEqualTo("Not found");
        assertThat(resp.correlationId()).isEqualTo("corr-123");
        assertThat(resp.timestamp()).isNotNull();
        assertThat(resp.details()).isEmpty();
    }

    @Test
    void detailsConstructorIncludesFieldErrors() {
        Map<String, String> details = Map.of("name", "must not be blank");
        ErrorResponse resp = new ErrorResponse("VALIDATION_ERROR", "Validation failed", "corr-456", details);

        assertThat(resp.details()).containsEntry("name", "must not be blank");
        assertThat(resp.error()).isEqualTo("VALIDATION_ERROR");
    }

    @Test
    void fullConstructorAcceptsAllFields() {
        var now = java.time.Instant.now();
        ErrorResponse resp = new ErrorResponse("ERR", "msg", "c1", now, Map.of("k", "v"));

        assertThat(resp.error()).isEqualTo("ERR");
        assertThat(resp.message()).isEqualTo("msg");
        assertThat(resp.correlationId()).isEqualTo("c1");
        assertThat(resp.timestamp()).isEqualTo(now);
        assertThat(resp.details()).containsEntry("k", "v");
    }
}
