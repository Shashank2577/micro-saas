package com.microsaas.observabilitystack.repository;

import com.microsaas.observabilitystack.entity.IncidentTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IncidentTimelineRepository extends JpaRepository<IncidentTimeline, UUID> {
    List<IncidentTimeline> findByTenantId(String tenantId);
    Optional<IncidentTimeline> findByIdAndTenantId(UUID id, String tenantId);
}
