package com.crosscutting.starter;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CcPropertiesTest {

    @Test
    void defaultTenancyModeIsMulti() {
        CcProperties props = new CcProperties();
        assertThat(props.getTenancy().getMode()).isEqualTo("multi");
    }

    @Test
    void defaultTenantIdIsNull() {
        CcProperties props = new CcProperties();
        assertThat(props.getTenancy().getDefaultTenantId()).isNull();
    }

    @Test
    void tenancyModeCanBeSetToSingle() {
        CcProperties.TenancyProperties tenancy = new CcProperties.TenancyProperties();
        UUID tenantId = UUID.randomUUID();
        tenancy.setMode("single");
        tenancy.setDefaultTenantId(tenantId);

        assertThat(tenancy.getMode()).isEqualTo("single");
        assertThat(tenancy.getDefaultTenantId()).isEqualTo(tenantId);
    }

    @Test
    void defaultAuthProperties() {
        CcProperties props = new CcProperties();
        assertThat(props.getAuth().getRealm()).isEqualTo("cc-platform");
        assertThat(props.getAuth().getResource()).isEqualTo("cc-backend");
    }

    @Test
    void defaultStorageProperties() {
        CcProperties props = new CcProperties();
        assertThat(props.getStorage().getEndpoint()).isEqualTo("http://localhost:9000");
        assertThat(props.getStorage().getAccessKey()).isEqualTo("minioadmin");
        assertThat(props.getStorage().getSecretKey()).isEqualTo("minioadmin");
    }

    @Test
    void defaultAiProperties() {
        CcProperties props = new CcProperties();
        assertThat(props.getAi().getGatewayUrl()).isEqualTo("http://localhost:4000");
        assertThat(props.getAi().getMasterKey()).isEqualTo("sk-dev");
    }

    @Test
    void defaultAuditProperties() {
        CcProperties props = new CcProperties();
        CcProperties.AuditProperties audit = props.getAudit();
        assertThat(audit.isSystemAuditEnabled()).isTrue();
        assertThat(audit.isAuthEventSyncEnabled()).isTrue();
        assertThat(audit.isLogRequestBody()).isTrue();
        assertThat(audit.isLogResponseBody()).isFalse();
        assertThat(audit.getExcludePaths()).containsExactly("/actuator/**", "/cc/health");
        assertThat(audit.getSensitiveFields()).contains("password", "secret", "token");
    }

    @Test
    void defaultSecurityProperties() {
        CcProperties props = new CcProperties();
        CcProperties.SecurityProperties security = props.getSecurity();
        assertThat(security.isRateLimitEnabled()).isTrue();
        assertThat(security.getDefaultRateLimit()).isEqualTo(100);
        assertThat(security.getCorsOrigins()).contains("http://localhost:3000", "http://localhost:5173");
        assertThat(security.getEncryptionKey()).isNull();
    }

    @Test
    void defaultRbacProperties() {
        CcProperties props = new CcProperties();
        CcProperties.RbacProperties rbac = props.getRbac();
        assertThat(rbac.isEnabled()).isTrue();
        assertThat(rbac.getAppPermissions()).isEmpty();
    }

    @Test
    void rbacAppPermissionCanBeConfigured() {
        CcProperties.RbacProperties.AppPermission ap = new CcProperties.RbacProperties.AppPermission();
        ap.setResource("tasks");
        ap.setActions(List.of("read", "write", "delete"));

        assertThat(ap.getResource()).isEqualTo("tasks");
        assertThat(ap.getActions()).containsExactly("read", "write", "delete");
    }

    @Test
    void auditPropertiesMutability() {
        CcProperties.AuditProperties audit = new CcProperties.AuditProperties();
        audit.setSystemAuditEnabled(false);
        audit.setLogResponseBody(true);
        audit.setSensitiveFields(List.of("apiKey"));
        audit.setExcludePaths(List.of("/custom/**"));

        assertThat(audit.isSystemAuditEnabled()).isFalse();
        assertThat(audit.isLogResponseBody()).isTrue();
        assertThat(audit.getSensitiveFields()).containsExactly("apiKey");
        assertThat(audit.getExcludePaths()).containsExactly("/custom/**");
    }

    @Test
    void securityPropertiesMutability() {
        CcProperties.SecurityProperties security = new CcProperties.SecurityProperties();
        security.setRateLimitEnabled(false);
        security.setDefaultRateLimit(200);
        security.setEncryptionKey("my-key");
        security.setCorsOrigins(List.of("https://example.com"));

        assertThat(security.isRateLimitEnabled()).isFalse();
        assertThat(security.getDefaultRateLimit()).isEqualTo(200);
        assertThat(security.getEncryptionKey()).isEqualTo("my-key");
        assertThat(security.getCorsOrigins()).containsExactly("https://example.com");
    }
}
