# Data Lineage Tracker

Comprehensive data lineage, governance, and audit tracking platform.

## Architecture

This application is built with:
- **Backend:** Spring Boot 3.3.5 (Java 21)
- **Frontend:** Next.js 14, React Flow (for lineage visualization)
- **Database:** PostgreSQL
- **AI Integration:** LiteLLM for PII detection

## Local Development

### Backend
1. Navigate to the `backend` directory.
2. Run `mvn spring-boot:run`. The backend runs on port `8167`.

### Frontend
1. Navigate to the `frontend` directory.
2. Run `npm install` and `npm run dev`. The frontend connects to the backend at port `8167`.

## Documentation

OpenAPI specification is available at `http://localhost:8167/swagger-ui.html` when running the backend.
