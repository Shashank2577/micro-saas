package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cashflowanalyzer.model.ForecastRun;
import com.microsaas.cashflowanalyzer.repository.ForecastRunRepository;
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
class ForecastingServiceTest {

    @Mock
    private ForecastRunRepository repository;

    @InjectMocks
    private ForecastingService service;

    private UUID tenantId;
    private ForecastRun run;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
        run = new ForecastRun();
        run.setId(UUID.randomUUID());
        run.setName("Q2 Forecast");
        run.setStatus("COMPLETED");
        run.setTenantId(tenantId);
    }

    @Test
    void create_ShouldSetTenantIdAndSave() {
        when(repository.save(any(ForecastRun.class))).thenReturn(run);
        ForecastRun created = service.create(run);
        assertNotNull(created);
        assertEquals(tenantId, created.getTenantId());
        verify(repository).save(run);
    }

    @Test
    void getById_ShouldReturnWhenFound() {
        when(repository.findByIdAndTenantId(run.getId(), tenantId)).thenReturn(Optional.of(run));
        ForecastRun found = service.getById(run.getId());
        assertNotNull(found);
    }
}
