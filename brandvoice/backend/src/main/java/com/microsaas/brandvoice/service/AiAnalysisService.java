package com.microsaas.brandvoice.service;

import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatRequest;
import com.microsaas.brandvoice.entity.AnalysisReport;
import com.microsaas.brandvoice.entity.ContentAsset;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiAnalysisService {
    private final AiService aiService;
    private final AnalysisReportService analysisReportService;
    private final ContentAssetService contentAssetService;

    public AnalysisReport analyzeContent(ContentAsset asset) {
        String prompt = "Analyze the following content for brand consistency. Content: " + asset.getContent();

        String response = aiService.chat(new ChatRequest("claude-sonnet-4-6", List.of(new ChatMessage("user", prompt)), null, null)).content();

        AnalysisReport report = AnalysisReport.builder()
            .tenantId(asset.getTenantId())
            .contentAssetId(asset.getId())
            .score(85.0) // Mock score
            .feedback(response)
            .generatedAt(LocalDateTime.now())
            .build();

        asset.setAiAnalysisScore(85.0);
        contentAssetService.save(asset);

        return analysisReportService.save(report);
    }
}
