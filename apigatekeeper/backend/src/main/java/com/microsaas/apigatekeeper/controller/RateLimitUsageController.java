package com.microsaas.apigatekeeper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratelimit")
public class RateLimitUsageController {

    @GetMapping("/usage/{userId}")
    public ResponseEntity<String> getUsage(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String userId) {
        // Mock implementation for usage check
        return ResponseEntity.ok("{\"userId\": \"" + userId + "\", \"remaining\": 95, \"reset\": 1713456000}");
    }

    @PostMapping("/reset/{userId}")
    public ResponseEntity<Void> resetUsage(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @PathVariable String userId) {
        // Mock implementation for reset
        return ResponseEntity.ok().build();
    }
}
