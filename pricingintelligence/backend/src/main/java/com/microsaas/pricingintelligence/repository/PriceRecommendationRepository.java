package com.microsaas.pricingintelligence.repository;

import com.microsaas.pricingintelligence.domain.PriceRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PriceRecommendationRepository extends JpaRepository<PriceRecommendation, UUID> {
    List<PriceRecommendation> findByTenantId(UUID tenantId);
    List<PriceRecommendation> findByTenantIdAndSegmentId(UUID tenantId, UUID segmentId);
}
