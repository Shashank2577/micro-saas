# CompBenchmark — Detailed Spec

## 1. Database Schema

All tables include `tenant_id` for hard multi-tenancy isolation and standard timestamps.

```sql
CREATE TABLE comp_bands (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_comp_bands_tenant ON comp_bands(tenant_id);

CREATE TABLE market_datasets (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_market_datasets_tenant ON market_datasets(tenant_id);

CREATE TABLE benchmark_runs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_benchmark_runs_tenant ON benchmark_runs(tenant_id);

CREATE TABLE gap_findings (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_gap_findings_tenant ON gap_findings(tenant_id);

CREATE TABLE adjustment_plans (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_adjustment_plans_tenant ON adjustment_plans(tenant_id);

CREATE TABLE approval_records (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_approval_records_tenant ON approval_records(tenant_id);
```

## 2. API Endpoints (REST Controllers)

Each module (CompBand, MarketDataset, BenchmarkRun, GapFinding, AdjustmentPlan, ApprovalRecord) has the following standard REST endpoints under `/api/v1/compensation-benchmark/<plural-resource>`:

- `GET /` - List all, returns `List<EntityDto>`.
- `POST /` - Create new, accepts `CreateEntityDto`, returns `EntityDto`.
- `GET /{id}` - Get by ID, returns `EntityDto`.
- `PATCH /{id}` - Update by ID, accepts `UpdateEntityDto`, returns `EntityDto`.
- `POST /{id}/validate` - Validate the entity, returns `{ "valid": boolean, "errors": [] }`.
- `POST /{id}/simulate` - Simulate the entity application (for specific modules), returns `{ "result": object }`.

Other Endpoints:
- `POST /api/v1/compensation-benchmark/ai/analyze` - Request body: `{ "context": string }`. Returns: `{ "analysis": string }`.
- `POST /api/v1/compensation-benchmark/ai/recommendations` - Request body: `{ "gap_id": uuid }`. Returns: `{ "recommendations": [] }`.
- `POST /api/v1/compensation-benchmark/workflows/execute` - Request body: `{ "workflow": string }`.
- `GET /api/v1/compensation-benchmark/health/contracts`
- `GET /api/v1/compensation-benchmark/metrics/summary`

## 3. Service Layer Methods

Each entity service (e.g. `BandsService`) implements:
- `Entity create(CreateEntityDto dto)`
- `Entity update(UUID id, UpdateEntityDto dto)`
- `List<Entity> list()`
- `Entity getById(UUID id)`
- `void delete(UUID id)`
- `ValidationResult validate(UUID id)`
- `SimulationResult simulate(UUID id)`

All operations filter by `tenant_id` from the context.
Operations emit events for `compbenchmark.band.updated`, `compbenchmark.gap.detected`, `compbenchmark.plan.recommended`.

## 4. AI Integration (LiteLLM)

- **Analyze**: Send prompt with `context` to analyze compensation data.
- **Recommendations**: Analyze a `GapFinding` and recommend adjustment plans.
- Uses `AiService` wrapper from `cc-starter` (which integrates with LiteLLM at `http://localhost:4000`).

## 5. React Components & Pages

- **Pages**:
  - `/comp-bands` - List of comp bands
  - `/comp-bands/[id]` - Comp band detail
  - `/market-datasets` - List of market datasets
  - `/market-datasets/[id]` - Dataset detail
  - `/benchmark-runs`
  - `/benchmark-runs/[id]`
  - `/gap-findings`
  - `/gap-findings/[id]`
  - `/adjustment-plans`
  - `/adjustment-plans/[id]`
  - `/approvals`
  - `/approvals/[id]`

- **Components**:
  - `DataTable` for lists.
  - `DetailView` for item specifics.
  - `CreateForm` / `EditForm` for mutations.

## 6. Error Handling

- Missing Tenant ID -> 401 Unauthorized
- Entity Not Found -> 404 Not Found
- Validation Errors -> 400 Bad Request
- AI Service Unavailable -> 503 Service Unavailable

## 7. Acceptance Criteria & Tests

- Backend tests verify `tenant_id` isolation.
- Integration tests verify event emission.
- Frontend component tests render list, handle form submissions.
