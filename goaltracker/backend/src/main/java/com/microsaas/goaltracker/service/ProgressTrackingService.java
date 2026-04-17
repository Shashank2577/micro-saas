package com.microsaas.goaltracker.service;

import com.microsaas.goaltracker.entity.Contribution;
import com.microsaas.goaltracker.entity.Goal;
import com.microsaas.goaltracker.entity.Milestone;
import com.microsaas.goaltracker.repository.ContributionRepository;
import com.microsaas.goaltracker.repository.GoalRepository;
import com.microsaas.goaltracker.repository.MilestoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressTrackingService {

    private final ContributionRepository contributionRepository;
    private final GoalRepository goalRepository;
    private final MilestoneRepository milestoneRepository;

    @Transactional
    public Contribution recordContribution(Goal goal, Contribution contribution) {
        contribution.setGoal(goal);
        contribution.setContributionDate(LocalDateTime.now());
        contribution.setStatus("COMPLETED");

        Contribution saved = contributionRepository.save(contribution);

        goal.setCurrentAmount(goal.getCurrentAmount().add(contribution.getAmount()));

        checkMilestones(goal);

        goalRepository.save(goal);
        return saved;
    }

    private void checkMilestones(Goal goal) {
        List<Milestone> milestones = milestoneRepository.findByGoalId(goal.getId());
        for (Milestone milestone : milestones) {
            if (!milestone.getAchieved() && goal.getCurrentAmount().compareTo(milestone.getAmount()) >= 0) {
                milestone.setAchieved(true);
                milestone.setAchievedAt(LocalDateTime.now());
                milestoneRepository.save(milestone);
            }
        }
    }
}
