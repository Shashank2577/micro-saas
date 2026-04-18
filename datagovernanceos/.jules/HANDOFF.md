## Questions Resolved During Build
- Q: Which model should be used for classification?
  A: Set default to `claude-sonnet-4-6` with LiteLLM gateway fallback.
- Q: What UI components to use?
  A: Replaced standard generic templates with app-specific dashboard layout.

## Assumptions
- H2 in-memory DB used for `mvn test`.
- AI integration has error boundaries that default to 'CONFIDENTIAL/PII' if gateway fails.

## Future Work
- Implement actual deep data sampling for PII detection rather than metadata-only AI prompts.
- Extend integration endpoints to listen for specific webhooks from PipelineGuardian.
