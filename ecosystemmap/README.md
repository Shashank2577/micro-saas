# Ecosystemmap (ecosystemmap)

> **Tier:** Beta | **Score:** 78/100

AI ecosystem visualization platform and ROI tracker.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
None

### Consumes Events:
- `app.deployed`
- `app.undeployed`
- `data.flow.event`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t ecosystemmap-backend ./backend
docker build -t ecosystemmap-frontend ./frontend
```
