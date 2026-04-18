# Handoff Notes

## Known Issues and Workarounds
- Testing with `@vitejs/plugin-react` inside Next.js App Router causes issues with preamble checks when the `page.tsx` starts with `"use client";`. We've added a sample test to verify Vitest functions, but deep component testing might require `swc` configurations or moving tests entirely to Jest + `@swc/jest`.
- The LiteLLM implementation simulates API calls. A valid `LLM_API_KEY` is required in production environments to correctly interact with GPT-3.5-turbo models.
- Multitenant isolation successfully integrates `com.crosscutting.starter.tenancy.TenantContext`. Wait, the tests required `TenantContext.set(UUID.randomUUID())` because `TenantContext.require()` expects a `UUID`, not a `String`. This is correctly updated in tests.
- Flyway migrations are updated correctly without duplicates. Duplicate migrations were removed.

## Future Work
- More comprehensive component integration tests using Cypress or Playwright.
- Actual distribution queue (pgmq) integration implementation. We've stubbed `distributeSurvey` inside the `SurveyService` since a background worker depends on specific `integration-manifest.json` event mapping logic not entirely modeled in synchronous code.
