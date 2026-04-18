CREATE TABLE carrier_performance (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    carrier_name VARCHAR(255) NOT NULL,
    on_time_rate DECIMAL(5,2),
    predicted_delay_mins INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_carrier_tenant ON carrier_performance(tenant_id);

CREATE TABLE routes (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    origin VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    estimated_arrival TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_routes_tenant ON routes(tenant_id);

CREATE TABLE exceptions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    route_id UUID REFERENCES routes(id),
    description TEXT,
    severity VARCHAR(50),
    recommended_action TEXT,
    status VARCHAR(50) NOT NULL,
    assigned_to VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_exceptions_tenant ON exceptions(tenant_id);
CREATE INDEX idx_exceptions_route ON exceptions(route_id);
