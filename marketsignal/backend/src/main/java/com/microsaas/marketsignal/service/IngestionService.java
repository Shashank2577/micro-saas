package com.microsaas.marketsignal.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.marketsignal.domain.entity.InformationSource;
import com.microsaas.marketsignal.domain.entity.MarketSignal;
import com.microsaas.marketsignal.dto.IngestSignalRequest;
import com.microsaas.marketsignal.repository.InformationSourceRepository;
import com.microsaas.marketsignal.repository.MarketSignalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IngestionService {

    private final MarketSignalRepository marketSignalRepository;
    private final InformationSourceRepository informationSourceRepository;
    private final LiteLLMClient liteLLMClient;

    @Transactional
    public MarketSignal ingestSignal(IngestSignalRequest request) {
        UUID tenantId = UUID.fromString(TenantContext.require().toString());

        InformationSource source = null;
        if (request.getSourceId() != null) {
            source = informationSourceRepository.findById(request.getSourceId()).orElse(null);
        }

        // Generate embedding
        float[] embedding = liteLLMClient.generateEmbedding(request.getTitle() + " " + request.getContent());

        // Analyze sentiment, strength and implications using AI
        String analysisPrompt = String.format("Analyze the following market signal:\nTitle: %s\nContent: %s\n\nProvide the analysis in this format:\nStrength: [1-10]\nSentiment: [Positive/Negative/Neutral]\nImplications: [Brief summary]", request.getTitle(), request.getContent());
        String analysis = liteLLMClient.generateCompletion(analysisPrompt, "You are an expert market analyst.");
        
        Integer strength = 5;
        String sentiment = "Neutral";
        String implications = analysis; // use raw for now unless parsed
        
        if (analysis.contains("Strength:")) {
            try {
                // simple parsing logic for demonstration
                int startIndex = analysis.indexOf("Strength:") + 9;
                int endIndex = analysis.indexOf("\n", startIndex);
                if(endIndex == -1) endIndex = analysis.length();
                strength = Integer.parseInt(analysis.substring(startIndex, endIndex).trim());
            } catch (Exception e) {}
        }
        if (analysis.contains("Sentiment:")) {
             int startIndex = analysis.indexOf("Sentiment:") + 10;
             int endIndex = analysis.indexOf("\n", startIndex);
             if(endIndex == -1) endIndex = analysis.length();
             sentiment = analysis.substring(startIndex, endIndex).trim();
        }

        MarketSignal signal = MarketSignal.builder()
                .tenantId(tenantId)
                .title(request.getTitle())
                .content(request.getContent())
                .source(source)
                .publishedAt(OffsetDateTime.now())
                .signalStrength(strength)
                .sentiment(sentiment)
                .strategicImplications(implications)
                .embedding(embedding)
                .build();

        return marketSignalRepository.save(signal);
    }
}
