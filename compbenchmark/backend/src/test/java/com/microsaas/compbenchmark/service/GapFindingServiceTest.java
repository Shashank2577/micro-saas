package com.microsaas.compbenchmark.service;

import com.microsaas.compbenchmark.services.*;

import com.crosscutting.event.EventPublisher;
import com.crosscutting.tenancy.TenantContext;
import com.microsaas.compbenchmark.model.GapFinding;
import com.microsaas.compbenchmark.repositories.GapFindingRepository;
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
class GapFindingServiceTest {

    @Mock
    private GapFindingRepository repository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private GapFindingService service;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testCreate() {
        GapFinding input = new GapFinding();
        input.setName("Test Gap");

        when(repository.save(any(GapFinding.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GapFinding result = service.create(input);

        assertNotNull(result.getId());
        assertEquals(tenantId, result.getTenantId());
        assertEquals("Test Gap", result.getName());
        assertEquals("DRAFT", result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(repository).save(input);
        verify(eventPublisher).publish(eq("compbenchmark.gap.detected"), any());
    }

    @Test
    void testList() {
        GapFinding gap1 = new GapFinding();
        GapFinding gap2 = new GapFinding();
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(gap1, gap2));

        List<GapFinding> result = service.list();

        assertEquals(2, result.size());
        verify(repository).findByTenantId(tenantId);
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        GapFinding gap = new GapFinding();
        gap.setId(id);
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(gap));

        GapFinding result = service.getById(id);

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
        GapFinding existing = new GapFinding();
        existing.setId(id);
        existing.setName("Old Gap");
        existing.setStatus("DRAFT");

        GapFinding updates = new GapFinding();
        updates.setName("New Gap");
        updates.setStatus("RESOLVED");

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(existing));
        when(repository.save(any(GapFinding.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GapFinding result = service.update(id, updates);

        assertEquals("New Gap", result.getName());
        assertEquals("RESOLVED", result.getStatus());
        verify(repository).findByIdAndTenantId(id, tenantId);
        verify(repository).save(existing);
        verify(eventPublisher).publish(eq("compbenchmark.gap.detected"), any());
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        GapFinding existing = new GapFinding();
        existing.setId(id);

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(existing));

        service.delete(id);

        verify(repository).findByIdAndTenantId(id, tenantId);
        verify(repository).delete(existing);
    }
}