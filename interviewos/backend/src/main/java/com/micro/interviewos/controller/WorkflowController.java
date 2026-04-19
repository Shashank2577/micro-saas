package com.micro.interviewos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interviews/workflows")
public class WorkflowController {

    @PostMapping("/execute")
    public ResponseEntity<Map<String, String>> execute(@RequestHeader("X-Tenant-Id") UUID tenantId, @RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(Map.of("status", "executed"));
    }
}
