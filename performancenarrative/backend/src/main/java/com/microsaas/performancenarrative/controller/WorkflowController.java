package com.microsaas.performancenarrative.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/performance/workflows")
public class WorkflowController {

    @PostMapping("/execute")
    public Map<String, String> execute(@RequestBody Map<String, Object> payload) {
        return Map.of("status", "EXECUTING", "workflowId", "wf-12345");
    }
}
