CREATE TABLE sku (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    sku_code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    cost_price DECIMAL(10, 2),
    current_price DECIMAL(10, 2),
    stock_quantity INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE demand_forecast (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    sku_id UUID NOT NULL REFERENCES sku(id),
    forecast_date DATE NOT NULL,
    predicted_demand INT NOT NULL,
    confidence_score DECIMAL(5, 4),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE pricing_recommendation (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    sku_id UUID NOT NULL REFERENCES sku(id),
    recommended_price DECIMAL(10, 2) NOT NULL,
    reasoning TEXT,
    margin_percentage DECIMAL(5, 2),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
