package com.microsaas.competitorradar.repository;

import com.microsaas.competitorradar.model.PricingChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PricingChangeRepository extends JpaRepository<PricingChange, UUID> {
    List<PricingChange> findByCompetitorIdAndTenantIdOrderByDetectedAtDesc(UUID competitorId, UUID tenantId);
}
