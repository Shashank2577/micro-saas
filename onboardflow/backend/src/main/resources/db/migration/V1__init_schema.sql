CREATE TABLE onboarding_plans (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    role VARCHAR(255) NOT NULL,
    department VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    plan_30_day JSONB,
    plan_60_day JSONB,
    plan_90_day JSONB,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE onboarding_tasks (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    plan_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    owner_team VARCHAR(50) NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    completed_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE onboarding_milestones (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    plan_id UUID NOT NULL,
    milestone_day INT NOT NULL,
    metrics JSONB,
    achieved_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE productivity_scores (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    employee_id UUID NOT NULL,
    week_number INT NOT NULL,
    score INT NOT NULL,
    factors JSONB
);
