package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.TeamHealthMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface TeamHealthMetricRepository extends JpaRepository<TeamHealthMetric, UUID> {
    List<TeamHealthMetric> findByTenantId(UUID tenantId);
}
