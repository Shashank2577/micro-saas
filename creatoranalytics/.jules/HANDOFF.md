## Questions Resolved During Build
- Mocked tenantId `00000000-0000-0000-0000-000000000000` in controllers since auth context isn't fully integrated yet.
- Generic metadata is stored as JSONB using hypersistence-utils.

## Assumptions
- LiteLLM calls and actual workflow trigger implementations are stubbed as basic endpoints.
- Integration event publishing requires setting up Spring Modulith or a message broker.

## Future Work
- [ ] Connect frontend to actual API endpoints via React Query/SWR
- [ ] Implement robust error handling and global exception handler
- [ ] Setup actual LiteLLM circuit breaker
- [ ] Full E2E tests
