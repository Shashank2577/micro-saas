## Questions Resolved During Build
- Q: How should we test the frontend without spinning up a live environment?
  A: Set up Vitest with React Testing Library and mocked API calls to verify component rendering logic.

## Assumptions
- AI model interaction is abstracted to return static success responses in development to prevent LiteLLM configuration blocking test runs. Can be substituted by uncommenting `aiService.generateText()` if the gateway is running.
- H2 Database configuration for backend tests successfully bypassed complex `cc-starter` requirements.

## Future Work
- [ ] Connect the "Create" buttons in the frontend UI to their respective API POST requests. Form fields are unmanaged.
- [ ] Implement explicit E2E tests covering `X-Tenant-ID` full multi-tenant isolation scenarios.
