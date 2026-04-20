package com.microsaas.onboardflow.repository;

import com.microsaas.onboardflow.model.OnboardingFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OnboardingFeedbackRepository extends JpaRepository<OnboardingFeedback, UUID> {
    List<OnboardingFeedback> findByTenantId(UUID tenantId);
    Optional<OnboardingFeedback> findByIdAndTenantId(UUID id, UUID tenantId);
}
