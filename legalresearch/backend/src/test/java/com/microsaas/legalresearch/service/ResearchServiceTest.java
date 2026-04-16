package com.microsaas.legalresearch.service;

import com.microsaas.legalresearch.domain.Citation;
import com.microsaas.legalresearch.domain.CitationType;
import com.microsaas.legalresearch.domain.MemoStatus;
import com.microsaas.legalresearch.domain.PracticeArea;
import com.microsaas.legalresearch.domain.ResearchMemo;
import com.microsaas.legalresearch.repository.CitationRepository;
import com.microsaas.legalresearch.repository.ResearchMemoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResearchServiceTest {

    @Mock
    private ResearchMemoRepository researchMemoRepository;
    
    @Mock
    private CitationRepository citationRepository;

    @InjectMocks
    private ResearchService researchService;

    @Test
    void createMemo_ShouldStoreWithDraftStatus() {
        UUID tenantId = UUID.randomUUID();
        
        when(researchMemoRepository.save(any(ResearchMemo.class))).thenAnswer(i -> {
            ResearchMemo m = i.getArgument(0);
            m.setId(UUID.randomUUID());
            return m;
        });

        ResearchMemo memo = researchService.createMemo("Test Memo", "What is the law?", PracticeArea.CORPORATE, tenantId);

        assertThat(memo.getId()).isNotNull();
        assertThat(memo.getStatus()).isEqualTo(MemoStatus.DRAFT);
        assertThat(memo.getTitle()).isEqualTo("Test Memo");
        assertThat(memo.getTenantId()).isEqualTo(tenantId);
    }

    @Test
    void addCitation_ShouldLinkToMemo() {
        UUID tenantId = UUID.randomUUID();
        ResearchMemo mockedMemo = ResearchMemo.builder().id(UUID.randomUUID()).tenantId(tenantId).status(MemoStatus.DRAFT).build();
        when(researchMemoRepository.findById(any())).thenReturn(Optional.of(mockedMemo));
        
        when(citationRepository.save(any(Citation.class))).thenAnswer(i -> {
            Citation c = i.getArgument(0);
            c.setId(UUID.randomUUID());
            return c;
        });

        Citation citation = researchService.addCitation(mockedMemo.getId(), CitationType.CASE, "123 F.3d 456", "A landmark case", tenantId);

        assertThat(citation.getId()).isNotNull();
        assertThat(citation.getMemo().getId()).isEqualTo(mockedMemo.getId());
        assertThat(citation.getType()).isEqualTo(CitationType.CASE);
        assertThat(citation.getReference()).isEqualTo("123 F.3d 456");
    }

    @Test
    void searchMemos_ShouldFindByKeywordInTitle() {
        UUID tenantId = UUID.randomUUID();
        ResearchMemo m1 = ResearchMemo.builder().title("Unique Keyword Memo").build();
        
        when(researchMemoRepository.searchMemos("Keyword", tenantId)).thenReturn(Collections.singletonList(m1));

        List<ResearchMemo> results = researchService.searchMemos("Keyword", tenantId);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Unique Keyword Memo");
    }
}
