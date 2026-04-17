package com.microsaas.brandvoice.repository;

import com.microsaas.brandvoice.entity.AuditResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuditResultRepository extends JpaRepository<AuditResult, UUID> {
    Optional<AuditResult> findByContentAuditIdAndTenantId(UUID contentAuditId, UUID tenantId);
    List<AuditResult> findByTenantIdOrderByCreatedAtDesc(UUID tenantId);
}
