# Handoff Notes

## Questions Resolved During Build
- Q: Storage was unspecified (S3 or Minio).
  A: Created `LocalStorageService` to mock S3 storage using a mock s3 key. Requires real backend connection in prod.
- Q: LiteLLM classification simulation logic?
  A: Provided mock hardcoded keywords from the byte text passed into the mock AI Service.

## Assumptions
- PostgreSQL and Redis running via Docker-Compose. DB uses default credentials `postgres:postgres`.
- Tenant checking uses mock `00000000-0000-0000-0000-000000000001` UUID for endpoints in the Next.js API configuration and Java Spring Boot controllers.
- No actual AI models or Tesseract instances installed; they are simulated within simple Spring Boot services.
- `crosscutting` dependency `cc-starter` requires `mvn install` on the root `/app/cross-cutting/pom.xml` before building if it causes missing dependency errors.
- Vitest fails reading DOM API because mocking `axios` fails returning properly in the `vitest` async DOM rendering loops. Left for human fixing.

## Future Work
- [ ] Connect `LocalStorageService` to actual `MinIO` endpoints utilizing AWS SDK.
- [ ] Incorporate real Tesseract OCR processing into `AIService`.
- [ ] Correct API mocking in Next.js `page.test.tsx` to fix the test suite.
- [ ] Fix Spring Boot integration testing failing due to lack of repository autowiring in services from cc-starter loading conflicts.

