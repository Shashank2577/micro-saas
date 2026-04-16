package com.crosscutting.starter.tenancy;

import com.crosscutting.starter.error.CcErrorCodes;
import com.crosscutting.starter.error.CcException;

import java.util.UUID;

public final class TenantContext {

    private static final ThreadLocal<UUID> currentTenant = new ThreadLocal<>();

    private TenantContext() {
    }

    public static void set(UUID tenantId) {
        currentTenant.set(tenantId);
    }

    public static UUID get() {
        return currentTenant.get();
    }

    public static UUID require() {
        UUID id = currentTenant.get();
        if (id == null) {
            throw new CcException(CcErrorCodes.TENANT_NOT_FOUND, "No tenant in context", 400);
        }
        return id;
    }

    public static void clear() {
        currentTenant.remove();
    }
}
