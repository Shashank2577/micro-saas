package com.microsaas.processminer.repository;

import com.microsaas.processminer.domain.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, UUID> {
    List<EventLog> findByTenantId(UUID tenantId);
    List<EventLog> findByTenantIdAndSystemType(UUID tenantId, String systemType);
    List<EventLog> findByTenantIdAndCaseId(UUID tenantId, String caseId);
    List<EventLog> findByTenantIdAndSystemTypeOrderByTimestampAsc(UUID tenantId, String systemType);
}
