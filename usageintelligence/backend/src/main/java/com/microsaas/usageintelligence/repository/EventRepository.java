package com.microsaas.usageintelligence.repository;

import com.microsaas.usageintelligence.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByTenantId(UUID tenantId);
    List<Event> findByTenantIdAndCreatedAtAfter(UUID tenantId, LocalDateTime createdAt);
    List<Event> findByTenantIdAndEventName(UUID tenantId, String eventName);
}
