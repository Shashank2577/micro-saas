package com.microsaas.knowledgevault.service;

import com.microsaas.knowledgevault.domain.KnowledgeDocument;
import com.microsaas.knowledgevault.domain.KnowledgeQuery;
import com.microsaas.knowledgevault.repository.KnowledgeDocumentRepository;
import com.microsaas.knowledgevault.repository.KnowledgeQueryRepository;
import com.crosscutting.starter.ai.AiService;
import com.crosscutting.starter.ai.ChatRequest;
import com.crosscutting.starter.ai.ChatMessage;
import com.crosscutting.starter.ai.ChatResponse;
import com.crosscutting.starter.search.SearchService;
import com.crosscutting.starter.search.SearchResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class KnowledgeService {

    private final KnowledgeDocumentRepository documentRepository;
    private final KnowledgeQueryRepository queryRepository;
    private final SearchService searchService;
    private final AiService aiService;

    public KnowledgeService(KnowledgeDocumentRepository documentRepository,
                            KnowledgeQueryRepository queryRepository,
                            SearchService searchService,
                            AiService aiService) {
        this.documentRepository = documentRepository;
        this.queryRepository = queryRepository;
        this.searchService = searchService;
        this.aiService = aiService;
    }

    @Transactional
    public KnowledgeDocument addDocument(KnowledgeDocument document) {
        KnowledgeDocument savedDocument = documentRepository.save(document);

        searchService.index(
            UUID.fromString(savedDocument.getTenantId()),
            "knowledge_document",
            savedDocument.getId(),
            savedDocument.getContent(),
            "{\"title\":\"" + savedDocument.getTitle() + "\",\"url\":\"" + savedDocument.getUrl() + "\"}"
        );

        return savedDocument;
    }

    public String answerQuery(String tenantId, String queryText) {
        // Find existing query to update frequency
        List<KnowledgeQuery> existingQueries = queryRepository.findByTenantIdOrderByFrequencyDesc(tenantId);
        Optional<KnowledgeQuery> existingQuery = existingQueries.stream()
                .filter(q -> q.getQueryText().equalsIgnoreCase(queryText))
                .findFirst();

        KnowledgeQuery query;
        if (existingQuery.isPresent()) {
            query = existingQuery.get();
            query.setFrequency(query.getFrequency() + 1);
            query.setLastAskedAt(Instant.now());
        } else {
            query = new KnowledgeQuery();
            query.setTenantId(tenantId);
            query.setQueryText(queryText);
            query.setFrequency(1);
        }

        // Retrieve relevant documents via semantic search
        List<SearchResult> searchResults = searchService.search(
            UUID.fromString(tenantId),
            queryText,
            "knowledge_document",
            5
        );

        if (searchResults.isEmpty()) {
            query.setGeneratedAnswer("I couldn't find an answer to your question in the knowledge base.");
            queryRepository.save(query);
            return query.getGeneratedAnswer();
        }

        // Construct context for the AI model
        String context = searchResults.stream()
                .map(r -> "Source (" + r.metadata() + "): " + r.content())
                .collect(Collectors.joining("\n\n"));

        String prompt = "You are a helpful company knowledge base assistant. Answer the user's question based ONLY on the provided context.\n" +
                "Context:\n" + context + "\n\n" +
                "Question: " + queryText;

        ChatRequest chatRequest = new ChatRequest("claude-sonnet-4-6", List.of(new ChatMessage("user", prompt)), null, null);

        try {
            ChatResponse chatResponse = aiService.chat(chatRequest);
            String answer = chatResponse.content();
            query.setGeneratedAnswer(answer);
        } catch (Exception e) {
             query.setGeneratedAnswer("I encountered an error trying to generate an answer.");
        }

        queryRepository.save(query);
        return query.getGeneratedAnswer();
    }

    public List<KnowledgeQuery> getTopQueries(String tenantId) {
        return queryRepository.findByTenantIdOrderByFrequencyDesc(tenantId);
    }
}
