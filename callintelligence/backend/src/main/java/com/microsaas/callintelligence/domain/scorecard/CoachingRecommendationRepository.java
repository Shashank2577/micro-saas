package com.microsaas.callintelligence.domain.scorecard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CoachingRecommendationRepository extends JpaRepository<CoachingRecommendation, UUID> {
    List<CoachingRecommendation> findByCallIdAndTenantId(UUID callId, UUID tenantId);
    List<CoachingRecommendation> findByRepIdAndTenantId(String repId, UUID tenantId);
}
