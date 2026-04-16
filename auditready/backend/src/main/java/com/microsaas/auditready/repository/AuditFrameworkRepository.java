package com.microsaas.auditready.repository;

import com.microsaas.auditready.domain.AuditFramework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuditFrameworkRepository extends JpaRepository<AuditFramework, UUID> {
    List<AuditFramework> findByTenantId(UUID tenantId);
    Optional<AuditFramework> findByIdAndTenantId(UUID id, UUID tenantId);
}
