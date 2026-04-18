CREATE TABLE research_projects (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    target_audience VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_projects_tenant ON research_projects(tenant_id);

CREATE TABLE interviews (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    project_id UUID NOT NULL REFERENCES research_projects(id) ON DELETE CASCADE,
    participant_name VARCHAR(255) NOT NULL,
    participant_email VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    transcript TEXT,
    summary TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_interviews_tenant ON interviews(tenant_id);
CREATE INDEX idx_interviews_project ON interviews(project_id);

CREATE TABLE insights (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    project_id UUID NOT NULL REFERENCES research_projects(id) ON DELETE CASCADE,
    theme VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    confidence_score DECIMAL,
    supporting_quotes JSONB,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_insights_tenant ON insights(tenant_id);
CREATE INDEX idx_insights_project ON insights(project_id);

CREATE TABLE reports (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    project_id UUID NOT NULL REFERENCES research_projects(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_reports_tenant ON reports(tenant_id);
CREATE INDEX idx_reports_project ON reports(project_id);
