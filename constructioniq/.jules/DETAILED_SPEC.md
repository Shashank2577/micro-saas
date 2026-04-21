# Detailed Spec

## Backend Component

### Entities
*   `Project`: Stores construction project details.
    *   `id`: UUID, Primary Key.
    *   `tenantId`: UUID, Not Null. Scopes project to tenant.
    *   `name`: String, Not Null. Project name.
    *   `status`: String, Not Null. Current status.
    *   `budget`: BigDecimal. Allocated budget.
    *   `startDate`: LocalDate. Expected start date.
    *   `endDate`: LocalDate. Expected end date.
    *   `createdAt`: OffsetDateTime. Creation timestamp.
    *   `updatedAt`: OffsetDateTime. Last updated timestamp.

### Endpoints (Base Path: /api/v1/projects)

*   `POST /`
    *   Request: `Project` JSON body.
    *   Response: `201 Created` with created `Project` JSON.
*   `GET /`
    *   Request: None.
    *   Response: `200 OK` with JSON array of `Project` objects.
*   `GET /{id}`
    *   Request: Path variable `id` (UUID).
    *   Response: `200 OK` with `Project` JSON, or `404 Not Found` if not exists or belongs to another tenant.
*   `PUT /{id}`
    *   Request: Path variable `id` (UUID), `Project` JSON body.
    *   Response: `200 OK` with updated `Project` JSON, or `404 Not Found` if not exists or belongs to another tenant.
*   `DELETE /{id}`
    *   Request: Path variable `id` (UUID).
    *   Response: `204 No Content`, or `404 Not Found` if not exists or belongs to another tenant.

### Services
*   `ProjectService`: Handles business logic and tenant scoping using `TenantContext`.
    *   `createProject(Project)`
    *   `getProjects()`
    *   `getProjectById(UUID)`
    *   `updateProject(UUID, Project)`
    *   `deleteProject(UUID)`
