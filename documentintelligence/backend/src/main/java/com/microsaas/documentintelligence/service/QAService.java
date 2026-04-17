package com.microsaas.documentintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.documentintelligence.dto.QAQuery;
import com.microsaas.documentintelligence.dto.QAResponse;
import com.microsaas.documentintelligence.model.DocumentChunk;
import com.microsaas.documentintelligence.repository.DocumentChunkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QAService {

    private final DocumentChunkRepository chunkRepository;

    @Transactional(readOnly = true)
    public QAResponse answerQuestion(UUID documentId, QAQuery query) {
        UUID tenantId = TenantContext.require();
        // In a real scenario, we'd filter chunks by documentId and tenantId, 
        // embed the question, and perform similarity search over this document's chunks.
        // Then we'd pass the context to an LLM.
        
        // Mock response
        return new QAResponse(
                "This is a mock answer to: " + query.getQuestion(),
                Collections.singletonList("Page 1")
        );
    }
}
