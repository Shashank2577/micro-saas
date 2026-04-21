# Session Notes - ConstructionIQ Stub

- Explored the codebase and found existing (very basic) project implementation.
- Decided to expand it into a proper stub with more entities (Site, Task, Incident) to make it "high-reliability".
- Encounted `cc-starter` dependency issues during tests. Resolved by installing `cc-starter` and its parent `cross-cutting` locally.
- Noticed missing `spring-security-test` when writing `@WebMvcTest`. Added it to `pom.xml`.
- Verified services with unit tests and controllers with mockmvc integration tests.
