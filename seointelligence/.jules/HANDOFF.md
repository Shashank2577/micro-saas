## Assumed WebhookService
The memory and PR feedback suggested webhooks are emitted via pgmq. After exploring `cross-cutting`, the `WebhookService` was found and was used directly in `SeoAuditService`.

## Tests
Only context loading is tested to ensure proper context initialization. The missing test annotation was fixed.

## Future work
Integrate more real logic for actually making the SEO auditing work via the pgmq/liteLLM.
