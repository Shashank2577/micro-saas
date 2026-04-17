# Session Notes

- Setup and scaffolding worked correctly out of the box using `scaffold-app.sh documentvault "Document Vault" 8113`
- Configured PostgreSQL and Next.js connections in `.yml` files and `.ts` files, making sure ports matched.
- Built JPA entities in Spring Boot for mapping `Documents`, `Versions`, `Audits`, `StorageQuotas`.
- Setup Repositories with tenant scoping.
- Wrote Services logic for uploads, checking files, virus scans, AI classification, testing storage quotas, making sure they used `tenantId` parameters.
- Built a Next.js frontend with drag/drop interfaces and search functionalities for searching matching titles on the backend.
- Attempted to set up backend `Spring Boot` tests with H2 SQL DB. Reached multiple `ConflictingBeanDefinitionException` and `UnsatisfiedDependencyException` involving auto-configured beans coming from `cc-starter` dependency that needed deeper fixes or custom mock profiles to run properly within an environment missing actual Redis/S3.
- Next.js UI component `vitest` tests failed to mock Axios calls successfully resulting in async rendering errors on `getByText`.
- Logged these issues into `HANDOFF.md` to notify that there are errors.

