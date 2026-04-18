# Detailed Specification: TelemetryCore

## 1. Overview
TelemetryCore is a centralized analytics and telemetry service. It collects, aggregates, and reports on usage metrics, user behavior, feature adoption, and custom events for all microservices across the multi-tenant SaaS ecosystem.

## 2. Database Schema (PostgreSQL)

### Table: `events`
*   `id` (UUID, Primary Key)
*   `tenant_id` (UUID, Indexed) - Multi-tenant isolation
*   `user_id` (String, Indexed) - Optional, for user-specific events
*   `session_id` (String, Indexed) - For session replay
*   `event_name` (String, Indexed) - e.g., 'page_view', 'button_click'
*   `properties` (JSONB) - Custom event properties (e.g., page_path, referrer, feature_name, variant)
*   `timestamp` (Timestamp with Time Zone, Indexed) - Event time

### Table: `metrics`
*   `id` (UUID, Primary Key)
*   `tenant_id` (UUID, Indexed)
*   `name` (String) - e.g., 'API calls per day'
*   `description` (String)
*   `aggregation_type` (String) - e.g., 'COUNT', 'SUM', 'AVG'
*   `created_at` (Timestamp)
*   `updated_at` (Timestamp)

### Table: `cohorts`
*   `id` (UUID, Primary Key)
*   `tenant_id` (UUID, Indexed)
*   `name` (String) - e.g., 'High Value Users'
*   `criteria` (JSONB) - Defines the rules for the cohort (e.g., revenue > 1000)
*   `created_at` (Timestamp)
*   `updated_at` (Timestamp)

### Table: `funnels`
*   `id` (UUID, Primary Key)
*   `tenant_id` (UUID, Indexed)
*   `name` (String) - e.g., 'Signup to Subscription'
*   `steps` (JSONB) - Array of event names representing funnel steps
*   `created_at` (Timestamp)
*   `updated_at` (Timestamp)

### Table: `experiments` (A/B Tests)
*   `id` (UUID, Primary Key)
*   `tenant_id` (UUID, Indexed)
*   `name` (String) - e.g., 'new_checkout'
*   `variants` (JSONB) - e.g., ['control', 'treatment']
*   `allocation_percentage` (Integer) - Percentage of users in experiment
*   `status` (String) - 'ACTIVE', 'COMPLETED'
*   `created_at` (Timestamp)
*   `updated_at` (Timestamp)

### Table: `alerts`
*   `id` (UUID, Primary Key)
*   `tenant_id` (UUID, Indexed)
*   `metric_id` (UUID, Foreign Key)
*   `condition` (String) - e.g., '<', '>', '=='
*   `threshold` (Double)
*   `notification_channel` (String) - e.g., 'email', 'webhook'
*   `created_at` (Timestamp)
*   `updated_at` (Timestamp)

## 3. REST API Endpoints

### Event Collection
*   **POST** `/api/v1/events`
    *   **Description**: Ingests one or more events.
    *   **Headers**: `X-Tenant-ID`
    *   **Body**: `{ events: [{ eventName: "page_view", userId: "u1", sessionId: "s1", properties: { page_path: "/home", referrer: "google" } }] }`
    *   **Response**: `202 Accepted`

### Metrics
*   **POST** `/api/v1/metrics`
    *   **Description**: Define a new custom metric.
    *   **Body**: `{ name: "API calls per day", description: "Daily API calls", aggregationType: "COUNT" }`
    *   **Response**: `201 Created`
*   **GET** `/api/v1/metrics/{id}/data`
    *   **Description**: Get aggregated data for a metric.
    *   **Query Params**: `startDate`, `endDate`, `interval`
    *   **Response**: `200 OK` (JSON array of data points)

### Cohorts
*   **POST** `/api/v1/cohorts`
    *   **Description**: Create a new cohort.
    *   **Body**: `{ name: "High revenue", criteria: { field: "revenue", operator: ">", value: 1000 } }`
    *   **Response**: `201 Created`
*   **GET** `/api/v1/cohorts/{id}/users`
    *   **Description**: Get users matching a cohort.

### Funnels
*   **POST** `/api/v1/funnels`
    *   **Description**: Define a new funnel.
    *   **Body**: `{ name: "Signup to Subscribe", steps: ["signup", "email_verified", "first_purchase", "subscription"] }`
*   **GET** `/api/v1/funnels/{id}/analysis`
    *   **Description**: Analyze funnel conversion rates.
    *   **Response**: `200 OK` (Conversion percentages between steps)

### Experiments
*   **POST** `/api/v1/experiments`
    *   **Description**: Create A/B test.
    *   **Body**: `{ name: "new_checkout", variants: ["A", "B"], allocationPercentage: 50 }`
*   **GET** `/api/v1/experiments/{id}/results`
    *   **Description**: Get experiment variant performance.

### Session Replay
*   **GET** `/api/v1/sessions/{sessionId}`
    *   **Description**: Get event timeline for a session.

### Alerts
*   **POST** `/api/v1/alerts`
*   **GET** `/api/v1/alerts`

### Data Export & Privacy
*   **GET** `/api/v1/export`
    *   **Description**: Export events as CSV.
    *   **Query Params**: `startDate`, `endDate`, `format=csv`
*   **DELETE** `/api/v1/privacy/users/{userId}`
    *   **Description**: GDPR compliance data deletion.
    *   **Response**: `204 No Content`

## 4. Frontend Components (Next.js)

### Pages
*   `/dashboard`: Real-time active users and key metrics overview.
*   `/events`: Raw event stream viewer.
*   `/metrics`: Custom metrics definition and charts.
*   `/cohorts`: Cohort builder and user list.
*   `/funnels`: Funnel creation and visualization.
*   `/experiments`: A/B test management and results.
*   `/sessions`: Session replay viewer.
*   `/alerts`: Alert configuration.

### Components
*   `RealtimeMetricCard`: Shows active users (polls or WebSocket).
*   `FunnelChart`: Visualizes drop-off rates.
*   `ExperimentResultsTable`: Shows variant performance.
*   `EventTimeline`: For session replay.

## 5. AI Integration
*   Use LiteLLM (`LiteLLMClient`) to analyze metric trends and detect anomalies, suggesting potential root causes based on event properties.

## 6. Testing Strategy
*   Unit tests for services (`EventService`, `MetricService`, `FunnelService`, etc.) ensuring at least 80% coverage.
*   Integration tests for all controllers using `@SpringBootTest`.
*   Frontend component tests with `vitest` and `@testing-library/react`.
*   Verify all acceptance criteria.
