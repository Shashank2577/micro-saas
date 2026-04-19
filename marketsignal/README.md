# Market Signal (marketsignal)

> **Tier:** Beta | **Score:** 70/100

A micro-saas ecosystem application.

## Architecture
- **Backend:** Spring Boot (Port: 8080)
- **Frontend:** Next.js (Port: 3000)

## Integration
This application integrates with the Nexus Hub.

### Emits Events:
- `{'eventType': 'marketsignal.pattern.detected', 'description': 'Fired when a new market pattern is detected', 'payloadSchema': {'type': 'object', 'properties': {'patternId': {'type': 'string'}, 'patternType': {'type': 'string'}, 'title': {'type': 'string'}}}}`
- `{'eventType': 'marketsignal.brief.generated', 'description': 'Fired when a new market brief is generated', 'payloadSchema': {'type': 'object', 'properties': {'briefId': {'type': 'string'}, 'title': {'type': 'string'}}}}`

### Consumes Events:
None

## Development
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && npm run dev
```

## Docker Deployment
```bash
docker build -t marketsignal-backend ./backend
docker build -t marketsignal-frontend ./frontend
```
