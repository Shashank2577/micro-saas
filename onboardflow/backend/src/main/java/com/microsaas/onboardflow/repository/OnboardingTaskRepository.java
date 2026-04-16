package com.microsaas.onboardflow.repository;

import com.microsaas.onboardflow.model.OnboardingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OnboardingTaskRepository extends JpaRepository<OnboardingTask, UUID> {
    List<OnboardingTask> findByPlanIdAndStatus(UUID planId, OnboardingTask.Status status);
}
