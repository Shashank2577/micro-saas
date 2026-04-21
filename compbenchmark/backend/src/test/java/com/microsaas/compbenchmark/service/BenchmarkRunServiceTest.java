package com.microsaas.compbenchmark.service;

import com.microsaas.compbenchmark.services.*;

import com.crosscutting.tenancy.TenantContext;
import com.microsaas.compbenchmark.model.BenchmarkRun;
import com.microsaas.compbenchmark.repositories.BenchmarkRunRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BenchmarkRunServiceTest {

    @Mock
    private BenchmarkRunRepository repository;

    @InjectMocks
    private BenchmarkRunService service;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testCreate() {
        BenchmarkRun input = new BenchmarkRun();
        input.setName("Test Run");

        when(repository.save(any(BenchmarkRun.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BenchmarkRun result = service.create(input);

        assertNotNull(result.getId());
        assertEquals(tenantId, result.getTenantId());
        assertEquals("Test Run", result.getName());
        assertEquals("DRAFT", result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(repository).save(input);
    }

    @Test
    void testList() {
        BenchmarkRun run1 = new BenchmarkRun();
        BenchmarkRun run2 = new BenchmarkRun();
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(run1, run2));

        List<BenchmarkRun> result = service.list();

        assertEquals(2, result.size());
        verify(repository).findByTenantId(tenantId);
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        BenchmarkRun run = new BenchmarkRun();
        run.setId(id);
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(run));

        BenchmarkRun result = service.getById(id);

        assertEquals(id, result.getId());
        verify(repository).findByIdAndTenantId(id, tenantId);
    }

    @Test
    void testGetByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.getById(id));
    }

    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        BenchmarkRun existing = new BenchmarkRun();
        existing.setId(id);
        existing.setName("Old Name");
        existing.setStatus("DRAFT");

        BenchmarkRun updates = new BenchmarkRun();
        updates.setName("New Name");
        updates.setStatus("COMPLETED");

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(existing));
        when(repository.save(any(BenchmarkRun.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BenchmarkRun result = service.update(id, updates);

        assertEquals("New Name", result.getName());
        assertEquals("COMPLETED", result.getStatus());
        verify(repository).findByIdAndTenantId(id, tenantId);
        verify(repository).save(existing);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        BenchmarkRun existing = new BenchmarkRun();
        existing.setId(id);

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(existing));

        service.delete(id);

        verify(repository).findByIdAndTenantId(id, tenantId);
        verify(repository).delete(existing);
    }
}