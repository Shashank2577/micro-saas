package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cashflowanalyzer.model.AnomalyFlag;
import com.microsaas.cashflowanalyzer.repository.AnomalyFlagRepository;
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
class AnomaliesServiceTest {

    @Mock
    private AnomalyFlagRepository repository;

    @InjectMocks
    private AnomaliesService service;

    private UUID tenantId;
    private AnomalyFlag flag;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
        flag = new AnomalyFlag();
        flag.setId(UUID.randomUUID());
        flag.setName("High AWS Bill");
        flag.setStatus("OPEN");
        flag.setTenantId(tenantId);
    }

    @Test
    void create_ShouldSetTenantIdAndSave() {
        when(repository.save(any(AnomalyFlag.class))).thenReturn(flag);
        AnomalyFlag created = service.create(flag);
        assertNotNull(created);
        assertEquals(tenantId, created.getTenantId());
        verify(repository).save(flag);
    }

    @Test
    void getById_ShouldReturnWhenFound() {
        when(repository.findByIdAndTenantId(flag.getId(), tenantId)).thenReturn(Optional.of(flag));
        AnomalyFlag found = service.getById(flag.getId());
        assertNotNull(found);
    }
}
