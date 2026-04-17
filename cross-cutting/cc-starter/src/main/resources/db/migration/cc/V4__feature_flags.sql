CREATE TABLE cc.feature_flags (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    key VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    default_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE cc.feature_flag_overrides (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    flag_id UUID NOT NULL REFERENCES cc.feature_flags(id) ON DELETE CASCADE,
    tenant_id UUID REFERENCES cc.tenants(id) ON DELETE CASCADE,
    user_id UUID REFERENCES cc.users(id) ON DELETE CASCADE,
    enabled BOOLEAN NOT NULL,
    CHECK (tenant_id IS NOT NULL OR user_id IS NOT NULL)
);

CREATE INDEX idx_ff_overrides_tenant ON cc.feature_flag_overrides(flag_id, tenant_id);
CREATE INDEX idx_ff_overrides_user ON cc.feature_flag_overrides(flag_id, user_id);
