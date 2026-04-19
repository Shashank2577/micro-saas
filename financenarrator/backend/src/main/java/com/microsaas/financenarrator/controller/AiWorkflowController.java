package com.microsaas.financenarrator.controller;

import com.microsaas.financenarrator.service.AiOrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/narratives")
@RequiredArgsConstructor
public class AiWorkflowController {

    private final AiOrchestrationService aiOrchestrationService;

    @PostMapping("/ai/analyze")
    public ResponseEntity<String> analyze(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(aiOrchestrationService.analyze(request));
    }

    @PostMapping("/ai/recommendations")
    public ResponseEntity<String> recommendations(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok(aiOrchestrationService.recommend(request));
    }

    @PostMapping("/workflows/execute")
    public ResponseEntity<String> executeWorkflow(@RequestBody Map<String, Object> request) {
        return ResponseEntity.ok("Workflow executed successfully for: " + request);
    }
}
