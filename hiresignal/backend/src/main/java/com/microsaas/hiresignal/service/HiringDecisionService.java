package com.microsaas.hiresignal.service;

import com.microsaas.hiresignal.model.HiringDecision;
import com.microsaas.hiresignal.repository.HiringDecisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class HiringDecisionService {

    private final HiringDecisionRepository repository;

    public List<HiringDecision> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<HiringDecision> findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public HiringDecision save(HiringDecision entity) {
        return repository.save(entity);
    }

    public HiringDecision update(UUID id, UUID tenantId, HiringDecision updateData) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(existing -> {
                    existing.setName(updateData.getName());
                    existing.setStatus(updateData.getStatus());
                    existing.setMetadataJson(updateData.getMetadataJson());
                    return repository.save(existing);
                }).orElseThrow(() -> new RuntimeException("HiringDecision not found"));
    }

    public Map<String, Object> validate(UUID id, UUID tenantId) {
        Optional<HiringDecision> entity = repository.findByIdAndTenantId(id, tenantId);
        Map<String, Object> response = new HashMap<>();
        if (entity.isPresent()) {
            response.put("valid", true);
            response.put("message", "HiringDecision is valid.");
        } else {
            response.put("valid", false);
            response.put("message", "HiringDecision not found.");
        }
        return response;
    }
}
