# ContractSense (contractsense)

> **Tier:** Beta | **Score:** 73/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `contract.analyzed`
- `risk.flagged`

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
docker build -t contractsense-backend ./backend
docker build -t contractsense-frontend ./frontend
```
