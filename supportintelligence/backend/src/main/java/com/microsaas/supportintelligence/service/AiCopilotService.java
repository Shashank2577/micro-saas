package com.microsaas.supportintelligence.service;

import com.microsaas.supportintelligence.entity.KnowledgeBaseArticle;
import com.microsaas.supportintelligence.entity.Ticket;
import com.microsaas.supportintelligence.repository.KnowledgeBaseArticleRepository;
import com.microsaas.supportintelligence.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiCopilotService {

    private final TicketRepository ticketRepository;
    private final KnowledgeBaseArticleRepository knowledgeBaseArticleRepository;

    public AiCopilotService(TicketRepository ticketRepository, KnowledgeBaseArticleRepository knowledgeBaseArticleRepository) {
        this.ticketRepository = ticketRepository;
        this.knowledgeBaseArticleRepository = knowledgeBaseArticleRepository;
    }

    public Ticket processNewTicket(Ticket ticket) {
        // Mock processing for MVP
        List<KnowledgeBaseArticle> articles = knowledgeBaseArticleRepository.findAll();

        String context = articles.stream()
                .map(a -> a.getTitle() + ": " + a.getContent())
                .collect(Collectors.joining("\n"));

        // Simulate AI suggested response generation based on context and ticket content
        String suggestedResponse = generateMockSuggestedResponse(ticket.getContent(), context);
        ticket.setAiSuggestedResponse(suggestedResponse);

        // Simulate Escalation Risk Score calculation
        double riskScore = calculateMockRiskScore(ticket.getContent());
        ticket.setEscalationRiskScore(riskScore);

        ticket.setStatus("OPEN");
        return ticketRepository.save(ticket);
    }

    private String generateMockSuggestedResponse(String issue, String context) {
        // In a real implementation this would call an LLM with the context and issue.
        return "Based on similar past tickets and our knowledge base, we suggest: Have you tried restarting your device and checking your network connection? Context analyzed length: " + context.length();
    }

    private double calculateMockRiskScore(String issue) {
        // Simple mock algorithm for MVP
        if (issue.toLowerCase().contains("urgent") || issue.toLowerCase().contains("asap") || issue.toLowerCase().contains("broken")) {
            return 0.85;
        } else if (issue.toLowerCase().contains("refund") || issue.toLowerCase().contains("manager")) {
             return 0.95;
        }
        return 0.20; // Default low risk
    }
}
