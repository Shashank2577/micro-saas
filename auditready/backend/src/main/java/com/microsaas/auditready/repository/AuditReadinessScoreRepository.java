package com.microsaas.auditready.repository;

import com.microsaas.auditready.domain.AuditReadinessScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuditReadinessScoreRepository extends JpaRepository<AuditReadinessScore, UUID> {
    List<AuditReadinessScore> findByFrameworkIdAndTenantId(UUID frameworkId, UUID tenantId);
    Optional<AuditReadinessScore> findFirstByFrameworkIdAndTenantIdOrderByCalculatedAtDesc(UUID frameworkId, UUID tenantId);
}
