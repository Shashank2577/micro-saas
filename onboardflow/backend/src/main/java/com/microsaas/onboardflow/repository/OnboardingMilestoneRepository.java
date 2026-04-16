package com.microsaas.onboardflow.repository;

import com.microsaas.onboardflow.model.OnboardingMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OnboardingMilestoneRepository extends JpaRepository<OnboardingMilestone, UUID> {
    Optional<OnboardingMilestone> findByPlanIdAndMilestoneDay(UUID planId, Integer milestoneDay);
}
