CREATE SCHEMA IF NOT EXISTS legalresearch;

CREATE TABLE legalresearch.research_threads (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    practice_area VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE legalresearch.research_memos (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    query TEXT NOT NULL,
    content TEXT,
    practice_area VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL,
    thread_id UUID REFERENCES legalresearch.research_threads(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE legalresearch.citations (
    id UUID PRIMARY KEY,
    memo_id UUID NOT NULL REFERENCES legalresearch.research_memos(id),
    citation_type VARCHAR(50) NOT NULL,
    reference VARCHAR(255) NOT NULL,
    summary TEXT,
    is_verified BOOLEAN DEFAULT FALSE,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
