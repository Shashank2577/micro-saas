package com.microsaas.auditvault.repository;

import com.microsaas.auditvault.model.ComplianceFinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComplianceFindingRepository extends JpaRepository<ComplianceFinding, UUID> {
    List<ComplianceFinding> findByTenantId(UUID tenantId);
    List<ComplianceFinding> findByTenantIdAndControlId(UUID tenantId, UUID controlId);
}
