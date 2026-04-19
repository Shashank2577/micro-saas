package com.microsaas.copyoptimizer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/copy")
public class HealthController {

    @GetMapping("/health/contracts")
    public ResponseEntity<Map<String, String>> contracts() {
        return ResponseEntity.ok(Map.of("status", "healthy"));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, Object>> summary() {
        return ResponseEntity.ok(Map.of("processed", 100));
    }
}
