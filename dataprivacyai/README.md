# DataPrivacyAI (dataprivacyai)

> **Tier:** Beta | **Score:** 70/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `privacy.risk-detected`
- `dsr.completed`

### Consumes Events:
- `user.data-requested`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t dataprivacyai-backend ./backend
docker build -t dataprivacyai-frontend ./frontend
```
