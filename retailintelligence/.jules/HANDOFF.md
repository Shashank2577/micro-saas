# Handoff Notes

## Questions Resolved
- Q: Do we need a real historical data pipeline for demand forecasting?
  A: Assumed no pipeline exists yet; stubbed LiteLLM requests with prompt instructions for simulated forecasting based on basic inputs.

## Assumptions
- LiteLLM returns structured JSON.
- No external sales system is connected yet, so stock deductions via `sales.order.placed` are stubbed.

## Future Work
- Integration with real POS systems for live inventory tracking.
