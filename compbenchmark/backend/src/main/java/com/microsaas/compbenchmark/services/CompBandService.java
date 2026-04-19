package com.microsaas.compbenchmark.services;

import com.crosscutting.tenancy.TenantContext;
import com.crosscutting.event.EventPublisher;
import com.microsaas.compbenchmark.model.CompBand;
import com.microsaas.compbenchmark.repositories.CompBandRepository;
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
public class CompBandService {
    private final CompBandRepository repository;
    private final EventPublisher eventPublisher;

    public CompBand create(CompBand band) {
        UUID tenantId = TenantContext.require();
        band.setId(UUID.randomUUID());
        band.setTenantId(tenantId);
        band.setCreatedAt(OffsetDateTime.now());
        band.setUpdatedAt(OffsetDateTime.now());
        if (band.getStatus() == null) band.setStatus("DRAFT");

        CompBand saved = repository.save(band);
        eventPublisher.publish("compbenchmark.band.updated", Map.of("id", saved.getId(), "name", saved.getName()));
        return saved;
    }

    public List<CompBand> list() {
        return repository.findByTenantId(TenantContext.require());
    }

    public CompBand getById(UUID id) {
        return repository.findByIdAndTenantId(id, TenantContext.require())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CompBand not found"));
    }

    public CompBand update(UUID id, CompBand updates) {
        CompBand existing = getById(id);
        if (updates.getName() != null) existing.setName(updates.getName());
        if (updates.getStatus() != null) existing.setStatus(updates.getStatus());
        if (updates.getMetadataJson() != null) existing.setMetadataJson(updates.getMetadataJson());
        existing.setUpdatedAt(OffsetDateTime.now());

        CompBand saved = repository.save(existing);
        eventPublisher.publish("compbenchmark.band.updated", Map.of("id", saved.getId(), "name", saved.getName()));
        return saved;
    }

    public void delete(UUID id) {
        CompBand existing = getById(id);
        repository.delete(existing);
    }

    public Map<String, Object> validate(UUID id) {
        CompBand existing = getById(id);
        return Map.of("valid", true, "errors", List.of());
    }

    public Map<String, Object> simulate(UUID id) {
        CompBand existing = getById(id);
        return Map.of("result", Map.of("simulatedCoverage", "85%"));
    }
}
