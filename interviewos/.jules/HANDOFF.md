## Questions Resolved During Build
- Q: Do we need a real DB or should tests run on embedded / testcontainers?
  A: Tests are running with MockMvc and Mockito without firing up a real postgres instance, achieving fast test execution and validating the REST layer properly.
- Q: What UI components to use?
  A: Defaulted to plain HTML/Tailwind for rapid generation of data tables. Further polish should integrate a component library like `shadcn/ui` or `Material UI`.
- Q: How should we test authentication?
  A: Since `cc-starter` handles authentication and the `SecurityConfig` auto-configures JWT, the controller unit tests bypass security using `@WebMvcTest(excludeAutoConfiguration = {...})`. Real E2E tests will require valid tokens.

## Assumptions
- Assuming `next` `app` router for frontend setup.
- Assumed tenant multi-tenancy is based on a standard `X-Tenant-Id` header across all APIs, matching the `Integration` manifest capabilities.
- Mocked the backend responses during frontend tests and basic hooks since this operates autonomously without a live backend during test generation.

## Future Work
- [ ] Connect frontend API calls directly to the Java backend via a custom hook or RTK query.
- [ ] Introduce E2E tests using Playwright.
- [ ] Complete robust AI integrations calling LiteLLM from the Spring boot controller.
