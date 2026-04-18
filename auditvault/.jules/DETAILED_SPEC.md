# Phase 1: Detailed Spec - AuditVault

## 1. Overview
AuditVault is an AI compliance evidence aggregation platform. It pulls evidence from connected ecosystem apps automatically, maps evidence to control frameworks (SOC 2, ISO 27001, GDPR, HIPAA, EU AI Act) using an AI pipeline, and generates audit-ready evidence packages.

## 2. Database Schema (Entities)

All entities must include: `id` (UUID), `tenant_id` (UUID), `created_at` (TIMESTAMP), `updated_at` (TIMESTAMP).

### Framework
- `name` (VARCHAR): e.g., "SOC 2 Type II"
- `description` (TEXT)
- `version` (VARCHAR)
- `status` (VARCHAR): "ACTIVE", "ARCHIVED"

### Control
- `framework_id` (UUID): FK to Framework
- `control_code` (VARCHAR): e.g., "CC1.1"
- `title` (VARCHAR)
- `description` (TEXT)
- `evidence_requirements` (TEXT)

### Evidence
- `source_app` (VARCHAR): e.g., "DeploySignal"
- `evidence_type` (VARCHAR): e.g., "DEPLOY_LOG", "ACCESS_REVIEW"
- `content` (TEXT): The actual evidence payload (JSON string or description).
- `url` (VARCHAR): Link to external source (if any)
- `collected_at` (TIMESTAMP)
- `status` (VARCHAR): "PENDING_MAPPING", "MAPPED", "REJECTED"

### EvidenceMapping
- `evidence_id` (UUID)
- `control_id` (UUID)
- `ai_confidence_score` (DOUBLE)
- `ai_rationale` (TEXT)
- `status` (VARCHAR): "AUTO_MAPPED", "APPROVED", "REJECTED"

### AuditPackage
- `framework_id` (UUID)
- `name` (VARCHAR)
- `generated_at` (TIMESTAMP)
- `status` (VARCHAR): "GENERATING", "READY", "FAILED"
- `download_url` (VARCHAR)

## 3. REST API Endpoints

### Frameworks
- `GET /api/v1/frameworks` - List frameworks
- `GET /api/v1/frameworks/{id}/controls` - List controls for a framework

### Evidence
- `POST /api/v1/evidence` - Ingest new evidence (often via webhook or API)
    - Request: `{ "sourceApp": "...", "evidenceType": "...", "content": "..." }`
- `GET /api/v1/evidence` - List evidence (filterable by status)
- `GET /api/v1/evidence/{id}` - Get evidence details

### Mappings
- `POST /api/v1/evidence/{id}/map` - Trigger AI to map evidence to controls
- `POST /api/v1/mappings/{id}/approve` - Approve AI mapping
- `POST /api/v1/mappings/{id}/reject` - Reject AI mapping

### Audit Packages
- `POST /api/v1/packages` - Generate new audit package
    - Request: `{ "frameworkId": "...", "name": "..." }`
- `GET /api/v1/packages` - List packages
- `GET /api/v1/packages/{id}` - Get package details/download URL

## 4. AI Integration Points
- **Evidence Mapping Pipeline**: When evidence arrives, LiteLLM is called with the evidence content and a list of active controls. The prompt asks the model to identify which controls this evidence satisfies and provide a rationale and confidence score.

## 5. React Components (Frontend)
- `DashboardPage`: Overview of frameworks, completion status, recent evidence.
- `FrameworkListPage`: Grid of active frameworks.
- `FrameworkDetailView`: Lists controls and mapping status.
- `EvidenceInbox`: Table of unmapped or pending evidence.
- `EvidenceReviewDialog`: UI to approve/reject AI mapping rationale.
- `AuditPackageGenerator`: Modal to trigger package creation.

## 6. Acceptance Criteria & Test Cases
1. **Tenant Isolation**: Evidence uploaded for Tenant A cannot be seen by Tenant B.
2. **Evidence Ingestion**: POST to `/api/v1/evidence` creates an evidence record in `PENDING_MAPPING` status.
3. **AI Mapping**: Calling `/api/v1/evidence/{id}/map` invokes AI service and creates an `EvidenceMapping` record.
4. **Package Generation**: Triggering a package creation aggregates approved evidence for a framework into a summary.

## 7. Assumptions & Constraints
- Audit Packages are generated synchronously or asynchronously depending on size, but we will mock a synchronous/quick generation for the scope of this implementation.
- `download_url` will be a dummy URL or local route returning a JSON/PDF summary.
