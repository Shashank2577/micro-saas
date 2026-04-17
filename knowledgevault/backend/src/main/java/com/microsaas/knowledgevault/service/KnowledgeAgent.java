package com.microsaas.knowledgevault.service;

import com.microsaas.knowledgevault.domain.KnowledgeDocument;
import com.microsaas.knowledgevault.repository.KnowledgeDocumentRepository;
import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class KnowledgeAgent {

    private static final Logger log = LoggerFactory.getLogger(KnowledgeAgent.class);

    private final KnowledgeDocumentRepository documentRepository;
    private final AiService aiService;

    public KnowledgeAgent(KnowledgeDocumentRepository documentRepository, AiService aiService) {
        this.documentRepository = documentRepository;
        this.aiService = aiService;
    }

    // Run every day to detect stale knowledge
    @Scheduled(cron = "0 0 0 * * ?")
    public void monitorFreshness() {
        log.info("Running freshness monitor...");
        
        // Find all FRESH documents older than 30 days
        Instant thresholdDate = Instant.now().minus(30, ChronoUnit.DAYS);
        List<KnowledgeDocument> freshDocs = documentRepository.findAll().stream()
                .filter(doc -> "FRESH".equals(doc.getFreshnessStatus()))
                .filter(doc -> doc.getUpdatedAt().isBefore(thresholdDate))
                .toList();

        for (KnowledgeDocument doc : freshDocs) {
            log.info("Checking document freshness for ID: {}", doc.getId());
            
            // In a real scenario, this might ping the source API to see if it changed,
            // or use AI to determine if the content represents time-sensitive information
            String prompt = "Review this document content and determine if it is likely to be outdated or stale. Answer with a simple 'YES' or 'NO'.\n\nContent:\n" + doc.getContent();
            
            try {
                ChatRequest chatRequest = new ChatRequest("claude-sonnet-4-6", List.of(new ChatMessage("user", prompt)), null, null);

                ChatResponse chatResponse = aiService.chat(chatRequest);
                String aiDecision = chatResponse.content().trim().toUpperCase();
                
                if (aiDecision.contains("YES")) {
                    doc.setFreshnessStatus("STALE");
                    documentRepository.save(doc);
                    log.info("Document {} marked as STALE", doc.getId());
                }
            } catch (Exception e) {
                log.error("Failed to check freshness for document {}", doc.getId(), e);
            }
        }
    }
}
