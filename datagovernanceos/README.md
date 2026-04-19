# DataGovernanceOS

DataGovernanceOS is a robust data governance platform that manages policies, steward assignments, policy violations, exception requests, control checks, and audit trails.

## Setup

1. **Prerequisites**
   - Java 21
   - Maven
   - Node.js & npm
   - Docker & Docker Compose (for local database)

2. **Backend Setup**
   ```bash
   cd backend
   mvn clean install
   ```
   Start the application:
   ```bash
   mvn spring-boot:run &
   ```

3. **Frontend Setup**
   ```bash
   cd frontend
   npm install
   npm run dev &
   ```

## Environment Variables

- `NEXT_PUBLIC_API_URL` (Frontend) - Defaults to `http://localhost:8283`
- `SPRING_DATASOURCE_URL` (Backend) - Database URL
- `CC_AI_API_KEY` (Backend) - AI service API key

## Testing

Run backend tests:
```bash
mvn -pl backend clean verify
```

Run frontend tests:
```bash
npm --prefix frontend test
```
