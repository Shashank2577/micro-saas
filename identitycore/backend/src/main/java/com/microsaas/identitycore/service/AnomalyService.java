package com.microsaas.identitycore.service;

import com.microsaas.identitycore.model.Anomaly;
import com.microsaas.identitycore.repository.AnomalyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AnomalyService {

    private final AnomalyRepository anomalyRepository;

    public AnomalyService(AnomalyRepository anomalyRepository) {
        this.anomalyRepository = anomalyRepository;
    }

    public List<Anomaly> getAnomaliesByTenant(UUID tenantId) {
        return anomalyRepository.findByTenantId(tenantId);
    }

    public Optional<Anomaly> getAnomalyById(UUID id, UUID tenantId) {
        return anomalyRepository.findByIdAndTenantId(id, tenantId);
    }

    @Transactional
    public Anomaly createAnomaly(UUID tenantId, Anomaly anomaly) {
        anomaly.setId(UUID.randomUUID());
        anomaly.setTenantId(tenantId);
        anomaly.setCreatedAt(OffsetDateTime.now());
        anomaly.setUpdatedAt(OffsetDateTime.now());
        anomaly.setDetectedAt(OffsetDateTime.now());
        if (anomaly.getStatus() == null) {
            anomaly.setStatus("OPEN");
        }
        return anomalyRepository.save(anomaly);
    }

    @Transactional
    public Anomaly updateAnomalyStatus(UUID id, UUID tenantId, String status) {
        return anomalyRepository.findByIdAndTenantId(id, tenantId).map(anomaly -> {
            anomaly.setStatus(status);
            anomaly.setUpdatedAt(OffsetDateTime.now());
            if ("RESOLVED".equals(status) || "IGNORED".equals(status)) {
                anomaly.setResolvedAt(OffsetDateTime.now());
            }
            return anomalyRepository.save(anomaly);
        }).orElseThrow(() -> new RuntimeException("Anomaly not found"));
    }
}
