package com.microsaas.telemetrycore.repository;

import com.microsaas.telemetrycore.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByTenantIdAndTimestampBetween(UUID tenantId, ZonedDateTime start, ZonedDateTime end);
    List<Event> findByTenantIdAndSessionIdOrderByTimestampAsc(UUID tenantId, String sessionId);
    void deleteByTenantIdAndUserId(UUID tenantId, String userId);
}
