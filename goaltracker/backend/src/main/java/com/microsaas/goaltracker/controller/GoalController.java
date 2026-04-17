package com.microsaas.goaltracker.controller;

import com.microsaas.goaltracker.entity.AutomatedTransfer;
import com.microsaas.goaltracker.entity.Contribution;
import com.microsaas.goaltracker.entity.Goal;
import com.microsaas.goaltracker.entity.Milestone;
import com.microsaas.goaltracker.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;
    private final SavingsCalculationService savingsCalculationService;
    private final AIRecommendationService aiRecommendationService;
    private final TransferAutomationService transferAutomationService;
    private final ProgressTrackingService progressTrackingService;

    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestHeader("X-Tenant-ID") String tenantId,
                                           @RequestBody Goal goal) {
        goal.setTenantId(tenantId);
        goal.setUserId("user-123");
        goal.setStatus("ACTIVE");
        return ResponseEntity.ok(goalService.createGoal(goal));
    }

    @GetMapping
    public ResponseEntity<List<Goal>> getGoals(@RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(goalService.getGoalsByTenant(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Goal> getGoal(@PathVariable UUID id) {
        return ResponseEntity.ok(goalService.getGoal(id));
    }

    @PostMapping("/{id}/contributions")
    public ResponseEntity<Contribution> recordContribution(@PathVariable UUID id, @RequestBody Contribution contribution) {
        Goal goal = goalService.getGoal(id);
        return ResponseEntity.ok(progressTrackingService.recordContribution(goal, contribution));
    }

    @PostMapping("/{id}/transfers")
    public ResponseEntity<AutomatedTransfer> setupTransfer(@PathVariable UUID id, @RequestBody AutomatedTransfer transfer) {
        Goal goal = goalService.getGoal(id);
        return ResponseEntity.ok(transferAutomationService.setupTransfer(goal, transfer));
    }

    @GetMapping("/{id}/savings-plan")
    public ResponseEntity<Map<String, Object>> getSavingsPlan(@PathVariable UUID id) {
        Goal goal = goalService.getGoal(id);
        BigDecimal monthlySavings = savingsCalculationService.calculateMonthlySavings(goal);
        return ResponseEntity.ok(Map.of(
                "goalId", id,
                "monthlySavingsRequired", monthlySavings
        ));
    }

    @GetMapping("/{id}/nudge")
    public ResponseEntity<Map<String, String>> getNudge(@PathVariable UUID id) {
        Goal goal = goalService.getGoal(id);
        String nudge = aiRecommendationService.generateMotivationNudge(goal);
        return ResponseEntity.ok(Map.of("nudge", nudge));
    }

    @GetMapping("/{id}/investments")
    public ResponseEntity<Map<String, String>> getInvestmentRecommendation(@PathVariable UUID id) {
        Goal goal = goalService.getGoal(id);
        String recommendation = aiRecommendationService.suggestInvestment(goal);
        return ResponseEntity.ok(Map.of("recommendation", recommendation));
    }
}
