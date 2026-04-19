package com.microsaas.debtnavigator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/debt/workflows")
public class WorkflowController {
    @PostMapping("/execute")
    public ResponseEntity<String> execute() {
        return ResponseEntity.ok("Workflow executed");
    }
}
