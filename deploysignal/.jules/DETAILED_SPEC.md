# Detailed Spec

## Backend Implementation
- `Deployment` entity with `id`, `tenantId`, `version`, `environment`, and `outcome`.
- `DeploymentRepository` with `findByTenantId`.
- `DeploymentController` with basic GET and POST CRUD endpoints.

## Database
- PostgreSQL
- Managed via Flyway script: `V1__init.sql` (Creates deployment table).

## Future Steps
- Add further entities like `DORASnapshot`, `RiskScore`, `Pipeline`, `ChangeFailure`.
- Improve error handling.
- Build LiteLLM client for AI patterns.
