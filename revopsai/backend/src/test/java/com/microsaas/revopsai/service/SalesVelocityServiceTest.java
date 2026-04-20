package com.microsaas.revopsai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.revopsai.model.SalesVelocity;
import com.microsaas.revopsai.repository.SalesVelocityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalesVelocityServiceTest {

    @Mock
    private SalesVelocityRepository repository;

    @InjectMocks
    private SalesVelocityService service;

    private final UUID tenantId = UUID.randomUUID();
    private SalesVelocity entity;

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
        entity = new SalesVelocity();
        entity.setId(UUID.randomUUID());
        entity.setTenantId(tenantId);
    }

    @Test
    void testGetAll() {
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(entity));

        List<SalesVelocity> result = service.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(entity.getId(), result.get(0).getId());
        verify(repository, times(1)).findByTenantId(tenantId);
    }

    @Test
    void testCreate() {
        when(repository.save(any(SalesVelocity.class))).thenReturn(entity);

        SalesVelocity result = service.create(new SalesVelocity());

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        verify(repository, times(1)).save(any(SalesVelocity.class));
    }
}
