package com.microsaas.licenseguard.repository;

import com.microsaas.licenseguard.domain.LicenseViolation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LicenseViolationRepository extends JpaRepository<LicenseViolation, UUID> {
    List<LicenseViolation> findByRepositoryIdAndTenantId(UUID repositoryId, UUID tenantId);
}
