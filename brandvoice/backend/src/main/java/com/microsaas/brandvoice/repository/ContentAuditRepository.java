package com.microsaas.brandvoice.repository;

import com.microsaas.brandvoice.entity.ContentAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContentAuditRepository extends JpaRepository<ContentAudit, UUID> {
    List<ContentAudit> findByTenantIdOrderByCreatedAtDesc(UUID tenantId);
    Optional<ContentAudit> findByIdAndTenantId(UUID id, UUID tenantId);
}
