CREATE SCHEMA IF NOT EXISTS interviewos;

CREATE TABLE interviewos.interview_guide (
    id UUID PRIMARY KEY,
    role_title VARCHAR(255) NOT NULL,
    interview_type VARCHAR(50) NOT NULL,
    questions TEXT,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE interviewos.interview_scorecard (
    id UUID PRIMARY KEY,
    guide_id UUID NOT NULL,
    candidate_id UUID NOT NULL,
    interviewer_id UUID NOT NULL,
    scores TEXT,
    overall_score INT NOT NULL,
    notes TEXT,
    tenant_id UUID NOT NULL,
    submitted_at TIMESTAMP NOT NULL
);

CREATE TABLE interviewos.evaluator_consistency (
    id UUID PRIMARY KEY,
    interviewer_id UUID NOT NULL,
    average_score DOUBLE PRECISION NOT NULL,
    total_evaluations INT NOT NULL,
    tenant_id UUID NOT NULL
);
