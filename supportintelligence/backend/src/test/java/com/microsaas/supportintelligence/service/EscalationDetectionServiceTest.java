package com.microsaas.supportintelligence.service;

import com.microsaas.supportintelligence.model.EscalationSignal;
import com.microsaas.supportintelligence.model.SupportTicket;
import com.microsaas.supportintelligence.repository.EscalationSignalRepository;
import com.microsaas.supportintelligence.repository.SupportTicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EscalationDetectionServiceTest {

    @Mock
    private EscalationSignalRepository escalationRepository;

    @Mock
    private SupportTicketRepository ticketRepository;

    @InjectMocks
    private EscalationDetectionService service;

    private UUID tenantId;
    private UUID ticketId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        ticketId = UUID.randomUUID();
    }

    @Test
    void testDetectEscalationHighUrgency() {
        SupportTicket ticket = SupportTicket.builder()
                .id(ticketId)
                .tenantId(tenantId)
                .urgency("HIGH")
                .build();

        when(ticketRepository.findByIdAndTenantId(ticketId, tenantId)).thenReturn(Optional.of(ticket));

        service.detectEscalation(tenantId, ticketId);

        verify(escalationRepository).save(any(EscalationSignal.class));
    }

    @Test
    void testDetectEscalationNormalUrgencyNoSignal() {
        SupportTicket ticket = SupportTicket.builder()
                .id(ticketId)
                .tenantId(tenantId)
                .urgency("NORMAL")
                .sentimentScore(0.8)
                .build();

        when(ticketRepository.findByIdAndTenantId(ticketId, tenantId)).thenReturn(Optional.of(ticket));

        service.detectEscalation(tenantId, ticketId);

        verify(escalationRepository, never()).save(any(EscalationSignal.class));
    }
}
