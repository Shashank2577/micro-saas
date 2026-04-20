package com.microsaas.supportintelligence.service;

import com.microsaas.supportintelligence.model.ResponseSuggestion;
import com.microsaas.supportintelligence.model.SupportTicket;
import com.microsaas.supportintelligence.repository.ResponseSuggestionRepository;
import com.microsaas.supportintelligence.repository.SupportTicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponseSuggestionServiceTest {

    @Mock
    private ResponseSuggestionRepository suggestionRepository;

    @Mock
    private SupportTicketRepository ticketRepository;

    @InjectMocks
    private ResponseSuggestionService service;

    private UUID tenantId;
    private UUID ticketId;
    private SupportTicket ticket;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        ticketId = UUID.randomUUID();
        ticket = SupportTicket.builder()
                .id(ticketId)
                .tenantId(tenantId)
                .category("Billing")
                .urgency("NORMAL")
                .build();
    }

    @Test
    void testGenerateSuggestion() {
        when(ticketRepository.findByIdAndTenantId(ticketId, tenantId)).thenReturn(Optional.of(ticket));
        when(suggestionRepository.save(any(ResponseSuggestion.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseSuggestion suggestion = service.generateSuggestion(tenantId, ticketId);

        assertNotNull(suggestion);
        assertEquals(tenantId, suggestion.getTenantId());
        assertEquals(ticketId, suggestion.getTicketId());
        assertTrue(suggestion.getSuggestedText().contains("Billing"));
        assertFalse(suggestion.getAcceptedByAgent());
        verify(suggestionRepository).save(any());
    }
}
