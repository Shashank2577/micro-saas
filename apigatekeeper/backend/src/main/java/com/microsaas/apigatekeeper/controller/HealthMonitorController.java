package com.microsaas.apigatekeeper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthMonitorController {

    @GetMapping("/health")
    public ResponseEntity<String> getHealth() {
        return ResponseEntity.ok("{\"status\": \"UP\"}");
    }

    @GetMapping("/health/services")
    public ResponseEntity<String> getServiceHealth() {
        return ResponseEntity.ok("{\"services\": []}");
    }

    @GetMapping("/metrics/gateway")
    public ResponseEntity<String> getMetrics() {
        return ResponseEntity.ok("{\"metrics\": {}}");
    }
}
