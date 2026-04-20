package com.microsaas.cashflowanalyzer.controller;

import com.microsaas.cashflowanalyzer.model.CashFlowSnapshot;
import com.microsaas.cashflowanalyzer.service.CashFlowAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private CashFlowAnalysisService cashFlowAnalysisService;

    @GetMapping("/cash-flow")
    public ResponseEntity<CashFlowSnapshot> getCashFlowStatement(@RequestHeader("X-Tenant-ID") String tenantId, @RequestParam(required = false) String month) {
        LocalDate date = month != null ? LocalDate.parse(month) : LocalDate.now();
        return ResponseEntity.ok(cashFlowAnalysisService.generateCashFlowStatement(tenantId, date));
    }
}
