# API Manager Detailed Specification

## Overview
API Manager is an API documentation and lifecycle management service. It provides OpenAPI/Swagger documentation, API versioning, deprecation management, developer portal, and integration with LiteLLM for API recommendations.

## Database Schema (PostgreSQL)
```sql
CREATE TABLE api_projects (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    base_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE api_versions (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    project_id UUID NOT NULL REFERENCES api_projects(id),
    version_string VARCHAR(50) NOT NULL,
    openapi_schema TEXT,
    status VARCHAR(50) NOT NULL, -- DRAFT, PUBLISHED, DEPRECATED, RETIRED
    release_notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE api_endpoints (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    version_id UUID NOT NULL REFERENCES api_versions(id),
    method VARCHAR(20) NOT NULL,
    path VARCHAR(255) NOT NULL,
    operation_id VARCHAR(255),
    summary TEXT,
    description TEXT,
    is_deprecated BOOLEAN DEFAULT FALSE,
    migration_guide TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE api_keys (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    project_id UUID NOT NULL REFERENCES api_projects(id),
    developer_id VARCHAR(255) NOT NULL,
    key_hash VARCHAR(255) NOT NULL,
    prefix VARCHAR(20) NOT NULL,
    scopes TEXT,
    status VARCHAR(50) NOT NULL, -- ACTIVE, REVOKED
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP
);

CREATE TABLE api_analytics (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    project_id UUID NOT NULL REFERENCES api_projects(id),
    endpoint_id UUID REFERENCES api_endpoints(id),
    path VARCHAR(255) NOT NULL,
    method VARCHAR(20) NOT NULL,
    status_code INTEGER NOT NULL,
    latency_ms INTEGER NOT NULL,
    timestamp TIMESTAMP NOT NULL
);
```

## REST API Endpoints

### Projects
- `GET /api/v1/projects` - List API projects
- `POST /api/v1/projects` - Create project
- `GET /api/v1/projects/{id}` - Get project details

### Versions & Schemas
- `POST /api/v1/projects/{id}/versions` - Upload/create new API version with OpenAPI schema
- `GET /api/v1/projects/{id}/versions` - List versions
- `GET /api/v1/projects/{id}/versions/{versionId}/schema` - Get parsed OpenAPI schema
- `POST /api/v1/projects/{id}/versions/compare` - Compare two versions for breaking changes
- `PUT /api/v1/projects/{id}/versions/{versionId}/status` - Update version status (e.g., mark deprecated)

### SDK Generation
- `POST /api/v1/projects/{id}/versions/{versionId}/sdk?language={lang}` - Generate client SDK

### Mock Server
- `ALL /mock/{projectId}/{versionId}/*` - Dynamic mock server returning examples based on OpenAPI schema

### API Keys
- `POST /api/v1/keys` - Generate new API key
- `GET /api/v1/keys` - List keys
- `DELETE /api/v1/keys/{id}` - Revoke key

### Analytics
- `GET /api/v1/analytics/projects/{id}/summary` - Top endpoints, latency, error rates

## Services

- `ProjectService`: Manage projects
- `VersionService`: Process OpenAPI schemas (validate, parse), manage lifecycle
- `SchemaService`: Diff schemas, detect breaking changes, extract endpoints
- `SdkService`: Generate SDKs (mock generation using LiteLLM/templates)
- `MockService`: Route requests to mock responses defined in schema
- `ApiKeyService`: Generate and validate API keys
- `AnalyticsService`: Aggregate analytics data

## Frontend Components (Next.js)

- `ProjectsList`: Dashboard showing all APIs
- `ProjectDetail`: Overview of API project
- `SwaggerViewer`: Interactive documentation viewer (Swagger UI or similar)
- `VersionManager`: Upload schemas, manage lifecycle, compare versions
- `ApiKeyManager`: Generate/revoke API keys
- `AnalyticsDashboard`: Charts for API usage and performance

## Acceptance Criteria
- Upload OpenAPI schema with 20 endpoints
- Generate interactive Swagger UI
- Request examples for endpoints
- Document versions separately
- Breaking change detection (v1.0 -> v2.0 field removal)
- Deprecation notices and migration guide
- SDK generation wrapper
- Mock server responding without real API
- Test scenario integration
- Developer portal features
- Analytics display

