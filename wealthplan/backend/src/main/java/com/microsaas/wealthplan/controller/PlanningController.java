package com.microsaas.wealthplan.controller;

import com.microsaas.wealthplan.dto.*;
import com.microsaas.wealthplan.entity.AllocationRecommendation;
import com.microsaas.wealthplan.entity.MonteCarloResult;
import com.microsaas.wealthplan.entity.Scenario;
import com.microsaas.wealthplan.service.AssetAllocationService;
import com.microsaas.wealthplan.service.InsuranceService;
import com.microsaas.wealthplan.service.MonteCarloService;
import com.microsaas.wealthplan.service.PlanningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/planning")
@RequiredArgsConstructor
public class PlanningController {
    private final PlanningService planningService;
    private final MonteCarloService monteCarloService;
    private final InsuranceService insuranceService;
    private final AssetAllocationService allocationService;

    @GetMapping("/retirement-readiness")
    public ResponseEntity<RetirementReadinessDto> calculateRetirementReadiness(
            @RequestParam int age,
            @RequestParam BigDecimal desiredIncome,
            @RequestParam BigDecimal currentSavings,
            @RequestParam BigDecimal monthlyContribution) {
        return ResponseEntity.ok(planningService.calculateRetirementReadiness(age, desiredIncome, currentSavings, monthlyContribution));
    }

    @PostMapping("/scenarios")
    public ResponseEntity<Scenario> createScenario(@RequestBody ScenarioDto dto) {
        return ResponseEntity.ok(planningService.createScenario(dto));
    }

    @PostMapping("/monte-carlo")
    public ResponseEntity<MonteCarloResult> runMonteCarlo(@RequestBody MonteCarloRequestDto dto) {
        return ResponseEntity.ok(monteCarloService.runSimulation(dto.getScenarioId()));
    }

    @GetMapping("/savings-rate")
    public ResponseEntity<SavingsRateDto> calculateSavingsRate(@RequestParam UUID goalId) {
        return ResponseEntity.ok(planningService.calculateSavingsRate(goalId));
    }

    @GetMapping("/insurance-gaps")
    public ResponseEntity<List<GapAnalysisDto>> analyzeInsuranceGaps() {
        return ResponseEntity.ok(insuranceService.analyzeGaps());
    }

    @GetMapping("/asset-allocation/optimize")
    public ResponseEntity<AllocationRecommendation> optimizeAllocation(
            @RequestParam int age,
            @RequestParam String riskTolerance) {
        return ResponseEntity.ok(allocationService.optimizeAllocation(age, riskTolerance));
    }
}
