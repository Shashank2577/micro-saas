package com.microsaas.competitorradar.repository;

import com.microsaas.competitorradar.model.CompetitorFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompetitorFeatureRepository extends JpaRepository<CompetitorFeature, UUID> {
    List<CompetitorFeature> findByTenantId(UUID tenantId);
}
