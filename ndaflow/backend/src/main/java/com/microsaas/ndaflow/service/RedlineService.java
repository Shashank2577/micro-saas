package com.microsaas.ndaflow.service;

import com.microsaas.ndaflow.domain.NdaRedline;
import com.microsaas.ndaflow.domain.NdaClause;
import com.microsaas.ndaflow.domain.RedlineStatus;
import com.microsaas.ndaflow.repository.NdaRedlineRepository;
import com.microsaas.ndaflow.repository.NdaClauseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedlineService {
    private final NdaRedlineRepository ndaRedlineRepository;
    private final NdaClauseRepository ndaClauseRepository;

    @Transactional
    public NdaRedline proposeRedline(UUID ndaId, UUID clauseId, String proposedText, String rationale, UUID tenantId) {
        NdaClause clause = ndaClauseRepository.findById(clauseId).orElseThrow();
        if (!clause.getTenantId().equals(tenantId) || !clause.getNdaId().equals(ndaId)) {
            throw new IllegalArgumentException("Clause does not belong to tenant or nda");
        }
        
        NdaRedline redline = NdaRedline.builder()
                .ndaId(ndaId)
                .clauseId(clauseId)
                .originalText(clause.getContent())
                .proposedText(proposedText)
                .rationale(rationale)
                .status(RedlineStatus.PENDING)
                .tenantId(tenantId)
                .build();
        return ndaRedlineRepository.save(redline);
    }

    @Transactional
    public NdaRedline acceptRedline(UUID id, UUID tenantId) {
        NdaRedline redline = ndaRedlineRepository.findById(id).orElseThrow();
        if (!redline.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Redline does not belong to tenant");
        }
        redline.setStatus(RedlineStatus.ACCEPTED);
        return ndaRedlineRepository.save(redline);
    }

    @Transactional
    public NdaRedline rejectRedline(UUID id, UUID tenantId) {
        NdaRedline redline = ndaRedlineRepository.findById(id).orElseThrow();
        if (!redline.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Redline does not belong to tenant");
        }
        redline.setStatus(RedlineStatus.REJECTED);
        return ndaRedlineRepository.save(redline);
    }

    public String suggestResponse(UUID redlineId, UUID tenantId) {
        // Mock AI suggestion string
        return "AI Suggestion: Consider accepting this redline as it aligns with standard industry practices.";
    }
}
