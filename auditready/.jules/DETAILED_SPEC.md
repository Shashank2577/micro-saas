# AuditReady Detailed Spec

## 1. Data Model
* **ControlFramework**: id, tenantId, name, description, version, createdAt, updatedAt
* **Control**: id, tenantId, frameworkId, name, description, status (IMPLEMENTED, PARTIAL, NOT_IMPLEMENTED), createdAt, updatedAt
* **Evidence**: id, tenantId, controlId, name, description, fileUrl, collectedAt, status (VALID, EXPIRED, PENDING)
* **ComplianceGap**: id, tenantId, controlId, description, severity (LOW, MEDIUM, HIGH, CRITICAL), status (OPEN, IN_PROGRESS, CLOSED), detectedAt
* **AuditReport**: id, tenantId, frameworkId, reportName, readinessScore, generatedAt, summary
* **RemediationWorkflow**: id, tenantId, gapId, title, description, assignedTo, status (TODO, IN_PROGRESS, DONE), dueDate
* **AuditTrail**: id, tenantId, entityType, entityId, action, performedBy, timestamp

## 2. API Endpoints
* `GET /api/frameworks`, `POST /api/frameworks`, `GET /api/frameworks/{id}`
* `GET /api/controls`, `POST /api/controls`, `PUT /api/controls/{id}`
* `GET /api/evidence`, `POST /api/evidence`, `GET /api/evidence/{id}`
* `GET /api/gaps`, `POST /api/gaps`, `PUT /api/gaps/{id}`
* `GET /api/reports`, `POST /api/reports` (triggers score calculation)
* `GET /api/remediations`, `POST /api/remediations`, `PUT /api/remediations/{id}`
* `GET /api/audit-trails`

## 3. Integrations
* Emits: `audit.score-calculated`, `evidence.collected`
* Consumes: `policy.published`, `compliance.gap-detected`

## 4. Frontend Pages
* `/` - Dashboard
* `/frameworks` - Control Frameworks
* `/evidence` - Evidence Management
* `/gaps` - Compliance Gaps
* `/reports` - Audit Reports

