# ObservabilityStack

Monitoring and observability platform. Aggregates logs, metrics, traces, and health checks across all microservices with alerting, incident management, and SLA tracking.

## Architecture
- **Backend**: Spring Boot 3.3.5, PostgreSQL, cc-starter
- **Frontend**: Next.js 14, React, TailwindCSS, ECharts

## Running Locally
1. Run database using Docker: `docker-compose -f ../infra/compose.cluster-a.yml up observabilitystack-db -d`
2. Start backend: `cd backend && mvn spring-boot:run`
3. Start frontend: `cd frontend && npm run dev`

## API Documentation
The API documentation is available via OpenAPI/Swagger UI at `http://localhost:8105/swagger-ui.html` when the backend is running.
