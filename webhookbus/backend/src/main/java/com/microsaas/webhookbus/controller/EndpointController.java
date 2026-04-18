package com.microsaas.webhookbus.controller;

import com.microsaas.webhookbus.entity.WebhookEndpoint;
import com.microsaas.webhookbus.service.EndpointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/endpoints")
public class EndpointController {

    private final EndpointService endpointService;

    public EndpointController(EndpointService endpointService) {
        this.endpointService = endpointService;
    }

    public static class CreateEndpointRequest {
        public String name;
        public String url;
    }

    @PostMapping
    public ResponseEntity<WebhookEndpoint> createEndpoint(@RequestBody CreateEndpointRequest request) {
        WebhookEndpoint endpoint = endpointService.createEndpoint(request.name, request.url);
        return ResponseEntity.ok(endpoint);
    }

    @GetMapping
    public ResponseEntity<List<WebhookEndpoint>> getEndpoints() {
        return ResponseEntity.ok(endpointService.getEndpoints());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebhookEndpoint> getEndpoint(@PathVariable UUID id) {
        return endpointService.getEndpoint(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
