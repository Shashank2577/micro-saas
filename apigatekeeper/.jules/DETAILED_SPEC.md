# APIGatekeeper Detailed Specification

## Overview

APIGatekeeper is an API gateway and routing service designed to route traffic from 100+ microservices, enforce rate limiting, manage API versioning, perform request/response transformations, and provide API marketplace features.

### Core Requirements
1. HTTP/gRPC request routing with service discovery integration.
2. API versioning management (v1, v2, semantic versioning).
3. Rate limiting per tenant/user/API key (sliding window, token bucket).
4. Request/response transformation and schema validation.
5. API key and OAuth2 token validation integration.
6. CORS policy management per API endpoint.
7. Request logging with latency tracking and error sampling.
8. Load balancing strategies (round-robin, least-connection, weighted).
9. Circuit breaker pattern for downstream service resilience.
10. Request/response caching with cache invalidation strategies.
11. API throttling with burst allowance.
12. Request authentication and authorization delegation.
13. Webhook routing and retry logic for event delivery.
14. API traffic analytics and usage reporting.

### Constraints
1. Gateway must route 1,000,000 requests/day per tenant.
2. Maximum request/response payload: 10MB.
3. Rate limit: configurable per API (default 100 req/min per user).
4. Request timeout: 30 seconds default, configurable per endpoint.
5. Cache TTL: configurable, max 24 hours.
6. Circuit breaker threshold: 50% error rate, 5 consecutive errors trips circuit.
7. Circuit recovery: exponential backoff, max 60 seconds between retries.
8. All requests include `X-Request-ID` correlation tracking.
9. API versioning: sunset notifications 30 days before v1 -> v2 migration.

---

## Database Schema (PostgreSQL)

```sql
-- V1__init.sql

CREATE TABLE routes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    route_id VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    method VARCHAR(50) NOT NULL,
    target_url TEXT NOT NULL,
    strip_prefix INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_routes_tenant_id ON routes(tenant_id);

CREATE TABLE api_versions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    api_name VARCHAR(255) NOT NULL,
    version VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL, -- ACTIVE, DEPRECATED, SUNSET
    sunset_date TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_api_versions_tenant_id ON api_versions(tenant_id);

CREATE TABLE rate_limit_policies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    replenish_rate INTEGER NOT NULL,
    burst_capacity INTEGER NOT NULL,
    requested_tokens INTEGER DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_rate_limit_policies_tenant_id ON rate_limit_policies(tenant_id);

CREATE TABLE cache_policies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    ttl_seconds INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_cache_policies_tenant_id ON cache_policies(tenant_id);

CREATE TABLE webhooks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    target_url TEXT NOT NULL,
    secret VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_webhooks_tenant_id ON webhooks(tenant_id);

CREATE TABLE webhook_deliveries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    webhook_id UUID NOT NULL REFERENCES webhooks(id),
    payload TEXT NOT NULL,
    status VARCHAR(50) NOT NULL, -- PENDING, SUCCESS, FAILED
    retry_count INTEGER DEFAULT 0,
    next_retry_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_webhook_deliveries_tenant_id ON webhook_deliveries(tenant_id);

CREATE TABLE request_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    request_id VARCHAR(255) NOT NULL,
    method VARCHAR(50) NOT NULL,
    path VARCHAR(1000) NOT NULL,
    status_code INTEGER NOT NULL,
    latency_ms BIGINT NOT NULL,
    user_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_request_logs_tenant_id ON request_logs(tenant_id);

CREATE TABLE analytics_snapshots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    total_requests BIGINT NOT NULL,
    error_count BIGINT NOT NULL,
    p50_latency_ms DOUBLE PRECISION NOT NULL,
    p95_latency_ms DOUBLE PRECISION NOT NULL,
    p99_latency_ms DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
CREATE INDEX idx_analytics_snapshots_tenant_id ON analytics_snapshots(tenant_id);
```

## REST API Endpoints

### 1. Route Management
*   **POST /gateway/routes**: Create a new route.
    *   Request Body: `{"routeId": "user-service", "path": "/api/v1/users/**", "method": "GET", "targetUrl": "http://user-service:8201", "stripPrefix": 1}`
    *   Response: `201 Created`
*   **GET /gateway/routes**: Get all routes.
    *   Response: `200 OK` (Array of Routes)
*   **GET /gateway/routes/{id}**: Get a route by ID.
    *   Response: `200 OK` (Route)
*   **PUT /gateway/routes/{id}**: Update a route.
    *   Request Body: Route details.
    *   Response: `200 OK`
*   **DELETE /gateway/routes/{id}**: Delete a route.
    *   Response: `204 No Content`

### 2. Rate Limiting
*   **POST /ratelimit/policies**: Create a rate limit policy.
    *   Request Body: `{"name": "Standard", "replenishRate": 100, "burstCapacity": 150}`
    *   Response: `201 Created`
*   **GET /ratelimit/policies**: Get all policies.
*   **DELETE /ratelimit/policies/{id}**: Delete a policy.

### 3. API Versioning
*   **POST /versioning/api-versions**: Create an API version record.
    *   Request Body: `{"apiName": "User API", "version": "v1", "status": "ACTIVE"}`
    *   Response: `201 Created`
*   **GET /versioning/api-versions**: Get all API versions.

### 4. Webhook Management
*   **POST /webhooks/register**: Register a webhook.
    *   Request Body: `{"eventType": "user.created", "targetUrl": "..."}`
    *   Response: `201 Created`
*   **GET /webhooks**: Get all webhooks.
*   **GET /webhooks/{id}/deliveries**: Get deliveries for a webhook.

### 5. Analytics
*   **GET /analytics/traffic**: Get traffic summary (total, errors, p50, p95, p99).
*   **GET /analytics/top-apis**: Get most accessed paths.

## React Components (Next.js)

1.  **Dashboard**: Main overview, showing analytics charts (ECharts) for traffic, errors, and latency.
2.  **RoutesList**: Table displaying all configured routes with actions to edit/delete.
3.  **RouteForm**: Modal/page to create or edit a route configuration.
4.  **RateLimitPoliciesList**: Table of rate limit configurations.
5.  **WebhooksList**: Table of registered webhooks and their delivery status.

## AI Integration Points
We will implement an AI assistant to help configure complex routes or suggest rate-limiting strategies based on descriptions.
*   LiteLLM integration to analyze a natural language description (e.g., "Route all user traffic to user-service but drop unauthenticated requests") and output a valid JSON route configuration.

## Acceptance Criteria Handled
*   All endpoints will have corresponding controllers and tests.
*   Spring Cloud Gateway configurations will be updated dynamically from the database using a custom `RouteDefinitionLocator`.
*   Resilience4j will be configured for circuit breaking and rate limiting in the backend application.
