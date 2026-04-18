# Handoff Notes

## Questions Resolved During Build
- Q: Do we need full user auth integration for keys? 
  A: Assumed developer keys tied to tenant scoping based on cc-starter configuration. Full OAuth/OIDC can be handled at the gateway layer later.

## Assumptions
- H2 memory database used for all test layers
- Standard testing library with Vitest selected for frontend testing
- Swagger schemas will be validated purely through parsing library constraints

## Future Work
- [ ] Add actual redis caching implementation for the loaded API schemas to reduce DB hits
- [ ] Build the deeper Analytics Dashboard using chart.js or Recharts
- [ ] Implement full API diffing for breaking changes via a detailed JSON path diffing
