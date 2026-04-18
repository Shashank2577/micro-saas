package com.microsaas.vendormonitor.repository;

import com.microsaas.vendormonitor.domain.PerformanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PerformanceRecordRepository extends JpaRepository<PerformanceRecord, UUID> {
    List<PerformanceRecord> findByTenantId(UUID tenantId);
    List<PerformanceRecord> findByTenantIdAndVendorId(UUID tenantId, UUID vendorId);
    List<PerformanceRecord> findByTenantIdAndVendorIdAndRecordedAtAfter(UUID tenantId, UUID vendorId, ZonedDateTime after);
    Optional<PerformanceRecord> findByIdAndTenantId(UUID id, UUID tenantId);
}
