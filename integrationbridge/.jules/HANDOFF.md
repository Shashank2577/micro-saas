# Handoff Notes

Mocked out missing webhook endpoints as the cc-starter package seems to auto-configure its own WebhookService which conflicts with ours, so we removed the one in backend/src/main/java/com/microsaas/integrationbridge/service/WebhookService.java
Also removed WebhookController due to conflict.
