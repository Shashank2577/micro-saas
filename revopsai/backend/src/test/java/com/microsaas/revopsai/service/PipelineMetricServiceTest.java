package com.microsaas.revopsai.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.revopsai.model.PipelineMetric;
import com.microsaas.revopsai.repository.PipelineMetricRepository;
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
class PipelineMetricServiceTest {

    @Mock
    private PipelineMetricRepository repository;

    @InjectMocks
    private PipelineMetricService service;

    private final UUID tenantId = UUID.randomUUID();
    private PipelineMetric entity;

    @BeforeEach
    void setUp() {
        TenantContext.set(tenantId);
        entity = new PipelineMetric();
        entity.setId(UUID.randomUUID());
        entity.setTenantId(tenantId);
    }

    @Test
    void testGetAll() {
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(entity));

        List<PipelineMetric> result = service.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(entity.getId(), result.get(0).getId());
        verify(repository, times(1)).findByTenantId(tenantId);
    }

    @Test
    void testCreate() {
        when(repository.save(any(PipelineMetric.class))).thenReturn(entity);

        PipelineMetric result = service.create(new PipelineMetric());

        assertNotNull(result);
        assertEquals(tenantId, result.getTenantId());
        verify(repository, times(1)).save(any(PipelineMetric.class));
    }
}
