# ContentOS Detailed Spec

## Backend Services
ContentOS needs a backend service that supports basic CRUD operations for content items.
This backend is a Spring Boot application using Java 21, Maven, PostgreSQL.

### Entities
`ContentItem`
- id: UUID
- tenantId: UUID
- title: String
- type: String
- status: String
- sourceItemId: UUID
- content: TEXT
- createdAt: OffsetDateTime

### REST Endpoints
- `GET /api/content-items`
- `GET /api/content-items/{id}`
- `POST /api/content-items`
- `PUT /api/content-items/{id}`
- `DELETE /api/content-items/{id}`

## Frontend
(Stubbed out for Phase 1/Phase 2)
