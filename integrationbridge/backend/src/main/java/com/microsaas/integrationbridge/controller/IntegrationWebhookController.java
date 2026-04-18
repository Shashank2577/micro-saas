package com.microsaas.integrationbridge.controller;

import com.microsaas.integrationbridge.service.IntegrationWebhookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/webhooks")
public class IntegrationWebhookController {

    private final IntegrationWebhookService webhookService;

    public IntegrationWebhookController(IntegrationWebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/{provider}/{tenantId}/{integrationId}")
    public ResponseEntity<Void> handleWebhook(
            @PathVariable String provider,
            @PathVariable UUID tenantId,
            @PathVariable UUID integrationId,
            @RequestBody String payload) {
        webhookService.processWebhook(provider, integrationId, tenantId, payload);
        return ResponseEntity.ok().build();
    }
}
