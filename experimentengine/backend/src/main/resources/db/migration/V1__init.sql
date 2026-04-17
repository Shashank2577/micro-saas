CREATE TABLE IF NOT EXISTS experiment (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    hypothesis TEXT,
    status VARCHAR(50) NOT NULL,
    started_at TIMESTAMP WITH TIME ZONE,
    ended_at TIMESTAMP WITH TIME ZONE,
    min_sample_size INTEGER,
    significance_threshold DOUBLE PRECISION,
    peek_protection BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_experiment_tenant ON experiment(tenant_id);

CREATE TABLE IF NOT EXISTS variant (
    id UUID PRIMARY KEY,
    experiment_id UUID NOT NULL REFERENCES experiment(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    traffic_percent DOUBLE PRECISION NOT NULL,
    feature_flag_key VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_variant_experiment ON variant(experiment_id);

CREATE TABLE IF NOT EXISTS metric (
    id UUID PRIMARY KEY,
    experiment_id UUID NOT NULL REFERENCES experiment(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_metric_experiment ON metric(experiment_id);

CREATE TABLE IF NOT EXISTS assignment (
    id UUID PRIMARY KEY,
    experiment_id UUID NOT NULL REFERENCES experiment(id) ON DELETE CASCADE,
    unit_id VARCHAR(255) NOT NULL,
    variant_id UUID NOT NULL REFERENCES variant(id) ON DELETE CASCADE,
    assigned_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_assignment_exp_unit ON assignment(experiment_id, unit_id);

CREATE TABLE IF NOT EXISTS event (
    id UUID PRIMARY KEY,
    experiment_id UUID NOT NULL REFERENCES experiment(id) ON DELETE CASCADE,
    unit_id VARCHAR(255) NOT NULL,
    event_name VARCHAR(255) NOT NULL,
    value DOUBLE PRECISION,
    properties JSONB,
    ts TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_event_exp_unit ON event(experiment_id, unit_id);

CREATE TABLE IF NOT EXISTS analysis_result (
    id UUID PRIMARY KEY,
    experiment_id UUID NOT NULL REFERENCES experiment(id) ON DELETE CASCADE,
    metric_id UUID NOT NULL REFERENCES metric(id) ON DELETE CASCADE,
    variant_id UUID NOT NULL REFERENCES variant(id) ON DELETE CASCADE,
    n_exposures INTEGER NOT NULL DEFAULT 0,
    n_conversions INTEGER NOT NULL DEFAULT 0,
    mean DOUBLE PRECISION,
    std_err DOUBLE PRECISION,
    p_value DOUBLE PRECISION,
    bayesian_prob_better DOUBLE PRECISION,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_analysis_result_exp_metric ON analysis_result(experiment_id, metric_id);
