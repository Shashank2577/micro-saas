CREATE TABLE models (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    purpose VARCHAR(255) NOT NULL,
    risk_tier VARCHAR(50) NOT NULL,
    owners_json JSONB,
    intended_uses TEXT,
    prohibited_uses TEXT,
    version VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_models_tenant ON models(tenant_id);

CREATE TABLE datasets (
    id UUID PRIMARY KEY,
    model_id UUID REFERENCES models(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    pii_flag BOOLEAN NOT NULL DEFAULT FALSE,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE performance_metrics (
    id UUID PRIMARY KEY,
    model_id UUID REFERENCES models(id),
    name VARCHAR(255) NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    slice VARCHAR(255),
    evaluated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_perf_metrics_model ON performance_metrics(model_id);

CREATE TABLE fairness_metrics (
    id UUID PRIMARY KEY,
    model_id UUID REFERENCES models(id),
    metric VARCHAR(50) NOT NULL,
    protected_attr VARCHAR(255) NOT NULL,
    group_a VARCHAR(255) NOT NULL,
    group_b VARCHAR(255) NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    threshold DOUBLE PRECISION NOT NULL,
    passed BOOLEAN NOT NULL,
    evaluated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_fairness_model ON fairness_metrics(model_id);

CREATE TABLE drift_signals (
    id UUID PRIMARY KEY,
    model_id UUID REFERENCES models(id),
    kind VARCHAR(50) NOT NULL,
    feature VARCHAR(255),
    baseline_stats JSONB,
    current_stats JSONB,
    score DOUBLE PRECISION NOT NULL,
    passed BOOLEAN NOT NULL,
    evaluated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_drift_model ON drift_signals(model_id);

CREATE TABLE prediction_logs (
    id UUID PRIMARY KEY,
    model_id UUID REFERENCES models(id),
    input_features_json JSONB,
    prediction VARCHAR(255),
    ground_truth VARCHAR(255),
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    sampled_prob DOUBLE PRECISION NOT NULL
);
CREATE INDEX idx_prediction_logs_model ON prediction_logs(model_id);

CREATE TABLE model_cards (
    id UUID PRIMARY KEY,
    model_id UUID REFERENCES models(id),
    version VARCHAR(50) NOT NULL,
    content_markdown TEXT,
    content_json JSONB,
    generated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_model_cards_model ON model_cards(model_id);

CREATE TABLE violations (
    id UUID PRIMARY KEY,
    model_id UUID REFERENCES models(id),
    type VARCHAR(255) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    detail TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    resolved_at TIMESTAMP WITH TIME ZONE
);
CREATE INDEX idx_violations_model ON violations(model_id);

CREATE TABLE audit_bundles (
    id UUID PRIMARY KEY,
    model_id UUID REFERENCES models(id),
    window_start TIMESTAMP WITH TIME ZONE NOT NULL,
    window_end TIMESTAMP WITH TIME ZONE NOT NULL,
    artifacts_ref VARCHAR(255) NOT NULL,
    generated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_audit_bundles_model ON audit_bundles(model_id);
