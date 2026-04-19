package com.microsaas.apievolver.controller;

import com.microsaas.apievolver.service.AiAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/api-evolution/ai")
@RequiredArgsConstructor
public class AiAnalysisController {
    private final AiAnalysisService service;

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyze(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(service.analyze(request, tenantId));
    }
}
