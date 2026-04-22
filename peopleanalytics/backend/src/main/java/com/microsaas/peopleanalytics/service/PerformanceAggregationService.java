package com.microsaas.peopleanalytics.service;

import com.microsaas.peopleanalytics.model.PerformanceMetric;
import com.microsaas.peopleanalytics.repository.PerformanceMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformanceAggregationService {
    private final PerformanceMetricRepository performanceMetricRepository;

    public Map<String, Object> getPerformanceDashboardData() {
        UUID tenantId = UUID.randomUUID();
        LocalDate now = LocalDate.now();
        LocalDate ninetyDaysAgo = now.minusDays(90);

        List<PerformanceMetric> metrics = performanceMetricRepository.findAllByTenantIdAndMetricDateBetween(
                tenantId, ninetyDaysAgo, now);

        Map<String, List<PerformanceMetric>> groupedByType = metrics.stream()
                .collect(Collectors.groupingBy(PerformanceMetric::getMetricType));

        Map<String, Object> dashboard = new HashMap<>();
        groupedByType.forEach((type, typeMetrics) -> {
            dashboard.put(type, aggregateMetrics(typeMetrics));
        });

        return dashboard;
    }

    private Map<String, Double> aggregateMetrics(List<PerformanceMetric> metrics) {
        double avgValue = metrics.stream()
                .mapToDouble(PerformanceMetric::getValue)
                .average()
                .orElse(0.0);
        double avgTarget = metrics.stream()
                .mapToDouble(PerformanceMetric::getTarget)
                .average()
                .orElse(0.0);

        return Map.of(
            "averageValue", avgValue,
            "averageTarget", avgTarget,
            "achievementRate", avgTarget > 0 ? (avgValue / avgTarget) * 100 : 0.0
        );
    }
}
