CREATE TABLE workspaces (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    subdomain VARCHAR(255) UNIQUE,
    status VARCHAR(50) NOT NULL,
    capacity_limit INT,
    branding_logo_url VARCHAR(255),
    features JSONB,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    created_by UUID,
    updated_by UUID,
    CONSTRAINT uk_workspaces_tenant_id UNIQUE (tenant_id)
);

CREATE TABLE workspace_members (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    user_id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    last_login_at TIMESTAMP,
    active_sessions INT DEFAULT 0,
    api_key_usage_count INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_workspace_members_tenant FOREIGN KEY (tenant_id) REFERENCES workspaces(tenant_id)
);

CREATE TABLE teams (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    parent_team_id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_teams_tenant FOREIGN KEY (tenant_id) REFERENCES workspaces(tenant_id),
    CONSTRAINT fk_teams_parent FOREIGN KEY (parent_team_id) REFERENCES teams(id)
);

CREATE TABLE team_members (
    id UUID PRIMARY KEY,
    team_id UUID NOT NULL,
    member_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_team_members_team FOREIGN KEY (team_id) REFERENCES teams(id),
    CONSTRAINT fk_team_members_member FOREIGN KEY (member_id) REFERENCES workspace_members(id)
);

CREATE TABLE invitations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    team_id UUID,
    token VARCHAR(255) UNIQUE NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_invitations_tenant FOREIGN KEY (tenant_id) REFERENCES workspaces(tenant_id),
    CONSTRAINT fk_invitations_team FOREIGN KEY (team_id) REFERENCES teams(id)
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    action VARCHAR(255) NOT NULL,
    actor_id UUID,
    target_id UUID,
    details JSONB,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_audit_logs_tenant FOREIGN KEY (tenant_id) REFERENCES workspaces(tenant_id)
);

CREATE TABLE sso_domains (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    domain VARCHAR(255) NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_sso_domains_tenant FOREIGN KEY (tenant_id) REFERENCES workspaces(tenant_id)
);
