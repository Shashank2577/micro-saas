package com.microsaas.seointelligence.repository;

import com.microsaas.seointelligence.model.SeoAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SeoAuditRepository extends JpaRepository<SeoAudit, UUID> {
    List<SeoAudit> findByTenantId(String tenantId);
}
