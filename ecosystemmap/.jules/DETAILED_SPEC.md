# EcosystemMap Detailed Specification

## 1. Overview
EcosystemMap is an AI ecosystem visualization platform that provides a real-time map of all deployed apps, the data flows between them, integration health, and compound value metrics. It identifies which integration opportunities are not yet active and shows the true ROI of the ecosystem investment.

## 2. Database Schema (PostgreSQL + Flyway)

`V1__init.sql`

```sql
CREATE TABLE deployed_apps (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    app_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL, -- ACTIVE, INACTIVE, ERROR
    deployed_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_deployed_apps_tenant ON deployed_apps(tenant_id);

CREATE TABLE data_flows (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    source_app_id UUID NOT NULL REFERENCES deployed_apps(id),
    target_app_id UUID NOT NULL REFERENCES deployed_apps(id),
    event_type VARCHAR(255) NOT NULL,
    health_status VARCHAR(50) NOT NULL, -- HEALTHY, DEGRADED, FAILING
    last_event_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_data_flows_tenant ON data_flows(tenant_id);

CREATE TABLE value_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    metric_name VARCHAR(255) NOT NULL, -- e.g., "Hours Saved", "Revenue Generated"
    metric_value NUMERIC(10, 2) NOT NULL,
    currency VARCHAR(10),
    calculated_at TIMESTAMP NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_value_metrics_tenant ON value_metrics(tenant_id);

CREATE TABLE integration_opportunities (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    source_app VARCHAR(255) NOT NULL,
    target_app VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    potential_value TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX idx_integration_opps_tenant ON integration_opportunities(tenant_id);
```

## 3. Backend (Spring Boot 3.3.5)

### Entities
- `DeployedApp`: id, tenantId, appId, name, status, deployedAt
- `DataFlow`: id, tenantId, sourceApp, targetApp, eventType, healthStatus, lastEventAt
- `ValueMetric`: id, tenantId, metricName, metricValue, currency, calculatedAt, description
- `IntegrationOpportunity`: id, tenantId, sourceApp, targetApp, description, potentialValue

### Repositories
- `DeployedAppRepository`: `findByTenantId`, `findByTenantIdAndAppId`
- `DataFlowRepository`: `findByTenantId`, `findByTenantIdAndSourceAppId`
- `ValueMetricRepository`: `findByTenantId`, `findTop5ByTenantIdOrderByCalculatedAtDesc`
- `IntegrationOpportunityRepository`: `findByTenantId`

### Services
- `EcosystemService`:
  - `getEcosystemMap(UUID tenantId)`: Returns nodes (apps) and edges (data flows).
  - `registerApp(UUID tenantId, AppDto appDto)`
  - `recordDataFlow(UUID tenantId, DataFlowDto flowDto)`
- `MetricsService`:
  - `calculateCompoundValue(UUID tenantId)`
  - `getMetrics(UUID tenantId)`
- `AiInsightsService`:
  - `analyzeEcosystem(UUID tenantId)`: Uses LiteLLM to suggest integration opportunities and estimate ROI.

### REST Endpoints
- `GET /api/v1/ecosystem/map` - Get the ecosystem visualization data (nodes & edges).
- `GET /api/v1/ecosystem/metrics` - Get ROI and compound value metrics.
- `GET /api/v1/ecosystem/opportunities` - Get AI-generated integration opportunities.
- `POST /api/v1/ecosystem/apps` - Register a newly deployed app.
- `POST /api/v1/ecosystem/flows` - Record a data flow / event between apps.
- `POST /api/v1/ecosystem/analyze` - Trigger an AI analysis of the ecosystem.

### AI Integration
- Client: LiteLLM via REST.
- Prompt: "Given the following deployed apps and their data flows: {data}. Suggest new integration opportunities and estimate potential ROI in hours saved or revenue."

## 4. Frontend (Next.js App Router, Tailwind, TS)

### Pages
- `/` (Dashboard): Shows the ecosystem map, key metrics, and opportunities.
- `/apps`: List of all deployed apps and their statuses.
- `/opportunities`: Detailed view of integration opportunities.

### Components
- `EcosystemMap`: A visual graph of apps (nodes) and flows (edges) using a library like `react-flow-renderer`.
- `MetricsCards`: Displays Total ROI, Hours Saved, etc.
- `OpportunitiesList`: List of AI-generated insights.
- `AppStatusList`: Table of apps and their health statuses.

## 5. Acceptance Criteria
- [ ] Users can view a visual map of deployed apps and their data flows.
- [ ] Users can see compound value metrics (e.g., hours saved).
- [ ] Users can see AI-suggested integration opportunities.
- [ ] The system accurately records and updates app health and data flow status.

## 6. Integration Manifest
```json
{
  "app": "ecosystemmap",
  "display_name": "EcosystemMap",
  "description": "AI ecosystem visualization platform and ROI tracker.",
  "emits": [],
  "consumes": [
    "app.deployed",
    "app.undeployed",
    "data.flow.event"
  ],
  "capabilities": [
    "ecosystem_visualization",
    "roi_tracking",
    "integration_discovery"
  ]
}
```
