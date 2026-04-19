package com.microsaas.hiresignal.service;

import com.microsaas.hiresignal.model.InterviewStage;
import com.microsaas.hiresignal.repository.InterviewStageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class InterviewStageService {

    private final InterviewStageRepository repository;

    public List<InterviewStage> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<InterviewStage> findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public InterviewStage save(InterviewStage entity) {
        return repository.save(entity);
    }

    public InterviewStage update(UUID id, UUID tenantId, InterviewStage updateData) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(existing -> {
                    existing.setName(updateData.getName());
                    existing.setStatus(updateData.getStatus());
                    existing.setMetadataJson(updateData.getMetadataJson());
                    return repository.save(existing);
                }).orElseThrow(() -> new RuntimeException("InterviewStage not found"));
    }

    public Map<String, Object> validate(UUID id, UUID tenantId) {
        Optional<InterviewStage> entity = repository.findByIdAndTenantId(id, tenantId);
        Map<String, Object> response = new HashMap<>();
        if (entity.isPresent()) {
            response.put("valid", true);
            response.put("message", "InterviewStage is valid.");
        } else {
            response.put("valid", false);
            response.put("message", "InterviewStage not found.");
        }
        return response;
    }
}
