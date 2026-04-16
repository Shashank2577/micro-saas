package com.microsaas.nexushub.event;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EcosystemEventRepository extends JpaRepository<EcosystemEvent, UUID> {
    List<EcosystemEvent> findByTenantIdOrderByCreatedAtDesc(UUID tenantId);
    List<EcosystemEvent> findByTenantIdAndEventTypeOrderByCreatedAtDesc(UUID tenantId, String eventType);
}
