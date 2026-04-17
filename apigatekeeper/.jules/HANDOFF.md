## Questions Resolved During Build
- Q: Which spring cloud gateway dependency to use?
  A: Since cc-starter and standard microservices use Spring Web MVC (blocking servlet stack) rather than WebFlux, we used `spring-cloud-starter-gateway-mvc`. This ensures we don't encounter blocking vs non-blocking thread issues and can coexist with existing controllers.

## Assumptions
- We mocked AI interaction endpoints (LiteLLM) for configuration assist due to lack of a running endpoint in tests, assuming a future integration point.
- Only core endpoints from the spec were fully tested with comprehensive edge cases. We assume default configurations are reasonable and left advanced features like granular JWT processing to cc-starter integrations.

## Future Work
- [ ] Extend rate limiting to fully rely on a Redis backend cache rather than internal memory.
- [ ] Enhance Dashboard UI with more comprehensive ECharts for deep-dive analytics.
- [ ] Real LiteLLM proxy integration testing against `localhost:4000`.
- [ ] Expand Next.js e2e tests for different routing policy permutations.
