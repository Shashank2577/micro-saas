package com.tenantmanager.repository;

import com.tenantmanager.domain.OnboardingMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OnboardingMilestoneRepository extends JpaRepository<OnboardingMilestone, UUID> {
    List<OnboardingMilestone> findByCustomerTenantIdAndTenantId(UUID customerTenantId, UUID tenantId);
    Optional<OnboardingMilestone> findByIdAndTenantId(UUID id, UUID tenantId);
}
