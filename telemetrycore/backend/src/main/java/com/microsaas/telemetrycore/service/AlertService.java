package com.microsaas.telemetrycore.service;

import com.microsaas.telemetrycore.model.Alert;
import com.microsaas.telemetrycore.model.Metric;
import com.microsaas.telemetrycore.repository.AlertRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AlertService {
    @Autowired
    private AlertRepository alertRepository;

    @Transactional
    public Alert createAlert(Alert alert) {
        alert.setTenantId(TenantContext.require());
        alert.setCreatedAt(ZonedDateTime.now());
        alert.setUpdatedAt(ZonedDateTime.now());
        return alertRepository.save(alert);
    }

    @Transactional(readOnly = true)
    public List<Alert> getAllAlerts() {
        return alertRepository.findByTenantId(TenantContext.require());
    }

    @Transactional
    public void evaluateAlerts(Double metricValue, String metricName, UUID tenantId) {
        List<Alert> alerts = alertRepository.findByTenantId(tenantId);
        for (Alert alert : alerts) {
            if (alert.getMetric() != null && metricName.equals(alert.getMetric().getName())) {
                boolean triggered = false;
                if (">".equals(alert.getCondition()) && metricValue > alert.getThreshold()) {
                    triggered = true;
                } else if ("<".equals(alert.getCondition()) && metricValue < alert.getThreshold()) {
                    triggered = true;
                }

                if (triggered) {
                    System.out.println("Alert triggered: " + alert.getId() + " via " + alert.getNotificationChannel());
                }
            }
        }
    }
}
