# ConstructionIQ - Detailed Specification

## Overview
ConstructionIQ is an AI-native construction project management platform. It helps construction firms manage projects, sites, resources, and safety with AI-driven insights for risk mitigation and schedule optimization.

## Domain Model

### Entities & Database Schema (DDL)

```sql
-- Existing V1 migration
CREATE TABLE construction_projects (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(50) NOT NULL, -- PLANNING, ACTIVE, COMPLETED, ON_HOLD
    budget DECIMAL(15,2),
    start_date DATE,
    end_date DATE,
    location VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_construction_projects_tenant ON construction_projects(tenant_id);

-- V2 migration additions
CREATE TABLE construction_sites (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL REFERENCES construction_projects(id),
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    manager_name VARCHAR(255),
    status VARCHAR(50) NOT NULL, -- ACTIVE, INACTIVE
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_construction_sites_tenant ON construction_sites(tenant_id);
CREATE INDEX idx_construction_sites_project ON construction_sites(project_id);

CREATE TABLE construction_tasks (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL REFERENCES construction_projects(id),
    tenant_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    assigned_to VARCHAR(255), -- User ID or Name
    status VARCHAR(50) NOT NULL, -- TODO, IN_PROGRESS, REVIEW, DONE
    priority VARCHAR(50) NOT NULL, -- LOW, MEDIUM, HIGH, CRITICAL
    due_date TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_construction_tasks_tenant ON construction_tasks(tenant_id);
CREATE INDEX idx_construction_tasks_project ON construction_tasks(project_id);

CREATE TABLE safety_incidents (
    id UUID PRIMARY KEY,
    project_id UUID NOT NULL REFERENCES construction_projects(id),
    tenant_id UUID NOT NULL,
    reported_by VARCHAR(255) NOT NULL,
    incident_date TIMESTAMP WITH TIME ZONE NOT NULL,
    description TEXT NOT NULL,
    severity VARCHAR(50) NOT NULL, -- LOW, MEDIUM, HIGH, FATAL
    status VARCHAR(50) NOT NULL, -- OPEN, INVESTIGATING, CLOSED
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_safety_incidents_tenant ON safety_incidents(tenant_id);
CREATE INDEX idx_safety_incidents_project ON safety_incidents(project_id);
```

## API Endpoints

### Project API (`/api/v1/projects`)
- `GET /`: List all projects for current tenant.
- `POST /`: Create a project.
- `GET /{id}`: Get project details.
- `PUT /{id}`: Update project.
- `DELETE /{id}`: Delete project.
- `POST /{id}/ai/risk-assessment`: Get AI risk assessment.
- `POST /{id}/ai/schedule-optimization`: Get AI schedule optimization.

### Site API (`/api/v1/projects/{projectId}/sites`)
- `GET /`: List sites for a project.
- `POST /`: Create a site for a project.
- `GET /{id}`: Get site details.
- `PUT /{id}`: Update site.
- `DELETE /{id}`: Delete site.

### Task API (`/api/v1/projects/{projectId}/tasks`)
- `GET /`: List tasks for a project.
- `POST /`: Create a task for a project.
- `GET /{id}`: Get task details.
- `PUT /{id}`: Update task.
- `DELETE /{id}`: Delete task.

### Safety API (`/api/v1/projects/{projectId}/incidents`)
- `GET /`: List safety incidents for a project.
- `POST /`: Report an incident.
- `GET /{id}`: Get incident details.
- `PUT /{id}`: Update incident.

## Service Layer Signatures

### `ProjectService`
- `List<Project> getProjects()`
- `Project createProject(Project project)`
- `Project getProject(UUID id)`
- `Project updateProject(UUID id, Project project)`
- `void deleteProject(UUID id)`
- `String getRiskAssessment(UUID projectId)`
- `String optimizeSchedule(UUID projectId)`

### `SiteService`
- `List<Site> getSites(UUID projectId)`
- `Site createSite(UUID projectId, Site site)`
- `Site getSite(UUID id)`
- `Site updateSite(UUID id, Site site)`
- `void deleteSite(UUID id)`

### `TaskService`
- `List<Task> getTasks(UUID projectId)`
- `Task createTask(UUID projectId, Task task)`
- `Task getTask(UUID id)`
- `Task updateTask(UUID id, Task task)`
- `void deleteTask(UUID id)`

### `SafetyIncidentService`
- `List<SafetyIncident> getIncidents(UUID projectId)`
- `SafetyIncident reportIncident(UUID projectId, SafetyIncident incident)`
- `SafetyIncident getIncident(UUID id)`
- `SafetyIncident updateIncident(UUID id, SafetyIncident incident)`

## AI Integration Logic

### Risk Assessment Prompt
"Analyze the following construction project data and identify potential risks (financial, safety, schedule). Provide mitigation strategies.
Project: {projectDetails}
Tasks: {tasks}
Safety Incidents: {incidents}"

### Schedule Optimization Prompt
"Analyze the current task list for the construction project and suggest optimizations to the schedule to reduce duration and improve resource efficiency.
Project: {projectDetails}
Tasks: {tasks}"

## Acceptance Criteria
- All entities have `tenant_id` and are filtered by `TenantContext.require()` in service layer.
- Flyway migrations `V1` and `V2` correctly set up the schema.
- AI service successfully calls LiteLLM via `AiService`.
- 100% of REST endpoints specified above are implemented.
- CRUD tests for each entity pass.
- Multi-tenancy isolation test passes (Tenant A cannot see Tenant B's data).
