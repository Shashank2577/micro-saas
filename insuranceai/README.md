# InsuranceAI (insuranceai)

> **Tier:** Beta | **Score:** 78/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `claim.created`
- `claim.status_updated`
- `policy.created`

### Consumes Events:
- `user.created`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t insuranceai-backend ./backend
docker build -t insuranceai-frontend ./frontend
```
