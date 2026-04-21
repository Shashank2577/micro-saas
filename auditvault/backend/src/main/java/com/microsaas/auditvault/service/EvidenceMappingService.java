package com.microsaas.auditvault.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.auditvault.model.Evidence;
import com.microsaas.auditvault.model.EvidenceMapping;
import com.microsaas.auditvault.repository.EvidenceMappingRepository;
import com.microsaas.auditvault.repository.EvidenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvidenceMappingService {
    private final EvidenceMappingRepository mappingRepository;
    private final EvidenceRepository evidenceRepository;

    @Transactional
    public EvidenceMapping approveMapping(UUID mappingId) {
        EvidenceMapping mapping = mappingRepository.findById(mappingId)
                .orElseThrow(() -> new IllegalArgumentException("Mapping not found"));
        if (!mapping.getTenantId().equals(TenantContext.require())) {
            throw new IllegalArgumentException("Tenant mismatch");
        }
        mapping.setStatus("APPROVED");
        return mappingRepository.save(mapping);
    }

    @Transactional
    public EvidenceMapping rejectMapping(UUID mappingId) {
        EvidenceMapping mapping = mappingRepository.findById(mappingId)
                .orElseThrow(() -> new IllegalArgumentException("Mapping not found"));
        if (!mapping.getTenantId().equals(TenantContext.require())) {
            throw new IllegalArgumentException("Tenant mismatch");
        }
        mapping.setStatus("REJECTED");

        Evidence evidence = evidenceRepository.findById(mapping.getEvidenceId()).orElseThrow();
        evidence.setStatus("PENDING_MAPPING");
        evidenceRepository.save(evidence);

        return mappingRepository.save(mapping);
    }
}
