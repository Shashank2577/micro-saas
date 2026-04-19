package com.microsaas.cashflowanalyzer.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cashflowanalyzer.model.CashflowPeriod;
import com.microsaas.cashflowanalyzer.repository.CashflowPeriodRepository;
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
class IngestionServiceTest {

    @Mock
    private CashflowPeriodRepository repository;

    @InjectMocks
    private IngestionService service;

    private UUID tenantId;
    private CashflowPeriod period;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        TenantContext.set(tenantId);
        period = new CashflowPeriod();
        period.setId(UUID.randomUUID());
        period.setName("Q1 2024");
        period.setStatus("ACTIVE");
        period.setTenantId(tenantId);
    }

    @Test
    void create_ShouldSetTenantIdAndSave() {
        when(repository.save(any(CashflowPeriod.class))).thenReturn(period);
        CashflowPeriod created = service.create(period);
        assertNotNull(created);
        assertEquals(tenantId, created.getTenantId());
        verify(repository).save(period);
    }

    @Test
    void getById_ShouldReturnWhenFound() {
        when(repository.findByIdAndTenantId(period.getId(), tenantId)).thenReturn(Optional.of(period));
        CashflowPeriod found = service.getById(period.getId());
        assertNotNull(found);
        assertEquals(period.getId(), found.getId());
    }

    @Test
    void getById_ShouldThrowWhenNotFound() {
        when(repository.findByIdAndTenantId(period.getId(), tenantId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(period.getId()));
    }

    @Test
    void list_ShouldReturnTenantScopedList() {
        when(repository.findByTenantId(tenantId)).thenReturn(List.of(period));
        List<CashflowPeriod> list = service.list();
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    void update_ShouldUpdateAndSave() {
        when(repository.findByIdAndTenantId(period.getId(), tenantId)).thenReturn(Optional.of(period));
        when(repository.save(any(CashflowPeriod.class))).thenReturn(period);

        CashflowPeriod updateData = new CashflowPeriod();
        updateData.setName("Updated Q1");
        updateData.setStatus("CLOSED");

        CashflowPeriod updated = service.update(period.getId(), updateData);
        assertEquals("Updated Q1", updated.getName());
        assertEquals("CLOSED", updated.getStatus());
    }

    @Test
    void delete_ShouldRemoveEntity() {
        when(repository.findByIdAndTenantId(period.getId(), tenantId)).thenReturn(Optional.of(period));
        service.delete(period.getId());
        verify(repository).delete(period);
    }
}
