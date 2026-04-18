package com.microsaas.billingai.service;

import com.microsaas.billingai.model.InvoiceRun;
import com.microsaas.billingai.repository.InvoiceRunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceRunService {
    private final InvoiceRunRepository repository;

    public List<InvoiceRun> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public InvoiceRun findById(UUID tenantId, UUID id) {
        return repository.findById(id)
                .filter(run -> run.getTenantId().equals(tenantId))
                .orElseThrow(() -> new RuntimeException("Invoice Run not found"));
    }

    public InvoiceRun create(UUID tenantId, InvoiceRun run) {
        run.setId(UUID.randomUUID());
        run.setTenantId(tenantId);
        run.setCreatedAt(OffsetDateTime.now());
        run.setUpdatedAt(OffsetDateTime.now());
        return repository.save(run);
    }

    public InvoiceRun update(UUID tenantId, UUID id, InvoiceRun runUpdate) {
        InvoiceRun existing = findById(tenantId, id);
        existing.setName(runUpdate.getName());
        existing.setStatus(runUpdate.getStatus());
        existing.setMetadataJson(runUpdate.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public boolean validate(UUID tenantId, UUID id) {
        findById(tenantId, id);
        return true;
    }
}
