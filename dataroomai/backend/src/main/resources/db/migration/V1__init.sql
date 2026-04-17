CREATE SCHEMA IF NOT EXISTS dataroomai;

CREATE TABLE dataroomai.data_rooms (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE dataroomai.documents (
    id UUID PRIMARY KEY,
    room_id UUID NOT NULL REFERENCES dataroomai.data_rooms(id),
    title VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    tenant_id UUID NOT NULL,
    upload_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_read_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE dataroomai.investor_sessions (
    id UUID PRIMARY KEY,
    room_id UUID NOT NULL REFERENCES dataroomai.data_rooms(id),
    investor_id UUID NOT NULL,
    time_spent BIGINT DEFAULT 0,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE dataroomai.session_documents (
    session_id UUID NOT NULL REFERENCES dataroomai.investor_sessions(id),
    document_id UUID NOT NULL REFERENCES dataroomai.documents(id),
    PRIMARY KEY (session_id, document_id)
);

CREATE TABLE dataroomai.session_questions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    session_id UUID NOT NULL REFERENCES dataroomai.investor_sessions(id),
    question TEXT NOT NULL,
    tenant_id UUID NOT NULL DEFAULT gen_random_uuid(),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE dataroomai.ai_answers (
    id UUID PRIMARY KEY,
    room_id UUID NOT NULL REFERENCES dataroomai.data_rooms(id),
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    confidence_score FLOAT NOT NULL,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE dataroomai.ai_answer_sources (
    answer_id UUID NOT NULL REFERENCES dataroomai.ai_answers(id),
    document_id UUID NOT NULL REFERENCES dataroomai.documents(id),
    PRIMARY KEY (answer_id, document_id)
);

CREATE TABLE dataroomai.diligence_gaps (
    id UUID PRIMARY KEY,
    room_id UUID NOT NULL REFERENCES dataroomai.data_rooms(id),
    category VARCHAR(100) NOT NULL,
    missing_item TEXT NOT NULL,
    importance VARCHAR(50) NOT NULL,
    tenant_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
