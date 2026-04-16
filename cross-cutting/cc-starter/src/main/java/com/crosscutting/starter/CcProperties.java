package com.crosscutting.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConfigurationProperties(prefix = "cc")
public class CcProperties {

    private TenancyProperties tenancy = new TenancyProperties();
    private AuthProperties auth = new AuthProperties();
    private StorageProperties storage = new StorageProperties();
    private AiProperties ai = new AiProperties();
    private AuditProperties audit = new AuditProperties();
    private SecurityProperties security = new SecurityProperties();
    private RbacProperties rbac = new RbacProperties();

    public RbacProperties getRbac() {
        return rbac;
    }

    public void setRbac(RbacProperties rbac) {
        this.rbac = rbac;
    }

    public TenancyProperties getTenancy() {
        return tenancy;
    }

    public void setTenancy(TenancyProperties tenancy) {
        this.tenancy = tenancy;
    }

    public AuthProperties getAuth() {
        return auth;
    }

    public void setAuth(AuthProperties auth) {
        this.auth = auth;
    }

    public StorageProperties getStorage() {
        return storage;
    }

    public void setStorage(StorageProperties storage) {
        this.storage = storage;
    }

    public AiProperties getAi() {
        return ai;
    }

    public void setAi(AiProperties ai) {
        this.ai = ai;
    }

    public AuditProperties getAudit() {
        return audit;
    }

    public void setAudit(AuditProperties audit) {
        this.audit = audit;
    }

    public SecurityProperties getSecurity() {
        return security;
    }

    public void setSecurity(SecurityProperties security) {
        this.security = security;
    }

    public static class TenancyProperties {
        private String mode = "multi";
        private UUID defaultTenantId;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public UUID getDefaultTenantId() {
            return defaultTenantId;
        }

        public void setDefaultTenantId(UUID defaultTenantId) {
            this.defaultTenantId = defaultTenantId;
        }
    }

    public static class AuthProperties {
        private String realm = "cc-platform";
        private String resource = "cc-backend";

        public String getRealm() {
            return realm;
        }

        public void setRealm(String realm) {
            this.realm = realm;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }
    }

    public static class StorageProperties {
        private String endpoint = "http://localhost:9000";
        /** DEV-ONLY default. Override via cc.storage.access-key in production. */
        private String accessKey = "minioadmin";
        /** DEV-ONLY default. Override via cc.storage.secret-key in production. */
        private String secretKey = "minioadmin";

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }
    }

    public static class AiProperties {
        private String gatewayUrl = "http://localhost:4000";
        /** DEV-ONLY default. Override via cc.ai.master-key in production. */
        private String masterKey = "sk-dev";

        public String getGatewayUrl() {
            return gatewayUrl;
        }

        public void setGatewayUrl(String gatewayUrl) {
            this.gatewayUrl = gatewayUrl;
        }

        public String getMasterKey() {
            return masterKey;
        }

        public void setMasterKey(String masterKey) {
            this.masterKey = masterKey;
        }
    }

    public static class AuditProperties {
        private boolean systemAuditEnabled = true;
        private boolean authEventSyncEnabled = true;
        private List<String> excludePaths = List.of("/actuator/**", "/cc/health");
        private boolean logRequestBody = true;
        private boolean logResponseBody = false;
        private List<String> sensitiveFields = List.of("password", "secret", "token", "apiKey", "authorization");

        public boolean isSystemAuditEnabled() {
            return systemAuditEnabled;
        }

        public void setSystemAuditEnabled(boolean systemAuditEnabled) {
            this.systemAuditEnabled = systemAuditEnabled;
        }

        public boolean isAuthEventSyncEnabled() {
            return authEventSyncEnabled;
        }

        public void setAuthEventSyncEnabled(boolean authEventSyncEnabled) {
            this.authEventSyncEnabled = authEventSyncEnabled;
        }

        public List<String> getExcludePaths() {
            return excludePaths;
        }

        public void setExcludePaths(List<String> excludePaths) {
            this.excludePaths = excludePaths;
        }

        public boolean isLogRequestBody() {
            return logRequestBody;
        }

        public void setLogRequestBody(boolean logRequestBody) {
            this.logRequestBody = logRequestBody;
        }

        public boolean isLogResponseBody() {
            return logResponseBody;
        }

        public void setLogResponseBody(boolean logResponseBody) {
            this.logResponseBody = logResponseBody;
        }

        public List<String> getSensitiveFields() {
            return sensitiveFields;
        }

        public void setSensitiveFields(List<String> sensitiveFields) {
            this.sensitiveFields = sensitiveFields;
        }
    }

    public static class SecurityProperties {
        private boolean rateLimitEnabled = true;
        private int defaultRateLimit = 100;
        private List<String> corsOrigins = List.of("http://localhost:3000", "http://localhost:5173");
        private String encryptionKey;

        public boolean isRateLimitEnabled() {
            return rateLimitEnabled;
        }

        public void setRateLimitEnabled(boolean rateLimitEnabled) {
            this.rateLimitEnabled = rateLimitEnabled;
        }

        public int getDefaultRateLimit() {
            return defaultRateLimit;
        }

        public void setDefaultRateLimit(int defaultRateLimit) {
            this.defaultRateLimit = defaultRateLimit;
        }

        public List<String> getCorsOrigins() {
            return corsOrigins;
        }

        public void setCorsOrigins(List<String> corsOrigins) {
            this.corsOrigins = corsOrigins;
        }

        public String getEncryptionKey() {
            return encryptionKey;
        }

        public void setEncryptionKey(String encryptionKey) {
            this.encryptionKey = encryptionKey;
        }
    }

    public static class RbacProperties {
        private boolean enabled = true;
        private List<AppPermission> appPermissions = new ArrayList<>();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<AppPermission> getAppPermissions() {
            return appPermissions;
        }

        public void setAppPermissions(List<AppPermission> appPermissions) {
            this.appPermissions = appPermissions;
        }

        public static class AppPermission {
            private String resource;
            private List<String> actions = new ArrayList<>();

            public String getResource() {
                return resource;
            }

            public void setResource(String resource) {
                this.resource = resource;
            }

            public List<String> getActions() {
                return actions;
            }

            public void setActions(List<String> actions) {
                this.actions = actions;
            }
        }
    }
}
