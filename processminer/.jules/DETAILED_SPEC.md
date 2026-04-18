# ProcessMiner Detailed Specification

## 1. Overview
ProcessMiner is an AI-powered business process intelligence platform. It ingests event logs from various systems, reconstructs process flows, identifies bottlenecks, compliance gaps, and automation opportunities.

## 2. Database Schema (PostgreSQL)

### Flyway V1__init.sql

```sql
CREATE TABLE event_logs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    system_type VARCHAR(50) NOT NULL, -- CRM, ERP, HR, etc.
    case_id VARCHAR(100) NOT NULL, -- Correlates events for the same process instance
    activity_name VARCHAR(255) NOT NULL,
    actor_id VARCHAR(100),
    timestamp TIMESTAMP NOT NULL,
    attributes JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_event_logs_tenant_case ON event_logs(tenant_id, case_id);
CREATE INDEX idx_event_logs_tenant_sys ON event_logs(tenant_id, system_type);

CREATE TABLE process_models (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    system_type VARCHAR(50) NOT NULL,
    bpmn_xml TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE analysis_results (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    process_model_id UUID NOT NULL REFERENCES process_models(id),
    type VARCHAR(50) NOT NULL, -- BOTTLENECK, COMPLIANCE_GAP, VARIANT
    severity VARCHAR(20), -- HIGH, MEDIUM, LOW
    description TEXT,
    details JSONB, -- specific metric details
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE policies (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    process_model_id UUID NOT NULL REFERENCES process_models(id),
    name VARCHAR(255) NOT NULL,
    rule_definition JSONB NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE automation_opportunities (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    process_model_id UUID NOT NULL REFERENCES process_models(id),
    activity_name VARCHAR(255) NOT NULL,
    estimated_roi DECIMAL(10, 2),
    effort_estimate VARCHAR(50), -- LOW, MEDIUM, HIGH
    rationale TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 3. Backend (Spring Boot 3.3.5)

### API Endpoints

- `POST /api/events/ingest`
  - Body: `{ systemType: string, caseId: string, activityName: string, actorId: string, timestamp: string, attributes: map }`
  - Response: `201 Created`
- `GET /api/events` (Filter by system, case)
- `POST /api/process-models/discover`
  - Body: `{ systemType: string }`
  - Triggers async discovery job. Returns `202 Accepted` with Job ID.
- `GET /api/process-models`
- `GET /api/process-models/{id}`
- `GET /api/process-models/{id}/analysis`
  - Response: `{ bottlenecks: [], variants: [], complianceGaps: [] }`
- `POST /api/policies`
  - Body: `{ processModelId: string, name: string, ruleDefinition: object }`
- `GET /api/automation-opportunities`
  - Response: `[ { activityName: string, estimatedRoi: number, effortEstimate: string, rationale: string } ]`

### Services
- `IngestionService`: Handles event log persistence.
- `ProcessDiscoveryService`: Stubbed algorithm that generates a mock BPMN based on event sequences.
- `AnalysisService`: Identifies bottlenecks based on time differences between events, checks compliance against policies, finds variants.
- `AIAnalysisService`: Uses LiteLLM to estimate automation ROI based on activity characteristics and text generation.

## 4. Frontend (Next.js)

### Pages
- `/` - Dashboard: overview of processes, total variants, top bottlenecks, total ROI.
- `/ingestion` - Upload/View event logs.
- `/processes` - List of discovered process models.
- `/processes/[id]` - Detailed view with simulated BPMN visual, variants, bottlenecks.
- `/compliance` - Define and view policies.
- `/automation` - View and score automation opportunities.

### State & API
- Use Axios via `src/lib/api.ts` with interceptors for `X-Tenant-ID`.
- Uses `react-chartjs-2` for metric charts.

## 5. Acceptance Criteria Mapping
- Event log ingestion supports 5+ system types -> Tested via controller tests checking systemType field constraint.
- Process reconstruction -> Stubbed BPMN output verified.
- Automation opportunity scoring -> AI service mocked in tests, verifying expected output structure.
