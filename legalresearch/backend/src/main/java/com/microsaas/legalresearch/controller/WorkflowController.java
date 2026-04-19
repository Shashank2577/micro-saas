package com.microsaas.legalresearch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/research/workflows")
public class WorkflowController {

    @PostMapping("/execute")
    public ResponseEntity<Map<String, String>> execute(@RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(Map.of("status", "workflow_queued"));
    }
}
