package com.microsaas.copyoptimizer.repository;

import com.microsaas.copyoptimizer.model.CopyVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CopyVariantRepository extends JpaRepository<CopyVariant, UUID> {
    List<CopyVariant> findByExperimentIdAndTenantId(UUID experimentId, UUID tenantId);
    Optional<CopyVariant> findByIdAndTenantId(UUID id, UUID tenantId);
}
