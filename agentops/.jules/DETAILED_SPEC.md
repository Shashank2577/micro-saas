# AgentOps Detailed Specification

## 1. Overview
**App Name:** agentops
**Port:** 8080
**Description:** AI agent observability and governance platform. Captures agent runs, tool calls, costs, latencies, and handles human escalations.
**Monetization:** $199/m up to 10k runs, $799/m unlimited.

## 2. Database Schema (PostgreSQL)

```sql
CREATE TABLE agent_run (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    agent_id VARCHAR(255) NOT NULL,
    workflow_id VARCHAR(255),
    started_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    status VARCHAR(50) NOT NULL, -- RUNNING, COMPLETED, FAILED, ESCALATED
    token_cost DECIMAL(10, 6) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE agent_step (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    run_id UUID NOT NULL REFERENCES agent_run(id) ON DELETE CASCADE,
    step_type VARCHAR(50) NOT NULL, -- LLM_CALL, TOOL_CALL, HUMAN_ESCALATION
    input TEXT,
    output TEXT,
    duration_ms BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE human_escalation (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    run_id UUID NOT NULL REFERENCES agent_run(id) ON DELETE CASCADE,
    reason TEXT NOT NULL,
    context TEXT,
    assigned_to VARCHAR(255),
    resolved_at TIMESTAMP,
    resolution TEXT,
    status VARCHAR(50) NOT NULL, -- PENDING, IN_PROGRESS, RESOLVED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE agent_health_metric (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    agent_id VARCHAR(255) NOT NULL,
    period_start TIMESTAMP NOT NULL,
    period_end TIMESTAMP NOT NULL,
    success_rate DECIMAL(5, 2),
    avg_cost DECIMAL(10, 6),
    avg_latency_ms BIGINT,
    escalation_rate DECIMAL(5, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cost_allocation (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    agent_id VARCHAR(255) NOT NULL,
    team_id VARCHAR(255),
    product_feature VARCHAR(255),
    period VARCHAR(50), -- e.g., '2026-04'
    total_cost DECIMAL(10, 6) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 3. REST API Endpoints

### Agent Runs
- `POST /api/runs` - Start an agent run
- `PUT /api/runs/{id}` - Update agent run status/cost
- `GET /api/runs` - List agent runs (with filters)
- `GET /api/runs/{id}` - Get run details (includes steps)

### Agent Steps
- `POST /api/runs/{id}/steps` - Add a step to a run

### Human Escalations
- `GET /api/escalations` - List pending escalations
- `POST /api/escalations/{id}/resolve` - Resolve an escalation
- `POST /api/runs/{id}/escalate` - Escalate a run

### Analytics
- `GET /api/metrics/health` - Get fleet health metrics
- `GET /api/metrics/costs` - Get cost allocation data

## 4. Services

- `AgentRunService`: Manages run lifecycle.
- `AgentStepService`: Records steps.
- `HumanEscalationService`: Manages escalation queues and resolutions.
- `MetricsService`: Computes health and costs.
- `LiteLLMClient`: AI client for agent loop/hallucination detection (analyzes run steps for bad patterns).

## 5. Frontend Pages & Components

- `Dashboard Page (/dashboard)`: Shows overall fleet health, total runs, success rate, cost, active escalations.
- `Agent Runs Page (/runs)`: Data table of all runs, filterable.
- `Run Detail Page (/runs/[id])`: Shows trace of steps, input/outputs, latency breakdown.
- `Escalation Queue Page (/escalations)`: List of pending escalations, UI to resolve them.
- `Cost Allocation Page (/costs)`: Breakdown of costs by agent/team.

## 6. Edge Cases & Validations
- Ensure `tenant_id` is applied to all queries.
- Validate `token_cost` updates properly.
- Handle massive outputs by truncating `AgentStep.output` if necessary (or storing in S3/blob, but for simplicity we use TEXT).

## 7. Acceptance Criteria
- [ ] Users can view dashboard metrics for their tenant.
- [ ] Users can see a list of agent runs.
- [ ] Users can drill down into a specific run and see all steps.
- [ ] Agents can report runs and steps via API.
- [ ] Escalations show up in the queue and can be resolved.
- [ ] All database queries are scoped by `tenant_id`.

