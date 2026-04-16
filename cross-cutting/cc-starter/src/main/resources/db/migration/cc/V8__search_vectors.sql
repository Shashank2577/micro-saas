-- Full-text search
CREATE TABLE cc.search_index (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL REFERENCES cc.tenants(id) ON DELETE CASCADE,
    resource_type VARCHAR(100) NOT NULL,
    resource_id UUID NOT NULL,
    content TEXT NOT NULL,
    search_vector TSVECTOR GENERATED ALWAYS AS (to_tsvector('english', content)) STORED,
    metadata JSONB NOT NULL DEFAULT '{}',
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(tenant_id, resource_type, resource_id)
);

CREATE INDEX idx_search_vector ON cc.search_index USING GIN(search_vector);
CREATE INDEX idx_search_tenant ON cc.search_index(tenant_id, resource_type);

-- Vector embeddings (for AI/semantic search)
CREATE TABLE cc.embeddings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL REFERENCES cc.tenants(id) ON DELETE CASCADE,
    resource_type VARCHAR(100) NOT NULL,
    resource_id UUID NOT NULL,
    model VARCHAR(100) NOT NULL,
    embedding vector(1536),
    metadata JSONB NOT NULL DEFAULT '{}',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(tenant_id, resource_type, resource_id, model)
);

-- Note: ivfflat index requires data to exist first for training
-- Run this manually after loading initial embeddings:
-- CREATE INDEX idx_embeddings_vector ON cc.embeddings
--     USING ivfflat (embedding vector_cosine_ops) WITH (lists = 100);
