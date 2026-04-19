package com.micro.interviewos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interviews/metrics")
public class MetricsController {

    @GetMapping("/summary")
    public ResponseEntity<Map<String, String>> summary(@RequestHeader("X-Tenant-Id") UUID tenantId) {
        return ResponseEntity.ok(Map.of("status", "summary"));
    }
}
