CREATE TABLE ecosystems (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE nodes (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    ecosystem_id UUID NOT NULL REFERENCES ecosystems(id) ON DELETE CASCADE,
    app_name VARCHAR(255) NOT NULL,
    node_type VARCHAR(255) NOT NULL,
    status VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE connections (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    ecosystem_id UUID NOT NULL REFERENCES ecosystems(id) ON DELETE CASCADE,
    source_node_id UUID NOT NULL REFERENCES nodes(id) ON DELETE CASCADE,
    target_node_id UUID NOT NULL REFERENCES nodes(id) ON DELETE CASCADE,
    connection_type VARCHAR(255) NOT NULL,
    status VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE roi_metrics (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    ecosystem_id UUID NOT NULL REFERENCES ecosystems(id) ON DELETE CASCADE,
    node_id UUID REFERENCES nodes(id) ON DELETE CASCADE,
    metric_name VARCHAR(255) NOT NULL,
    metric_value DECIMAL(19, 4) NOT NULL,
    currency_code VARCHAR(10),
    period VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE data_flow_events (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    connection_id UUID NOT NULL REFERENCES connections(id) ON DELETE CASCADE,
    event_type VARCHAR(255) NOT NULL,
    payload_size_bytes BIGINT,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE deployment_statuses (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    node_id UUID NOT NULL REFERENCES nodes(id) ON DELETE CASCADE,
    status VARCHAR(255) NOT NULL,
    status_reason TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE ecosystem_snapshots (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    ecosystem_id UUID NOT NULL REFERENCES ecosystems(id) ON DELETE CASCADE,
    snapshot_data JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE ai_insights (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    ecosystem_id UUID NOT NULL REFERENCES ecosystems(id) ON DELETE CASCADE,
    insight_type VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- Indexes for multitenancy and common queries
CREATE INDEX idx_ecosystems_tenant ON ecosystems(tenant_id);
CREATE INDEX idx_nodes_tenant_eco ON nodes(tenant_id, ecosystem_id);
CREATE INDEX idx_connections_tenant_eco ON connections(tenant_id, ecosystem_id);
CREATE INDEX idx_roi_metrics_tenant_eco ON roi_metrics(tenant_id, ecosystem_id);
CREATE INDEX idx_data_flow_events_tenant_conn ON data_flow_events(tenant_id, connection_id);
CREATE INDEX idx_deployment_statuses_tenant_node ON deployment_statuses(tenant_id, node_id);
CREATE INDEX idx_ecosystem_snapshots_tenant_eco ON ecosystem_snapshots(tenant_id, ecosystem_id);
CREATE INDEX idx_ai_insights_tenant_eco ON ai_insights(tenant_id, ecosystem_id);
