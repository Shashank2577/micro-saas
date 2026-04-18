# WorkspaceManager - Detailed Specification

## 1. Overview
WorkspaceManager is a multi-tenant workspace and organization management service. It handles workspace creation, member management, team hierarchies, workspace settings, and invite flows.

## 2. Database Schema (PostgreSQL)

All tables must include a `tenant_id` (UUID) column for tenant isolation, `created_at`, `updated_at`, and `created_by`/`updated_by`.

### Tables:

**`workspaces`**
- `id` (UUID, PK)
- `tenant_id` (UUID, UK, for isolation context)
- `name` (VARCHAR)
- `subdomain` (VARCHAR, UK)
- `status` (VARCHAR) - ACTIVE, SUSPENDED
- `capacity_limit` (INT)
- `branding_logo_url` (VARCHAR)
- `features` (JSONB)
- `created_at`, `updated_at`, etc.

**`workspace_members`**
- `id` (UUID, PK)
- `tenant_id` (UUID, FK to workspaces.tenant_id)
- `user_id` (UUID) - global user reference
- `email` (VARCHAR)
- `name` (VARCHAR)
- `role` (VARCHAR) - ADMIN, MEMBER, GUEST
- `status` (VARCHAR) - ACTIVE, INACTIVE, DEPROVISIONED
- `last_login_at` (TIMESTAMP)
- `active_sessions` (INT)
- `api_key_usage_count` (INT)

**`teams`**
- `id` (UUID, PK)
- `tenant_id` (UUID, FK to workspaces.tenant_id)
- `name` (VARCHAR)
- `parent_team_id` (UUID, FK to teams.id, NULLable for hierarchy)

**`team_members`**
- `id` (UUID, PK)
- `team_id` (UUID, FK to teams.id)
- `member_id` (UUID, FK to workspace_members.id)

**`invitations`**
- `id` (UUID, PK)
- `tenant_id` (UUID, FK to workspaces.tenant_id)
- `email` (VARCHAR)
- `role` (VARCHAR)
- `team_id` (UUID, FK to teams.id, NULLable)
- `token` (VARCHAR, UK)
- `expires_at` (TIMESTAMP)
- `status` (VARCHAR) - PENDING, ACCEPTED, EXPIRED

**`audit_logs`**
- `id` (UUID, PK)
- `tenant_id` (UUID, FK to workspaces.tenant_id)
- `action` (VARCHAR)
- `actor_id` (UUID)
- `target_id` (UUID)
- `details` (JSONB)
- `created_at` (TIMESTAMP)

**`sso_domains`**
- `id` (UUID, PK)
- `tenant_id` (UUID, FK to workspaces.tenant_id)
- `domain` (VARCHAR)
- `verified` (BOOLEAN)

## 3. API Endpoints

### Workspaces
- `POST /api/v1/workspaces` - Create workspace
- `GET /api/v1/workspaces/{id}` - Get workspace details
- `PUT /api/v1/workspaces/{id}` - Update workspace settings
- `POST /api/v1/workspaces/{id}/suspend` - Suspend workspace

### Members
- `GET /api/v1/members` - List members (filterable by team, role)
- `POST /api/v1/members/invite` - Send invitation
- `POST /api/v1/members/invite/accept` - Accept invitation
- `POST /api/v1/members/bulk-import` - Import members via CSV
- `DELETE /api/v1/members/{id}` - Remove member (with retention options)
- `PUT /api/v1/members/bulk-update` - Bulk update roles

### Teams
- `GET /api/v1/teams` - List teams (with hierarchy)
- `POST /api/v1/teams` - Create team
- `PUT /api/v1/teams/{id}` - Update team
- `DELETE /api/v1/teams/{id}` - Delete team

### Audit & Settings
- `GET /api/v1/audit-logs` - Get audit logs
- `GET /api/v1/sso-domains` - List SSO domains
- `POST /api/v1/sso-domains` - Add SSO domain

## 4. Services

- `WorkspaceService`: Manages workspace lifecycle, subdomain validation, caching in Redis.
- `MemberService`: Manages invitations, bulk imports (parsing CSV), user joining via SSO, deprovisioning.
- `TeamService`: Handles hierarchical structure. Uses LiteLLM for "team recommendation suggestions".
- `AuditService`: Logs actions like member added, role changed, team changed, member removed.

## 5. Frontend Components (Next.js)
- `WorkspaceSettings`: Form for branding, domain, SSO.
- `MemberDirectory`: Data table with bulk action checkboxes.
- `InviteModal`: Sends invitations.
- `TeamHierarchy`: Visual representation of teams.
- `AuditLogViewer`: Table of audit events.
- `DeprovisionModal`: Handles data retention options.

## 6. Integrations
- Redis: Cache active workspace context and session counts.
- LiteLLM: `cc.ai.gateway-url` used to analyze team structures and suggest optimal team naming or restructuring.
