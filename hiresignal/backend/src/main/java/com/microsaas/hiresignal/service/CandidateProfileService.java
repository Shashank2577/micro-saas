package com.microsaas.hiresignal.service;

import com.microsaas.hiresignal.model.CandidateProfile;
import com.microsaas.hiresignal.repository.CandidateProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class CandidateProfileService {

    private final CandidateProfileRepository repository;

    public List<CandidateProfile> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    public Optional<CandidateProfile> findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    public CandidateProfile save(CandidateProfile entity) {
        return repository.save(entity);
    }

    public CandidateProfile update(UUID id, UUID tenantId, CandidateProfile updateData) {
        return repository.findByIdAndTenantId(id, tenantId)
                .map(existing -> {
                    existing.setName(updateData.getName());
                    existing.setStatus(updateData.getStatus());
                    existing.setMetadataJson(updateData.getMetadataJson());
                    return repository.save(existing);
                }).orElseThrow(() -> new RuntimeException("CandidateProfile not found"));
    }

    public Map<String, Object> validate(UUID id, UUID tenantId) {
        Optional<CandidateProfile> entity = repository.findByIdAndTenantId(id, tenantId);
        Map<String, Object> response = new HashMap<>();
        if (entity.isPresent()) {
            response.put("valid", true);
            response.put("message", "CandidateProfile is valid.");
        } else {
            response.put("valid", false);
            response.put("message", "CandidateProfile not found.");
        }
        return response;
    }
}
