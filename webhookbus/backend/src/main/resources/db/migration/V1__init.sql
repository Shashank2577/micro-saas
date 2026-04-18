CREATE TABLE webhook_endpoints (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(1024) NOT NULL,
    secret VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_webhook_endpoints_tenant ON webhook_endpoints(tenant_id);

CREATE TABLE webhook_events (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    source VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    payload JSONB NOT NULL,
    received_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_webhook_events_tenant ON webhook_events(tenant_id);
CREATE INDEX idx_webhook_events_received ON webhook_events(received_at);

CREATE TABLE webhook_deliveries (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    event_id UUID NOT NULL REFERENCES webhook_events(id),
    endpoint_id UUID NOT NULL REFERENCES webhook_endpoints(id),
    status VARCHAR(50) NOT NULL,
    status_code INTEGER,
    attempt_count INTEGER DEFAULT 0,
    last_attempt_at TIMESTAMP WITH TIME ZONE,
    next_attempt_at TIMESTAMP WITH TIME ZONE,
    response_body TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_webhook_deliveries_tenant ON webhook_deliveries(tenant_id);
CREATE INDEX idx_webhook_deliveries_event ON webhook_deliveries(event_id);
CREATE INDEX idx_webhook_deliveries_endpoint ON webhook_deliveries(endpoint_id);
CREATE INDEX idx_webhook_deliveries_status ON webhook_deliveries(status);
