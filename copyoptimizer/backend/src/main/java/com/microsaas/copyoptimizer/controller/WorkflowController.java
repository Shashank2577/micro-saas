package com.microsaas.copyoptimizer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/copy/workflows")
public class WorkflowController {

    @PostMapping("/execute")
    public ResponseEntity<Map<String, String>> execute(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody Map<String, Object> payload) {
        // Stub for workflow execution
        return ResponseEntity.ok(Map.of("status", "accepted"));
    }
}
