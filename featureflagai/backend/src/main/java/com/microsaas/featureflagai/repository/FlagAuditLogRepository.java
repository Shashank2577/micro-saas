package com.microsaas.featureflagai.repository;

import com.microsaas.featureflagai.domain.FlagAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlagAuditLogRepository extends JpaRepository<FlagAuditLog, UUID> {
    List<FlagAuditLog> findByFlagIdAndTenantIdOrderByTimestampDesc(UUID flagId, UUID tenantId);
}
