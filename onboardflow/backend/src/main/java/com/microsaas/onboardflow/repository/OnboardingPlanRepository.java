package com.microsaas.onboardflow.repository;
import com.microsaas.onboardflow.model.OnboardingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface OnboardingPlanRepository extends JpaRepository<OnboardingPlan, UUID> {
    List<OnboardingPlan> findByTenantId(UUID tenantId);
    Optional<OnboardingPlan> findByIdAndTenantId(UUID id, UUID tenantId);
}
