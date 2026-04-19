# DataGovernanceOS (datagovernanceos)

> **Tier:** Beta | **Score:** 63/100

Data governance and classification

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `data_asset.created`
- `policy.created`
- `audit.failed`

### Consumes Events:
- `pipeline.failed`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t datagovernanceos-backend ./backend
docker build -t datagovernanceos-frontend ./frontend
```
