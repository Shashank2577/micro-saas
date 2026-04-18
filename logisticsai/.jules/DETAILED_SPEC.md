# LogisticsAI - Detailed Specification

## 1. Overview
LogisticsAI is an AI logistics intelligence platform designed to improve carrier selection, route optimization, and inventory positioning using real-time conditions and predictive models.

**Key Features:**
- Carrier performance monitoring with on-time prediction
- Demand forecasting for inventory positioning
- Route optimization with real-time exception handling
- AI Agent for exception management (routes issues to the right person with context and recommended action)

## 2. Database Schema

```sql
CREATE TABLE carrier_performance (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    carrier_name VARCHAR(255) NOT NULL,
    on_time_rate DECIMAL(5,2),
    predicted_delay_mins INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE routes (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    origin VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    estimated_arrival TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE exceptions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    route_id UUID REFERENCES routes(id),
    description TEXT,
    severity VARCHAR(50),
    recommended_action TEXT,
    status VARCHAR(50) NOT NULL,
    assigned_to VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 3. REST API Endpoints

### Carrier Performance API
- `GET /api/carriers`: List all carriers with performance metrics
- `POST /api/carriers`: Add a new carrier
- `GET /api/carriers/{id}`: Get specific carrier details

### Route Optimization API
- `GET /api/routes`: List all active routes
- `POST /api/routes`: Create a new route
- `POST /api/routes/{id}/optimize`: AI-driven route optimization

### Exception Management API (AI Agent)
- `GET /api/exceptions`: List all exceptions
- `POST /api/exceptions`: Report an exception (triggers AI analysis)
- `PUT /api/exceptions/{id}/resolve`: Mark exception as resolved

## 4. Frontend React Components

### Pages
- `/dashboard`: High-level view of carrier performance and active exceptions
- `/routes`: Route management and map view
- `/exceptions`: Exception management and AI recommendations

### Components
- `CarrierList`: Table showing carriers and on-time predictions
- `RouteOptimizer`: Form to input origin/destination and show AI optimized path
- `ExceptionAgent`: Chat/Action interface for handling logistics exceptions

## 5. AI Integrations
- **LiteLLM**: Used in the exception management pipeline to analyze descriptions, classify severity, and generate recommended actions.

## 6. Acceptance Criteria
1. Users can view and manage carriers and their predicted performance.
2. Users can create routes and trigger AI optimization.
3. Exceptions trigger AI agent analysis to suggest recommended actions.
4. All endpoints respect tenant isolation.
5. Service layer has ≥80% test coverage.
