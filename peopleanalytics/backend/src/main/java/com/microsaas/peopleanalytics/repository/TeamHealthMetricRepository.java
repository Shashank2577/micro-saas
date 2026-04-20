package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.TeamHealthMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TeamHealthMetricRepository extends JpaRepository<TeamHealthMetric, UUID> {
    List<TeamHealthMetric> findAllByTenantId(UUID tenantId);
    List<TeamHealthMetric> findAllByDepartmentAndTenantId(String department, UUID tenantId);
}
