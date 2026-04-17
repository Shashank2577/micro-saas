package com.microsaas.goaltracker.controller;

import com.microsaas.goaltracker.entity.Goal;
import com.microsaas.goaltracker.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

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
}
