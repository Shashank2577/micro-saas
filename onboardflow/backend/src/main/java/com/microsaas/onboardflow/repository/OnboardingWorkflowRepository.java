package com.microsaas.onboardflow.repository;

import com.microsaas.onboardflow.model.OnboardingWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OnboardingWorkflowRepository extends JpaRepository<OnboardingWorkflow, UUID> {
    List<OnboardingWorkflow> findByTenantId(UUID tenantId);
    Optional<OnboardingWorkflow> findByIdAndTenantId(UUID id, UUID tenantId);
}
