## Questions Resolved During Build
- Q: Which specific A/B testing mechanism to use?
  A: Implemented via LiteLLM to optimize content based on a goal passed in variables `optimization_goal`.
- Q: What Redis library to use for rate limiting?
  A: Used `spring-boot-starter-data-redis` and `StringRedisTemplate` to implement simple counter-based rate limiting per user per channel per day.

## Assumptions
- SendGrid API: Used `sendgrid-java` library. If no key is provided (`dummy`), it simulates success.
- Twilio API: Used `twilio` library. If no credentials are provided (`dummy`), it simulates success.
- LiteLLM: Used simple REST template for interaction.

## Future Work
- [ ] Implement push notifications (FCM).
- [ ] More robust A/B testing framework collecting metrics per variant.
