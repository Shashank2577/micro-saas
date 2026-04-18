package com.microsaas.legalresearch.service;

import com.microsaas.legalresearch.domain.ResearchQuery;
import com.microsaas.legalresearch.repository.ResearchQueryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResearchQueryServiceTest {

    @Mock
    private ResearchQueryRepository repository;

    @InjectMocks
    private ResearchQueryService service;

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        ResearchQuery query = new ResearchQuery();
        query.setId(id);
        query.setTenantId(tenantId);

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(query));

        ResearchQuery found = service.findById(id, tenantId);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    void testFindById_NotFound() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(id, tenantId));
    }
}
