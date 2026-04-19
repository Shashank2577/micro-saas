# BillingSync (billingsync)

> **Tier:** Production Candidate | **Score:** 88/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `{'event': 'invoice.generated', 'description': 'Fired when a new invoice is generated for a subscription.'}`
- `{'event': 'payment.succeeded', 'description': 'Fired when a payment is successfully processed.'}`
- `{'event': 'payment.failed', 'description': 'Fired when a payment fails, initiating dunning management.'}`

### Consumes Events:
- `{'event': 'tenant.created', 'description': 'Listen for new tenant creation to initialize billing context.'}`
- `{'event': 'usage.metered', 'description': 'Listen for general usage events from other services to record meter events.'}`

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t billingsync-backend ./backend
docker build -t billingsync-frontend ./frontend
```
