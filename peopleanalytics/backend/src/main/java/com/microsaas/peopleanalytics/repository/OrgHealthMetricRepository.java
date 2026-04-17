package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.OrgHealthMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrgHealthMetricRepository extends JpaRepository<OrgHealthMetric, UUID> {
    List<OrgHealthMetric> findByTenantId(UUID tenantId);
}
