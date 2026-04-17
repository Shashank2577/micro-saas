package com.crosscutting.starter.tenancy;

import com.crosscutting.starter.error.CcException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TenantContextTest {

    @AfterEach
    void cleanup() {
        TenantContext.clear();
    }

    @Test
    void setAndGet() {
        UUID tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
        assertEquals(tenantId, TenantContext.get());
    }

    @Test
    void getReturnsNullWhenNotSet() {
        assertNull(TenantContext.get());
    }

    @Test
    void requireReturnsTenantWhenSet() {
        UUID tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
        UUID result = TenantContext.require();
        assertNotNull(result);
        assertEquals(tenantId, result);
    }

    @Test
    void requireThrowsWhenNotSet() {
        CcException exception = assertThrows(CcException.class, TenantContext::require);
        assertEquals("TENANT_NOT_FOUND", exception.getErrorCode());
        assertEquals(400, exception.getHttpStatus());
    }

    @Test
    void clearRemovesTenant() {
        TenantContext.set(UUID.randomUUID());
        TenantContext.clear();
        assertNull(TenantContext.get());
    }
}
