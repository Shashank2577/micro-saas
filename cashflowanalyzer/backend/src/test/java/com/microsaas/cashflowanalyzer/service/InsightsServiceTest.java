package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cashflowanalyzer.model.TrendSignal;
import com.microsaas.cashflowanalyzer.repository.TrendSignalRepository;
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
class InsightsServiceTest {

    @Mock
    private TrendSignalRepository repository;

    @InjectMocks
    private InsightsService service;

    private UUID tenantId;
    private TrendSignal signal;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
        signal = new TrendSignal();
        signal.setId(UUID.randomUUID());
        signal.setName("Increasing Cloud Spend");
        signal.setStatus("ACTIVE");
        signal.setTenantId(tenantId);
    }

    @Test
    void create_ShouldSetTenantIdAndSave() {
        when(repository.save(any(TrendSignal.class))).thenReturn(signal);
        TrendSignal created = service.create(signal);
        assertNotNull(created);
        assertEquals(tenantId, created.getTenantId());
        verify(repository).save(signal);
    }

    @Test
    void getById_ShouldReturnWhenFound() {
        when(repository.findByIdAndTenantId(signal.getId(), tenantId)).thenReturn(Optional.of(signal));
        TrendSignal found = service.getById(signal.getId());
        assertNotNull(found);
    }
}
