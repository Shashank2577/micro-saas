CREATE SCHEMA IF NOT EXISTS jobcraftai;

CREATE TABLE jobcraftai.job_description (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    department VARCHAR(255),
    role_level VARCHAR(255),
    requirements JSONB,
    generated_content TEXT,
    bias_score DECIMAL,
    seo_score DECIMAL,
    version INT DEFAULT 1,
    status VARCHAR(50) DEFAULT 'DRAFT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE jobcraftai.bias_flag (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    job_id UUID NOT NULL REFERENCES jobcraftai.job_description(id),
    phrase TEXT NOT NULL,
    bias_type VARCHAR(50) NOT NULL,
    suggestion TEXT,
    severity VARCHAR(50) NOT NULL
);

CREATE TABLE jobcraftai.job_variant (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    job_id UUID NOT NULL REFERENCES jobcraftai.job_description(id),
    variant_name VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    views INT DEFAULT 0,
    applications INT DEFAULT 0,
    quality_score DECIMAL
);

CREATE TABLE jobcraftai.hire_outcome (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    job_id UUID NOT NULL REFERENCES jobcraftai.job_description(id),
    candidate_id UUID NOT NULL,
    outcome VARCHAR(50) NOT NULL,
    source_channel VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
