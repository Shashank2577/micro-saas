package com.microsaas.taxoptimizer.domain.repository;

import com.microsaas.taxoptimizer.domain.entity.TaxScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaxScenarioRepository extends JpaRepository<TaxScenario, UUID> {
    List<TaxScenario> findByTenantIdAndProfileId(UUID tenantId, UUID profileId);
    Optional<TaxScenario> findByTenantIdAndId(UUID tenantId, UUID id);
}
