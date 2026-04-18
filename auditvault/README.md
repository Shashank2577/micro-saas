# AuditVault

AI compliance evidence aggregation platform.

## Purpose
Pulls evidence from all connected ecosystem apps automatically. Maps evidence to control frameworks (SOC 2, ISO 27001, GDPR, HIPAA, EU AI Act). Generates audit-ready evidence packages with one click. Tracks evidence freshness and completeness continuously.

## Running Locally

1. Start infrastructure: `cd ../infra && docker compose up -d`
2. Start backend: `cd backend && mvn spring-boot:run`
3. Start frontend: `cd frontend && npm run dev`

## Testing

Backend: `cd backend && mvn test`
Frontend: `cd frontend && npm test`

## Environment Variables
- `X-Tenant-ID`: Set for tenant testing
- `SPRING_DATASOURCE_URL`: PostgreSQL URL
- `CC_AUTH_KEYCLOAK_URL`: Keycloak URL
