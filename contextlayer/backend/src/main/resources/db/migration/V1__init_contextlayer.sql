CREATE TABLE IF NOT EXISTS customer_contexts (
    id UUID PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    tenant_id UUID NOT NULL,
    profile JSONB,
    preferences JSONB,
    attributes JSONB,
    last_updated_at TIMESTAMP WITH TIME ZONE,
    updated_by_app VARCHAR(255)
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_customer_tenant ON customer_contexts (customer_id, tenant_id);
CREATE INDEX IF NOT EXISTS idx_tenant_updated ON customer_contexts (tenant_id, last_updated_at);

CREATE TABLE IF NOT EXISTS context_versions (
    version_id UUID PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    tenant_id UUID NOT NULL,
    context_snapshot JSONB,
    created_at TIMESTAMP WITH TIME ZONE,
    created_by_app VARCHAR(255),
    change_description TEXT
);
CREATE INDEX IF NOT EXISTS idx_customer_created ON context_versions (customer_id, created_at);

CREATE TABLE IF NOT EXISTS interaction_histories (
    interaction_id UUID PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    tenant_id UUID NOT NULL,
    app_id VARCHAR(255) NOT NULL,
    interaction_type VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE,
    metadata JSONB,
    outcomes TEXT
);
CREATE INDEX IF NOT EXISTS idx_customer_app_timestamp ON interaction_histories (customer_id, app_id, timestamp);

CREATE TABLE IF NOT EXISTS customer_preferences (
    id UUID PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    tenant_id UUID NOT NULL,
    preference_key VARCHAR(255) NOT NULL,
    preference_value TEXT,
    source_app VARCHAR(255),
    valid_from TIMESTAMP WITH TIME ZONE,
    valid_until TIMESTAMP WITH TIME ZONE
);
CREATE INDEX IF NOT EXISTS idx_customer_prefkey ON customer_preferences (customer_id, preference_key);

CREATE TABLE IF NOT EXISTS consent_records (
    id UUID PRIMARY KEY,
    customer_id VARCHAR(255) NOT NULL,
    tenant_id UUID NOT NULL,
    consent_type VARCHAR(255) NOT NULL,
    granted BOOLEAN NOT NULL,
    consented_at TIMESTAMP WITH TIME ZONE,
    consented_by_app VARCHAR(255),
    expiry_date TIMESTAMP WITH TIME ZONE
);
CREATE INDEX IF NOT EXISTS idx_customer_consent_type ON consent_records (customer_id, consent_type);
