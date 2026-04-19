package com.microsaas.cashflowanalyzer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/cashflow/workflows")
public class WorkflowController {

    @PostMapping("/execute")
    public ResponseEntity<Map<String, String>> executeWorkflow() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Workflow executed asynchronously");
        return ResponseEntity.accepted().body(response);
    }
}
