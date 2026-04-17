CREATE TABLE copy_experiments (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    goal VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE copy_variants (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    experiment_id UUID NOT NULL REFERENCES copy_experiments(id),
    variant_name VARCHAR(255) NOT NULL,
    headline TEXT NOT NULL,
    body TEXT NOT NULL,
    cta TEXT NOT NULL,
    predicted_conversion DECIMAL,
    actual_conversion DECIMAL,
    impressions INT NOT NULL DEFAULT 0,
    conversions INT NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE psych_triggers (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    variant_id UUID NOT NULL REFERENCES copy_variants(id),
    trigger_type VARCHAR(50) NOT NULL,
    usage_text TEXT NOT NULL,
    strength_score DECIMAL NOT NULL
);

CREATE TABLE audience_segments (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    attributes JSONB NOT NULL,
    historical_conversion DECIMAL
);
