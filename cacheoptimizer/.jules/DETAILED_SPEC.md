# CacheOptimizer Detailed Specification

## Overview
CacheOptimizer is a distributed caching and CDN service that manages cache policies, invalidation, edge distribution, and cache-aside patterns for all microservices.

## Database Schema (PostgreSQL)

```sql
CREATE TABLE cache_policies (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    app_name VARCHAR(255) NOT NULL,
    namespace VARCHAR(255) NOT NULL,
    ttl_seconds BIGINT NOT NULL,
    strategy VARCHAR(50) NOT NULL, -- CACHE_ASIDE, WRITE_THROUGH, WRITE_BEHIND
    compression_enabled BOOLEAN DEFAULT false,
    stale_while_revalidate BOOLEAN DEFAULT false,
    stale_ttl_seconds BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_cache_policies_tenant ON cache_policies(tenant_id);

CREATE TABLE cache_analytics (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    namespace VARCHAR(255) NOT NULL,
    hit_count BIGINT DEFAULT 0,
    miss_count BIGINT DEFAULT 0,
    total_size_bytes BIGINT DEFAULT 0,
    timestamp TIMESTAMP NOT NULL
);

CREATE INDEX idx_cache_analytics_tenant ON cache_analytics(tenant_id);
```

## Backend Services & Endpoints

### 1. `CachePolicyController`
- `POST /api/v1/policies`: Create a new cache policy.
  - **Request**: `CachePolicyDto`
  - **Response**: `CachePolicyDto`
- `GET /api/v1/policies`: List policies.
  - **Response**: `List<CachePolicyDto>`
- `PUT /api/v1/policies/{id}`: Update policy.
- `DELETE /api/v1/policies/{id}`: Delete policy.

### 2. `CacheOperationsController`
- `GET /api/v1/cache/{namespace}/{key}`: Get a cached item.
- `PUT /api/v1/cache/{namespace}/{key}`: Put an item in cache.
- `DELETE /api/v1/cache/{namespace}/{key}`: Invalidate a cached item.
- `POST /api/v1/cache/warm`: Warm up cache.
  - **Request**: `{ namespace: string, keys: string[] }`

### 3. `AnalyticsController`
- `GET /api/v1/analytics`: Get hit rates and statistics.

### Services

- `CachePolicyService`: Manages policies.
- `CacheService`: Core caching logic handling Caffeine (L0), Redis (L1), thundering herd (Redis distributed locks), compression (GZIP), and stale-while-revalidate.
- `AnalyticsService`: Records hits/misses async, aggregates them.
- `LiteLlmService`: Predicts which keys should be preloaded/warmed up based on historical analytics patterns.

## Frontend (Next.js)

- **Pages**:
  - `/`: Dashboard showing overall hit rate, size, and analytics charts.
  - `/policies`: List and manage cache policies.
  - `/policies/new`: Create a new policy.
- **Components**:
  - `StatCard`: Reusable analytics card.
  - `PolicyList`: Data table for policies.
  - `PolicyForm`: Form to edit/create.
  - `AnalyticsChart`: React Query based chart for hits vs misses.

## AI Integration Points
- `LiteLlmPredictionService` will periodically take cache miss logs and predict keys that should be warmed up.

## Error Handling
- Rate limiting for API requests.
- Fallback to database on Redis failure.
- Distributed locking failures will abort the fetch or fallback safely.
