package com.microsaas.peopleanalytics.repository;

import com.microsaas.peopleanalytics.model.PerformanceMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PerformanceMetricRepository extends JpaRepository<PerformanceMetric, UUID> {
    List<PerformanceMetric> findAllByTenantId(UUID tenantId);
    List<PerformanceMetric> findAllByEmployeeIdAndTenantId(UUID employeeId, UUID tenantId);
    List<PerformanceMetric> findAllByTenantIdAndMetricDateBetween(UUID tenantId, LocalDate start, LocalDate end);
}
