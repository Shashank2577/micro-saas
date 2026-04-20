# SupportIntelligence App Specification

## 1. Overview
SupportIntelligence is an AI support co-pilot. It gives support agents AI-suggested responses grounded in knowledge base, past tickets, and product docs. It detects escalation signals, mines ticket patterns for product issues, measures deflection rate, and tracks agent efficiency.

## 2. Dependencies
*   Backend: Spring Boot 3.3.5 (Java 21), PostgreSQL 16 (with pgvector), cc-starter, LiteLLM
*   Frontend: Next.js 14, TypeScript, Tailwind CSS, React Real-time
*   External Integrations: Zendesk, Intercom, Freshdesk, Slack, GitHub

## 3. Database Schema

```sql
CREATE TABLE support_tickets (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    external_ticket_id VARCHAR(255),
    category VARCHAR(255),
    urgency VARCHAR(50),
    sentiment_score FLOAT,
    status VARCHAR(50),
    resolved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE response_suggestions (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    ticket_id UUID NOT NULL REFERENCES support_tickets(id),
    suggested_text TEXT,
    confidence_score FLOAT,
    accepted_by_agent BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE escalation_signals (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    ticket_id UUID NOT NULL REFERENCES support_tickets(id),
    signal_type VARCHAR(255),
    severity VARCHAR(50),
    escalated_to VARCHAR(255),
    resolved_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ticket_patterns (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    pattern_type VARCHAR(255),
    occurrence_count INT,
    first_seen TIMESTAMP,
    last_seen TIMESTAMP,
    product_issue_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_issues (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255),
    description TEXT,
    ticket_count INT,
    status VARCHAR(50),
    assigned_to_product_team VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE agent_metrics (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    agent_id UUID NOT NULL,
    period VARCHAR(50),
    tickets_resolved INT,
    avg_resolution_time FLOAT,
    csat_score FLOAT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Additional table for Knowledge Base if needed, keeping simple for now
CREATE TABLE knowledge_base (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    title VARCHAR(255),
    content TEXT,
    embedding VECTOR(1536),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 4. API Endpoints
*   `GET /api/tickets` - List tickets
*   `GET /api/tickets/{id}` - Get ticket details
*   `POST /api/tickets/{id}/suggestions` - Generate a response suggestion
*   `POST /api/tickets/{id}/suggestions/{suggestionId}/accept` - Mark suggestion as accepted
*   `GET /api/escalations` - List escalation signals
*   `GET /api/patterns` - List ticket patterns
*   `GET /api/product-issues` - List product issues
*   `GET /api/metrics` - List agent metrics

## 5. Services
*   `ResponseSuggestionService`: Calls LiteLLM for suggestions
*   `KnowledgeBaseService`: Manages KB articles and embeddings
*   `EscalationDetectionService`: Detects escalation signals
*   `TicketClassificationService`: Classifies tickets
*   `SentimentAnalysisService`: Extracts sentiment
*   `PatternMiningService`: Mines patterns
*   `ProductInsightService`: Aggregates issues
*   `AgentProductivityService`: Calculates metrics
*   `TicketIntegrationService`: Syncs with platforms

## 6. Frontend Pages
*   `/dashboard`: Agent metrics and summary
*   `/tickets`: Ticket list
*   `/tickets/[id]`: Ticket detail with AI suggestions side-panel
*   `/escalations`: Escalation signals
*   `/insights`: Ticket patterns and product issues

## 7. Plan
1.  Setup base Spring Boot + Next.js structure.
2.  Add dependencies (cc-starter, flyway, pgvector, LiteLLM, WebSocket).
3.  Implement Backend entities, repositories, and services.
4.  Implement Backend controllers and API documentation.
5.  Implement Frontend pages and components.
6.  Test and verify.
