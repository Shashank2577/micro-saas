package com.microsaas.expenseintelligence.controller;

import com.microsaas.expenseintelligence.model.Expense;
import com.microsaas.expenseintelligence.service.PolicyCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policy-checks")
@RequiredArgsConstructor
public class PolicyCheckController {

    private final PolicyCheckService policyCheckService;

    @PostMapping("/check")
    public ResponseEntity<Void> checkExpense(@RequestBody Expense expense) {
        policyCheckService.checkExpense(expense);
        return ResponseEntity.ok().build();
    }
}
