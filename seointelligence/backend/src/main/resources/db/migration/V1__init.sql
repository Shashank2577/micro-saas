CREATE SCHEMA IF NOT EXISTS seointelligence;

CREATE TABLE seointelligence.seo_audits (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    url VARCHAR(1024) NOT NULL,
    status VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_seo_audits_tenant_id ON seointelligence.seo_audits(tenant_id);
