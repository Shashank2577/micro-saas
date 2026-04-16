CREATE TABLE job_requisitions (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    department VARCHAR(255) NOT NULL,
    requirements TEXT,
    status VARCHAR(50) NOT NULL,
    opened_at TIMESTAMP WITH TIME ZONE NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE candidates (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    source VARCHAR(50) NOT NULL,
    fit_score INT,
    status VARCHAR(50) NOT NULL,
    requisition_id UUID NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE evaluations (
    id UUID PRIMARY KEY,
    candidate_id UUID NOT NULL,
    requisition_id UUID NOT NULL,
    evaluator_id UUID NOT NULL,
    scorecard TEXT,
    consistency_score INT,
    recommendation VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL
);

CREATE TABLE hire_outcomes (
    id UUID PRIMARY KEY,
    candidate_id UUID NOT NULL,
    hired BOOLEAN NOT NULL,
    retention_months INT,
    performance_score INT,
    tenant_id UUID NOT NULL
);
