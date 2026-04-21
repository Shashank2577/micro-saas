package com.microsaas.experimentengine.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.experimentengine.domain.model.Goal;
import com.microsaas.experimentengine.domain.repository.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    private final GoalRepository goalRepository;

    @Autowired
    public GoalController(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @GetMapping
    public ResponseEntity<List<Goal>> getAllGoals() {
        return ResponseEntity.ok(goalRepository.findByTenantId(TenantContext.require()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Goal> getGoalById(@PathVariable UUID id) {
        return goalRepository.findByIdAndTenantId(id, TenantContext.require())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Goal> createGoal(@RequestBody Goal goal) {
        goal.setTenantId(TenantContext.require());
        return ResponseEntity.ok(goalRepository.save(goal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Goal> updateGoal(@PathVariable UUID id, @RequestBody Goal goal) {
        return goalRepository.findByIdAndTenantId(id, TenantContext.require())
                .map(existing -> {
                    goal.setId(existing.getId());
                    goal.setTenantId(existing.getTenantId());
                    return ResponseEntity.ok(goalRepository.save(goal));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable UUID id) {
        return goalRepository.findByIdAndTenantId(id, TenantContext.require())
                .map(existing -> {
                    goalRepository.delete(existing);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
