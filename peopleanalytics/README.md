# People Analytics App

This application provides analytics on workforce health, productivity, and org planning.

## Backend
Built with Java 21, Spring Boot, and PostgreSQL.

### Setup and Running
1. Make sure you have Docker running.
2. In the `backend` directory, run: `mvn clean package`
3. Run the application: `java -jar target/peopleanalytics-0.0.1-SNAPSHOT.jar`

### Env Vars
- `SPRING_DATASOURCE_URL`: The JDBC URL for PostgreSQL.
- `SPRING_DATASOURCE_USERNAME`: PostgreSQL username.
- `SPRING_DATASOURCE_PASSWORD`: PostgreSQL password.
- `SPRING_DATA_REDIS_HOST`: Redis host.
- `SPRING_DATA_REDIS_PORT`: Redis port.

## Frontend
Built with Next.js 15.

### Setup and Running
1. In the `frontend` directory, run: `npm install`
2. Run the application: `npm run dev`

### Env Vars
None required out of the box, relies on proxying requests to the backend.

## Runbook
If tests fail in the backend, verify PostgreSQL and Redis are running. The backend relies heavily on `X-Tenant-ID` header for multi-tenancy.
