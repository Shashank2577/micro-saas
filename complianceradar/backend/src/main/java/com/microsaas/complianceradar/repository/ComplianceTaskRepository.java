package com.microsaas.complianceradar.repository;

import com.microsaas.complianceradar.domain.ComplianceTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComplianceTaskRepository extends JpaRepository<ComplianceTask, UUID> {
    List<ComplianceTask> findByTenantId(UUID tenantId);
    Optional<ComplianceTask> findByIdAndTenantId(UUID id, UUID tenantId);
}
