package com.microsaas.vendormonitor.repository;

import com.microsaas.vendormonitor.domain.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {
    List<Alert> findByTenantId(UUID tenantId);
    List<Alert> findByTenantIdAndStatus(UUID tenantId, String status);
    List<Alert> findByTenantIdAndVendorId(UUID tenantId, UUID vendorId);
    Optional<Alert> findByIdAndTenantId(UUID id, UUID tenantId);
}
