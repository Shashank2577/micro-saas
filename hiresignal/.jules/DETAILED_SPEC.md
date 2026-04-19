# HireSignal — Detailed Specification

## 1. Product Intent
- **App**: `hiresignal`
- **Domain**: HR Talent
- **Outcome**: Candidate fit scoring and hiring pipeline intelligence
- **Primary actors**: Recruiter, Hiring manager, People ops

## 2. Database Schema (PostgreSQL)

```sql
CREATE TABLE candidate_profiles (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE fit_signals (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE interview_stages (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE hiring_decisions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE pipeline_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE requisitions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
```

## 3. REST API Endpoints

### Candidate Profiles
- `GET /api/v1/hiring/candidate-profiles`
  - Response: `List<CandidateProfileDto>`
- `POST /api/v1/hiring/candidate-profiles`
  - Request: `CandidateProfileDto`
  - Response: `CandidateProfileDto`
- `GET /api/v1/hiring/candidate-profiles/{id}`
  - Response: `CandidateProfileDto`
- `PATCH /api/v1/hiring/candidate-profiles/{id}`
  - Request: `CandidateProfileDto`
  - Response: `CandidateProfileDto`
- `POST /api/v1/hiring/candidate-profiles/{id}/validate`
  - Response: `{ "valid": boolean, "message": string }`

### Fit Signals
- `GET /api/v1/hiring/fit-signals`
  - Response: `List<FitSignalDto>`
- `POST /api/v1/hiring/fit-signals`
  - Request: `FitSignalDto`
  - Response: `FitSignalDto`
- `GET /api/v1/hiring/fit-signals/{id}`
  - Response: `FitSignalDto`
- `PATCH /api/v1/hiring/fit-signals/{id}`
  - Request: `FitSignalDto`
  - Response: `FitSignalDto`
- `POST /api/v1/hiring/fit-signals/{id}/validate`
  - Response: `{ "valid": boolean, "message": string }`

### Interview Stages
- `GET /api/v1/hiring/interview-stages`
- `POST /api/v1/hiring/interview-stages`
- `GET /api/v1/hiring/interview-stages/{id}`
- `PATCH /api/v1/hiring/interview-stages/{id}`
- `POST /api/v1/hiring/interview-stages/{id}/validate`

### Hiring Decisions
- `GET /api/v1/hiring/hiring-decisions`
- `POST /api/v1/hiring/hiring-decisions`
- `GET /api/v1/hiring/hiring-decisions/{id}`
- `PATCH /api/v1/hiring/hiring-decisions/{id}`
- `POST /api/v1/hiring/hiring-decisions/{id}/validate`

### Pipeline Metrics
- `GET /api/v1/hiring/pipeline-metrics`
- `POST /api/v1/hiring/pipeline-metrics`
- `GET /api/v1/hiring/pipeline-metrics/{id}`
- `PATCH /api/v1/hiring/pipeline-metrics/{id}`
- `POST /api/v1/hiring/pipeline-metrics/{id}/validate`

### AI & Workflows
- `POST /api/v1/hiring/ai/analyze`
  - Request: `{ "text": string }`
  - Response: `{ "analysis": string }`
- `POST /api/v1/hiring/workflows/execute`
  - Request: `{ "workflow_id": string }`
  - Response: `{ "status": string }`
- `GET /api/v1/hiring/metrics/summary`
  - Response: `{ "summary": string }`

## 4. Frontend Components & Pages

### Pages (Next.js App Router)
- `/candidates` - Renders `CandidateList`
- `/signals` - Renders `FitSignalList`
- `/stages` - Renders `InterviewStageList`
- `/decisions` - Renders `HiringDecisionList`
- `/pipeline` - Renders `PipelineMetricList`
- `/requisitions` - Renders `RequisitionList`

### Components
- `CandidateList`: Renders table of candidates.
- `FitSignalList`: Renders table of fit signals.
- `InterviewStageList`: Renders table of interview stages.
- `HiringDecisionList`: Renders table of hiring decisions.
- `PipelineMetricList`: Renders table of pipeline metrics.
- `RequisitionList`: Renders table of requisitions.

## 5. Event Contract
- **Emits**: `hiresignal.candidate.shortlisted`, `hiresignal.risk.flagged`, `hiresignal.hire.confirmed`
- **Consumes**: `jobcraftai.posting.published`, `interviewos.score.submitted`, `peopleanalytics.org.signal.updated`

## 6. Service Method Signatures
```java
public List<CandidateProfile> findAll();
public Optional<CandidateProfile> findById(UUID id);
public CandidateProfile save(CandidateProfile entity);
public CandidateProfile update(UUID id, CandidateProfile entity);
public ValidationResult validate(UUID id);
// And similar for other entities
```

## 7. Edge Cases and Validations
- **Tenant Isolation**: Every request must include `X-Tenant-ID`. Repository queries must filter by `tenant_id`.
- **Validation**: Name is mandatory. Status is mandatory.
- **AI Integration**: AI analyze uses LiteLLM via gateway with timeout and retry.
