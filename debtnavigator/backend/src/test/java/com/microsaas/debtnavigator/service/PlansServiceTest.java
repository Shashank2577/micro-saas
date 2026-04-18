package com.microsaas.debtnavigator.service;

import com.microsaas.debtnavigator.entity.PaymentPlan;
import com.microsaas.debtnavigator.repository.PaymentPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlansServiceTest {

    @Mock
    private PaymentPlanRepository repository;

    @InjectMocks
    private PlansService service;

    @Test
    void testCreate() {
        PaymentPlan plan = new PaymentPlan();
        when(repository.save(any(PaymentPlan.class))).thenReturn(plan);
        assertNotNull(service.create(new PaymentPlan()));
        verify(repository).save(any(PaymentPlan.class));
    }

    @Test
    void testGetById() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(new PaymentPlan()));
        assertTrue(service.getById(id, tenantId).isPresent());
    }
}
