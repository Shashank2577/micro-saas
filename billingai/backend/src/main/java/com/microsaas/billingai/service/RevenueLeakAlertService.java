package com.microsaas.billingai.service;

import com.microsaas.billingai.model.RevenueLeakAlert;
import com.microsaas.billingai.repository.RevenueLeakAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RevenueLeakAlertService {
    private final RevenueLeakAlertRepository repository;

    public List<RevenueLeakAlert> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public RevenueLeakAlert findById(UUID tenantId, UUID id) {
        return repository.findById(id)
                .filter(alert -> alert.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Revenue Leak Alert not found"));
    }

    public RevenueLeakAlert create(UUID tenantId, RevenueLeakAlert alert) {
        alert.setId(UUID.randomUUID());
        alert.setTenantId(tenantId);
        alert.setCreatedAt(OffsetDateTime.now());
        alert.setUpdatedAt(OffsetDateTime.now());
        return repository.save(alert);
    }

    public RevenueLeakAlert update(UUID tenantId, UUID id, RevenueLeakAlert alertUpdate) {
        RevenueLeakAlert existing = findById(tenantId, id);
        existing.setName(alertUpdate.getName());
        existing.setStatus(alertUpdate.getStatus());
        existing.setMetadataJson(alertUpdate.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public boolean validate(UUID tenantId, UUID id) {
        findById(tenantId, id);
        return true;
    }
}
