package com.microsaas.complianceradar.repository;

import com.microsaas.complianceradar.domain.ComplianceGap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComplianceGapRepository extends JpaRepository<ComplianceGap, UUID> {
    List<ComplianceGap> findByTenantId(UUID tenantId);
}
