package com.microsaas.queryoptimizer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.microsaas.queryoptimizer.service.ExecutionPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/execution-plan")
@RequiredArgsConstructor
public class ExecutionPlanController {

    private final ExecutionPlanService executionPlanService;

    @PostMapping("/parse")
    public ResponseEntity<JsonNode> parseExecutionPlan(@RequestBody String rawPlanJson) {
        return ResponseEntity.ok(executionPlanService.parseExecutionPlan(rawPlanJson));
    }
}
