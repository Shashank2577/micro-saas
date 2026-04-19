# DataCatalogAI - Detailed Specification

## 1. Overview
DataCatalogAI is a Next-15 production application for data governance. It serves as a data asset catalog with ownership, lineage context, and semantic enrichment capabilities.

## 2. Database Schema (PostgreSQL)

```sql
-- V1__init.sql

CREATE TABLE data_assets (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_data_assets_tenant_id ON data_assets(tenant_id);

CREATE TABLE ownership_records (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_ownership_records_tenant_id ON ownership_records(tenant_id);

CREATE TABLE lineage_refs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_lineage_refs_tenant_id ON lineage_refs(tenant_id);

CREATE TABLE semantic_tags (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_semantic_tags_tenant_id ON semantic_tags(tenant_id);

CREATE TABLE policy_bindings (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_policy_bindings_tenant_id ON policy_bindings(tenant_id);

CREATE TABLE discovery_queries (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_discovery_queries_tenant_id ON discovery_queries(tenant_id);
```

## 3. API Endpoints

All endpoints have base path `/api/v1/data-catalog` and require `X-Tenant-Id` header.

### Data Assets
- `GET /data-assets` -> Returns List<DataAsset>
- `POST /data-assets` -> Creates DataAsset
- `GET /data-assets/{id}` -> Returns DataAsset
- `PATCH /data-assets/{id}` -> Updates DataAsset
- `POST /data-assets/{id}/validate` -> Validates DataAsset

### Ownership Records
- `GET /ownership-records`
- `POST /ownership-records`
- `GET /ownership-records/{id}`
- `PATCH /ownership-records/{id}`
- `POST /ownership-records/{id}/validate`

### Lineage Refs
- `GET /lineage-refs`
- `POST /lineage-refs`
- `GET /lineage-refs/{id}`
- `PATCH /lineage-refs/{id}`
- `POST /lineage-refs/{id}/validate`

### Semantic Tags
- `GET /semantic-tags`
- `POST /semantic-tags`
- `GET /semantic-tags/{id}`
- `PATCH /semantic-tags/{id}`
- `POST /semantic-tags/{id}/validate`

### Policy Bindings
- `GET /policy-bindings`
- `POST /policy-bindings`
- `GET /policy-bindings/{id}`
- `PATCH /policy-bindings/{id}`
- `POST /policy-bindings/{id}/validate`

### AI & Workflows & Metrics
- `POST /ai/analyze`
- `POST /workflows/execute`
- `GET /metrics/summary`

## 4. Event Contracts

Emits:
- `datacatalogai.asset.registered`
- `datacatalogai.policy.bound`
- `datacatalogai.asset.deprecated`

Consumes:
- `datalineagetracker.link.updated`
- `dataqualityai.drift.detected`
- `datagovernanceos.policy.updated`

## 5. Frontend Structure

Pages:
- `/` - Dashboard with Metrics Summary
- `/data-assets` - List and manage Data Assets
- `/ownership-records` - List and manage Ownership Records
- `/lineage-refs` - List and manage Lineage Refs
- `/semantic-tags` - List and manage Semantic Tags
- `/policy-bindings` - List and manage Policy Bindings

Components:
- `DataAssetList`, `DataAssetForm`
- `OwnershipRecordList`, `OwnershipRecordForm`
- `LineageRefList`, `LineageRefForm`
- `SemanticTagList`, `SemanticTagForm`
- `PolicyBindingList`, `PolicyBindingForm`

## 6. Acceptance Criteria
1. All module slices implemented with API + UI + tests.
2. Strict tenant isolation and RBAC checks on all endpoints.
3. Event emit/consume contracts implemented and validated.
4. OpenAPI includes all endpoints from this spec.
5. Backend and frontend test suites pass in CI.
6. LiteLLM calls are guarded with timeout/retry/circuit-breaker.
7. No TODO or stubbed production logic remains.
