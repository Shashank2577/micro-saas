# BudgetMaster Handoff Notes

## Assumptions Made
- The AI optimization features utilize the `AiService` from the `cc-starter` library. We're assuming the LiteLLM gateway responds correctly to the structured prompts.
- Transactions are recorded with `CLEARED` status by default. If pending transactions need separate handling, the schema supports it but the UI simplifies it.
- Categories use an Envelope budget paradigm, where money allocated is tracked per category. Alerting evaluates the threshold against these specific categories.
- User authentication and initial tenant provisioning are assumed to be handled by the ecosystem (via the `cc-starter` `TenantContext`). Testing uses a random/generated tenant context.

## Questions Encountered
- Q: What happens to rolling over amounts if a category carryover is enabled at the end of the month?
  A: Currently, this requires a background job to transfer balances. We've flagged `carryover=true` in the schema and UI. An async processor would be the next step.

## Future Work
- [ ] Implement end-of-month background job to process category carryover amounts.
- [ ] Implement family budget sharing controls in the frontend (e.g. member allowance UI).
- [ ] Extend testing coverage, particularly E2E integration tests.
