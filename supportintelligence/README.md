# SupportIntelligence (supportintelligence)

> **Tier:** Alpha | **Score:** 40/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `ticket.created`
- `ticket.escalated`
- `ticket.resolved`

### Consumes Events:
- `product.updated`
- `feature.released`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t supportintelligence-backend ./backend
docker build -t supportintelligence-frontend ./frontend
```
