package com.microsaas.auditready.service;

import com.microsaas.auditready.domain.EvidenceItem;
import com.microsaas.auditready.repository.EvidenceItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvidenceService {

    private final EvidenceItemRepository evidenceItemRepository;

    public EvidenceItem addEvidence(UUID controlId, EvidenceItem.SourceType sourceType, String content, UUID tenantId) {
        EvidenceItem item = EvidenceItem.builder()
                .id(UUID.randomUUID())
                .controlId(controlId)
                .sourceType(sourceType)
                .content(content)
                .collectedAt(Instant.now())
                .status(EvidenceItem.Status.PENDING)
                .tenantId(tenantId)
                .build();
        return evidenceItemRepository.save(item);
    }

    public List<EvidenceItem> listEvidence(UUID controlId, UUID tenantId) {
        return evidenceItemRepository.findByControlIdAndTenantId(controlId, tenantId);
    }

    public Optional<EvidenceItem> acceptEvidence(UUID id, UUID tenantId) {
        return evidenceItemRepository.findByIdAndTenantId(id, tenantId).map(item -> {
            item.setStatus(EvidenceItem.Status.ACCEPTED);
            return evidenceItemRepository.save(item);
        });
    }

    public Optional<EvidenceItem> rejectEvidence(UUID id, UUID tenantId) {
        return evidenceItemRepository.findByIdAndTenantId(id, tenantId).map(item -> {
            item.setStatus(EvidenceItem.Status.REJECTED);
            return evidenceItemRepository.save(item);
        });
    }
}
