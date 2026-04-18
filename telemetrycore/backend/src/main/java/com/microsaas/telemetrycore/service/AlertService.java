package com.microsaas.telemetrycore.service;

import com.microsaas.telemetrycore.model.Alert;
import com.microsaas.telemetrycore.repository.AlertRepository;
import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

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
}
