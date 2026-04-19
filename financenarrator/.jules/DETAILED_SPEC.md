# FinanceNarrator — Detailed Implementation Spec

## Overview
FinanceNarrator is a micro-saas ecosystem application designed to generate executive-grade financial narratives from structured financial datasets. It serves CFOs, FP&A analysts, and board prep owners.

## Database Schema (PostgreSQL)

```sql
-- V1__init_finance_narrator.sql

CREATE TABLE IF NOT EXISTS tenant.narrative_requests (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_narrative_requests_tenant ON tenant.narrative_requests(tenant_id);

CREATE TABLE IF NOT EXISTS tenant.narrative_sections (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_narrative_sections_tenant ON tenant.narrative_sections(tenant_id);

CREATE TABLE IF NOT EXISTS tenant.supporting_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_supporting_metrics_tenant ON tenant.supporting_metrics(tenant_id);

CREATE TABLE IF NOT EXISTS tenant.tone_profiles (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_tone_profiles_tenant ON tenant.tone_profiles(tenant_id);

CREATE TABLE IF NOT EXISTS tenant.approval_reviews (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_approval_reviews_tenant ON tenant.approval_reviews(tenant_id);

CREATE TABLE IF NOT EXISTS tenant.export_artifacts (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_export_artifacts_tenant ON tenant.export_artifacts(tenant_id);
```

## Entities & Repositories
6 Entities matching the tables above: `NarrativeRequest`, `NarrativeSection`, `SupportingMetric`, `ToneProfile`, `ApprovalReview`, `ExportArtifact`.
Fields: `id`, `tenantId`, `name`, `status`, `metadataJson`, `createdAt`, `updatedAt`.
JpaRepositories for each entity:
```java
public interface NarrativeRequestRepository extends JpaRepository<NarrativeRequest, UUID> {
    List<NarrativeRequest> findByTenantId(UUID tenantId);
    Optional<NarrativeRequest> findByIdAndTenantId(UUID id, UUID tenantId);
}
```
(Repeated for all 6 entities).

## Services
For each of the 6 entities, we will have a Service interface and implementation:
- `create(T dto)`
- `update(UUID id, T dto)`
- `list()`
- `getById(UUID id)`
- `delete(UUID id)`
- `validate(UUID id)`
- `simulate(UUID id)`

Additionally, we need:
- `AiOrchestrationService`: interacts with LiteLLM to analyze inputs, generate text, provide recommendations.
- `WorkflowExecutionService`: executes generation steps in a queue or synchronously.
- `EventPublisherService`: emits `financenarrator.narrative.generated`, `financenarrator.review.requested`, `financenarrator.export.completed`.

## REST API Contract

### NarrativeRequests
- `GET /api/v1/narratives/narrative-requests`: Lists all for tenant.
- `POST /api/v1/narratives/narrative-requests`: Creates new.
- `GET /api/v1/narratives/narrative-requests/{id}`: Gets one.
- `PATCH /api/v1/narratives/narrative-requests/{id}`: Updates status/name/metadata.
- `POST /api/v1/narratives/narrative-requests/{id}/validate`: Triggers validation logic.
- `POST /api/v1/narratives/narrative-requests/{id}/simulate`: Triggers simulation logic.

(Repeated for `narrative-sections`, `supporting-metrics`, `tone-profiles`, `approval-reviews`, `export-artifacts`).

### AI/Workflows
- `POST /api/v1/narratives/ai/analyze`
- `POST /api/v1/narratives/ai/recommendations`
- `POST /api/v1/narratives/workflows/execute`
- `GET /api/v1/narratives/health/contracts`
- `GET /api/v1/narratives/metrics/summary`

## AI Integration Points
- Wrap LiteLLM with a retry policy, timeout policy, circuit breaker.
- Prompting for generating narratives from data + tone profile + supporting metrics.
- JSON struct parsing for recommendations.

## Frontend UI (Next.js)
Pages:
- `/narrative-requests`: List + Create
- `/narrative-requests/[id]`: Detail view, edit, validation
- `/narrative-sections`: List + Create
- `/narrative-sections/[id]`: Detail view
- `/supporting-metrics`: List + Create
- `/supporting-metrics/[id]`: Detail view
- `/tone-profiles`: List + Create
- `/tone-profiles/[id]`: Detail view
- `/approval-reviews`: List + Create
- `/approval-reviews/[id]`: Detail view
- `/exports`: List + Create
- `/exports/[id]`: Detail view

Components:
- Data tables for list views.
- Detail/Edit form with validation.
- Sidebar or top nav linking to the above.
- AI Generation trigger component (e.g., button + modal/toast).

## Events
Consumes:
- `cashflowanalyzer.insight.published`
- `budgetpilot.variance.alerted`
- `equityintelligence.round.updated`
(Requires creating listeners or marking them in integration-manifest.json).

Emits:
- `financenarrator.narrative.generated`
- `financenarrator.review.requested`
- `financenarrator.export.completed`
