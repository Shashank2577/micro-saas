package com.microsaas.hiresignal.service;

import com.microsaas.hiresignal.model.FitSignal;
import com.microsaas.hiresignal.repository.FitSignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class FitSignalService {

    private final FitSignalRepository repository;

    public List<FitSignal> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<FitSignal> findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public FitSignal save(FitSignal entity) {
        return repository.save(entity);
    }

    public FitSignal update(UUID id, UUID tenantId, FitSignal updateData) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(existing -> {
                    existing.setName(updateData.getName());
                    existing.setStatus(updateData.getStatus());
                    existing.setMetadataJson(updateData.getMetadataJson());
                    return repository.save(existing);
                }).orElseThrow(() -> new RuntimeException("FitSignal not found"));
    }

    public Map<String, Object> validate(UUID id, UUID tenantId) {
        Optional<FitSignal> entity = repository.findByIdAndTenantId(id, tenantId);
        Map<String, Object> response = new HashMap<>();
        if (entity.isPresent()) {
            response.put("valid", true);
            response.put("message", "FitSignal is valid.");
        } else {
            response.put("valid", false);
            response.put("message", "FitSignal not found.");
        }
        return response;
    }
}
