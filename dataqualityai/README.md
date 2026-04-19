# DataQualityAI (dataqualityai)

> **Tier:** Alpha | **Score:** 50/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `dq.test.failed`
- `dq.score.degraded`
- `dq.schema.drift`
- `dq.issue.opened`

### Consumes Events:
- `asset.loaded`
- `schema.changed`
- `pipeline.completed`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t dataqualityai-backend ./backend
docker build -t dataqualityai-frontend ./frontend
```
