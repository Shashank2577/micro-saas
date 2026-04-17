CREATE TABLE data_assets (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    source_system VARCHAR(255),
    owner_id UUID,
    steward_id UUID,
    classification VARCHAR(50),
    description TEXT,
    retention_days INT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_data_assets_tenant ON data_assets(tenant_id);

CREATE TABLE data_lineage_links (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    source_asset_id UUID NOT NULL REFERENCES data_assets(id),
    target_asset_id UUID NOT NULL REFERENCES data_assets(id),
    transformation_logic TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_data_lineage_links_tenant ON data_lineage_links(tenant_id);
CREATE INDEX idx_data_lineage_links_source ON data_lineage_links(source_asset_id);
CREATE INDEX idx_data_lineage_links_target ON data_lineage_links(target_asset_id);

CREATE TABLE governance_policies (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    policy_type VARCHAR(50) NOT NULL,
    rules JSONB,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_governance_policies_tenant ON governance_policies(tenant_id);

CREATE TABLE pii_tags (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    asset_id UUID NOT NULL REFERENCES data_assets(id),
    field_name VARCHAR(255) NOT NULL,
    tag_type VARCHAR(50) NOT NULL,
    confidence_score DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_pii_tags_tenant ON pii_tags(tenant_id);
CREATE INDEX idx_pii_tags_asset ON pii_tags(asset_id);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    asset_id UUID NOT NULL REFERENCES data_assets(id),
    user_id UUID NOT NULL,
    action VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_audit_logs_tenant ON audit_logs(tenant_id);
CREATE INDEX idx_audit_logs_asset ON audit_logs(asset_id);

CREATE TABLE incidents (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    asset_id UUID NOT NULL REFERENCES data_assets(id),
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_incidents_tenant ON incidents(tenant_id);
CREATE INDEX idx_incidents_asset ON incidents(asset_id);
