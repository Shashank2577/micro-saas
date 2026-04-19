# CashflowAI (cashflowai)

> **Tier:** Alpha | **Score:** 55/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `cashflow.shortfall-predicted`

### Consumes Events:
- `invoice.paid`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t cashflowai-backend ./backend
docker build -t cashflowai-frontend ./frontend
```
