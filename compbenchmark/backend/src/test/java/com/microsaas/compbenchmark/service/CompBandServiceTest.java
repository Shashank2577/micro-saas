package com.microsaas.compbenchmark.service;

import com.microsaas.compbenchmark.services.*;

import com.crosscutting.event.EventPublisher;
import com.crosscutting.tenancy.TenantContext;
import com.microsaas.compbenchmark.model.CompBand;
import com.microsaas.compbenchmark.repositories.CompBandRepository;
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
class CompBandServiceTest {

    @Mock
    private CompBandRepository repository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private CompBandService service;

    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
    }

    @Test
    void testCreate() {
        CompBand input = new CompBand();
        input.setName("Test Band");

        when(repository.save(any(CompBand.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompBand result = service.create(input);

        assertNotNull(result.getId());
        assertEquals(tenantId, result.getTenantId());
        assertEquals("Test Band", result.getName());
        assertEquals("DRAFT", result.getStatus());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(repository).save(input);
        verify(eventPublisher).publish(eq("compbenchmark.band.updated"), any());
    }

    @Test
    void testList() {
        CompBand band1 = new CompBand();
        CompBand band2 = new CompBand();
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(band1, band2));

        List<CompBand> result = service.list();

        assertEquals(2, result.size());
        verify(repository).findByTenantId(tenantId);
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        CompBand band = new CompBand();
        band.setId(id);
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(band));

        CompBand result = service.getById(id);

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
        CompBand existing = new CompBand();
        existing.setId(id);
        existing.setName("Old Name");
        existing.setStatus("DRAFT");

        CompBand updates = new CompBand();
        updates.setName("New Name");
        updates.setStatus("ACTIVE");

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(existing));
        when(repository.save(any(CompBand.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompBand result = service.update(id, updates);

        assertEquals("New Name", result.getName());
        assertEquals("ACTIVE", result.getStatus());
        verify(repository).findByIdAndTenantId(id, tenantId);
        verify(repository).save(existing);
        verify(eventPublisher).publish(eq("compbenchmark.band.updated"), any());
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        CompBand existing = new CompBand();
        existing.setId(id);

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(existing));

        service.delete(id);

        verify(repository).findByIdAndTenantId(id, tenantId);
        verify(repository).delete(existing);
    }
}