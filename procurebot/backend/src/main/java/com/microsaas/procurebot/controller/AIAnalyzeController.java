package com.microsaas.procurebot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/procurement")
public class AIAnalyzeController {

    @PostMapping("/ai/analyze")
    public ResponseEntity<String> analyze() {
        return ResponseEntity.ok("Analysis started");
    }

    @PostMapping("/workflows/execute")
    public ResponseEntity<String> executeWorkflow() {
        return ResponseEntity.ok("Workflow executed");
    }

    @RequestMapping("/metrics/summary")
    public ResponseEntity<String> metrics() {
        return ResponseEntity.ok("Metrics summary");
    }
}
