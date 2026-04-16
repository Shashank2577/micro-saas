package com.microsaas.legalresearch.service;

import com.microsaas.legalresearch.domain.PracticeArea;
import com.microsaas.legalresearch.domain.ResearchMemo;
import com.microsaas.legalresearch.domain.ResearchThread;
import com.microsaas.legalresearch.repository.ResearchMemoRepository;
import com.microsaas.legalresearch.repository.ResearchThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ThreadService {

    private final ResearchThreadRepository researchThreadRepository;
    private final ResearchMemoRepository researchMemoRepository;

    @Transactional
    public ResearchThread createThread(String title, PracticeArea practiceArea, UUID tenantId) {
        ResearchThread thread = ResearchThread.builder()
                .title(title)
                .practiceArea(practiceArea)
                .tenantId(tenantId)
                .build();
        return researchThreadRepository.save(thread);
    }

    @Transactional
    public void addMemoToThread(UUID threadId, UUID memoId, UUID tenantId) {
        ResearchThread thread = researchThreadRepository.findById(threadId)
                .filter(t -> t.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Thread not found or access denied"));

        ResearchMemo memo = researchMemoRepository.findById(memoId)
                .filter(m -> m.getTenantId().equals(tenantId))
                .orElseThrow(() -> new IllegalArgumentException("Memo not found or access denied"));

        memo.setThread(thread);
        researchMemoRepository.save(memo);
    }

    @Transactional(readOnly = true)
    public List<ResearchThread> listThreads(UUID tenantId) {
        return researchThreadRepository.findByTenantId(tenantId);
    }
}
