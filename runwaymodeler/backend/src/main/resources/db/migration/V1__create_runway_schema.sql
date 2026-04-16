CREATE TABLE runway_model (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    current_burn DECIMAL(19, 2) NOT NULL,
    current_cash DECIMAL(19, 2) NOT NULL,
    runway_days INT NOT NULL,
    tenant_id UUID NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE headcount_scenario (
    id UUID PRIMARY KEY,
    model_id UUID NOT NULL REFERENCES runway_model(id),
    name VARCHAR(255) NOT NULL,
    additional_heads INT NOT NULL,
    monthly_cost_per_head DECIMAL(19, 2) NOT NULL,
    start_date DATE NOT NULL,
    impact_on_runway_days INT NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE funding_round (
    id UUID PRIMARY KEY,
    model_id UUID NOT NULL REFERENCES runway_model(id),
    amount DECIMAL(19, 2) NOT NULL,
    valuation_cap DECIMAL(19, 2),
    dilution_percent DOUBLE PRECISION,
    expected_close_date DATE NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE runway_projection (
    id UUID PRIMARY KEY,
    model_id UUID NOT NULL REFERENCES runway_model(id),
    month DATE NOT NULL,
    projected_cash DECIMAL(19, 2) NOT NULL,
    projected_burn DECIMAL(19, 2) NOT NULL,
    tenant_id UUID NOT NULL
);
