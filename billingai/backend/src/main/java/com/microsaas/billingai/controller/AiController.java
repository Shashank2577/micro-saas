package com.microsaas.billingai.controller;

import com.microsaas.billingai.service.AiClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class AiController {
    private final AiClientService aiService;

    @PostMapping("/ai/analyze")
    public String analyze(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody Map<String, String> request) {
        return aiService.analyze(request.getOrDefault("query", "default"));
    }

    @PostMapping("/workflows/execute")
    public String executeWorkflow(@RequestHeader("X-Tenant-ID") UUID tenantId, @RequestBody Map<String, String> request) {
        return aiService.executeWorkflow(request.getOrDefault("workflow", "default"));
    }

    @GetMapping("/metrics/summary")
    public String getSummary(@RequestHeader("X-Tenant-ID") UUID tenantId) {
        return aiService.getSummary();
    }
}
