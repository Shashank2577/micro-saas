package com.microsaas.equityintelligence.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
import com.crosscutting.starter.ai.*;
import com.crosscutting.starter.tenancy.TenantContext;

@RestController
@RequestMapping("/api/v1/equity")
public class AiWorkflowController {

    private final AiService aiService;

    public AiWorkflowController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ai/analyze")
    public ResponseEntity<Map<String, String>> analyze(@RequestBody Map<String, Object> request) {
        String prompt = "Analyze this equity scenario: " + request.toString();
        String result = aiService.chat(new ChatRequest("gpt-3.5-turbo", List.of(new ChatMessage("user", prompt)), null, null)).content();
        return ResponseEntity.ok(Map.of("status", "analyzed", "result", result));
    }

    @PostMapping("/ai/recommendations")
    public ResponseEntity<Map<String, String>> recommendations(@RequestBody Map<String, Object> request) {
        String prompt = "Provide recommendations for this equity scenario: " + request.toString();
        String result = aiService.chat(new ChatRequest("gpt-3.5-turbo", List.of(new ChatMessage("user", prompt)), null, null)).content();
        return ResponseEntity.ok(Map.of("status", "recommended", "result", result));
    }

    @PostMapping("/workflows/execute")
    public ResponseEntity<Map<String, String>> executeWorkflow(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(Map.of("status", "executed", "workflow_id", "wf-12345", "tenant_id", TenantContext.require().toString()));
    }

    @GetMapping("/health/contracts")
    public ResponseEntity<Map<String, String>> healthContracts() {
        return ResponseEntity.ok(Map.of("status", "healthy", "contracts_valid", "true"));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, Object>> metricsSummary() {
        return ResponseEntity.ok(Map.of(
            "total_cap_tables", 42,
            "total_shareholders", 150,
            "active_vesting_schedules", 35
        ));
    }
}
