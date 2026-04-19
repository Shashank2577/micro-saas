# Data Governance (datagovernance)

> **Tier:** Production Candidate | **Score:** 88/100

Data management and compliance service. Handles data retention policies, GDPR/CCPA compliance, data lineage, PII detection, and audit trails.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `{'eventType': 'dsar.completed', 'description': 'Fired when a Data Subject Access Request is completed'}`
- `{'eventType': 'consent.updated', 'description': 'Fired when user consent is updated'}`

### Consumes Events:
- `{'eventType': 'user.deleted', 'description': 'Listen to user deletion events to automate GDPR erasure'}`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t datagovernance-backend ./backend
docker build -t datagovernance-frontend ./frontend
```
