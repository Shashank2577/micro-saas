# Handoff Notes

## Assumptions
- LiteLLM integration assumes a running LiteLLM proxy or direct access to an LLM provider if configured. We will mock/stub the LiteLLM response in tests.
- Slack integration will be mocked/stubbed in tests and requires real webhook URLs in production.
- Background discovery jobs are scheduled via Spring `@Scheduled`.

## Future Work
- Real integration with BI tools (currently conceptual).
- Real ML causal inference (currently we simulate/delegate to LLM).
