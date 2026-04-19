# Documentvault (documentvault)

> **Tier:** Production Candidate | **Score:** 83/100

Secure document storage and management system with OCR and versioning.

## Architecture
- **Backend:** Spring Boot (Port: 8113)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `document.uploaded`
- `document.shared`
- `document.deleted`

### Consumes Events:
- `tenant.created`
- `tenant.deleted`
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
docker build -t documentvault-backend ./backend
docker build -t documentvault-frontend ./frontend
```
