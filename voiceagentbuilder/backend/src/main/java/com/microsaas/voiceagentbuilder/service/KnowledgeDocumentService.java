package com.microsaas.voiceagentbuilder.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.voiceagentbuilder.dto.KnowledgeDocumentDto;
import com.microsaas.voiceagentbuilder.model.KnowledgeDocument;
import com.microsaas.voiceagentbuilder.repository.KnowledgeDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KnowledgeDocumentService {
    private final KnowledgeDocumentRepository documentRepository;

    public List<KnowledgeDocument> getDocuments(UUID agentId) {
        return documentRepository.findByAgentIdAndTenantId(agentId, TenantContext.require());
    }

    public KnowledgeDocument createDocument(UUID agentId, KnowledgeDocumentDto dto) {
        KnowledgeDocument document = KnowledgeDocument.builder()
                .agentId(agentId)
                .tenantId(TenantContext.require())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
        return documentRepository.save(document);
    }

    public void deleteDocument(UUID agentId, UUID id) {
        KnowledgeDocument document = documentRepository.findByIdAndAgentIdAndTenantId(id, agentId, TenantContext.require())
                .orElseThrow(() -> new RuntimeException("Document not found"));
        documentRepository.delete(document);
    }
}
