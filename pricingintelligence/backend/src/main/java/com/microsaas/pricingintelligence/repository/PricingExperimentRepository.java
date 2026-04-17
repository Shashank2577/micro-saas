package com.microsaas.pricingintelligence.repository;

import com.microsaas.pricingintelligence.domain.PricingExperiment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PricingExperimentRepository extends JpaRepository<PricingExperiment, UUID> {
    List<PricingExperiment> findByTenantId(UUID tenantId);
}
