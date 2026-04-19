package com.microsaas.apievolver.controller;

import com.microsaas.apievolver.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/api-evolution/metrics")
@RequiredArgsConstructor
public class MetricsController {
    private final MetricsService service;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummary(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return ResponseEntity.ok(service.getSummary(tenantId));
    }
}
