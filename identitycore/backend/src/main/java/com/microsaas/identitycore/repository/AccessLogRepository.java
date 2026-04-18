package com.microsaas.identitycore.repository;

import com.microsaas.identitycore.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, UUID> {
    List<AccessLog> findByTenantId(UUID tenantId);
    List<AccessLog> findByTenantIdAndUserId(UUID tenantId, UUID userId);
}
