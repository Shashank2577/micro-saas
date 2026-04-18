package com.microsaas.voiceagentbuilder.repository;

import com.microsaas.voiceagentbuilder.model.CallLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CallLogRepository extends JpaRepository<CallLog, UUID> {
    List<CallLog> findByAgentIdAndTenantId(UUID agentId, UUID tenantId);
    List<CallLog> findByTenantId(UUID tenantId);
    Optional<CallLog> findByIdAndTenantId(UUID id, UUID tenantId);
}
