package com.microsaas.voiceagentbuilder.controller;

import com.microsaas.voiceagentbuilder.dto.KnowledgeDocumentDto;
import com.microsaas.voiceagentbuilder.model.KnowledgeDocument;
import com.microsaas.voiceagentbuilder.service.KnowledgeDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agents/{agentId}/documents")
@RequiredArgsConstructor
public class KnowledgeDocumentController {
    private final KnowledgeDocumentService documentService;

    @GetMapping
    public List<KnowledgeDocument> getDocuments(@PathVariable UUID agentId) {
        return documentService.getDocuments(agentId);
    }

    @PostMapping
    public KnowledgeDocument createDocument(@PathVariable UUID agentId, @RequestBody KnowledgeDocumentDto dto) {
        return documentService.createDocument(agentId, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable UUID agentId, @PathVariable UUID id) {
        documentService.deleteDocument(agentId, id);
    }
}
