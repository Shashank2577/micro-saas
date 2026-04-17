package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.model.PerformanceMetric;
import com.microsaas.peopleanalytics.model.Employee;
import com.microsaas.peopleanalytics.repository.PerformanceMetricRepository;
import com.microsaas.peopleanalytics.repository.EmployeeRepository;
import com.microsaas.peopleanalytics.dto.PerformanceMetricDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerformanceAggregationService {

    private final PerformanceMetricRepository metricRepository;
    private final EmployeeRepository employeeRepository;

    public List<PerformanceMetric> getMetrics(UUID tenantId) {
        return metricRepository.findByTenantId(tenantId);
    }

    @Transactional
    public PerformanceMetric addMetric(UUID tenantId, PerformanceMetricDto dto) {
        Employee emp = employeeRepository.findById(dto.getEmployeeId())
                .filter(e -> e.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        PerformanceMetric metric = new PerformanceMetric();
        metric.setTenantId(tenantId);
        metric.setEmployee(emp);
        metric.setMetricType(dto.getMetricType());
        metric.setMetricName(dto.getMetricName());
        metric.setMetricValue(dto.getMetricValue());
        metric.setMetricDate(dto.getMetricDate());
        metric.setNotes(dto.getNotes());
        return metricRepository.save(metric);
    }
}
