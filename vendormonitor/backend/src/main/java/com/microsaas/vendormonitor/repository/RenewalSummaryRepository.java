package com.microsaas.vendormonitor.repository;

import com.microsaas.vendormonitor.domain.RenewalSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RenewalSummaryRepository extends JpaRepository<RenewalSummary, UUID> {
    List<RenewalSummary> findByTenantId(UUID tenantId);
    List<RenewalSummary> findByTenantIdAndVendorId(UUID tenantId, UUID vendorId);
    Optional<RenewalSummary> findByIdAndTenantId(UUID id, UUID tenantId);
}
