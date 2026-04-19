# ContractPortfolio (contractportfolio)

> **Tier:** Alpha | **Score:** 55/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `contract.expiring-soon`
- `renewal.alert-triggered`

### Consumes Events:
- `document.uploaded`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t contractportfolio-backend ./backend
docker build -t contractportfolio-frontend ./frontend
```
