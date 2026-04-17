CREATE TABLE historical_data (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    customer_id VARCHAR(255) NOT NULL,
    data_type VARCHAR(50) NOT NULL, -- CONVERSION, EXPANSION, CHURN
    price_point DECIMAL(10, 2) NOT NULL,
    recorded_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_historical_data_tenant ON historical_data(tenant_id);
CREATE INDEX idx_historical_data_customer ON historical_data(customer_id);

CREATE TABLE elasticity_model (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    segment VARCHAR(255) NOT NULL,
    base_price DECIMAL(10, 2) NOT NULL,
    elasticity_coefficient DECIMAL(10, 4) NOT NULL,
    confidence_score DECIMAL(5, 4) NOT NULL,
    last_updated_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_elasticity_model_tenant ON elasticity_model(tenant_id);

CREATE TABLE pricing_experiment (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    segment VARCHAR(255) NOT NULL,
    control_price DECIMAL(10, 2) NOT NULL,
    variant_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL, -- DRAFT, RUNNING, COMPLETED
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_pricing_experiment_tenant ON pricing_experiment(tenant_id);

CREATE TABLE experiment_result (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    experiment_id UUID NOT NULL REFERENCES pricing_experiment(id),
    variant_type VARCHAR(50) NOT NULL, -- CONTROL, VARIANT
    conversion_rate DECIMAL(5, 4) NOT NULL,
    revenue_impact DECIMAL(10, 2) NOT NULL,
    is_significant BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_experiment_result_tenant ON experiment_result(tenant_id);
CREATE INDEX idx_experiment_result_experiment ON experiment_result(experiment_id);
