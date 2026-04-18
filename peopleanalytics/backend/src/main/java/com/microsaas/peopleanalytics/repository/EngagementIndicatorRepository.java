package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.EngagementIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EngagementIndicatorRepository extends JpaRepository<EngagementIndicator, UUID> {
    List<EngagementIndicator> findByTenantId(UUID tenantId);
    Optional<EngagementIndicator> findByIdAndTenantId(UUID id, UUID tenantId);
}
