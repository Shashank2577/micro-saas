package com.microsaas.regulatoryfiling.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/filings/workflows")
public class WorkflowController {

    @PostMapping("/execute")
    public Map<String, Object> execute(@RequestBody Map<String, Object> request) {
        return Map.of("status", "success", "executionId", "wf-1234");
    }
}
