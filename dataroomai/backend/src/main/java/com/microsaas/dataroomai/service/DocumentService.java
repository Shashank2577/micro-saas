package com.microsaas.dataroomai.service;

import com.microsaas.dataroomai.domain.Document;
import com.microsaas.dataroomai.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Transactional(readOnly = true)
    public List<Document> getDocuments(UUID roomId, UUID tenantId) {
        return documentRepository.findByRoomIdAndTenantId(roomId, tenantId);
    }

    @Transactional(readOnly = true)
    public Document getDocument(UUID id, UUID tenantId) {
        return documentRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    @Transactional
    public Document createDocument(Document document) {
        return documentRepository.save(document);
    }
}
