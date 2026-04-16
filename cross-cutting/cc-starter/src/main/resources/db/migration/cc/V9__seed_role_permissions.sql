-- Map super_admin → wildcard permission
INSERT INTO cc.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM cc.roles r, cc.permissions p
WHERE r.name = 'super_admin' AND r.is_system = TRUE AND p.resource = '*' AND p.action = '*'
ON CONFLICT DO NOTHING;

-- Map org_admin → tenant + user + role management
INSERT INTO cc.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM cc.roles r, cc.permissions p
WHERE r.name = 'org_admin' AND r.is_system = TRUE
  AND (p.resource, p.action) IN (
    ('tenant', 'admin'), ('tenant', 'read'),
    ('user', 'read'), ('user', 'write'), ('user', 'delete'),
    ('role', 'read'), ('role', 'write'), ('role', 'delete')
  )
ON CONFLICT DO NOTHING;

-- Map member → read-only
INSERT INTO cc.role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM cc.roles r, cc.permissions p
WHERE r.name = 'member' AND r.is_system = TRUE
  AND (p.resource, p.action) IN (
    ('tenant', 'read'),
    ('user', 'read'),
    ('role', 'read')
  )
ON CONFLICT DO NOTHING;
