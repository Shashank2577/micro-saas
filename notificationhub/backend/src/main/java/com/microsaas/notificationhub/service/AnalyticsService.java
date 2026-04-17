package com.microsaas.notificationhub.service;

import com.microsaas.notificationhub.domain.entity.NotificationDelivery;
import com.microsaas.notificationhub.domain.repository.NotificationDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final NotificationDeliveryRepository deliveryRepository;

    @Transactional(readOnly = true)
    public Map<String, Long> getDeliveryStats(String tenantId) {
        List<NotificationDelivery> deliveries = deliveryRepository.findByTenantId(tenantId);
        Map<String, Long> stats = new HashMap<>();

        long total = deliveries.size();
        long delivered = deliveries.stream().filter(d -> "DELIVERED".equals(d.getStatus())).count();
        long failed = deliveries.stream().filter(d -> "FAILED".equals(d.getStatus())).count();

        stats.put("total", total);
        stats.put("delivered", delivered);
        stats.put("failed", failed);
        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Double> getOpenRates(String tenantId) {
        List<NotificationDelivery> deliveries = deliveryRepository.findByTenantId(tenantId);
        long totalDelivered = deliveries.stream().filter(d -> "DELIVERED".equals(d.getStatus())).count();
        long opened = deliveries.stream().filter(d -> Boolean.TRUE.equals(d.getOpened())).count();

        Map<String, Double> stats = new HashMap<>();
        stats.put("openRate", totalDelivered > 0 ? (double) opened / totalDelivered * 100 : 0.0);
        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Double> getClickRates(String tenantId) {
        List<NotificationDelivery> deliveries = deliveryRepository.findByTenantId(tenantId);
        long totalDelivered = deliveries.stream().filter(d -> "DELIVERED".equals(d.getStatus())).count();
        long clicked = deliveries.stream().filter(d -> Boolean.TRUE.equals(d.getClicked())).count();

        Map<String, Double> stats = new HashMap<>();
        stats.put("clickRate", totalDelivered > 0 ? (double) clicked / totalDelivered * 100 : 0.0);
        return stats;
    }
}
