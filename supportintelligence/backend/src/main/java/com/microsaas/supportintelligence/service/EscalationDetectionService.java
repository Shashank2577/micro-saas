package com.microsaas.supportintelligence.service;

import com.microsaas.supportintelligence.model.EscalationSignal;
import com.microsaas.supportintelligence.model.SupportTicket;
import com.microsaas.supportintelligence.repository.EscalationSignalRepository;
import com.microsaas.supportintelligence.repository.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EscalationDetectionService {

    private final EscalationSignalRepository escalationRepository;
    private final SupportTicketRepository ticketRepository;

    @Transactional
    public void detectEscalation(UUID tenantId, UUID ticketId) {
        SupportTicket ticket = ticketRepository.findByIdAndTenantId(ticketId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        log.info("Analyzing ticket for escalation signals: {}", ticketId);

        boolean shouldEscalate = false;
        String signalType = "";

        // Simulating LiteLLM integration since the cross-cutting module does not actually expose LiteLlmClient
        if ("HIGH".equals(ticket.getUrgency()) || (ticket.getSentimentScore() != null && ticket.getSentimentScore() < 0.3)) {
            shouldEscalate = true;
            signalType = "AI-Detected: Customer Anger / High Complexity";
        }

        if (shouldEscalate) {
            EscalationSignal signal = EscalationSignal.builder()
                    .tenantId(tenantId)
                    .ticketId(ticketId)
                    .signalType(signalType)
                    .severity("CRITICAL")
                    .escalatedTo("Senior Support Team")
                    .build();
            escalationRepository.save(signal);
        }
    }

    @Transactional(readOnly = true)
    public List<EscalationSignal> getAllEscalations(UUID tenantId) {
        return escalationRepository.findByTenantId(tenantId);
    }

    @Transactional
    public EscalationSignal resolveEscalation(UUID tenantId, UUID escalationId) {
        EscalationSignal signal = escalationRepository.findByIdAndTenantId(escalationId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Escalation not found"));
        signal.setResolvedAt(ZonedDateTime.now());
        return escalationRepository.save(signal);
    }
}
