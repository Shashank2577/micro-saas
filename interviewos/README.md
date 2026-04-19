# InterviewOS (interviewos)

> **Tier:** Beta | **Score:** 60/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `interview.completed`

### Consumes Events:
- `candidate.hired`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t interviewos-backend ./backend
docker build -t interviewos-frontend ./frontend
```
