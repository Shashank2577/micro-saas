package com.microsaas.featureflagai.repository;

import com.microsaas.featureflagai.domain.FeatureFlag;
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
