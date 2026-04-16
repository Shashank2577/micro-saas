package com.microsaas.brandvoice.repository;

import com.microsaas.brandvoice.model.BrandProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrandProfileRepository extends JpaRepository<BrandProfile, UUID> {
    List<BrandProfile> findByTenantId(UUID tenantId);
    Optional<BrandProfile> findByIdAndTenantId(UUID id, UUID tenantId);
}
