package com.microsaas.procurebot.service;

import com.microsaas.procurebot.dto.VendorOfferRequest;
import com.microsaas.procurebot.model.VendorOffer;
import com.microsaas.procurebot.repository.VendorOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorOfferService {

    private final VendorOfferRepository repository;

    @Transactional(readOnly = true)
    public List<VendorOffer> findAll(UUID tenantId) {
        return repository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public VendorOffer findById(UUID id, UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("VendorOffer not found"));
    }

    @Transactional
    public VendorOffer create(UUID tenantId, VendorOfferRequest request) {
        VendorOffer entity = VendorOffer.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
                .metadataJson(request.getMetadataJson())
                .build();
        return repository.save(entity);
    }

    @Transactional
    public VendorOffer update(UUID id, UUID tenantId, VendorOfferRequest request) {
        VendorOffer entity = findById(id, tenantId);
        if (request.getName() != null) entity.setName(request.getName());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getMetadataJson() != null) entity.setMetadataJson(request.getMetadataJson());
        return repository.save(entity);
    }

    public void validate(UUID id, UUID tenantId) {
        VendorOffer entity = findById(id, tenantId);
        // Add business validation logic here
    }
}
