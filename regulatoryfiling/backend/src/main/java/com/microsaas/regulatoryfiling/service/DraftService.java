package com.microsaas.regulatoryfiling.service;

import com.microsaas.regulatoryfiling.domain.DraftStatus;
import com.microsaas.regulatoryfiling.domain.FilingDraft;
import com.microsaas.regulatoryfiling.domain.FilingObligation;
import com.microsaas.regulatoryfiling.repository.FilingDraftRepository;
import com.microsaas.regulatoryfiling.repository.FilingObligationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DraftService {

    private final FilingDraftRepository draftRepository;
    private final FilingObligationRepository obligationRepository;

    @Transactional
    public FilingDraft generateDraft(UUID obligationId, UUID tenantId) {
        FilingObligation obligation = obligationRepository.findByIdAndTenantId(obligationId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Obligation not found"));

        String mockContent = String.format("Draft content for %s (%s) due on %s", 
                obligation.getName(), obligation.getJurisdiction(), obligation.getDueDate());

        FilingDraft draft = FilingDraft.builder()
                .id(UUID.randomUUID())
                .obligationId(obligationId)
                .content(mockContent)
                .generatedAt(LocalDateTime.now())
                .status(DraftStatus.DRAFT)
                .tenantId(tenantId)
                .build();

        return draftRepository.save(draft);
    }

    @Transactional(readOnly = true)
    public FilingDraft getDraft(UUID obligationId, UUID tenantId) {
        return draftRepository.findByObligationIdAndTenantId(obligationId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Draft not found"));
    }
}
