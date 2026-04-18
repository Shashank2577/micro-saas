CREATE TABLE connectors (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    config JSONB,
    status VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE integrations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    source_connector_id UUID REFERENCES connectors(id),
    target_connector_id UUID REFERENCES connectors(id),
    status VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE field_mappings (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    integration_id UUID REFERENCES integrations(id),
    source_field VARCHAR(255),
    target_field VARCHAR(255),
    transform_logic TEXT,
    is_ai_suggested BOOLEAN DEFAULT false,
    confidence_score DOUBLE PRECISION,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sync_history (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    integration_id UUID REFERENCES integrations(id),
    status VARCHAR(50),
    records_processed INTEGER,
    records_failed INTEGER,
    error_message TEXT,
    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE
);
