# Healthcaredocai (healthcaredocai)

> **Tier:** Beta | **Score:** 78/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
None

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
docker build -t healthcaredocai-backend ./backend
docker build -t healthcaredocai-frontend ./frontend
```
