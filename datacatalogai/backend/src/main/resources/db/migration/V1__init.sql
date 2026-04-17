CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE catalogs (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_catalogs_tenant ON catalogs(tenant_id);

CREATE TABLE data_sources (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    catalog_id UUID NOT NULL REFERENCES catalogs(id),
    type VARCHAR(50) NOT NULL,
    connection_encrypted TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_data_sources_tenant ON data_sources(tenant_id);

CREATE TABLE assets (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    source_id UUID NOT NULL REFERENCES data_sources(id),
    fqn VARCHAR(512) NOT NULL,
    type VARCHAR(50) NOT NULL,
    row_count_estimate BIGINT,
    owner_id VARCHAR(255),
    trust_score INT DEFAULT 0,
    tags_json JSONB,
    description_ai TEXT,
    description_human TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_assets_tenant ON assets(tenant_id);

CREATE TABLE columns (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    asset_id UUID NOT NULL REFERENCES assets(id),
    name VARCHAR(255) NOT NULL,
    data_type VARCHAR(255) NOT NULL,
    is_nullable BOOLEAN DEFAULT true,
    is_primary_key BOOLEAN DEFAULT false,
    pii_category VARCHAR(100),
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_columns_tenant ON columns(tenant_id);

CREATE TABLE query_logs (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    asset_id UUID NOT NULL REFERENCES assets(id),
    executed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    executor_user_id VARCHAR(255),
    duration_ms BIGINT,
    rows_returned BIGINT
);
CREATE INDEX idx_query_logs_tenant ON query_logs(tenant_id);

CREATE TABLE semantic_embeddings (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    asset_id UUID NOT NULL REFERENCES assets(id),
    vector vector(1536),
    text_source TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_semantic_embeddings_tenant ON semantic_embeddings(tenant_id);

CREATE TABLE relationships (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    from_asset_id UUID NOT NULL REFERENCES assets(id),
    to_asset_id UUID NOT NULL REFERENCES assets(id),
    rel_type VARCHAR(50) NOT NULL,
    confidence FLOAT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_relationships_tenant ON relationships(tenant_id);

CREATE TABLE glossary_terms (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    term VARCHAR(255) NOT NULL,
    definition TEXT,
    linked_asset_ids JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_glossary_terms_tenant ON glossary_terms(tenant_id);
