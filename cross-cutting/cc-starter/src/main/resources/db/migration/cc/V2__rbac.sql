CREATE TABLE cc.roles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID REFERENCES cc.tenants(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    is_system BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(tenant_id, name)
);

CREATE TABLE cc.permissions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    resource VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    description TEXT,
    UNIQUE(resource, action)
);

CREATE TABLE cc.role_permissions (
    role_id UUID NOT NULL REFERENCES cc.roles(id) ON DELETE CASCADE,
    permission_id UUID NOT NULL REFERENCES cc.permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE cc.user_roles (
    user_id UUID NOT NULL REFERENCES cc.users(id) ON DELETE CASCADE,
    role_id UUID NOT NULL REFERENCES cc.roles(id) ON DELETE CASCADE,
    tenant_id UUID NOT NULL REFERENCES cc.tenants(id) ON DELETE CASCADE,
    assigned_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, role_id, tenant_id)
);

-- TODO: This table is currently unused. Resource-level ACLs are not referenced by any
-- entity, repository, or service in cc-starter. The RBAC system uses role-based permissions
-- (roles -> role_permissions -> permissions) rather than resource-level ACLs.
-- Consider removing in a future migration or implementing resource-level access control.
CREATE TABLE cc.resource_acl (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tenant_id UUID NOT NULL REFERENCES cc.tenants(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES cc.users(id) ON DELETE CASCADE,
    resource_type VARCHAR(100) NOT NULL,
    resource_id UUID NOT NULL,
    permission VARCHAR(50) NOT NULL,
    granted_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(tenant_id, user_id, resource_type, resource_id, permission)
);

CREATE INDEX idx_acl_resource ON cc.resource_acl(tenant_id, resource_type, resource_id);
CREATE INDEX idx_user_roles_tenant ON cc.user_roles(tenant_id, user_id);

-- Partial unique index to prevent duplicate system roles (NULL tenant_id)
CREATE UNIQUE INDEX idx_roles_system_name ON cc.roles (name) WHERE tenant_id IS NULL;

-- Seed system roles
INSERT INTO cc.roles (name, description, is_system) VALUES
    ('super_admin', 'Platform super administrator', TRUE),
    ('org_admin', 'Organization administrator', TRUE),
    ('member', 'Standard organization member', TRUE)
ON CONFLICT DO NOTHING;

-- Seed common permissions
INSERT INTO cc.permissions (resource, action) VALUES
    ('*', '*'),
    ('tenant', 'admin'),
    ('tenant', 'read'),
    ('user', 'read'),
    ('user', 'write'),
    ('user', 'delete'),
    ('role', 'read'),
    ('role', 'write'),
    ('role', 'delete')
ON CONFLICT DO NOTHING;
