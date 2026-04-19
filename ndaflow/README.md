# NDAFlow (ndaflow)

> **Tier:** Beta | **Score:** 70/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `nda.executed`
- `nda.expiring-soon`

### Consumes Events:
None

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t ndaflow-backend ./backend
docker build -t ndaflow-frontend ./frontend
```
