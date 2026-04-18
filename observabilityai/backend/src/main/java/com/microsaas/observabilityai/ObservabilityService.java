package com.microsaas.observabilityai;

import com.crosscutting.starter.tenancy.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ObservabilityService {

    private final ObservabilitySignalRepository signalRepository;
    private final ObservabilityAlertRepository alertRepository;

    public ObservabilityService(ObservabilitySignalRepository signalRepository, ObservabilityAlertRepository alertRepository) {
        this.signalRepository = signalRepository;
        this.alertRepository = alertRepository;
    }

    public ObservabilitySignal createSignal(ObservabilitySignal signal) {
        signal.setTenantId(TenantContext.require());
        return signalRepository.save(signal);
    }

    public List<ObservabilitySignal> getSignals(String type) {
        UUID tenantId = TenantContext.require();
        if (type != null && !type.isEmpty()) {
            return signalRepository.findByTenantIdAndSignalType(tenantId, type);
        }
        return signalRepository.findByTenantId(tenantId);
    }

    public List<ObservabilitySignal> getSignalsByTraceId(String traceId) {
        return signalRepository.findByTenantIdAndTraceId(TenantContext.require(), traceId);
    }

    public ObservabilityAlert createAlert(ObservabilityAlert alert) {
        alert.setTenantId(TenantContext.require());
        alert.setStatus("OPEN");
        return alertRepository.save(alert);
    }

    public List<ObservabilityAlert> getAlerts() {
        return alertRepository.findByTenantId(TenantContext.require());
    }
}
