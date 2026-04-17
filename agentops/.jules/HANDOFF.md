# Handoff Notes

## Questions Resolved During Build
- Q: Do we need a custom UI for agent trace output?
  A: For now, we are displaying the JSON output natively in a simple `pre` block, which is sufficient per the core spec.
- Q: Where does `AgentHealthMetric` and `CostAllocation` data come from?
  A: Assumed they are pre-aggregated by a cron job or analytics worker that we have stubbed/mocked via the DB. Future implementation should include an async background job or event consumer to compute these on a schedule.

## Assumptions
- `LiteLLMClient` AI loop detection was deferred for future phase since core functionality is focused on telemetry capturing. We just provide the basic endpoints to record the steps.
- Security uses `X-Tenant-ID` which conforms to the `cc-starter` standard mode.

## Future Work
- [ ] Implement AI agent anomaly detection (e.g. using LiteLLM to analyze loops).
- [ ] Write integration test verifying OpenAPI UI is generated accurately via swagger.
- [ ] Create E2E tests using Playwright.
- [ ] Ensure database scale optimization with heavy step output rows (e.g. partition by time or push payload to S3).

## Integration Notes
- Ensure keycloak realm "cc" has the appropriate client roles.
