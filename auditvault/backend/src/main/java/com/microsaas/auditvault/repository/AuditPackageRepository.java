package com.microsaas.auditvault.repository;

import com.microsaas.auditvault.model.AuditPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditPackageRepository extends JpaRepository<AuditPackage, UUID> {
    List<AuditPackage> findByTenantId(UUID tenantId);
}
