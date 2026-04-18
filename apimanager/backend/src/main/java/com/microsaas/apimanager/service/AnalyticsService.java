package com.microsaas.apimanager.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.apimanager.model.ApiAnalytics;
import com.microsaas.apimanager.repository.ApiAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final ApiAnalyticsRepository analyticsRepository;

    public Map<String, Object> getSummary(UUID projectId) {
        List<ApiAnalytics> data = analyticsRepository.findByProjectIdAndTenantId(projectId, TenantContext.require().toString());
        
        long totalCalls = data.size();
        long errorCalls = data.stream().filter(a -> a.getStatusCode() >= 400).count();
        double errorRate = totalCalls > 0 ? (double) errorCalls / totalCalls : 0;
        
        double avgLatency = data.stream().mapToInt(ApiAnalytics::getLatencyMs).average().orElse(0);

        List<Map.Entry<String, Long>> topEndpoints = data.stream()
            .collect(Collectors.groupingBy(a -> a.getMethod() + " " + a.getPath(), Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .toList();

        return Map.of(
            "totalCalls", totalCalls,
            "errorRate", errorRate,
            "avgLatency", avgLatency,
            "topEndpoints", topEndpoints
        );
    }
}
