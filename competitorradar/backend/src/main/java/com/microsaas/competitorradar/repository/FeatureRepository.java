package com.microsaas.competitorradar.repository;

import com.microsaas.competitorradar.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, UUID> {
    List<Feature> findByTenantId(UUID tenantId);
}
