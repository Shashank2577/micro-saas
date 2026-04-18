# DataGovernanceOS Detailed Specification

## Overview
DataGovernanceOS is a multi-tenant micro-SaaS application for defining, managing, and automating data governance policies. It provides a centralized control plane for tracking data assets, defining governance rules (e.g., PII protection, retention schedules), auditing compliance, and managing access requests. The AI component automates the classification of new data assets and evaluates potential risks based on content schemas and metadata.

## Core Entities
1. **DataAsset**
   - `id` (UUID, primary key)
   - `tenantId` (UUID, required)
   - `name` (String, required)
   - `type` (String, e.g., DATABASE, TABLE, S3_BUCKET, API_ENDPOINT)
   - `description` (Text)
   - `classification` (String, e.g., PUBLIC, INTERNAL, CONFIDENTIAL, RESTRICTED)
   - `piiStatus` (Boolean)
   - `owner` (String)
   - `createdAt` (Timestamp)
   - `updatedAt` (Timestamp)

2. **GovernancePolicy**
   - `id` (UUID, primary key)
   - `tenantId` (UUID, required)
   - `name` (String, required)
   - `description` (Text)
   - `ruleDefinition` (JSONB, e.g., conditions for applying the policy)
   - `enforcementLevel` (String, e.g., AUDIT_ONLY, BLOCK, WARN)
   - `status` (String, e.g., DRAFT, ACTIVE, DEPRECATED)
   - `createdAt` (Timestamp)
   - `updatedAt` (Timestamp)

3. **ComplianceAudit**
   - `id` (UUID, primary key)
   - `tenantId` (UUID, required)
   - `assetId` (UUID, foreign key to DataAsset)
   - `policyId` (UUID, foreign key to GovernancePolicy)
   - `status` (String, e.g., PASSED, FAILED, WARNING)
   - `findings` (Text)
   - `auditedAt` (Timestamp)

## API Endpoints (REST)
All endpoints require `X-Tenant-ID` header.

- `GET /api/v1/assets`: List all data assets for the tenant.
- `POST /api/v1/assets`: Register a new data asset.
- `GET /api/v1/assets/{id}`: Get data asset details.
- `PUT /api/v1/assets/{id}`: Update a data asset.
- `DELETE /api/v1/assets/{id}`: Delete a data asset.

- `GET /api/v1/policies`: List all governance policies.
- `POST /api/v1/policies`: Create a new policy.
- `GET /api/v1/policies/{id}`: Get policy details.
- `PUT /api/v1/policies/{id}`: Update a policy.

- `GET /api/v1/audits`: List compliance audit results.
- `POST /api/v1/audits/run`: Trigger a new compliance audit for an asset against all active policies.

- `POST /api/v1/ai/classify`: AI endpoint to suggest classification and PII status for a data asset based on its schema/description.

## AI Integration
- Service: `DataClassificationAiService`
- Uses LiteLLM client (`claude-sonnet-4-6` or configured default model).
- Prompt: Accepts data asset metadata, schema information, and sample data (if provided/mocked) and returns a JSON response recommending `classification` and `piiStatus`.

## Integration Hub (Event Integration)
- **Emits**: `data_asset.created`, `policy.created`, `audit.failed`
- **Consumes**: `pipeline.failed` (from PipelineGuardian/Data Quality tools)
- **Capabilities**: `policy_lookup`, `asset_classification`

## UI/Frontend Pages
- **Dashboard (`/`)**: Overview of total assets, active policies, recent audit failures.
- **Assets (`/assets`)**: Data table of assets with classification status. Button to register new asset.
- **Policies (`/policies`)**: List of active policies and rule definitions.
- **Audits (`/audits`)**: Audit logs and compliance status reports.

## Testing Strategy
- **Backend**: Spring Boot integration tests with H2 in-memory DB (`@ActiveProfiles("test")`, `spring.flyway.enabled=false`).
- Mock LiteLLM response in tests.
- **Frontend**: Vitest + React Testing Library for components. Mock API client.

