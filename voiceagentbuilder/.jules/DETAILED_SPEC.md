# VoiceAgentBuilder - Detailed Specification

## 1. Overview
Voice AI agent platform. Build, deploy, and monitor voice agents for customer service, sales, and support using a visual builder. Full conversation intelligence: intent detection, entity extraction, escalation logic, and CRM integration. Pre-built connectors for Twilio and major telephony providers.

AI Pattern: Multi-modal (speech-to-text -> agent -> text-to-speech) + pipeline.
Monetization: $0.05/minute, $299/month minimum.

## 2. Database Schema (PostgreSQL)

```sql
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
    status VARCHAR(50) DEFAULT 'DRAFT', -- DRAFT, ACTIVE, ARCHIVED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_agents_tenant ON agents(tenant_id);

-- Intents (Agent Capabilities)
CREATE TABLE agent_intents (
    id UUID PRIMARY KEY,
    agent_id UUID NOT NULL REFERENCES agents(id) ON DELETE CASCADE,
    tenant_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    action_type VARCHAR(50), -- ESCALATE, END_CALL, API_CALL
    action_config JSONB,
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
    embedding vector(1536), -- Assuming pgvector, optional for this scope if pure text is used via LiteLLM
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Calls (Conversations)
CREATE TABLE call_logs (
    id UUID PRIMARY KEY,
    agent_id UUID NOT NULL REFERENCES agents(id),
    tenant_id UUID NOT NULL,
    caller_number VARCHAR(50),
    duration_seconds INTEGER DEFAULT 0,
    status VARCHAR(50) DEFAULT 'IN_PROGRESS', -- IN_PROGRESS, COMPLETED, FAILED, ESCALATED
    cost NUMERIC(10, 4) DEFAULT 0.0,
    transcript TEXT,
    recording_url TEXT,
    started_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    ended_at TIMESTAMP WITH TIME ZONE
);
CREATE INDEX idx_calls_tenant_agent ON call_logs(tenant_id, agent_id);
```

## 3. Backend API (Spring Boot)

Base path: `/api/v1`

### Agents (`/agents`)
- `GET /agents`
  - Returns list of agents for the tenant.
- `POST /agents`
  - Creates a new agent.
  - Body: `{ "name", "description", "systemPrompt", "voiceId", "language" }`
- `GET /agents/{id}`
  - Returns agent details.
- `PUT /agents/{id}`
  - Updates agent.
- `DELETE /agents/{id}`
  - Deletes agent.

### Intents (`/agents/{agentId}/intents`)
- `GET /agents/{agentId}/intents`
- `POST /agents/{agentId}/intents`
  - Body: `{ "name", "description", "actionType", "actionConfig" }`
- `DELETE /agents/{agentId}/intents/{id}`

### Knowledge Base (`/agents/{agentId}/documents`)
- `GET /agents/{agentId}/documents`
- `POST /agents/{agentId}/documents`
  - Body: `{ "title", "content" }`
- `DELETE /agents/{agentId}/documents/{id}`

### Call Logs (`/agents/{agentId}/calls`)
- `GET /agents/{agentId}/calls`
- `GET /calls/{id}` (global, but tenant-scoped)

### AI Simulation Endpoint (`/agents/{agentId}/simulate`)
- `POST /agents/{agentId}/simulate`
  - Used for testing the agent via text before voice deployment.
  - Body: `{ "message": "User input text" }`
  - Response: `{ "reply": "Agent response text", "intentTriggered": "intent_id_or_null" }`

## 4. Services

- `AgentService`: CRUD for agents.
- `IntentService`: CRUD for intents.
- `KnowledgeService`: CRUD for documents.
- `CallLogService`: Read-only for call logs, updated via internal events/webhooks.
- `AiSimulationService`: Uses `LiteLLMClient` to send `system_prompt` + `knowledge_documents` + `message` to the LLM and return the response.

## 5. Frontend (Next.js)

### Pages
- `/` (Dashboard): Shows total calls, duration, cost, and active agents.
- `/agents`: List of agents.
- `/agents/new`: Create agent wizard.
- `/agents/[id]`: Agent builder. Tabs:
  - Settings (Prompt, Voice)
  - Knowledge Base (Upload/paste text)
  - Intents (Define escalation logic)
  - Simulator (Chat UI to test the agent)
- `/calls`: Global call history.

### Components
- `AgentCard`
- `SimulatorPanel` (Chat interface)
- `KnowledgeManager`
- `IntentBuilder`

## 6. Integration Manifest
- `emits`: `call.completed`, `agent.escalated`
- `consumes`: None required for MVP.

## 7. Acceptance Criteria
1. User can create an agent with a system prompt and voice settings.
2. User can add text documents to the agent's knowledge base.
3. User can define intents (e.g., "Escalate to human").
4. User can simulate a conversation with the agent using the `AiSimulationService` which hits the real LLM endpoint via `LiteLLMClient`.
5. Frontend allows testing the agent via a chat interface.

