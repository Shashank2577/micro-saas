package com.microsaas.onboardflow.service;

import com.crosscutting.ai.AiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.onboardflow.model.OnboardingMilestone;
import com.microsaas.onboardflow.model.OnboardingPlan;
import com.microsaas.onboardflow.model.OnboardingTask;
import com.microsaas.onboardflow.repository.OnboardingMilestoneRepository;
import com.microsaas.onboardflow.repository.OnboardingPlanRepository;
import com.microsaas.onboardflow.repository.OnboardingTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final OnboardingPlanRepository planRepository;
    private final OnboardingTaskRepository taskRepository;
    private final OnboardingMilestoneRepository milestoneRepository;
    private final AiService aiService;
    private final ObjectMapper objectMapper;

    @Transactional
    public OnboardingPlan generatePlan(UUID employeeId, String role, String department, LocalDate startDate) {
        String prompt = String.format("Generate a 30/60/90 day onboarding plan for a new %s in the %s department. " +
                "Return ONLY a valid JSON object with keys 'plan30Day', 'plan60Day', 'plan90Day', where each is an object containing " +
                "milestones, goals, and metrics.", role, department);

        String aiResponse = aiService.generateText(prompt);
        Map<String, Object> planData;
        try {
            planData = objectMapper.readValue(aiResponse, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            // Fallback empty plan if JSON parsing fails
            planData = Map.of(
                    "plan30Day", Map.of(),
                    "plan60Day", Map.of(),
                    "plan90Day", Map.of()
            );
        }

        OnboardingPlan plan = OnboardingPlan.builder()
                .employeeId(employeeId)
                .role(role)
                .department(department)
                .startDate(startDate)
                .plan30Day((Map<String, Object>) planData.getOrDefault("plan30Day", Map.of()))
                .plan60Day((Map<String, Object>) planData.getOrDefault("plan60Day", Map.of()))
                .plan90Day((Map<String, Object>) planData.getOrDefault("plan90Day", Map.of()))
                .status(OnboardingPlan.Status.DRAFT)
                .build();

        return planRepository.save(plan);
    }

    public List<OnboardingPlan> getAllPlans() {
        return planRepository.findAll();
    }

    public OnboardingPlan getPlan(UUID id) {
        return planRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Plan not found"));
    }

    @Transactional
    public OnboardingTask updateTaskStatus(UUID planId, UUID taskId, OnboardingTask.Status status) {
        OnboardingTask task = taskRepository.findById(taskId).orElseThrow(() -> new IllegalArgumentException("Task not found"));
        if (!task.getPlanId().equals(planId)) {
            throw new IllegalArgumentException("Task does not belong to this plan");
        }

        task.setStatus(status);
        if (status == OnboardingTask.Status.COMPLETED) {
            task.setCompletedAt(Instant.now());
        }

        return taskRepository.save(task);
    }

    public List<OnboardingTask> getBlockedTasks(UUID planId) {
        return taskRepository.findByPlanIdAndStatus(planId, OnboardingTask.Status.BLOCKED);
    }

    @Transactional
    public OnboardingMilestone assessMilestone(UUID planId, int day) {
        OnboardingPlan plan = getPlan(planId);
        
        String prompt = String.format("Assess the %d day milestone achievement for a %s in %s. Return a JSON object with a score and feedback.", day, plan.getRole(), plan.getDepartment());
        String aiResponse = aiService.generateText(prompt);

        Map<String, Object> metrics;
        try {
            metrics = objectMapper.readValue(aiResponse, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            metrics = Map.of("error", "Failed to parse AI response");
        }

        OnboardingMilestone milestone = milestoneRepository.findByPlanIdAndMilestoneDay(planId, day)
                .orElse(OnboardingMilestone.builder()
                        .planId(planId)
                        .milestoneDay(day)
                        .build());
        
        milestone.setMetrics(metrics);
        milestone.setAchievedAt(Instant.now());

        return milestoneRepository.save(milestone);
    }
}
