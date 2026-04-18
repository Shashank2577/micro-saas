# AuthVault Detailed Specification

## Overview
AuthVault is a multi-tenant authentication and authorization service that provides OAuth2/OpenID Connect, JWT token management, RBAC, and identity federation for all micro-saas apps.
Port: 8101

## Database Schema (PostgreSQL)

```sql
-- V2__init_authvault.sql

CREATE TABLE auth_tenants (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    domain VARCHAR(255) UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE auth_users (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50),
    mfa_enabled BOOLEAN DEFAULT FALSE,
    mfa_secret VARCHAR(255),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    status VARCHAR(50) DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tenant FOREIGN KEY (tenant_id) REFERENCES auth_tenants(id),
    UNIQUE (tenant_id, email)
);

CREATE TABLE auth_roles (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tenant_role FOREIGN KEY (tenant_id) REFERENCES auth_tenants(id),
    UNIQUE (tenant_id, name)
);

CREATE TABLE auth_permissions (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE auth_role_permissions (
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES auth_roles(id),
    CONSTRAINT fk_permission FOREIGN KEY (permission_id) REFERENCES auth_permissions(id)
);

CREATE TABLE auth_user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES auth_users(id),
    CONSTRAINT fk_role_user FOREIGN KEY (role_id) REFERENCES auth_roles(id)
);

CREATE TABLE auth_oauth_clients (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    client_id VARCHAR(255) NOT NULL UNIQUE,
    client_secret_hash VARCHAR(255) NOT NULL,
    redirect_uris TEXT,
    grant_types VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tenant_client FOREIGN KEY (tenant_id) REFERENCES auth_tenants(id)
);

CREATE TABLE auth_audit_logs (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    user_id UUID,
    event_type VARCHAR(100) NOT NULL,
    ip_address VARCHAR(45),
    device_info TEXT,
    status VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## REST Endpoints

### Authentication & Tokens
- `POST /api/v1/auth/login`: Authenticate user and return JWT + Refresh Token
- `POST /api/v1/auth/mfa/verify`: Verify TOTP code and issue final JWT
- `POST /api/v1/auth/refresh`: Rotate refresh token and issue new access token
- `POST /api/v1/auth/logout`: Revoke tokens
- `POST /api/v1/auth/introspect`: Token introspection

### OAuth2 / OIDC
- `GET /oauth2/authorize`: Authorization code flow entry point
- `POST /oauth2/token`: Token exchange endpoint
- `GET /.well-known/openid-configuration`: OIDC discovery

### User Management
- `POST /api/v1/users`: Register/create new user
- `GET /api/v1/users/{id}`: Get user details
- `PUT /api/v1/users/{id}`: Update user profile
- `POST /api/v1/users/{id}/mfa/setup`: Generate TOTP secret and QR code uri

### Role & Permission Management (RBAC)
- `POST /api/v1/roles`: Create custom role
- `GET /api/v1/roles`: List roles
- `POST /api/v1/users/{userId}/roles`: Assign role to user

### Tenant Management
- `POST /api/v1/tenants`: Create new tenant (automates schema/DB setup logically)
- `GET /api/v1/tenants/{id}`: Get tenant details

## React Components (Next.js)
- `LoginForm`: Handles username/password and MFA step.
- `SignupForm`: User registration with tenant context.
- `MFASetup`: Displays QR code for TOTP enrollment and verifies initial code.
- `Dashboard`: Overview of user profile and active sessions.
- `UserManagement`: Admin table to view and manage users in the tenant.
- `RoleManagement`: Admin UI to create roles and assign permissions.
- `OAuthAppsList`: Manage registered OAuth clients.

## Error Handling
- `401 Unauthorized`: Invalid credentials, expired token.
- `403 Forbidden`: Insufficient permissions (RBAC/ABAC).
- `404 Not Found`: Resource not found.
- `429 Too Many Requests`: Rate limiting triggered.

## Acceptance Criteria
1. New tenant creates organization with automated database schema provisioning.
2. Admin user registers with email, password, phone number for MFA setup.
3. User completes TOTP enrollment and successfully authenticates with 2FA.
4. OAuth2 authorization code flow generates JWT with tenant_id and user_id claims.
5. Refresh token endpoint rotates token and issues new 15-minute access token.
6. Admin creates custom RBAC role: 'Manager' with permissions for create/read/update on assets.
7. User assigned 'Manager' role can perform authorized actions, blocked on unauthorized operations.
8. Failed login attempt rate limited: 5 attempts block for 15 minutes.
9. Audit log shows complete trace: login timestamp, IP, device, MFA method, success/failure.

