package com.microsaas.onboardflow.repository;

import com.microsaas.onboardflow.model.OnboardingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OnboardingPlanRepository extends JpaRepository<OnboardingPlan, UUID> {
}
