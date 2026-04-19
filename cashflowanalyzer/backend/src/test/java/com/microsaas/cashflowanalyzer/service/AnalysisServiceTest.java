package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cashflowanalyzer.model.CashMovement;
import com.microsaas.cashflowanalyzer.repository.CashMovementRepository;
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
class AnalysisServiceTest {

    @Mock
    private CashMovementRepository repository;

    @InjectMocks
    private AnalysisService service;

    private UUID tenantId;
    private CashMovement movement;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
        movement = new CashMovement();
        movement.setId(UUID.randomUUID());
        movement.setName("Salary");
        movement.setStatus("CLEARED");
        movement.setTenantId(tenantId);
    }

    @Test
    void create_ShouldSetTenantIdAndSave() {
        when(repository.save(any(CashMovement.class))).thenReturn(movement);
        CashMovement created = service.create(movement);
        assertNotNull(created);
        assertEquals(tenantId, created.getTenantId());
        verify(repository).save(movement);
    }

    @Test
    void getById_ShouldReturnWhenFound() {
        when(repository.findByIdAndTenantId(movement.getId(), tenantId)).thenReturn(Optional.of(movement));
        CashMovement found = service.getById(movement.getId());
        assertNotNull(found);
    }
}
