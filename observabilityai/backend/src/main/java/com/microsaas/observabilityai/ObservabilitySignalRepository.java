package com.microsaas.observabilityai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ObservabilitySignalRepository extends JpaRepository<ObservabilitySignal, UUID> {
    List<ObservabilitySignal> findByTenantId(UUID tenantId);
    List<ObservabilitySignal> findByTenantIdAndSignalType(UUID tenantId, String signalType);
    List<ObservabilitySignal> findByTenantIdAndTraceId(UUID tenantId, String traceId);
}
