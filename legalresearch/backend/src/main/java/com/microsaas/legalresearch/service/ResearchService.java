package com.microsaas.legalresearch.service;

import com.microsaas.legalresearch.domain.Citation;
import com.microsaas.legalresearch.domain.CitationType;
import com.microsaas.legalresearch.domain.MemoStatus;
import com.microsaas.legalresearch.domain.PracticeArea;
import com.microsaas.legalresearch.domain.ResearchMemo;
import com.microsaas.legalresearch.repository.CitationRepository;
import com.microsaas.legalresearch.repository.ResearchMemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResearchService {

    private final ResearchMemoRepository researchMemoRepository;
    private final CitationRepository citationRepository;

    @Transactional
    public ResearchMemo createMemo(String title, String query, PracticeArea practiceArea, UUID tenantId) {
        ResearchMemo memo = ResearchMemo.builder()
                .title(title)
                .query(query)
                .practiceArea(practiceArea)
                .status(MemoStatus.DRAFT)
                .tenantId(tenantId)
                .build();
        return researchMemoRepository.save(memo);
    }

    @Transactional
    public Citation addCitation(UUID memoId, CitationType type, String reference, String summary, UUID tenantId) {
        ResearchMemo memo = researchMemoRepository.findById(memoId)
                .filter(m -> m.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Memo not found or access denied"));

        Citation citation = Citation.builder()
                .memo(memo)
                .type(type)
                .reference(reference)
                .summary(summary)
                .isVerified(false)
                .tenantId(tenantId)
                .build();
        
        return citationRepository.save(citation);
    }

    @Transactional(readOnly = true)
    public List<Citation> listCitations(UUID memoId, UUID tenantId) {
        return citationRepository.findByMemoIdAndTenantId(memoId, tenantId);
    }

    @Transactional
    public ResearchMemo completeMemo(UUID id, UUID tenantId) {
        ResearchMemo memo = researchMemoRepository.findById(id)
                .filter(m -> m.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Memo not found or access denied"));
        
        memo.setStatus(MemoStatus.COMPLETE);
        return researchMemoRepository.save(memo);
    }

    @Transactional(readOnly = true)
    public List<ResearchMemo> searchMemos(String query, UUID tenantId) {
        return researchMemoRepository.searchMemos(query, tenantId);
    }

    @Transactional(readOnly = true)
    public List<ResearchMemo> listMemos(UUID tenantId) {
        return researchMemoRepository.findByTenantId(tenantId);
    }
}
