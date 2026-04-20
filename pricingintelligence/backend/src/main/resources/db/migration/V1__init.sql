CREATE TABLE pricing_experiments (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    variants JSONB,
    results JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customer_segments (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    criteria JSONB,
    size INTEGER,
    avg_ltv DECIMAL(19, 4),
    churn_rate DOUBLE PRECISION,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE conversion_records (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    customer_id VARCHAR(255) NOT NULL,
    price_offered DECIMAL(19, 4) NOT NULL,
    features_included JSONB,
    converted BOOLEAN NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE elasticity_models (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    segment_id UUID NOT NULL,
    price_range_min DECIMAL(19, 4) NOT NULL,
    price_range_max DECIMAL(19, 4) NOT NULL,
    elasticity_coefficient DOUBLE PRECISION,
    r_squared DOUBLE PRECISION,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE price_recommendations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    segment_id UUID NOT NULL,
    current_price DECIMAL(19, 4) NOT NULL,
    recommended_price DECIMAL(19, 4) NOT NULL,
    confidence_score DOUBLE PRECISION,
    estimated_revenue_lift DOUBLE PRECISION,
    rationale TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE churn_analyses (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    segment_id UUID NOT NULL,
    price_tier VARCHAR(255),
    churn_rate DOUBLE PRECISION,
    price_sensitivity DOUBLE PRECISION,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
