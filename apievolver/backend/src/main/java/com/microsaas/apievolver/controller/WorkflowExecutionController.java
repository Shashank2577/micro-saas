package com.microsaas.apievolver.controller;

import com.microsaas.apievolver.service.WorkflowExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/api-evolution/workflows")
@RequiredArgsConstructor
public class WorkflowExecutionController {
    private final WorkflowExecutionService service;

    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> execute(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(service.execute(request, tenantId));
    }
}
