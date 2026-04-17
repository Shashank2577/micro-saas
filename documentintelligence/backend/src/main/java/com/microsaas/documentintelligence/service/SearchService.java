package com.microsaas.documentintelligence.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.documentintelligence.dto.SearchQuery;
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
public class SearchService {

    private final DocumentChunkRepository chunkRepository;

    @Transactional(readOnly = true)
    public List<DocumentChunk> search(SearchQuery query) {
        UUID tenantId = TenantContext.require();
        // In a real scenario, we embed the query string and use vector search
        
        // Mock response returning empty list or dummy chunks
        return Collections.emptyList();
    }
}
