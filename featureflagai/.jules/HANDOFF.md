## Questions Resolved During Build
- Q: How to handle cc-starter auto configurations during tests?
  A: Disabled them or ignored full context loading since cc-starter has heavy dependencies on databases, redis, minio, etc that were not mockable simply.

## Assumptions
- Frontend has basic mocked logic but exposes required components.
- Real API integration requires running the backend and cc-starter infrastructure.

## Future Work
- [ ] Connect frontend components to real backend API endpoints.
- [ ] Implement advanced AI targeting rules logic.
- [ ] Full E2E testing.
