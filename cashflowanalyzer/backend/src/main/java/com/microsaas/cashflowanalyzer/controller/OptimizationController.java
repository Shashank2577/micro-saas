package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.Recommendation;
import com.microsaas.cashflowanalyzer.model.RecurringCharge;
import com.microsaas.cashflowanalyzer.service.OptimizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/optimization")
public class OptimizationController {

    @Autowired
    private OptimizationService optimizationService;

    @GetMapping("/recurring")
    public ResponseEntity<List<RecurringCharge>> getRecurringCharges(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(optimizationService.findRecurringCharges(tenantId));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<Recommendation>> getRecommendations(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(optimizationService.getOptimizationRecommendations(tenantId));
    }
}
