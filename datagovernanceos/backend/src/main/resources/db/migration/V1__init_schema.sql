CREATE TABLE data_assets (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100),
    description TEXT,
    classification VARCHAR(50),
    pii_status BOOLEAN DEFAULT FALSE,
    owner VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_data_assets_tenant ON data_assets(tenant_id);

CREATE TABLE governance_policies (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    rule_definition JSONB,
    enforcement_level VARCHAR(50),
    status VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_gov_policies_tenant ON governance_policies(tenant_id);

CREATE TABLE compliance_audits (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    asset_id UUID NOT NULL REFERENCES data_assets(id),
    policy_id UUID NOT NULL REFERENCES governance_policies(id),
    status VARCHAR(50),
    findings TEXT,
    audited_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_comp_audits_tenant ON compliance_audits(tenant_id);
