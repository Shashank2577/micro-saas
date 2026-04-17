package com.microsaas.datacatalogai.service;

import com.microsaas.datacatalogai.domain.model.GlossaryTerm;
import com.microsaas.datacatalogai.domain.repository.GlossaryTermRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class GlossaryService {
    private final GlossaryTermRepository glossaryTermRepository;

    public List<GlossaryTerm> listTerms(String tenantId) {
        return glossaryTermRepository.findAllByTenantId(tenantId);
    }

    @Transactional
    public GlossaryTerm createTerm(String tenantId, GlossaryTerm term) {
        term.setTenantId(tenantId);
        return glossaryTermRepository.save(term);
    }

    @Transactional
    public GlossaryTerm updateTerm(String tenantId, UUID termId, GlossaryTerm updates) {
        GlossaryTerm term = glossaryTermRepository.findByIdAndTenantId(termId, tenantId)
                .orElseThrow(() -> new RuntimeException("Term not found"));
        term.setDefinition(updates.getDefinition());
        term.setLinkedAssetIds(updates.getLinkedAssetIds());
        term.setUpdatedAt(Instant.now());
        return glossaryTermRepository.save(term);
    }
}
