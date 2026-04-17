# DataLineageTracker Detailed Specification

## 1. Overview
DataLineageTracker is a comprehensive data lineage and governance platform. It tracks data flow from source to consumption, enforces governance policies, manages data ownership, and ensures compliance.

## 2. Database Schema (PostgreSQL)

All tables include `id` (UUID), `tenant_id` (UUID), `created_at` (TIMESTAMP), `updated_at` (TIMESTAMP).

### Tables:

**`data_assets`**
- `id` (UUID, primary key)
- `tenant_id` (UUID, indexed)
- `name` (VARCHAR)
- `type` (VARCHAR: TABLE, VIEW, STREAM, REPORT, MODEL)
- `source_system` (VARCHAR)
- `owner_id` (UUID)
- `steward_id` (UUID)
- `classification` (VARCHAR: PUBLIC, INTERNAL, CONFIDENTIAL, RESTRICTED)
- `description` (TEXT)
- `retention_days` (INT)
- `created_at`, `updated_at`

**`data_lineage_links`**
- `id` (UUID, primary key)
- `tenant_id` (UUID, indexed)
- `source_asset_id` (UUID, FK to data_assets)
- `target_asset_id` (UUID, FK to data_assets)
- `transformation_logic` (TEXT)
- `created_at`, `updated_at`

**`governance_policies`**
- `id` (UUID, primary key)
- `tenant_id` (UUID, indexed)
- `name` (VARCHAR)
- `description` (TEXT)
- `policy_type` (VARCHAR: ACCESS, RETENTION, DLP, COMPLIANCE)
- `rules` (JSONB)
- `is_active` (BOOLEAN)
- `created_at`, `updated_at`

**`pii_tags`**
- `id` (UUID, primary key)
- `tenant_id` (UUID, indexed)
- `asset_id` (UUID, FK to data_assets)
- `field_name` (VARCHAR)
- `tag_type` (VARCHAR: EMAIL, SSN, CREDIT_CARD, PHONE)
- `confidence_score` (DOUBLE)
- `created_at`, `updated_at`

**`audit_logs`**
- `id` (UUID, primary key)
- `tenant_id` (UUID, indexed)
- `asset_id` (UUID, FK to data_assets)
- `user_id` (UUID)
- `action` (VARCHAR: READ, EXPORT, MODIFY)
- `status` (VARCHAR: ALLOWED, DENIED)
- `reason` (TEXT) - e.g. "DLP rule blocked export"
- `created_at`, `updated_at`

**`incidents`**
- `id` (UUID, primary key)
- `tenant_id` (UUID, indexed)
- `asset_id` (UUID, FK to data_assets)
- `type` (VARCHAR: PII_LEAK, UNAUTHORIZED_ACCESS, SCHEMA_CHANGE)
- `status` (VARCHAR: OPEN, INVESTIGATING, RESOLVED)
- `description` (TEXT)
- `created_at`, `updated_at`

## 3. REST Endpoints

Prefix: `/api/v1`
Headers required: `X-Tenant-ID: UUID`

### Data Assets
- `GET /assets` - List assets (supports filtering by type, classification)
- `GET /assets/{id}` - Get asset details
- `POST /assets` - Create asset
- `PUT /assets/{id}` - Update asset
- `DELETE /assets/{id}` - Delete asset

### Lineage
- `GET /lineage/upstream/{assetId}` - Get upstream lineage graph for an asset
- `GET /lineage/downstream/{assetId}` - Get downstream impact analysis for an asset
- `POST /lineage/links` - Create lineage link between assets

### Governance & Compliance
- `GET /policies` - List policies
- `POST /policies` - Create policy
- `GET /compliance/dashboard` - Get compliance readiness stats (GDPR, HIPAA, etc.)

### AI & PII
- `POST /assets/{id}/scan-pii` - Triggers LiteLLM to analyze asset schema/sample data for PII tags

### Audit & Security
- `GET /audit-logs` - Get access logs
- `POST /audit-logs/evaluate` - Evaluates a data access request against DLP/policies and records log
- `GET /incidents` - List incidents

## 4. Frontend Components (Next.js & React Flow)

### Pages
- `/` - Dashboard (Compliance stats, recent incidents, asset summary using Chart.js)
- `/assets` - Asset catalog
- `/assets/[id]` - Asset detail view
- `/lineage/[id]` - Interactive lineage graph using React Flow
- `/governance` - Policy management
- `/audit` - Audit trails

### Components
- `LineageGraph`: Uses React Flow. Nodes are assets, edges are links. Custom node styles based on classification and PII tags.
- `ComplianceChart`: Uses Chart.js to show readiness metrics.
- `AssetForm`: For creating/editing metadata and ownership.
- `PolicyManager`: Define rules visually.

## 5. AI Integration (LiteLLM)
- `PiiScannerService`: Takes asset schema and a sample of synthetic data, calls LiteLLM to detect fields containing PII, and returns structured JSON with tags.

## 6. Integration Manifest
- **Emits**: `lineage.asset.created`, `lineage.incident.opened`, `lineage.policy.violated`
- **Consumes**: `auth.user.login` (to track active users), `data.pipeline.run` (to auto-create lineage links)

## 7. Error Handling
- Standard Spring `@RestControllerAdvice` returning `ProblemDetail` or custom API error response.
- 404 for missing assets.
- 403 for cross-tenant access attempts.
- 400 for validation errors.
