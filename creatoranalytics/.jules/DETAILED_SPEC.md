# CreatorAnalytics Detailed Spec

## 1. Database Schema (Flyway V1__init.sql)

```sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE content_assets (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_content_assets_tenant_id ON content_assets(tenant_id);

CREATE TABLE channel_metrics (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_channel_metrics_tenant_id ON channel_metrics(tenant_id);

CREATE TABLE attribution_models (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_attribution_models_tenant_id ON attribution_models(tenant_id);

CREATE TABLE roi_snapshots (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_roi_snapshots_tenant_id ON roi_snapshots(tenant_id);

CREATE TABLE audience_segments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_audience_segments_tenant_id ON audience_segments(tenant_id);

CREATE TABLE performance_insights (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL,
    name VARCHAR(180) NOT NULL,
    status VARCHAR(40) NOT NULL,
    metadata_json JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_performance_insights_tenant_id ON performance_insights(tenant_id);
```

## 2. API Endpoints

### Content Assets
- `GET /api/v1/creator-analytics/content-assets` (Returns List<ContentAsset>)
- `POST /api/v1/creator-analytics/content-assets` (Creates ContentAsset)
- `GET /api/v1/creator-analytics/content-assets/{id}` (Returns ContentAsset)
- `PATCH /api/v1/creator-analytics/content-assets/{id}` (Updates ContentAsset)
- `POST /api/v1/creator-analytics/content-assets/{id}/validate` (Validates asset)

### Channel Metrics
- `GET /api/v1/creator-analytics/channel-metrics`
- `POST /api/v1/creator-analytics/channel-metrics`
- `GET /api/v1/creator-analytics/channel-metrics/{id}`
- `PATCH /api/v1/creator-analytics/channel-metrics/{id}`
- `POST /api/v1/creator-analytics/channel-metrics/{id}/validate`

### Attribution Models
- `GET /api/v1/creator-analytics/attribution-models`
- `POST /api/v1/creator-analytics/attribution-models`
- `GET /api/v1/creator-analytics/attribution-models/{id}`
- `PATCH /api/v1/creator-analytics/attribution-models/{id}`
- `POST /api/v1/creator-analytics/attribution-models/{id}/validate`

### ROI Snapshots
- `GET /api/v1/creator-analytics/roisnapshots`
- `POST /api/v1/creator-analytics/roisnapshots`
- `GET /api/v1/creator-analytics/roisnapshots/{id}`
- `PATCH /api/v1/creator-analytics/roisnapshots/{id}`
- `POST /api/v1/creator-analytics/roisnapshots/{id}/validate`

### Audience Segments
- `GET /api/v1/creator-analytics/audience-segments`
- `POST /api/v1/creator-analytics/audience-segments`
- `GET /api/v1/creator-analytics/audience-segments/{id}`
- `PATCH /api/v1/creator-analytics/audience-segments/{id}`
- `POST /api/v1/creator-analytics/audience-segments/{id}/validate`

### Custom & Workflows
- `POST /api/v1/creator-analytics/ai/analyze` (LiteLLM call guarded with CircuitBreaker/Retry)
- `POST /api/v1/creator-analytics/workflows/execute` (Triggers background workflow)
- `GET /api/v1/creator-analytics/metrics/summary` (Returns aggregated summary)

## 3. Frontend Pages & Components
- **Pages**:
  - `/creator-analytics/dashboard` (Summary + Metrics)
  - `/creator-analytics/content-assets` (CRUD list)
  - `/creator-analytics/roisnapshots` (CRUD list)
  - `/creator-analytics/audience-segments` (CRUD list)
- **Components**:
  - `ContentAssetForm`
  - `ContentAssetTable`
  - `ROIChart`
  - `AudienceSegmentList`

## 4. Acceptance Criteria & Tests
- Backend Unit Tests (≥80% coverage on Services)
- Frontend Component Tests (Vitest)
- OpenAPI Specification coverage for all endpoints
