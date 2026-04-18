package com.microsaas.retailintelligence.repository;

import com.microsaas.retailintelligence.entity.PricingRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PricingRecommendationRepository extends JpaRepository<PricingRecommendation, UUID> {
    List<PricingRecommendation> findByTenantIdAndStatus(UUID tenantId, String status);
    Optional<PricingRecommendation> findByIdAndTenantId(UUID id, UUID tenantId);
}
