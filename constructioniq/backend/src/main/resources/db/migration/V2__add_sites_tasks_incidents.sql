ALTER TABLE construction_projects ADD COLUMN description TEXT;
ALTER TABLE construction_projects ADD COLUMN location VARCHAR(255);

CREATE TABLE construction_sites (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL REFERENCES construction_projects(id) ON DELETE CASCADE,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    manager_name VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_construction_sites_tenant ON construction_sites(tenant_id);
CREATE INDEX idx_construction_sites_project ON construction_sites(project_id);

CREATE TABLE construction_tasks (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL REFERENCES construction_projects(id) ON DELETE CASCADE,
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    assigned_to VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    due_date TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_construction_tasks_tenant ON construction_tasks(tenant_id);
CREATE INDEX idx_construction_tasks_project ON construction_tasks(project_id);

CREATE TABLE safety_incidents (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL REFERENCES construction_projects(id) ON DELETE CASCADE,
    tenant_id UUID NOT NULL,
    reported_by VARCHAR(255) NOT NULL,
    incident_date TIMESTAMP WITH TIME ZONE NOT NULL,
    description TEXT NOT NULL,
    severity VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_safety_incidents_tenant ON safety_incidents(tenant_id);
CREATE INDEX idx_safety_incidents_project ON safety_incidents(project_id);
