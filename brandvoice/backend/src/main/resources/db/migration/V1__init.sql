CREATE SCHEMA IF NOT EXISTS brandvoice;

CREATE TABLE brandvoice.brand_profiles (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    industry VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE brandvoice.brand_guidelines (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    brand_profile_id UUID NOT NULL REFERENCES brandvoice.brand_profiles(id),
    category VARCHAR(255),
    rule TEXT,
    created_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE brandvoice.tone_of_voices (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    brand_profile_id UUID NOT NULL REFERENCES brandvoice.brand_profiles(id),
    adjective VARCHAR(255),
    definition TEXT,
    usage_example TEXT
);

CREATE TABLE brandvoice.vocabulary_lists (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    brand_profile_id UUID NOT NULL REFERENCES brandvoice.brand_profiles(id),
    type VARCHAR(50),
    word VARCHAR(255),
    alternative VARCHAR(255)
);

CREATE TABLE brandvoice.content_assets (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255),
    content TEXT,
    type VARCHAR(100),
    status VARCHAR(50),
    ai_analysis_score DOUBLE PRECISION,
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE brandvoice.analysis_reports (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    content_asset_id UUID NOT NULL REFERENCES brandvoice.content_assets(id),
    score DOUBLE PRECISION,
    feedback TEXT,
    generated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE brandvoice.content_projects (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255),
    status VARCHAR(50),
    target_audience VARCHAR(255),
    due_date TIMESTAMP WITH TIME ZONE
);

CREATE TABLE brandvoice.campaigns (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255),
    objective TEXT,
    start_date TIMESTAMP WITH TIME ZONE,
    end_date TIMESTAMP WITH TIME ZONE,
    status VARCHAR(50)
);
