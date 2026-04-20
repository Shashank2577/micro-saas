package com.microsaas.supportintelligence.service;

import com.microsaas.supportintelligence.model.SupportTicket;
import com.microsaas.supportintelligence.repository.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SentimentAnalysisService {

    private final SupportTicketRepository ticketRepository;

    @Transactional
    public SupportTicket analyzeSentiment(UUID tenantId, UUID ticketId) {
        SupportTicket ticket = ticketRepository.findByIdAndTenantId(ticketId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        // Mocking AI sentiment analysis
        log.info("Analyzing sentiment for ticket: {}", ticketId);

        // Simulating a computed score between 0.0 and 1.0
        double simulatedScore = 0.45;
        ticket.setSentimentScore(simulatedScore);

        return ticketRepository.save(ticket);
    }
}
