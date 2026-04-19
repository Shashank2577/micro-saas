# LicenseGuard (licenseguard)

> **Tier:** Alpha | **Score:** 45/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `license.violation-detected`
- `sbom.generated`

### Consumes Events:
- `code.pushed`
- `pr.merged`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t licenseguard-backend ./backend
docker build -t licenseguard-frontend ./frontend
```
