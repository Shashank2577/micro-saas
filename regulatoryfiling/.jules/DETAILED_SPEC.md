# Detailed Spec for RegulatoryFiling

## Overview
This is the detailed specification for the RegulatoryFiling application.

### Domain Model (Entities)

All entities must include: `id` (UUID), `tenantId` (UUID), `name` (String), `status` (String enum), `metadataJson` (String mapped to JSONB), `createdAt` (OffsetDateTime), `updatedAt` (OffsetDateTime).

1. `FilingObligation`
2. `JurisdictionSchedule`
3. `SubmissionPacket`
4. `FilingDeadline`
5. `ValidationCheck`
6. `SubmissionReceipt`

### REST Endpoints
For each of the 6 entities, we need:
- `GET /api/v1/filings/<entity-plural>`
- `POST /api/v1/filings/<entity-plural>`
- `GET /api/v1/filings/<entity-plural>/{id}`
- `PATCH /api/v1/filings/<entity-plural>/{id}`
- `POST /api/v1/filings/<entity-plural>/{id}/validate`

Additionally:
- `POST /api/v1/filings/ai/analyze`
- `POST /api/v1/filings/ai/recommendations`
- `POST /api/v1/filings/workflows/execute`
- `GET /api/v1/filings/health/contracts`
- `GET /api/v1/filings/metrics/summary`

### Service Layer Methods
For each module (Obligations, Calendar, Packets, Validations, Submissions, Receipts), we need service methods:
- `create`
- `update`
- `list`
- `getById`
- `delete`
- `validate`
- `simulate`

### Frontend Requirements
- For each module, we need a table/list view with filters and pagination.
- Detail page with timeline/activity.
- Create/edit forms with schema validation.

### Integrations & AI
- Uses `cc-starter` `AiService` wrapper.
- Emits events: `regulatoryfiling.deadline.alerted`, `regulatoryfiling.packet.ready`, `regulatoryfiling.filing.submitted`.
- Consumes events: `complianceradar.change.detected`, `policyforge.policy.published`, `auditready.gap.opened`.

### Testing
- Backend unit tests for all services.
- Integration tests for all controllers.
- Frontend tests.
