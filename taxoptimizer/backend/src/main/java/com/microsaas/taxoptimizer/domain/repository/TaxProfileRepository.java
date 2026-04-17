package com.microsaas.taxoptimizer.domain.repository;

import com.microsaas.taxoptimizer.domain.entity.TaxProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaxProfileRepository extends JpaRepository<TaxProfile, UUID> {
    Optional<TaxProfile> findByTenantIdAndId(UUID tenantId, UUID id);
    Optional<TaxProfile> findByTenantIdAndUserId(UUID tenantId, UUID userId);
}
