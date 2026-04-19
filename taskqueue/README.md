# TaskQueue (taskqueue)

> **Tier:** Production Candidate | **Score:** 88/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `{'eventType': 'taskqueue.job.completed', 'schema': {'jobId': 'string', 'name': 'string', 'status': 'string', 'result': 'string'}}`
- `{'eventType': 'taskqueue.job.failed', 'schema': {'jobId': 'string', 'name': 'string', 'status': 'string', 'error': 'string'}}`

### Consumes Events:
- `{'eventType': 'taskqueue.job.enqueue', 'schema': {'name': 'string', 'priority': 'string', 'payload': 'string'}}`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t taskqueue-backend ./backend
docker build -t taskqueue-frontend ./frontend
```
