package com.microsaas.marketsignal.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.marketsignal.domain.entity.MarketBrief;
import com.microsaas.marketsignal.domain.entity.MarketPattern;
import com.microsaas.marketsignal.repository.MarketBriefRepository;
import com.microsaas.marketsignal.repository.MarketPatternRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BriefGenerationService {

    private final MarketBriefRepository marketBriefRepository;
    private final MarketPatternRepository marketPatternRepository;
    private final LiteLLMClient liteLLMClient;

    @Transactional
    public MarketBrief generateBrief() {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        List<MarketPattern> recentPatterns = marketPatternRepository.findByTenantIdOrderByDetectedAtDesc(tenantId);
        
        String patternContext = recentPatterns.stream()
                .limit(5)
                .map(p -> "- " + p.getTitle() + " (" + p.getPatternType() + "): " + p.getDescription())
                .collect(Collectors.joining("\n"));

        String prompt = "Generate a weekly market intelligence brief based on these recent patterns:\n\n" + patternContext + "\n\nProvide a Title, a Summary, and a detailed Content section using Markdown.";
        String aiResponse = liteLLMClient.generateCompletion(prompt, "You are a Chief Strategy Officer writing a briefing for the executive team.");

        MarketBrief brief = MarketBrief.builder()
                .tenantId(tenantId)
                .title("Weekly Market Brief")
                .summary("Executive summary of recent market movements.")
                .content(aiResponse)
                .build();

        return marketBriefRepository.save(brief);
    }
}
