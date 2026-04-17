package com.microsaas.apigatekeeper.controller;

import com.microsaas.apigatekeeper.entity.Webhook;
import com.microsaas.apigatekeeper.entity.WebhookDelivery;
import com.microsaas.apigatekeeper.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService service;

    @PostMapping("/register")
    public ResponseEntity<Webhook> registerWebhook(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestBody Webhook webhook) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerWebhook(tenantId, webhook));
    }

    @GetMapping
    public ResponseEntity<List<Webhook>> getWebhooks(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(service.getWebhooks(tenantId));
    }

    @GetMapping("/{id}/deliveries")
    public ResponseEntity<List<WebhookDelivery>> getDeliveries(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable UUID id) {
        return ResponseEntity.ok(service.getDeliveries(tenantId, id));
    }
}
