# ObservabilityStack Detailed Specification

## 1. Overview
ObservabilityStack is a monitoring and observability platform. It aggregates logs, metrics, traces, and health checks across all microservices with alerting, incident management, and SLA tracking.

## 2. Database Schema (PostgreSQL)

```sql
CREATE TABLE health_checks (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    service_name VARCHAR(255) NOT NULL,
    endpoint VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    last_checked_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE alert_rules (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    condition VARCHAR(500) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE alerts (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    rule_id UUID REFERENCES alert_rules(id),
    status VARCHAR(50) NOT NULL,
    triggered_at TIMESTAMP NOT NULL,
    resolved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE alert_history (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    alert_id UUID REFERENCES alerts(id),
    status VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

CREATE TABLE incidents (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL,
    severity VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    resolved_at TIMESTAMP
);

CREATE TABLE incident_timelines (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    incident_id UUID REFERENCES incidents(id),
    message TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

CREATE TABLE runbooks (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE slos (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    target_percentage DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE slo_compliance (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    slo_id UUID REFERENCES slos(id),
    compliance_percentage DOUBLE PRECISION NOT NULL,
    measured_at TIMESTAMP NOT NULL
);

CREATE TABLE oncall_schedules (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE dashboards (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE dashboard_queries (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    dashboard_id UUID REFERENCES dashboards(id),
    query_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

## 3. Backend Implementation

### Entities
- `HealthCheck`: id, tenantId, serviceName, endpoint, status, lastCheckedAt, createdAt, updatedAt
- `AlertRule`: id, tenantId, name, condition, severity, createdAt, updatedAt
- `Alert`: id, tenantId, rule (ManyToOne), status, triggeredAt, resolvedAt, createdAt, updatedAt
- `AlertHistory`: id, tenantId, alert (ManyToOne), status, timestamp
- `Incident`: id, tenantId, title, description, status, severity, createdAt, updatedAt, resolvedAt
- `IncidentTimeline`: id, tenantId, incident (ManyToOne), message, timestamp
- `Runbook`: id, tenantId, name, content, createdAt, updatedAt
- `SLO`: id, tenantId, name, targetPercentage, createdAt, updatedAt
- `SLOCompliance`: id, tenantId, slo (ManyToOne), compliancePercentage, measuredAt
- `OnCallSchedule`: id, tenantId, name, userId, startTime, endTime, createdAt, updatedAt
- `Dashboard`: id, tenantId, name, createdAt, updatedAt
- `DashboardQuery`: id, tenantId, dashboard (ManyToOne), queryText, createdAt, updatedAt

### REST API Endpoints
**Logs**
- `POST /api/logs/ingest`
- `GET /api/logs/search`
- `GET /api/logs/{logId}`
- `POST /api/logs/export`

**Metrics**
- `POST /api/metrics/push`
- `GET /api/metrics/query`
- `GET /api/metrics/range`
- `GET /api/metrics/labels`

**Traces**
- `POST /api/traces/ingest`
- `GET /api/traces/{traceId}`
- `GET /api/traces/search`
- `GET /api/traces/{traceId}/waterfall`

**Health Checks**
- `POST /api/health/checks`
- `GET /api/health/checks`
- `GET /api/health/checks/{checkId}`
- `GET /api/health/status`

**Alerts**
- `POST /api/alerts`
- `GET /api/alerts`
- `PUT /api/alerts/{alertId}`
- `DELETE /api/alerts/{alertId}`
- `GET /api/alerts/{alertId}/history`

**Incidents**
- `POST /api/incidents`
- `GET /api/incidents`
- `GET /api/incidents/{incidentId}`
- `PUT /api/incidents/{incidentId}`
- `POST /api/incidents/{incidentId}/escalate`

**SLOs**
- `POST /api/slos`
- `GET /api/slos`
- `GET /api/slos/{sloId}`
- `GET /api/slos/{sloId}/compliance`

**On-Call**
- `POST /api/oncall/schedules`
- `GET /api/oncall/schedules`
- `GET /api/oncall/current-on-call`
- `POST /api/oncall/escalate`

**Dashboards**
- `POST /api/dashboards`
- `GET /api/dashboards`
- `PUT /api/dashboards/{dashboardId}`
- `DELETE /api/dashboards/{dashboardId}`

### AI Integration
- `LiteLLM` will be used for anomaly detection and alert suggestions in the background or during log search context enrichment.

## 4. Frontend Implementation

### Next.js Pages
- `/`: Dashboard overview
- `/logs`: Log search UI
- `/metrics`: Metrics query UI
- `/traces`: Tracing waterfall view
- `/alerts`: Alert management UI
- `/incidents`: Incident response UI
- `/slos`: SLA/SLO tracking UI
- `/oncall`: On-call schedules

### Components
- `LogSearchPanel`
- `MetricsChart`
- `TraceWaterfall`
- `IncidentList`
- `RunbookViewer` (Markdown rendered)
- `HealthStatusGrid`

## 5. Integration Manifest
`observabilitystack/integration-manifest.json` will export these capabilities.
