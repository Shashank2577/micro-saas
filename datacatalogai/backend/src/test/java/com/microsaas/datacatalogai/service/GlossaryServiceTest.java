package com.microsaas.datacatalogai.service;

import com.microsaas.datacatalogai.domain.model.GlossaryTerm;
import com.microsaas.datacatalogai.domain.repository.GlossaryTermRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GlossaryServiceTest {

    @Mock
    private GlossaryTermRepository glossaryTermRepository;

    @InjectMocks
    private GlossaryService glossaryService;

    @Test
    void testListTerms() {
        String tenantId = "tenant1";
        GlossaryTerm term = new GlossaryTerm();
        term.setId(UUID.randomUUID());
        term.setTenantId(tenantId);

        when(glossaryTermRepository.findAllByTenantId(tenantId)).thenReturn(List.of(term));

        List<GlossaryTerm> result = glossaryService.listTerms(tenantId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(glossaryTermRepository, times(1)).findAllByTenantId(tenantId);
    }

    @Test
    void testCreateTerm() {
        String tenantId = "tenant1";
        GlossaryTerm term = new GlossaryTerm();
        term.setTerm("Test Term");

        when(glossaryTermRepository.save(any(GlossaryTerm.class))).thenAnswer(i -> i.getArguments()[0]);

        GlossaryTerm result = glossaryService.createTerm(tenantId, term);

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        assertEquals("Test Term", result.getTerm());
        verify(glossaryTermRepository, times(1)).save(term);
    }

    @Test
    void testUpdateTerm() {
        String tenantId = "tenant1";
        UUID termId = UUID.randomUUID();

        GlossaryTerm existingTerm = new GlossaryTerm();
        existingTerm.setId(termId);
        existingTerm.setTenantId(tenantId);
        existingTerm.setDefinition("Old Def");

        GlossaryTerm updates = new GlossaryTerm();
        updates.setDefinition("New Def");
        updates.setLinkedAssetIds(List.of(UUID.randomUUID()));

        when(glossaryTermRepository.findByIdAndTenantId(termId, tenantId)).thenReturn(Optional.of(existingTerm));
        when(glossaryTermRepository.save(any(GlossaryTerm.class))).thenAnswer(i -> i.getArguments()[0]);

        GlossaryTerm result = glossaryService.updateTerm(tenantId, termId, updates);

        assertNotNull(result);
        assertEquals("New Def", result.getDefinition());
        assertNotNull(result.getLinkedAssetIds());
        assertFalse(result.getLinkedAssetIds().isEmpty());
        assertNotNull(result.getUpdatedAt());
        verify(glossaryTermRepository, times(1)).findByIdAndTenantId(termId, tenantId);
        verify(glossaryTermRepository, times(1)).save(existingTerm);
    }
}
