package com.microsaas.engagementpulse.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.engagementpulse.model.ActionPlan;
import com.microsaas.engagementpulse.model.Alert;
import com.microsaas.engagementpulse.repository.ActionPlanRepository;
import com.microsaas.engagementpulse.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AlertService {
    private final AlertRepository alertRepository;
    private final ActionPlanRepository actionPlanRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository, ActionPlanRepository actionPlanRepository) {
        this.alertRepository = alertRepository;
        this.actionPlanRepository = actionPlanRepository;
    }

    @Transactional(readOnly = true)
    public List<Alert> getUnresolvedAlerts() {
        UUID tenantId = TenantContext.require();
        return alertRepository.findByTenantIdAndResolvedFalse(tenantId);
    }

    @Transactional
    public void resolveAlert(UUID alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        alert.setResolved(true);
        alertRepository.save(alert);
    }

    @Transactional
    public ActionPlan addActionPlan(UUID alertId, String description) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        
        ActionPlan plan = new ActionPlan();
        plan.setAlert(alert);
        plan.setDescription(description);
        plan.setTenantId(TenantContext.require());
        plan.setStatus("SUGGESTED");

        return actionPlanRepository.save(plan);
    }
}
