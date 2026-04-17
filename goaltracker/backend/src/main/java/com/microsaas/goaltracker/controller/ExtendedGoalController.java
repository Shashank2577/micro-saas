package com.microsaas.goaltracker.controller;

import com.microsaas.goaltracker.entity.Goal;
import com.microsaas.goaltracker.entity.GoalSharing;
import com.microsaas.goaltracker.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
public class ExtendedGoalController {

    private final GoalService goalService;

    @PutMapping("/{id}")
    public ResponseEntity<Goal> updateGoal(@PathVariable UUID id, @RequestBody Goal goalDetails) {
        Goal goal = goalService.getGoal(id);

        // Allowed only if progress < 80% (business logic requirement)
        double progress = goal.getCurrentAmount().doubleValue() / goal.getTargetAmount().doubleValue();
        if (progress >= 0.8) {
            return ResponseEntity.badRequest().build();
        }

        goal.setTitle(goalDetails.getTitle());
        goal.setTargetAmount(goalDetails.getTargetAmount());
        goal.setDeadline(goalDetails.getDeadline());
        return ResponseEntity.ok(goalService.updateGoal(goal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable UUID id) {
        goalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<Goal> pauseGoal(@PathVariable UUID id) {
        Goal goal = goalService.getGoal(id);
        goal.setPaused(true);
        return ResponseEntity.ok(goalService.updateGoal(goal));
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<String> shareGoal(@PathVariable UUID id, @RequestBody GoalSharing shareReq) {
        // Mocking the sharing functionality
        return ResponseEntity.ok("Goal shared successfully with " + shareReq.getSharedWithEmail());
    }
}
