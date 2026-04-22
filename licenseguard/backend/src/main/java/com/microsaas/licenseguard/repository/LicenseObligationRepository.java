package com.microsaas.licenseguard.repository;

import com.microsaas.licenseguard.domain.LicenseObligation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LicenseObligationRepository extends JpaRepository<LicenseObligation, UUID> {
    List<LicenseObligation> findByTenantId(UUID tenantId);
    List<LicenseObligation> findByLicenseIdAndTenantId(UUID licenseId, UUID tenantId);
}
