# Tax Optimizer (taxoptimizer)

> **Tier:** Beta | **Score:** 70/100

AI-driven tax planning and optimization platform

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `{'event': 'tax_liability_calculated', 'schema': {'tenantId': 'string', 'profileId': 'string', 'taxYear': 'number', 'totalLiability': 'number'}}`
- `{'event': 'tax_package_generated', 'schema': {'tenantId': 'string', 'profileId': 'string', 'accountantEmail': 'string'}}`

### Consumes Events:
- `{'event': 'user_onboarded', 'source': 'nexus-hub'}`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t taxoptimizer-backend ./backend
docker build -t taxoptimizer-frontend ./frontend
```
