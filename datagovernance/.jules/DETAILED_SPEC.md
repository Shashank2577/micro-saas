# DataGovernance Detailed Spec

## 1. Overview
Data Governance is a micro-SaaS application handling data retention policies, GDPR/CCPA compliance, data lineage, PII detection, and audit trails.

## 2. Database Schema (PostgreSQL)

```sql
CREATE TABLE data_retention_policy (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    data_type VARCHAR(255) NOT NULL,
    retention_days INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE data_subject_request (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    subject_email VARCHAR(255) NOT NULL,
    request_type VARCHAR(50) NOT NULL, -- 'ACCESS', 'DELETION'
    status VARCHAR(50) NOT NULL, -- 'PENDING', 'IN_PROGRESS', 'COMPLETED', 'FAILED'
    created_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP
);

CREATE TABLE pii_record (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    source_table VARCHAR(255) NOT NULL,
    source_column VARCHAR(255) NOT NULL,
    pii_type VARCHAR(100) NOT NULL,
    is_masked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE consent_record (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    processing_purpose VARCHAR(255) NOT NULL,
    is_granted BOOLEAN NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

CREATE TABLE data_lineage_node (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    field_name VARCHAR(255) NOT NULL,
    origin_service VARCHAR(255) NOT NULL,
    current_service VARCHAR(255) NOT NULL,
    transformation_logic TEXT,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE audit_log (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    actor VARCHAR(255) NOT NULL,
    action VARCHAR(255) NOT NULL,
    resource VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    details TEXT
);
```

## 3. REST API Endpoints

### Data Retention Policies
- `GET /api/v1/policies`
- `POST /api/v1/policies`
  - Request: `{ "dataType": "USER_DATA", "retentionDays": 90 }`
  - Response: `{ "id": "...", "dataType": "USER_DATA", "retentionDays": 90, ... }`
- `DELETE /api/v1/policies/{id}`

### Data Subject Access Requests (DSAR)
- `GET /api/v1/dsar`
- `POST /api/v1/dsar`
  - Request: `{ "subjectEmail": "user@example.com", "requestType": "ACCESS" }`
- `POST /api/v1/dsar/{id}/process` (admin triggers processing)

### Consent Management
- `GET /api/v1/consent/{email}`
- `POST /api/v1/consent`
  - Request: `{ "userEmail": "user@example.com", "processingPurpose": "MARKETING", "isGranted": true }`

### PII Detection (AI Integration)
- `POST /api/v1/pii/detect`
  - Request: `{ "text": "Email: john@example.com, Phone: 555-1234" }`
  - Response: Uses LiteLLM to detect PII and returns classification.

### Lineage
- `GET /api/v1/lineage?field={fieldName}`

### Audit
- `GET /api/v1/audit?startDate={date}&endDate={date}`

## 4. Frontend Application (Next.js)

### Pages
- `/` - Dashboard with overview of compliance status.
- `/policies` - Manage retention policies.
- `/dsar` - Manage and fulfill Data Subject Access Requests.
- `/consent` - View and manage consent records.
- `/audit` - View audit logs.
- `/lineage` - View data lineage for specific fields.

### Components
- `PolicyList`: Table of policies.
- `DsarTable`: Table of requests.
- `PiiDetector`: Text area to test PII detection.
- `AuditLogViewer`: Component to filter and view logs.

## 5. AI Integration (LiteLLM)
- `PiiDetectionService` will call LiteLLM to analyze provided text and extract PII types like 'EMAIL', 'PHONE', 'SSN'.

## 6. Testing Strategy
- Backend: Unit tests for services with JUnit and Mockito. `@DataJpaTest` with `application-test.properties` and `@ActiveProfiles("test")`.
- Frontend: Vitest + React Testing Library for components.

