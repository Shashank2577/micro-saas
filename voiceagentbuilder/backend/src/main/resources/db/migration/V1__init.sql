-- Agents
CREATE TABLE agents (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    system_prompt TEXT NOT NULL,
    provider VARCHAR(50) DEFAULT 'litellm',
    voice_id VARCHAR(100),
    language VARCHAR(20) DEFAULT 'en-US',
    status VARCHAR(50) DEFAULT 'DRAFT',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_agents_tenant ON agents(tenant_id);

-- Intents
CREATE TABLE agent_intents (
    id UUID PRIMARY KEY,
    agent_id UUID NOT NULL REFERENCES agents(id) ON DELETE CASCADE,
    tenant_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    action_type VARCHAR(50),
    action_config TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_intents_agent ON agent_intents(agent_id);

-- Knowledge Base Documents
CREATE TABLE knowledge_documents (
    id UUID PRIMARY KEY,
    agent_id UUID NOT NULL REFERENCES agents(id) ON DELETE CASCADE,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_documents_agent ON knowledge_documents(agent_id);

-- Calls
CREATE TABLE call_logs (
    id UUID PRIMARY KEY,
    agent_id UUID NOT NULL REFERENCES agents(id),
    tenant_id UUID NOT NULL,
    caller_number VARCHAR(50),
    duration_seconds INTEGER DEFAULT 0,
    status VARCHAR(50) DEFAULT 'IN_PROGRESS',
    cost NUMERIC(10, 4) DEFAULT 0.0,
    transcript TEXT,
    recording_url TEXT,
    started_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP WITH TIME ZONE
);
CREATE INDEX idx_calls_tenant_agent ON call_logs(tenant_id, agent_id);
