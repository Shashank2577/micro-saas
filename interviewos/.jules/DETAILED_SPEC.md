# InterviewOS — Detailed Technical Spec

## 1. Introduction
This spec outlines the full backend and frontend implementation for `interviewos`, an HR Talent orchestration app.

## 2. Database Schema (PostgreSQL)

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- InterviewPlan
CREATE TABLE interview_plans (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_interview_plans_tenant ON interview_plans(tenant_id);

-- QuestionBank
CREATE TABLE question_banks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_question_banks_tenant ON question_banks(tenant_id);

-- Scorecard
CREATE TABLE scorecards (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_scorecards_tenant ON scorecards(tenant_id);

-- EvaluationRecord
CREATE TABLE evaluation_records (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_evaluation_records_tenant ON evaluation_records(tenant_id);

-- CalibrationSignal
CREATE TABLE calibration_signals (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_calibration_signals_tenant ON calibration_signals(tenant_id);

-- DecisionPacket
CREATE TABLE decision_packets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_decision_packets_tenant ON decision_packets(tenant_id);
```

## 3. API Endpoints

All endpoints have a `/api/v1/interviews` prefix and require a valid Tenant isolation context (usually provided by headers or JWT in a real app, mock header `X-Tenant-Id` for this spec).

### 3.1. Interview Plans
- `GET /interview-plans` - List all
- `POST /interview-plans` - Create
- `GET /interview-plans/{id}` - Get by id
- `PATCH /interview-plans/{id}` - Update
- `POST /interview-plans/{id}/validate` - Validate plan

### 3.2. Question Banks
- `GET /question-banks`
- `POST /question-banks`
- `GET /question-banks/{id}`
- `PATCH /question-banks/{id}`
- `POST /question-banks/{id}/validate`

### 3.3. Scorecards
- `GET /scorecards`
- `POST /scorecards`
- `GET /scorecards/{id}`
- `PATCH /scorecards/{id}`
- `POST /scorecards/{id}/validate`

### 3.4. Evaluation Records
- `GET /evaluation-records`
- `POST /evaluation-records`
- `GET /evaluation-records/{id}`
- `PATCH /evaluation-records/{id}`
- `POST /evaluation-records/{id}/validate`

### 3.5. Calibration Signals
- `GET /calibration-signals`
- `POST /calibration-signals`
- `GET /calibration-signals/{id}`
- `PATCH /calibration-signals/{id}`
- `POST /calibration-signals/{id}/validate`

### 3.6. Additional Endpoints
- `POST /ai/analyze` - Analyze interviews with LLM
- `POST /workflows/execute` - Execute a workflow
- `GET /metrics/summary` - Get metrics summary

## 4. Frontend Routes & Components
Base Path: `/`
Pages:
- `/plans` (Interview Plans)
- `/questions` (Question Banks)
- `/scorecards` (Scorecards)
- `/evaluations` (Evaluation Records)
- `/calibration` (Calibration Signals)

Components:
- Data Table for each entity
- Create/Edit Modal for each entity

## 5. Integrations & Events
- `interviewos.score.submitted`
- `interviewos.calibration.required`
- `interviewos.packet.ready`

Consumes:
- `hiresignal.candidate.shortlisted`
- `jobcraftai.posting.published`
- `performancenarrative.review.finalized`
