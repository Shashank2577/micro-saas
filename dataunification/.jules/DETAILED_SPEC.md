# DataUnification Detailed Spec

## 1. Overview
DataUnification is a multi-tenant platform to consolidate data from multiple disparate sources, map schemas, resolve conflicts, and sync data in real-time or batch.

## 2. Database Schema (PostgreSQL)

```sql
CREATE TABLE data_source (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL, -- e.g., DATABASE, API, FILE
    connection_details JSONB,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_ds_tenant ON data_source(tenant_id);

CREATE TABLE schema_mapping (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    source_id UUID REFERENCES data_source(id),
    mapping_rules JSONB, -- stores visual mappings
    version INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_sm_tenant ON schema_mapping(tenant_id);

CREATE TABLE sync_job (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    mapping_id UUID REFERENCES schema_mapping(id),
    status VARCHAR(50) NOT NULL, -- PENDING, RUNNING, COMPLETED, FAILED
    type VARCHAR(50) NOT NULL, -- BATCH, REALTIME
    records_processed INT DEFAULT 0,
    error_log TEXT,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_sj_tenant ON sync_job(tenant_id);

CREATE TABLE audit_log (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    job_id UUID REFERENCES sync_job(id),
    record_id VARCHAR(255),
    action VARCHAR(50), -- CREATE, UPDATE, DELETE, CONFLICT_RESOLVED
    details JSONB,
    created_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_al_tenant ON audit_log(tenant_id);
```

## 3. Backend (Spring Boot)

### 3.1 Dependencies
- cc-starter
- spring-boot-starter-data-jpa
- spring-boot-starter-web
- springdoc-openapi-starter-webmvc-ui
- liteLLM client (via WebClient)
- debezium (CDC simulation/abstraction)
- pgmq integration

### 3.2 Entities
- `DataSource`
- `SchemaMapping`
- `SyncJob`
- `AuditLog`

### 3.3 Services
- `DataSourceService`: manage connections.
- `SchemaMappingService`: CRUD for mappings + AI suggestions via LiteLLM.
- `SyncJobService`: dispatch jobs to pgmq, handle state.
- `AuditLogService`: write trail.

### 3.4 REST Controllers
- `POST /api/sources`, `GET /api/sources`, `GET /api/sources/{id}`
- `POST /api/mappings`, `GET /api/mappings`, `GET /api/mappings/{id}`, `POST /api/mappings/suggest` (AI)
- `POST /api/jobs`, `GET /api/jobs`, `GET /api/jobs/{id}/rollback`
- `GET /api/audit-logs`

## 4. Frontend (Next.js App Router)

### 4.1 Pages
- `/` (Dashboard): Overview, Sync job statuses (Chart.js for monitoring).
- `/sources`: List and add new data sources.
- `/mappings`: Schema mapping list, and visual editor using React Flow.
- `/jobs`: Job history and logs.

### 4.2 State & Components
- Uses React Query or custom hooks for data fetching.
- `React Flow` setup in `/mappings/[id]` to visualize source -> target nodes.
- Chart.js in `/components/MonitoringChart.tsx`.

## 5. Integration Manifest
- Emits: `sync.completed`, `sync.failed`
- Consumes: none by default
- Capabilities: `schema_reconciliation`

## 6. Acceptance Criteria Tests
- Save and list >3 sources.
- Visual mapping (mock API representation) saves mapping JSON.
- Batch sync creation triggers async mock.
- Error logic sets `FAILED` status and returns details.
