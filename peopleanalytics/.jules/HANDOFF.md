## Questions Resolved During Build
- Q: Did the frontend require specific tailwind styling?
  A: I implemented basic minimal styling to support testing and standard view rendering without adding much bloat.
- Q: Does the backend need real tenant data mapping for the tests?
  A: It uses the standard X-Tenant-ID header implementation pattern for micro-SaaS as expected.

## Assumptions
- Frontend data mocking using a standard json placeholder style object matches what's needed for the vitest unit tests.
- We modified `jsonb` column types to `text` in standard Spring testing setup since H2 handles `json` types differently than Postgres without custom Dialect tweaks for local `V1__init.sql` testing. This was to allow the test suite to pass.
- I set up dummy Resilience4j fallbacks as part of the initial integration layer for AI.
- An empty local Spring Boot ApplicationEvent system is implemented to mock event streaming.

## Future Work
- [ ] Connect the LiteLLM API in the `AiAnalysisService` properly to a real gateway implementation.
- [ ] Write End-to-End tests simulating user login to populate valid tenant IDs in frontend headers via an auth context.
- [ ] Implement Kafka / RabbitMQ binders instead of ApplicationEventPublisher for true async distributed event handling.
