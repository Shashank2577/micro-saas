# ComplianceRadar Detailed Specification

## 1. Domain Models

The following JPA entities map exactly to the provided authoritative domain model. They all share common attributes (UUID id, UUID tenant_id, String name, String status, JsonNode metadata_json, Instant created_at, Instant updated_at).

### RegulationUpdate
Represents an external regulatory change detected via feeds.

### JurisdictionRule
Represents a normalized rule or requirement specific to a jurisdiction.

### ImpactAssessment
Represents an analysis of how a RegulationUpdate or JurisdictionRule impacts the organization.

### ControlGap
Represents a discovered gap between internal controls and a new JurisdictionRule.

### TaskAssignment
Represents a workflow task assigned to close a ControlGap.

### DeadlineAlert
Represents an automated alert or notification regarding an impending compliance deadline.

## 2. Database Schema (Flyway V1__init.sql)
Tables will be created in the `complianceradar` schema.
Columns: `id UUID PRIMARY KEY`, `tenant_id UUID NOT NULL`, `name VARCHAR(180)`, `status VARCHAR(40)`, `metadata_json JSONB`, `created_at TIMESTAMPTZ`, `updated_at TIMESTAMPTZ`.
Indexes will be created on `tenant_id`.

## 3. Service Layer
- **FeedsService**: CRUD operations for RegulationUpdate. Methods: `create`, `update`, `list`, `getById`, `delete`, `validate`, `simulate`.
- **NormalizationService**: CRUD for JurisdictionRule. Integrates with LiteLLM (`AiService`) to extract rules from `RegulationUpdate` text.
- **ImpactAssessmentService**: CRUD for ImpactAssessment. Evaluates impact.
- **GapMappingService**: CRUD for ControlGap. Maps to internal policies.
- **TasksService**: CRUD for TaskAssignment.
- **AlertsService**: CRUD for DeadlineAlert.

## 4. API Endpoints
All endpoints are scoped by `X-Tenant-ID` header.
- `GET/POST /api/v1/regulations/regulation-updates`
- `GET/PATCH/DELETE /api/v1/regulations/regulation-updates/{id}`
- `POST /api/v1/regulations/regulation-updates/{id}/validate`
- `GET/POST /api/v1/regulations/jurisdiction-rules`
- `GET/PATCH/DELETE /api/v1/regulations/jurisdiction-rules/{id}`
- `POST /api/v1/regulations/jurisdiction-rules/{id}/validate`
- `GET/POST /api/v1/regulations/impact-assessments`
- `GET/PATCH/DELETE /api/v1/regulations/impact-assessments/{id}`
- `POST /api/v1/regulations/impact-assessments/{id}/validate`
- `GET/POST /api/v1/regulations/control-gaps`
- `GET/PATCH/DELETE /api/v1/regulations/control-gaps/{id}`
- `POST /api/v1/regulations/control-gaps/{id}/validate`
- `GET/POST /api/v1/regulations/task-assignments`
- `GET/PATCH/DELETE /api/v1/regulations/task-assignments/{id}`
- `POST /api/v1/regulations/task-assignments/{id}/validate`
- `POST /api/v1/regulations/ai/analyze`
- `POST /api/v1/regulations/ai/recommendations`
- `POST /api/v1/regulations/workflows/execute`
- `GET /api/v1/regulations/health/contracts`
- `GET /api/v1/regulations/metrics/summary`

## 5. Integrations & Events
- **Emits**: `complianceradar.change.detected`, `complianceradar.gap.opened`, `complianceradar.deadline.alerted`
- **Consumes**: `policyforge.policy.published`, `auditready.readiness.scored`, `regulatoryfiling.filing.submitted`

## 6. AI/LLM Integration
Use LiteLLM via `com.crosscutting.starter.ai.AiService` with retry and circuit breaker logic. Output parsed into structured JSON schemas.

## 7. Frontend Pages (Next.js)
Pages built with standard App Router architecture (`/app/[module]/page.tsx` for list, `/app/[module]/[id]/page.tsx` for detail/form). Generic multi-tenant API client configured to inject `X-Tenant-ID`.

## 8. Assumptions
- Since `cc-starter` is an existing shared library, we assume `TenantContext.require()` and `AiService` are available after local installation.
- Tests will utilize H2 in-memory DB and testcontainers for Postgres, with `cc.tenancy.enabled=false` overrides where appropriate for test setup.
