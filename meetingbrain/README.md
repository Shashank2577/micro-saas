# MeetingBrain (meetingbrain)

> **Tier:** Beta | **Score:** 73/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `MeetingAnalyzedEvent`
- `ActionItemCreatedEvent`

### Consumes Events:
- `ProjectCreatedEvent`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t meetingbrain-backend ./backend
docker build -t meetingbrain-frontend ./frontend
```
