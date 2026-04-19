package com.microsaas.hiresignal.controller;

import com.microsaas.hiresignal.service.AiWorkflowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/hiring")
@RequiredArgsConstructor
public class AiWorkflowController {

    private final AiWorkflowService service;

    @PostMapping("/ai/analyze")
    public ResponseEntity<Map<String, String>> analyze(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(service.analyze(request));
    }

    @PostMapping("/workflows/execute")
    public ResponseEntity<Map<String, String>> executeWorkflow(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(service.executeWorkflow(request));
    }

    @GetMapping("/metrics/summary")
    public ResponseEntity<Map<String, String>> getMetricsSummary() {
        return ResponseEntity.ok(service.getMetricsSummary());
    }
}
