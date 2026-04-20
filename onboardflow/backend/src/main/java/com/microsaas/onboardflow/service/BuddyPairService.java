package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.dto.BuddyPairRequest;
import com.microsaas.onboardflow.model.BuddyPair;
import com.microsaas.onboardflow.repository.BuddyPairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuddyPairService {

    private final BuddyPairRepository repository;

    @Transactional(readOnly = true)
    public List<BuddyPair> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public BuddyPair findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("BuddyPair not found"));
    }

    @Transactional
    public BuddyPair create(UUID tenantId, BuddyPairRequest request) {
        BuddyPair entity = BuddyPair.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public BuddyPair update(UUID id, UUID tenantId, BuddyPairRequest request) {
        BuddyPair entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        BuddyPair entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
