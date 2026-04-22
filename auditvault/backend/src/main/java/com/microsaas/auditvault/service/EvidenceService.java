package com.microsaas.auditvault.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.auditvault.dto.EvidenceIngestRequest;
import com.microsaas.auditvault.model.Evidence;
import com.microsaas.auditvault.repository.EvidenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvidenceService {
    private final EvidenceRepository evidenceRepository;

    @Transactional
    public Evidence ingestEvidence(EvidenceIngestRequest request) {
        Evidence evidence = Evidence.builder()
                .tenantId(TenantContext.require())
                .sourceApp(request.getSourceApp())
                .evidenceType(request.getEvidenceType())
                .content(request.getContent())
                .url(request.getUrl())
                .status("PENDING_MAPPING")
                .collectedAt(OffsetDateTime.now())
                .build();
        return evidenceRepository.save(evidence);
    }

    public List<Evidence> listEvidence() {
        return evidenceRepository.findByTenantId(TenantContext.require());
    }

    public Evidence getEvidence(UUID id) {
        Evidence evidence = evidenceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evidence not found"));
        if (!evidence.getTenantId().equals(TenantContext.require())) {
            throw new IllegalArgumentException("Tenant mismatch");
        }
        return evidence;
    }
}
