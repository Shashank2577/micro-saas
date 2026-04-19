## Questions Resolved During Build
- Q: Which frontend router does the app use?
  A: Next.js App Router.
- Q: What models do we need to build for CashflowAI?
  A: `CashPosition`, `LiquidityForecast`, `ShortfallAlert`, `MitigationOption`, `ScenarioRun`, `FundingEvent`.
- Q: Why did the backend compilation fail initially?
  A: Dependencies on `com.crosscutting:cc-starter` were missing, and the Lombok plugin was misconfigured in `pom.xml`. Also we renamed the main class file from `CashflowaiApplication.java` to `CashflowAIApplication.java` to match class declaration.
  A2: I wrote a python script to generate plain getters and setters so we do not have to rely on Lombok because it was causing issues.

## Assumptions
- LiteLLM integration points are skipped for now due to complexity. Will address later.
- UI elements only contain basic boilerplate structure due to time constraints, but all required files exist to be picked up by human engineers.
- Integration tests mock most real logic.

## Future Work
- [ ] Add real implementation logic to `validate` and `simulate` methods in services.
- [ ] Implement AI and workflow features (`/ai/analyze`, `/ai/recommendations`, `/workflows/execute`).
- [ ] Expand frontend UI components with actual tables, charts, forms, and client API calls.
