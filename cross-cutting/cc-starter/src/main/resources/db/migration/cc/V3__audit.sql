-- Layer 1: System audit (AUTOMATIC — every API request)
CREATE TABLE audit.system_log (
    id BIGSERIAL PRIMARY KEY,
    tenant_id UUID,
    user_id UUID,
    event_type VARCHAR(50) NOT NULL,        -- AUTH, RBAC, CONFIG, API, DATA_ACCESS
    action VARCHAR(100) NOT NULL,            -- e.g., LOGIN, PERMISSION_CHECK, ROLE_ASSIGNED
    resource_type VARCHAR(100),
    resource_id VARCHAR(255),
    request_method VARCHAR(10),
    request_path TEXT,
    request_body JSONB,                      -- sanitized (no passwords/tokens)
    response_status INT,
    details JSONB NOT NULL DEFAULT '{}',
    ip_address INET,
    user_agent TEXT,
    correlation_id UUID,
    duration_ms INT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_sys_audit_tenant_time ON audit.system_log(tenant_id, created_at DESC);
CREATE INDEX idx_sys_audit_user ON audit.system_log(user_id, created_at DESC);
CREATE INDEX idx_sys_audit_event ON audit.system_log(event_type, action);
CREATE INDEX idx_sys_audit_resource ON audit.system_log(resource_type, resource_id);
CREATE INDEX idx_sys_audit_correlation ON audit.system_log(correlation_id);

-- Layer 2: Business audit (OPT-IN via @Audited — before/after state)
CREATE TABLE audit.business_log (
    id BIGSERIAL PRIMARY KEY,
    tenant_id UUID,
    user_id UUID,
    action VARCHAR(100) NOT NULL,            -- e.g., CREATE_INVOICE, APPROVE_ORDER
    resource_type VARCHAR(100) NOT NULL,
    resource_id UUID,
    before_state JSONB,                      -- entity state before change
    after_state JSONB,                       -- entity state after change
    details JSONB NOT NULL DEFAULT '{}',
    ip_address INET,
    correlation_id UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_biz_audit_tenant ON audit.business_log(tenant_id, created_at DESC);
CREATE INDEX idx_biz_audit_resource ON audit.business_log(resource_type, resource_id);
CREATE INDEX idx_biz_audit_user ON audit.business_log(user_id, created_at DESC);

-- Layer 3: Auth events (synced from Keycloak — login, logout, MFA, password changes)
CREATE TABLE audit.auth_events (
    id BIGSERIAL PRIMARY KEY,
    keycloak_event_id VARCHAR(255),
    event_type VARCHAR(100) NOT NULL,        -- LOGIN, LOGIN_ERROR, LOGOUT, REGISTER, UPDATE_PASSWORD
    user_id UUID,
    tenant_id UUID,
    ip_address INET,
    client_id VARCHAR(255),
    session_id VARCHAR(255),
    details JSONB NOT NULL DEFAULT '{}',
    error VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_auth_events_user ON audit.auth_events(user_id, created_at DESC);
CREATE INDEX idx_auth_events_type ON audit.auth_events(event_type, created_at DESC);
