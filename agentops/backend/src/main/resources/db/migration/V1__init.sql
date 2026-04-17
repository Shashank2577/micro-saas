CREATE TABLE agent_run (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    agent_id VARCHAR(255) NOT NULL,
    workflow_id VARCHAR(255),
    started_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    token_cost DECIMAL(10, 6) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE agent_step (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    run_id UUID NOT NULL REFERENCES agent_run(id) ON DELETE CASCADE,
    step_type VARCHAR(50) NOT NULL,
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
    status VARCHAR(50) NOT NULL,
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
    period VARCHAR(50),
    total_cost DECIMAL(10, 6) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_agent_run_tenant ON agent_run(tenant_id);
CREATE INDEX idx_agent_step_tenant ON agent_step(tenant_id);
CREATE INDEX idx_human_escalation_tenant ON human_escalation(tenant_id);
CREATE INDEX idx_agent_health_metric_tenant ON agent_health_metric(tenant_id);
CREATE INDEX idx_cost_allocation_tenant ON cost_allocation(tenant_id);
