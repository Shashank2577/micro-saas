package com.microsaas.onboardflow.controller;

import com.microsaas.onboardflow.model.OnboardingMilestone;
import com.microsaas.onboardflow.model.OnboardingPlan;
import com.microsaas.onboardflow.model.OnboardingTask;
import com.microsaas.onboardflow.service.OnboardingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class OnboardingPlanController {

    private final OnboardingService onboardingService;

    @PostMapping
    public ResponseEntity<OnboardingPlan> generatePlan(@RequestBody GeneratePlanRequest request) {
        return ResponseEntity.ok(onboardingService.generatePlan(
                request.employeeId(), request.role(), request.department(), request.startDate()));
    }

    @GetMapping
    public ResponseEntity<List<OnboardingPlan>> getAllPlans() {
        return ResponseEntity.ok(onboardingService.getAllPlans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OnboardingPlan> getPlan(@PathVariable UUID id) {
        return ResponseEntity.ok(onboardingService.getPlan(id));
    }

    @PutMapping("/{id}/tasks/{taskId}")
    public ResponseEntity<OnboardingTask> updateTaskStatus(
            @PathVariable UUID id,
            @PathVariable UUID taskId,
            @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(onboardingService.updateTaskStatus(id, taskId, request.status()));
    }

    @GetMapping("/{id}/blockers")
    public ResponseEntity<List<OnboardingTask>> getBlockedTasks(@PathVariable UUID id) {
        return ResponseEntity.ok(onboardingService.getBlockedTasks(id));
    }

    @PostMapping("/{id}/milestones/{day}/assess")
    public ResponseEntity<OnboardingMilestone> assessMilestone(@PathVariable UUID id, @PathVariable int day) {
        return ResponseEntity.ok(onboardingService.assessMilestone(id, day));
    }

    public record GeneratePlanRequest(UUID employeeId, String role, String department, LocalDate startDate) {}
    public record UpdateTaskRequest(OnboardingTask.Status status) {}
}
