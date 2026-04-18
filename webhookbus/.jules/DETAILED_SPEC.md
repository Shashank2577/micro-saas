# Webhook Bus Detailed Specification

## Overview
Webhook Bus is an enterprise webhook gateway designed to receive, validate, route, and deliver webhook events. It ensures guaranteed delivery, retry mechanisms, and observability for asynchronous event-driven architectures.

## Domain Model (Database Schema)

### 1. `webhook_endpoints`
Represents a configured destination for webhook events.

```sql
CREATE TABLE webhook_endpoints (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(1024) NOT NULL,
    secret VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL, -- ACTIVE, INACTIVE
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_webhook_endpoints_tenant ON webhook_endpoints(tenant_id);
```

### 2. `webhook_events`
Represents an incoming webhook event payload.

```sql
CREATE TABLE webhook_events (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    source VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    payload JSONB NOT NULL,
    received_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_webhook_events_tenant ON webhook_events(tenant_id);
CREATE INDEX idx_webhook_events_received ON webhook_events(received_at);
```

### 3. `webhook_deliveries`
Represents an attempt to deliver an event to a specific endpoint.

```sql
CREATE TABLE webhook_deliveries (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    event_id UUID NOT NULL REFERENCES webhook_events(id),
    endpoint_id UUID NOT NULL REFERENCES webhook_endpoints(id),
    status VARCHAR(50) NOT NULL, -- PENDING, SUCCESS, FAILED
    status_code INTEGER,
    attempt_count INTEGER DEFAULT 0,
    last_attempt_at TIMESTAMP WITH TIME ZONE,
    next_attempt_at TIMESTAMP WITH TIME ZONE,
    response_body TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_webhook_deliveries_tenant ON webhook_deliveries(tenant_id);
CREATE INDEX idx_webhook_deliveries_event ON webhook_deliveries(event_id);
CREATE INDEX idx_webhook_deliveries_endpoint ON webhook_deliveries(endpoint_id);
CREATE INDEX idx_webhook_deliveries_status ON webhook_deliveries(status);
```

## API Specifications

### REST Endpoints

1. **POST `/api/v1/endpoints`**
   - Purpose: Register a new webhook endpoint.
   - Request Body: `{ "name": "...", "url": "...", "secret": "..." }`
   - Response: `Endpoint` object.

2. **GET `/api/v1/endpoints`**
   - Purpose: List endpoints for the tenant.
   - Response: `List<Endpoint>`

3. **POST `/api/v1/ingest`**
   - Purpose: Receive incoming webhook payloads.
   - Headers: Optional `X-Webhook-Source`, `X-Webhook-Event-Type`
   - Request Body: Any valid JSON.
   - Response: `202 Accepted` with `Event` ID.
   - Behavior: Creates an `Event` record, then asynchronously creates `Delivery` records for all ACTIVE endpoints.

4. **GET `/api/v1/events`**
   - Purpose: List received events.
   - Query Params: `page`, `size`
   - Response: Paginated `Event` objects.

5. **GET `/api/v1/deliveries`**
   - Purpose: List delivery attempts.
   - Query Params: `eventId`, `endpointId`, `status`, `page`, `size`
   - Response: Paginated `Delivery` objects.

6. **POST `/api/v1/deliveries/{id}/replay`**
   - Purpose: Manually retry a failed delivery.
   - Response: `202 Accepted`

## Services & Business Logic

1. **`EndpointService`**: CRUD operations for `webhook_endpoints`.
2. **`IngestionService`**: Handles `/api/v1/ingest`. Validates payload, saves `webhook_events`, and triggers delivery scheduling.
3. **`DeliveryService`**: Executes HTTP POST to the endpoint URL. Calculates HMAC signature using the endpoint secret and adds it to `X-Signature` header. Updates `webhook_deliveries` status, response code, and schedules next attempt if failed (exponential backoff).
4. **`EventService`**: Retrieves events and deliveries.

## Frontend Application

### Pages

1. **Dashboard (`/`)**
   - High-level metrics: Total events received, success vs. failure rates.
   - Recent activity feed.

2. **Endpoints (`/endpoints`)**
   - List view of configured endpoints.
   - "Add Endpoint" modal/form.
   - View endpoint details and generated secret.

3. **Events & Deliveries (`/events`)**
   - Table of incoming events.
   - Expandable rows to see delivery attempts for each event.
   - "Replay" button for failed deliveries.
   - Payload JSON viewer.

## Integrations

- **LiteLLM**: Optional AI categorization of incoming unknown event types (for future enhancement).
- **Nexus Hub**: Emits `webhook.received` and `webhook.delivered` events to the central event bus.

## Testing Strategy
- **Backend**: 
  - Unit tests for services with mocked repository and `TenantContext`.
  - Mock `RestTemplate` for `DeliveryService` to test success, failure, and backoff logic.
  - Controller tests mocking services.
- **Frontend**:
  - Component rendering tests.
  - Mock API responses for data loading.
