## Questions Resolved During Build
- Q: Did not find `TenantContext.require()` returning string; resolved it by ensuring correct parsing.
- Q: Spec assumes AI logs returning strict JSON shapes, implemented simple `{}` logic for now, needing proper integrations.
- Q: How does Okta ingestion work? Setup as standard POST access-logs.

## Assumptions
- LiteLLM integration endpoint `http://localhost:4000` is mock mapped. Real env will need to manage variables properly.
- All testing assumes simple string mappings of mock dependencies.
- Next.js UI is bare bones using basic Tailwind grids, needs UI polish in further iterations.

## Future Work
- [ ] Connect genuine LLM model testing instances.
- [ ] Incorporate E2E testing using Playwright.
- [ ] Enhance complex reporting aggregations.
