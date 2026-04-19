package com.microsaas.contractportfolio.service;

import com.microsaas.contractportfolio.domain.ClauseExtraction;
import com.microsaas.contractportfolio.repository.ClauseExtractionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClauseExtractionServiceTest {

    @Mock
    private ClauseExtractionRepository repository;

    @InjectMocks
    private ClauseExtractionService service;

    @Test
    void testCreate() {
        ClauseExtraction entity = new ClauseExtraction();
        when(repository.save(any(ClauseExtraction.class))).thenReturn(entity);
        ClauseExtraction result = service.create(entity);
        assertNotNull(result);
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new ClauseExtraction()));
        Optional<ClauseExtraction> result = service.getById(id, tenantId);
        assertTrue(result.isPresent());
    }
}
