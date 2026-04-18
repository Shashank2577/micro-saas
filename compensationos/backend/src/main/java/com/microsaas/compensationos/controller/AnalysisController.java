package com.microsaas.compensationos.controller;

import com.microsaas.compensationos.dto.BudgetForecastResponse;
import com.microsaas.compensationos.dto.PayEquityAnalysisResponse;
import com.microsaas.compensationos.service.BudgetingService;
import com.microsaas.compensationos.service.PayEquityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
@Tag(name = "Analysis & Dashboards")
public class AnalysisController {

    private final PayEquityService payEquityService;
    private final BudgetingService budgetingService;

    @GetMapping("/pay-equity")
    @Operation(summary = "Analyze pay equity by demographics")
    public ResponseEntity<PayEquityAnalysisResponse> getPayEquity(
            @RequestParam(required = false) String role) {
        return ResponseEntity.ok(payEquityService.analyzePayEquity(role));
    }

    @GetMapping("/budget-forecast")
    @Operation(summary = "Forecast compensation budget")
    public ResponseEntity<BudgetForecastResponse> getBudgetForecast(
            @RequestParam(defaultValue = "12") int months) {
        return ResponseEntity.ok(budgetingService.forecastBudget(months));
    }

    @GetMapping("/market-trends")
    @Operation(summary = "Get market compensation trends")
    public ResponseEntity<Map<String, Object>> getMarketTrends() {
        // Stub for dashboard visuals
        return ResponseEntity.ok(Map.of(
            "trendData", "Historical data points would go here."
        ));
    }
}
