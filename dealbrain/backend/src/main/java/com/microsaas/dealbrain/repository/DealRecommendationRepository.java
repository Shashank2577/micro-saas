package com.microsaas.dealbrain.repository;

import com.microsaas.dealbrain.model.DealRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DealRecommendationRepository extends JpaRepository<DealRecommendation, UUID> {
    List<DealRecommendation> findByTenantIdAndDealId(UUID tenantId, UUID dealId);
}
