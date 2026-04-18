package com.microsaas.vendormonitor.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.vendormonitor.domain.Alert;
import com.microsaas.vendormonitor.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public List<Alert> getOpenAlerts() {
        return alertRepository.findByTenantIdAndStatus(TenantContext.require(), "OPEN");
    }

    public List<Alert> getAlertsForVendor(UUID vendorId) {
        return alertRepository.findByTenantIdAndVendorId(TenantContext.require(), vendorId);
    }

    public Alert updateAlertStatus(UUID id, String status) {
        Alert alert = alertRepository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));
        
        alert.setStatus(status);
        if ("RESOLVED".equalsIgnoreCase(status)) {
            alert.setResolvedAt(ZonedDateTime.now());
        }
        
        return alertRepository.save(alert);
    }
}
