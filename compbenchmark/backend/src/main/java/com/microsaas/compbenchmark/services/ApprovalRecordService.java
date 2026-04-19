package com.microsaas.compbenchmark.services;

import com.crosscutting.tenancy.TenantContext;
import com.microsaas.compbenchmark.model.ApprovalRecord;
import com.microsaas.compbenchmark.repositories.ApprovalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApprovalRecordService {
    private final ApprovalRecordRepository repository;

    public ApprovalRecord create(ApprovalRecord entity) {
        UUID tenantId = TenantContext.require();
        entity.setId(UUID.randomUUID());
        entity.setTenantId(tenantId);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setUpdatedAt(OffsetDateTime.now());
        if (entity.getStatus() == null) entity.setStatus("DRAFT");
        return repository.save(entity);
    }

    public List<ApprovalRecord> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    public ApprovalRecord getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }

    public ApprovalRecord update(UUID id, ApprovalRecord updates) {
        ApprovalRecord existing = getById(id);
        if (updates.getName() != null) existing.setName(updates.getName());
        if (updates.getStatus() != null) existing.setStatus(updates.getStatus());
        if (updates.getMetadataJson() != null) existing.setMetadataJson(updates.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());
        return repository.save(existing);
    }

    public void delete(UUID id) {
        ApprovalRecord existing = getById(id);
        repository.delete(existing);
    }

    public Map<String, Object> validate(UUID id) {
        getById(id);
        return Map.of("valid", true, "errors", List.of());
    }

    public Map<String, Object> simulate(UUID id) {
        getById(id);
        return Map.of("result", Map.of("simulatedCoverage", "85%"));
    }
}
