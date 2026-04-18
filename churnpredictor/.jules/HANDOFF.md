# Handoff Notes

## Questions Resolved
- Q: Did not specify how AI offers are formatted for email exactly.
  A: Set up basic system prompts and text output formatting to return just plain text or simulated email body via the AiOfferService integration.
- Q: How often are predictions recalculated?
  A: Currently a manual `/recalculate` batch endpoint is provided, meant to be triggered by cron jobs eventually or an event-driven mechanism. Mock data fills this right now.

## Assumptions
- I assumed prediction models are completely mocked for this platform, avoiding setting up actual Python/Sklearn REST servers via Docker. They generate mocked probabilities on calculation.
- We opted out of Lombok on the entity/DTO classes due to persistent compilation pipeline issues locally in Maven; getters and setters have been statically generated instead to keep things clean and functional.

## Future Work
- Implement PgMQ async consumers/emitters natively to automate the prediction recalcs on `customer.usage.updated` events.
- Actually interface the LiteLLM component to an external key when not running locally.
- Stand up Python fastAPI microservice to execute real scikit-learn models.
