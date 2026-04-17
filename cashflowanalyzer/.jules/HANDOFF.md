## Questions Resolved During Build
- Q: How to handle initial Plaid sync without an actual API token?
  A: Created mock data generation within `AccountAggregationService.syncTransactions` and `connectAccount`.

## Assumptions
- Auth is mocked via cc-starter properties (`cc.auth.enabled: false`) for internal dev.
- LiteLLM configuration uses `http://localhost:4000` assuming a local instance is running as per memory.
- Using Vitest instead of Jest for Next.js testing for a faster setup.

## Future Work
- Implement actual Plaid client connection.
- Implement real ML model training feedback loop (currently LiteLLM is used directly for prompt-based categorization).
- Add E2E tests using Cypress or Playwright.
