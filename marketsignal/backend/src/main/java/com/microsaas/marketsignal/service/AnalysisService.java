package com.microsaas.marketsignal.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.marketsignal.domain.entity.MarketPattern;
import com.microsaas.marketsignal.domain.entity.MarketSignal;
import com.microsaas.marketsignal.domain.enums.PatternType;
import com.microsaas.marketsignal.repository.MarketPatternRepository;
import com.microsaas.marketsignal.repository.MarketSignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final MarketSignalRepository marketSignalRepository;
    private final MarketPatternRepository marketPatternRepository;
    private final LiteLLMClient liteLLMClient;

    @Transactional
    public MarketPattern detectPatterns() {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());
        List<MarketSignal> recentSignals = marketSignalRepository.findByTenantIdOrderByPublishedAtDesc(tenantId);
        
        if (recentSignals.isEmpty()) return null;

        String signalsSummary = recentSignals.stream()
                .limit(20) // take top 20 for context limits
                .map(s -> "ID: " + s.getId() + "\nTitle: " + s.getTitle() + "\nContent: " + s.getContent())
                .collect(Collectors.joining("\n\n"));

        String prompt = "Based on the following recent market signals, detect a key market pattern (Competitive Shift, Opportunity Gap, or Risk Flag).\n\n" + signalsSummary + "\n\nFormat your response as:\nTitle: [Pattern Title]\nType: [COMPETITIVE_SHIFT | OPPORTUNITY_GAP | RISK_FLAG]\nDescription: [Detailed description]\nSignal IDs: [Comma separated list of IDs from the provided text that contribute to this pattern]";
        
        String aiResponse = liteLLMClient.generateCompletion(prompt, "You are a strategic business analyst finding patterns in market data.");

        // Parse AI response (basic mock parsing)
        String title = "Detected Pattern";
        PatternType type = PatternType.OPPORTUNITY_GAP;
        String desc = aiResponse;

        if (aiResponse.contains("Title:")) {
            try {
                title = aiResponse.substring(aiResponse.indexOf("Title:") + 6, aiResponse.indexOf("\n")).trim();
            } catch (Exception e) {}
        }
        if (aiResponse.contains("Type:")) {
             try {
                String rawType = aiResponse.substring(aiResponse.indexOf("Type:") + 5, aiResponse.indexOf("\n", aiResponse.indexOf("Type:"))).trim();
                type = PatternType.valueOf(rawType);
             } catch (Exception e) {}
        }

        MarketPattern pattern = MarketPattern.builder()
                .tenantId(tenantId)
                .title(title)
                .description(desc)
                .patternType(type)
                .signals(recentSignals.subList(0, Math.min(3, recentSignals.size()))) // mock assignment
                .build();

        return marketPatternRepository.save(pattern);
    }
}
