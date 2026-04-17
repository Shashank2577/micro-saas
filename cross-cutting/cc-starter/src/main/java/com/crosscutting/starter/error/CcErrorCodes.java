package com.crosscutting.starter.error;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CcErrorCodes {

    public static final String TENANT_NOT_FOUND = "TENANT_NOT_FOUND";
    public static final String TENANT_DISABLED = "TENANT_DISABLED";
    public static final String FORBIDDEN = "FORBIDDEN";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String PERMISSION_DENIED = "PERMISSION_DENIED";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String RESOURCE_CONFLICT = "RESOURCE_CONFLICT";
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    public static final String RATE_LIMITED = "RATE_LIMITED";
    public static final String INTERNAL_ERROR = "INTERNAL_ERROR";

    // --- convenience factory methods ---

    public static CcException tenantNotFound(String message) {
        return new CcException(TENANT_NOT_FOUND, message, 404);
    }

    public static CcException tenantDisabled(String message) {
        return new CcException(TENANT_DISABLED, message, 403);
    }

    public static CcException forbidden(String message) {
        return new CcException(FORBIDDEN, message, 403);
    }

    public static CcException unauthorized(String message) {
        return new CcException(UNAUTHORIZED, message, 401);
    }

    public static CcException permissionDenied(String message) {
        return new CcException(PERMISSION_DENIED, message, 403);
    }

    public static CcException resourceNotFound(String message) {
        return new CcException(RESOURCE_NOT_FOUND, message, 404);
    }

    public static CcException resourceConflict(String message) {
        return new CcException(RESOURCE_CONFLICT, message, 409);
    }

    public static CcException validationError(String message) {
        return new CcException(VALIDATION_ERROR, message, 400);
    }

    public static CcException rateLimited(String message) {
        return new CcException(RATE_LIMITED, message, 429);
    }

    public static CcException internalError(String message) {
        return new CcException(INTERNAL_ERROR, message, 500);
    }

    public static CcException internalError(String message, Throwable cause) {
        return new CcException(INTERNAL_ERROR, message, 500, cause);
    }
}
