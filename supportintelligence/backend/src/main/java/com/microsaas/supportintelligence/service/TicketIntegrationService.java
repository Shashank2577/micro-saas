package com.microsaas.supportintelligence.service;

import com.microsaas.supportintelligence.model.SupportTicket;
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
public class TicketIntegrationService {

    private final SupportTicketRepository ticketRepository;
    private final EscalationDetectionService escalationDetectionService;

    @Transactional
    public SupportTicket syncExternalTicket(UUID tenantId, String externalTicketId, String category, String urgency) {
        log.info("Syncing external ticket: {}", externalTicketId);

        SupportTicket ticket = SupportTicket.builder()
                .tenantId(tenantId)
                .externalTicketId(externalTicketId)
                .category(category)
                .urgency(urgency)
                .status("OPEN")
                .build();

        SupportTicket savedTicket = ticketRepository.save(ticket);

        // Trigger escalation detection asynchronously or directly
        escalationDetectionService.detectEscalation(tenantId, savedTicket.getId());

        return savedTicket;
    }

    @Transactional(readOnly = true)
    public List<SupportTicket> getAllTickets(UUID tenantId) {
        return ticketRepository.findByTenantId(tenantId);
    }

    @Transactional(readOnly = true)
    public SupportTicket getTicket(UUID tenantId, UUID ticketId) {
        return ticketRepository.findByIdAndTenantId(ticketId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
    }
}
