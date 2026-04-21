package com.microsaas.goaltracker.service;

import com.microsaas.goaltracker.entity.Goal;
import com.microsaas.goaltracker.entity.Milestone;
import com.microsaas.goaltracker.repository.GoalRepository;
import com.microsaas.goaltracker.repository.MilestoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final MilestoneRepository milestoneRepository;

    @Transactional
    public Goal createGoal(Goal goal) {
        if (goal.getCurrentAmount() == null) {
            goal.setCurrentAmount(BigDecimal.ZERO);
        }
        Goal savedGoal = goalRepository.save(goal);

        // Generate milestones (25%, 50%, 75%, 100%)
        createMilestone(savedGoal, new BigDecimal("25.0"));
        createMilestone(savedGoal, new BigDecimal("50.0"));
        createMilestone(savedGoal, new BigDecimal("75.0"));
        createMilestone(savedGoal, new BigDecimal("100.0"));

        return savedGoal;
    }

    private void createMilestone(Goal goal, BigDecimal percentage) {
        BigDecimal amount = goal.getTargetAmount().multiply(percentage).divide(new BigDecimal("100.0"));
        Milestone milestone = Milestone.builder()
                .goal(goal)
                .percentage(percentage)
                .amount(amount)
                .achieved(false)
                .build();
        milestoneRepository.save(milestone);
    }

    public List<Goal> getGoalsByTenant(String tenantId) {
        return goalRepository.findByTenantId(tenantId);
    }

    public Goal getGoal(UUID id) {
        return goalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));
    }

    @Transactional
    public Goal updateGoal(UUID id, Goal goalDetails) {
        Goal goal = getGoal(id);
        goal.setTitle(goalDetails.getTitle());
        goal.setCategory(goalDetails.getCategory());
        goal.setTargetAmount(goalDetails.getTargetAmount());
        goal.setDeadline(goalDetails.getDeadline());
        goal.setPriority(goalDetails.getPriority());
        goal.setStatus(goalDetails.getStatus());
        return goalRepository.save(goal);
    }

    @Transactional
    public void deleteGoal(UUID id) {
        goalRepository.deleteById(id);
    }
}
