package com.microsaas.onboardflow.repository;

import com.microsaas.onboardflow.model.OnboardingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OnboardingTaskRepository extends JpaRepository<OnboardingTask, UUID> {
    List<OnboardingTask> findByTenantId(UUID tenantId);
    Optional<OnboardingTask> findByIdAndTenantId(UUID id, UUID tenantId);
}
