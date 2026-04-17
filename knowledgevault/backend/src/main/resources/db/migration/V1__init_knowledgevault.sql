CREATE TABLE knowledge_sources (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    connection_status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE knowledge_documents (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    source_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    url VARCHAR(255) NOT NULL,
    freshness_status VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (source_id) REFERENCES knowledge_sources(id)
);

CREATE TABLE knowledge_queries (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    query_text TEXT NOT NULL,
    generated_answer TEXT,
    frequency INT NOT NULL,
    last_asked_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL
);
