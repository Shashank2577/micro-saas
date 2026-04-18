CREATE TABLE prospect (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    domain VARCHAR(255),
    industry VARCHAR(255),
    region VARCHAR(255),
    crm_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE icp_profile (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    criteria JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE signal (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    prospect_id UUID NOT NULL REFERENCES prospect(id),
    type VARCHAR(255) NOT NULL,
    source VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    detected_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE prospect_brief (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    prospect_id UUID NOT NULL REFERENCES prospect(id),
    content TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_prospect_tenant ON prospect(tenant_id);
CREATE INDEX idx_signal_tenant_prospect ON signal(tenant_id, prospect_id);
CREATE INDEX idx_brief_tenant_prospect ON prospect_brief(tenant_id, prospect_id);
