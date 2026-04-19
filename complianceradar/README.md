# ComplianceRadar (complianceradar)

> **Tier:** Alpha | **Score:** 50/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `compliance.gap-detected`
- `compliance.task-completed`

### Consumes Events:
- `regulation.changed`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t complianceradar-backend ./backend
docker build -t complianceradar-frontend ./frontend
```
