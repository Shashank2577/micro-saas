CREATE TABLE knowledge_base_articles (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL
);

CREATE TABLE tickets (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    status VARCHAR(50),
    ai_suggested_response TEXT,
    escalation_risk_score DOUBLE PRECISION,
    created_at TIMESTAMP
);
