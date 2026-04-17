package com.crosscutting.starter.tenancy;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TenantEntityTest {

    @Test
    void defaultStatusIsActive() {
        Tenant tenant = new Tenant();
        assertThat(tenant.getStatus()).isEqualTo("active");
    }

    @Test
    void allFieldsAreSettable() {
        Tenant tenant = new Tenant();
        UUID id = UUID.randomUUID();

        tenant.setId(id);
        tenant.setName("Acme Corp");
        tenant.setSlug("acme-corp");
        tenant.setStatus("disabled");
        tenant.setSettings(Map.of("feature", "enabled"));

        assertThat(tenant.getId()).isEqualTo(id);
        assertThat(tenant.getName()).isEqualTo("Acme Corp");
        assertThat(tenant.getSlug()).isEqualTo("acme-corp");
        assertThat(tenant.getStatus()).isEqualTo("disabled");
        assertThat(tenant.getSettings()).containsEntry("feature", "enabled");
    }

    @Test
    void tenantMembershipFields() {
        TenantMembership membership = new TenantMembership();
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();

        membership.setTenantId(tenantId);
        membership.setUserId(userId);
        membership.setJoinedAt(now);

        assertThat(membership.getTenantId()).isEqualTo(tenantId);
        assertThat(membership.getUserId()).isEqualTo(userId);
        assertThat(membership.getJoinedAt()).isEqualTo(now);
    }
}
