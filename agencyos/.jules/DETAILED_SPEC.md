# AgencyOS Detailed Spec

## Overview
AgencyOS is an all-in-one SaaS platform designed to help digital agencies manage their clients, projects, tasks, and billing. This specification covers the technical implementation details for the MVP.

## Architecture

### Backend Stack
- Spring Boot 3.3.5 (Java 21)
- Maven
- PostgreSQL (schema: `agencyos`)
- Flyway for migrations
- Spring Data JPA
- Springdoc OpenAPI

### Frontend Stack
- Next.js (App Router)
- TypeScript
- Tailwind CSS
- React

---

## Database Schema (agencyos)

```sql
CREATE SCHEMA IF NOT EXISTS agencyos;

CREATE TABLE agencyos.clients (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(50),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE agencyos.projects (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    client_id UUID NOT NULL REFERENCES agencyos.clients(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL, -- e.g., 'PLANNED', 'IN_PROGRESS', 'COMPLETED'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE agencyos.tasks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id VARCHAR(255) NOT NULL,
    project_id UUID NOT NULL REFERENCES agencyos.projects(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL, -- e.g., 'TODO', 'IN_PROGRESS', 'DONE'
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance and tenant scoping
CREATE INDEX idx_clients_tenant ON agencyos.clients(tenant_id);
CREATE INDEX idx_projects_tenant ON agencyos.projects(tenant_id);
CREATE INDEX idx_tasks_tenant ON agencyos.tasks(tenant_id);
```

---

## REST Endpoints

All endpoints require the `X-Tenant-ID` header.

### Clients
- `GET /api/clients` - List all clients
- `GET /api/clients/{id}` - Get a client
- `POST /api/clients` - Create a client
- `PUT /api/clients/{id}` - Update a client
- `DELETE /api/clients/{id}` - Delete a client

### Projects
- `GET /api/projects?clientId={id}` - List projects (optionally filtered by client)
- `GET /api/projects/{id}` - Get a project
- `POST /api/projects` - Create a project
- `PUT /api/projects/{id}` - Update a project
- `DELETE /api/projects/{id}` - Delete a project

### Tasks
- `GET /api/tasks?projectId={id}` - List tasks (optionally filtered by project)
- `GET /api/tasks/{id}` - Get a task
- `POST /api/tasks` - Create a task
- `PUT /api/tasks/{id}` - Update a task
- `DELETE /api/tasks/{id}` - Delete a task

---

## Frontend Components

### Pages
- `/` - Dashboard
- `/clients` - Client List
- `/clients/[id]` - Client Details & Projects
- `/projects/[id]` - Project Details & Tasks

### Key Components
- `ClientCard` - Summary of client info.
- `ProjectList` - List of projects for a client.
- `TaskBoard` - Kanban-style board for tasks.
- `CreateClientModal` - Form to create a new client.

## Acceptance Criteria
- [ ] Users can create, read, update, and delete clients.
- [ ] Users can create projects associated with a client.
- [ ] Users can create tasks within a project.
- [ ] All data is properly scoped to the `tenant_id`.
- [ ] Backend test coverage is >= 80%.

