package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.entity.OptimizationRun;
import com.microsaas.debtnavigator.repository.OptimizationRunRepository;
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
class OptimizationServiceTest {

    @Mock
    private OptimizationRunRepository repository;

    @InjectMocks
    private OptimizationService service;

    @Test
    void testCreate() {
        OptimizationRun run = new OptimizationRun();
        when(repository.save(any(OptimizationRun.class))).thenReturn(run);
        assertNotNull(service.create(new OptimizationRun()));
        verify(repository).save(any(OptimizationRun.class));
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new OptimizationRun()));
        assertTrue(service.getById(id, tenantId).isPresent());
    }
}
