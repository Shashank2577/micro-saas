package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cashflowanalyzer.model.NarrativeInsight;
import com.microsaas.cashflowanalyzer.repository.NarrativeInsightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportingServiceTest {

    @Mock
    private NarrativeInsightRepository repository;

    @InjectMocks
    private ReportingService service;

    private UUID tenantId;
    private NarrativeInsight insight;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
        insight = new NarrativeInsight();
        insight.setId(UUID.randomUUID());
        insight.setName("Monthly Summary");
        insight.setStatus("PUBLISHED");
        insight.setTenantId(tenantId);
    }

    @Test
    void create_ShouldSetTenantIdAndSave() {
        when(repository.save(any(NarrativeInsight.class))).thenReturn(insight);
        NarrativeInsight created = service.create(insight);
        assertNotNull(created);
        assertEquals(tenantId, created.getTenantId());
        verify(repository).save(insight);
    }

    @Test
    void getById_ShouldReturnWhenFound() {
        when(repository.findByIdAndTenantId(insight.getId(), tenantId)).thenReturn(Optional.of(insight));
        NarrativeInsight found = service.getById(insight.getId());
        assertNotNull(found);
    }
}
