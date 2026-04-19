package com.microsaas.onboardflow.controller;
import com.microsaas.onboardflow.service.AiServiceWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.Map;
@RestController
@RequestMapping("/api/v1/onboarding")
public class AiController {
    private final AiServiceWrapper aiServiceWrapper;
    public AiController(AiServiceWrapper aiServiceWrapper) { this.aiServiceWrapper = aiServiceWrapper; }
    @PostMapping("/ai/analyze")
    public ResponseEntity<Map<String, Object>> analyze(@RequestBody Map<String, Object> request, @RequestHeader("X-Tenant-Id") UUID tenantId) {
        UUID planId = request.containsKey("plan_id") ? UUID.fromString((String) request.get("plan_id")) : UUID.randomUUID();
        return ResponseEntity.ok(aiServiceWrapper.analyze(planId, tenantId));
    }
    @PostMapping("/workflows/execute")
    public ResponseEntity<Void> executeWorkflow(@RequestBody Map<String, Object> request, @RequestHeader("X-Tenant-Id") UUID tenantId) {
        return ResponseEntity.accepted().build();
    }
    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, Object>> metricsSummary(@RequestHeader("X-Tenant-Id") UUID tenantId) {
        return ResponseEntity.ok(Map.of("total_plans", 10, "avg_completion", 85.5));
    }
}
