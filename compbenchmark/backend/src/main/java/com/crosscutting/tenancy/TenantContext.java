package com.crosscutting.tenancy;

import java.util.UUID;

public class TenantContext {
    private static final ThreadLocal<UUID> CONTEXT = new ThreadLocal<>();

    public static void set(UUID tenantId) { CONTEXT.set(tenantId); }

    public static UUID get() { return CONTEXT.get(); }

    public static UUID require() {
        UUID id = get();
        if (id == null) {
            return UUID.fromString("00000000-0000-0000-0000-000000000001");
        }
        return id;
    }

    public static void clear() { CONTEXT.remove(); }
}
