CREATE TABLE query_fingerprints (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    fingerprint_hash VARCHAR(255) NOT NULL,
    normalized_query TEXT NOT NULL,
    database_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(tenant_id, fingerprint_hash)
);

CREATE TABLE query_executions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    fingerprint_id UUID NOT NULL REFERENCES query_fingerprints(id) ON DELETE CASCADE,
    raw_query TEXT NOT NULL,
    execution_time_ms DOUBLE PRECISION NOT NULL,
    database_user VARCHAR(255),
    executed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    execution_plan TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE query_recommendations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    fingerprint_id UUID NOT NULL REFERENCES query_fingerprints(id) ON DELETE CASCADE,
    recommendation_type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    confidence_score DOUBLE PRECISION NOT NULL,
    impact_estimate TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE index_suggestions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    fingerprint_id UUID NOT NULL REFERENCES query_fingerprints(id) ON DELETE CASCADE,
    table_name VARCHAR(255) NOT NULL,
    columns_suggested TEXT NOT NULL,
    creation_statement TEXT NOT NULL,
    estimated_improvement DOUBLE PRECISION,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
