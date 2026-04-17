package com.microsaas.datalineagetracker.repository;

import com.microsaas.datalineagetracker.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findAllByTenantId(UUID tenantId);
    List<AuditLog> findAllByTenantIdAndAssetId(UUID tenantId, UUID assetId);
}
