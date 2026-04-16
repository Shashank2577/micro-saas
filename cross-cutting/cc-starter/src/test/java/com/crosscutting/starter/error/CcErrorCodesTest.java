package com.crosscutting.starter.error;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CcErrorCodesTest {

    static Stream<Arguments> factoryMethods() {
        return Stream.of(
                Arguments.of("TENANT_NOT_FOUND", 404, CcErrorCodes.tenantNotFound("msg")),
                Arguments.of("TENANT_DISABLED", 403, CcErrorCodes.tenantDisabled("msg")),
                Arguments.of("FORBIDDEN", 403, CcErrorCodes.forbidden("msg")),
                Arguments.of("UNAUTHORIZED", 401, CcErrorCodes.unauthorized("msg")),
                Arguments.of("PERMISSION_DENIED", 403, CcErrorCodes.permissionDenied("msg")),
                Arguments.of("RESOURCE_NOT_FOUND", 404, CcErrorCodes.resourceNotFound("msg")),
                Arguments.of("RESOURCE_CONFLICT", 409, CcErrorCodes.resourceConflict("msg")),
                Arguments.of("VALIDATION_ERROR", 400, CcErrorCodes.validationError("msg")),
                Arguments.of("RATE_LIMITED", 429, CcErrorCodes.rateLimited("msg")),
                Arguments.of("INTERNAL_ERROR", 500, CcErrorCodes.internalError("msg"))
        );
    }

    @ParameterizedTest
    @MethodSource("factoryMethods")
    void factoryMethodProducesCorrectErrorCodeAndStatus(String expectedCode, int expectedStatus, CcException ex) {
        assertThat(ex.getErrorCode()).isEqualTo(expectedCode);
        assertThat(ex.getHttpStatus()).isEqualTo(expectedStatus);
        assertThat(ex.getMessage()).isEqualTo("msg");
    }

    @Test
    void internalErrorWithCausePreservesCause() {
        RuntimeException cause = new RuntimeException("root");
        CcException ex = CcErrorCodes.internalError("wrapped", cause);

        assertThat(ex.getErrorCode()).isEqualTo("INTERNAL_ERROR");
        assertThat(ex.getHttpStatus()).isEqualTo(500);
        assertThat(ex.getCause()).isEqualTo(cause);
    }
}
