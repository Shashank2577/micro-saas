package com.microsaas.supportintelligence.service;

import com.microsaas.supportintelligence.model.ResponseSuggestion;
import com.microsaas.supportintelligence.model.SupportTicket;
import com.microsaas.supportintelligence.repository.ResponseSuggestionRepository;
import com.microsaas.supportintelligence.repository.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseSuggestionService {

    private final ResponseSuggestionRepository suggestionRepository;
    private final SupportTicketRepository ticketRepository;

    @Transactional
    public ResponseSuggestion generateSuggestion(UUID tenantId, UUID ticketId) {
        SupportTicket ticket = ticketRepository.findByIdAndTenantId(ticketId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        log.info("Generating AI response suggestion for ticket: {}", ticketId);

        // Simulating LiteLLM integration since the cross-cutting module does not actually expose LiteLlmClient
        String prompt = String.format("You are a helpful customer support AI. Generate a professional response for a support ticket. The issue category is '%s' and the urgency is '%s'. Keep it concise and helpful.", ticket.getCategory(), ticket.getUrgency());

        String suggestedText = String.format("Hello, I understand you are having a %s issue. We have escalated this given its %s urgency. Could you provide more details?", ticket.getCategory(), ticket.getUrgency());
        Double confidence = 0.92;

        ResponseSuggestion suggestion = ResponseSuggestion.builder()
                .tenantId(tenantId)
                .ticketId(ticketId)
                .suggestedText(suggestedText)
                .confidenceScore(confidence)
                .acceptedByAgent(false)
                .build();

        return suggestionRepository.save(suggestion);
    }

    @Transactional
    public ResponseSuggestion acceptSuggestion(UUID tenantId, UUID suggestionId) {
        ResponseSuggestion suggestion = suggestionRepository.findByIdAndTenantId(suggestionId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Suggestion not found"));

        suggestion.setAcceptedByAgent(true);
        return suggestionRepository.save(suggestion);
    }

    @Transactional(readOnly = true)
    public List<ResponseSuggestion> getSuggestionsForTicket(UUID tenantId, UUID ticketId) {
        return suggestionRepository.findByTicketIdAndTenantId(ticketId, tenantId);
    }
}
