package com.crosscutting.starter.error;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CcExceptionTest {

    @Test
    void constructorSetsAllFields() {
        CcException ex = new CcException("TEST_ERROR", "Something went wrong", 400);

        assertThat(ex.getErrorCode()).isEqualTo("TEST_ERROR");
        assertThat(ex.getMessage()).isEqualTo("Something went wrong");
        assertThat(ex.getHttpStatus()).isEqualTo(400);
    }

    @Test
    void constructorWithCausePreservesCause() {
        RuntimeException cause = new RuntimeException("root cause");
        CcException ex = new CcException("INTERNAL_ERROR", "Wrapped", 500, cause);

        assertThat(ex.getCause()).isEqualTo(cause);
        assertThat(ex.getErrorCode()).isEqualTo("INTERNAL_ERROR");
        assertThat(ex.getHttpStatus()).isEqualTo(500);
    }
}
