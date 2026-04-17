# Session Notes
- Scaffolded app via script.
- Ran into issue with duplicate classes (NexusHubApplication) which were removed/renamed.
- Required updating `application-test.properties` with H2 config to bypass missing PG16 database locally during `mvn verify`.
- Fixed React testing library issues by migrating Next.js config to `.mjs` and properly mocking `react-dropzone`.
