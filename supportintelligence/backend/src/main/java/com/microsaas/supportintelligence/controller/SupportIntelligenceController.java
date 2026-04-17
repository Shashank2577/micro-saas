package com.microsaas.supportintelligence.controller;

import com.microsaas.supportintelligence.entity.KnowledgeBaseArticle;
import com.microsaas.supportintelligence.entity.Ticket;
import com.microsaas.supportintelligence.repository.KnowledgeBaseArticleRepository;
import com.microsaas.supportintelligence.repository.TicketRepository;
import com.microsaas.supportintelligence.service.AiCopilotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/support")
public class SupportIntelligenceController {

    private final TicketRepository ticketRepository;
    private final KnowledgeBaseArticleRepository knowledgeBaseArticleRepository;
    private final AiCopilotService aiCopilotService;

    public SupportIntelligenceController(TicketRepository ticketRepository, KnowledgeBaseArticleRepository knowledgeBaseArticleRepository, AiCopilotService aiCopilotService) {
        this.ticketRepository = ticketRepository;
        this.knowledgeBaseArticleRepository = knowledgeBaseArticleRepository;
        this.aiCopilotService = aiCopilotService;
    }

    @PostMapping("/tickets")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) {
        Ticket processedTicket = aiCopilotService.processNewTicket(ticket);
        return ResponseEntity.ok(processedTicket);
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketRepository.findAll());
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable UUID id) {
        return ticketRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/knowledge-base")
    public ResponseEntity<KnowledgeBaseArticle> createArticle(@RequestBody KnowledgeBaseArticle article) {
        return ResponseEntity.ok(knowledgeBaseArticleRepository.save(article));
    }

    @GetMapping("/knowledge-base")
    public ResponseEntity<List<KnowledgeBaseArticle>> getAllArticles() {
        return ResponseEntity.ok(knowledgeBaseArticleRepository.findAll());
    }
}
