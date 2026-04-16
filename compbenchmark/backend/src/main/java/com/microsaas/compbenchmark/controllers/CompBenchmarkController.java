package com.microsaas.compbenchmark.controllers;

import com.microsaas.compbenchmark.model.CompGap;
import com.microsaas.compbenchmark.model.EmployeeComp;
import com.microsaas.compbenchmark.model.MarketBenchmark;
import com.microsaas.compbenchmark.model.PayEquityAudit;
import com.microsaas.compbenchmark.services.CompBenchmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CompBenchmarkController {

    private final CompBenchmarkService compBenchmarkService;

    @PostMapping("/employees/comp")
    public ResponseEntity<EmployeeComp> addEmployeeComp(@RequestBody EmployeeComp employeeComp) {
        return ResponseEntity.ok(compBenchmarkService.addEmployeeComp(employeeComp));
    }

    @GetMapping("/employees/comp")
    public ResponseEntity<List<EmployeeComp>> getEmployeeComps() {
        return ResponseEntity.ok(compBenchmarkService.getAllEmployeeComps());
    }

    @GetMapping("/benchmarks/{title}/{level}")
    public ResponseEntity<MarketBenchmark> getMarketBenchmark(@PathVariable String title, @PathVariable String level) {
        return compBenchmarkService.getMarketBenchmark(title, level)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/analysis/gaps")
    public ResponseEntity<List<CompGap>> computeGaps() {
        return ResponseEntity.ok(compBenchmarkService.computeGaps());
    }

    @GetMapping("/analysis/pay-equity")
    public ResponseEntity<List<PayEquityAudit>> getPayEquityAudit(@RequestParam(required = false) PayEquityAudit.GroupDimension dimension) {
        return ResponseEntity.ok(compBenchmarkService.getPayEquityAudit(dimension));
    }

    @GetMapping("/employees/{id}/offer-recommendation")
    public ResponseEntity<Map<String, BigDecimal>> getOfferRecommendation(@PathVariable String id) {
        try {
            return ResponseEntity.ok(compBenchmarkService.getOfferRecommendation(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
