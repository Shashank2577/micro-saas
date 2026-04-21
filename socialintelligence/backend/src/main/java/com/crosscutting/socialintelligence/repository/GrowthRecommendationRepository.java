package com.crosscutting.socialintelligence.repository;

import com.crosscutting.socialintelligence.domain.GrowthRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GrowthRecommendationRepository extends JpaRepository<GrowthRecommendation, UUID> {
    List<GrowthRecommendation> findByTenantId(UUID tenantId);
}
