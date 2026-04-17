package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.PerformanceMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;
import java.time.LocalDate;

public interface PerformanceMetricRepository extends JpaRepository<PerformanceMetric, UUID> {
    List<PerformanceMetric> findByTenantId(UUID tenantId);
    List<PerformanceMetric> findByTenantIdAndMetricDateAfter(UUID tenantId, LocalDate date);
    List<PerformanceMetric> findByTenantIdAndEmployeeId(UUID tenantId, UUID employeeId);
}
