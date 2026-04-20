package com.microsaas.supportintelligence.controller;

import com.microsaas.supportintelligence.model.ResponseSuggestion;
import com.microsaas.supportintelligence.model.SupportTicket;
import com.microsaas.supportintelligence.service.ResponseSuggestionService;
import com.microsaas.supportintelligence.service.TicketIntegrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Ticket management API")
public class TicketController {

    private final TicketIntegrationService ticketService;
    private final ResponseSuggestionService suggestionService;

    private UUID getTenantId() {
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }

    @Operation(summary = "Get all tickets")
    @GetMapping
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets(getTenantId()));
    }

    @Operation(summary = "Get ticket by ID")
    @GetMapping("/{id}")
    public ResponseEntity<SupportTicket> getTicket(@PathVariable UUID id) {
        return ResponseEntity.ok(ticketService.getTicket(getTenantId(), id));
    }

    @Operation(summary = "Sync external ticket")
    @PostMapping("/sync")
    public ResponseEntity<SupportTicket> syncTicket(@RequestParam String externalTicketId, @RequestParam String category, @RequestParam String urgency) {
        return ResponseEntity.ok(ticketService.syncExternalTicket(getTenantId(), externalTicketId, category, urgency));
    }

    @Operation(summary = "Generate a response suggestion for a ticket")
    @PostMapping("/{id}/suggestions")
    public ResponseEntity<ResponseSuggestion> generateSuggestion(@PathVariable UUID id) {
        return ResponseEntity.ok(suggestionService.generateSuggestion(getTenantId(), id));
    }

    @Operation(summary = "Get suggestions for a ticket")
    @GetMapping("/{id}/suggestions")
    public ResponseEntity<List<ResponseSuggestion>> getSuggestions(@PathVariable UUID id) {
        return ResponseEntity.ok(suggestionService.getSuggestionsForTicket(getTenantId(), id));
    }

    @Operation(summary = "Accept a response suggestion")
    @PostMapping("/{id}/suggestions/{suggestionId}/accept")
    public ResponseEntity<ResponseSuggestion> acceptSuggestion(@PathVariable UUID id, @PathVariable UUID suggestionId) {
        return ResponseEntity.ok(suggestionService.acceptSuggestion(getTenantId(), suggestionId));
    }
}
