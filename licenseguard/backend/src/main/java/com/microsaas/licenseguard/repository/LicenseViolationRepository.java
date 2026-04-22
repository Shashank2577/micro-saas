package com.microsaas.licenseguard.repository;

import com.microsaas.licenseguard.domain.LicenseViolation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LicenseViolationRepository extends JpaRepository<LicenseViolation, UUID> {
    List<LicenseViolation> findByRepositoryIdAndTenantId(UUID repositoryId, UUID tenantId);
}
