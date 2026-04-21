package com.microsaas.experimentengine.domain.repository;

import com.microsaas.experimentengine.domain.model.FeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeatureFlagRepository extends JpaRepository<FeatureFlag, UUID> {
    List<FeatureFlag> findByTenantId(UUID tenantId);
    Optional<FeatureFlag> findByIdAndTenantId(UUID id, UUID tenantId);
}
