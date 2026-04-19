
# PerformanceNarrative — Detailed Implementation Spec

## 1) Product Intent
- **App:** `performancenarrative`
- **Domain:** HR Talent
- **Outcome:** Performance review narrative drafting with calibration support
- **Primary actors:** Manager, HRBP, People ops

## 2) Database Schema (Flyway V1__init.sql)

```sql
CREATE SCHEMA IF NOT EXISTS performancenarrative;

CREATE TABLE performancenarrative.review_cycle (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE performancenarrative.employee_review (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE performancenarrative.calibration_note (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE performancenarrative.goal_evidence (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE performancenarrative.narrative_draft (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE performancenarrative.feedback_item (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
```

## 3) REST API Endpoints

### Review Cycles
- `GET /api/v1/performance/review-cycles` - List all cycles
- `POST /api/v1/performance/review-cycles` - Create a cycle
- `GET /api/v1/performance/review-cycles/{id}` - Get cycle by ID
- `PATCH /api/v1/performance/review-cycles/{id}` - Update cycle
- `POST /api/v1/performance/review-cycles/{id}/validate` - Validate a cycle

### Employee Reviews
- `GET /api/v1/performance/employee-reviews` - List all reviews
- `POST /api/v1/performance/employee-reviews` - Create a review
- `GET /api/v1/performance/employee-reviews/{id}` - Get review by ID
- `PATCH /api/v1/performance/employee-reviews/{id}` - Update review
- `POST /api/v1/performance/employee-reviews/{id}/validate` - Validate a review

### Calibration Notes
- `GET /api/v1/performance/calibration-notes` - List all notes
- `POST /api/v1/performance/calibration-notes` - Create a note
- `GET /api/v1/performance/calibration-notes/{id}` - Get note by ID
- `PATCH /api/v1/performance/calibration-notes/{id}` - Update note
- `POST /api/v1/performance/calibration-notes/{id}/validate` - Validate a note

### Goal Evidences
- `GET /api/v1/performance/goal-evidences` - List all evidences
- `POST /api/v1/performance/goal-evidences` - Create an evidence
- `GET /api/v1/performance/goal-evidences/{id}` - Get evidence by ID
- `PATCH /api/v1/performance/goal-evidences/{id}` - Update evidence
- `POST /api/v1/performance/goal-evidences/{id}/validate` - Validate an evidence

### Narrative Drafts
- `GET /api/v1/performance/narrative-drafts` - List all drafts
- `POST /api/v1/performance/narrative-drafts` - Create a draft
- `GET /api/v1/performance/narrative-drafts/{id}` - Get draft by ID
- `PATCH /api/v1/performance/narrative-drafts/{id}` - Update draft
- `POST /api/v1/performance/narrative-drafts/{id}/validate` - Validate a draft

### Feedback Items
- `GET /api/v1/performance/feedback-items` - List all items (Wait, spec doesn't list endpoints for feedback specifically in section 5 but lists 6 services in section 6, wait, spec says:
- `GET /api/v1/performance/ai/analyze`
- `POST /api/v1/performance/ai/recommendations`
- `POST /api/v1/performance/workflows/execute`
- `GET /api/v1/performance/health/contracts`
- `GET /api/v1/performance/metrics/summary`

Wait, wait. Let's look at the spec for Feedback endpoints:
Service Layer Contract requires `feedbackService: create/update/list/getById/delete/validate/simulate`.
I will add the feedback endpoints as well, as UI needs it, even if section 5 omits them (or meant to be included). Actually I'll implement feedback exactly like others.

## 4) Services
- `ReviewCycleService`
- `EmployeeReviewService`
- `CalibrationNoteService`
- `GoalEvidenceService`
- `NarrativeDraftService`
- `FeedbackItemService`
Each with methods: `create`, `update`, `list`, `getById`, `delete`, `validate`, `simulate`

## 5) Event Integrations
Emits: `performancenarrative.review.drafted`, `performancenarrative.calibration.flagged`, `performancenarrative.review.finalized`
Consumes: `goaltracker.goal.updated`, `peopleanalytics.signal.updated`, `retentionsignal.risk.detected`

## 6) AI / LLM
`AiService` wrapper. Request schema validation, retry, fallback.

## 7) Frontend Pages
Next.js pages under `/app/performance/`:
- `/review-cycles` (list, create, edit, validate)
- `/employee-reviews`
- `/calibration-notes`
- `/goal-evidences`
- `/narrative-drafts`
- `/feedback-items`

## 8) Testing
Backend: JUnit/Mockito, Testcontainers
Frontend: vitest + Testing Library
