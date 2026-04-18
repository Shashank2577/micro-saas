# API Manager

API Manager is a comprehensive API documentation and lifecycle management microservice. It provides OpenAPI/Swagger documentation, API versioning, deprecation management, and a developer portal.

## Features
- **OpenAPI Schema Management**: Upload and validate OpenAPI 3.0 schemas.
- **Interactive Documentation**: Swagger UI integration for viewing all endpoints, models, and responses.
- **Version Control**: Manage multiple versions of an API and document them separately.
- **Developer Portal**: View API documentation, generate and manage API keys.
- **Analytics**: Track API usage and error rates (Placeholder for full feature).

## Technology Stack
### Backend
- Spring Boot 3.3.5 (Java 21)
- Spring Data JPA (PostgreSQL)
- Springdoc OpenAPI (Swagger generation)
- Flyway (Database migrations)

### Frontend
- Next.js 14.x (React)
- Tailwind CSS
- Swagger-ui-react

## Local Setup

### Prerequisites
- JDK 21
- Node.js 18+
- Docker & Docker Compose (for infrastructure)
- Maven

### Running Backend
cd backend
mvn spring-boot:run

The backend will be available at http://localhost:8118.

### Running Frontend
cd frontend
npm install
npm run build

The frontend will be available at http://localhost:3000.

### Running Tests
**Backend:**
cd backend
mvn clean verify

**Frontend:**
cd frontend
npm run build

## Infrastructure
This service integrates with the broader MicroSaaS ecosystem. Ensure that PostgreSQL and Redis are running via infra/docker-compose.yml.
