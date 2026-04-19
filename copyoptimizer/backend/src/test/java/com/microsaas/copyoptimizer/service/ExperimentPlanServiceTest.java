package com.microsaas.copyoptimizer.service;

import com.microsaas.copyoptimizer.model.ExperimentPlan;
import com.microsaas.copyoptimizer.repository.ExperimentPlanRepository;
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
class ExperimentPlanServiceTest {

    @Mock
    private ExperimentPlanRepository repository;

    @InjectMocks
    private ExperimentPlanService service;

    @Test
    void create_ShouldSaveAndReturnEntity() {
        ExperimentPlan entity = new ExperimentPlan();
        entity.setName("Test Exp");

        when(repository.save(any(ExperimentPlan.class))).thenReturn(entity);

        ExperimentPlan result = service.create(entity);

        assertNotNull(result);
        assertEquals("Test Exp", result.getName());
    }

    @Test
    void getById_WhenExists_ShouldReturnEntity() {
        UUID id = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        ExperimentPlan entity = new ExperimentPlan();
        entity.setId(id);

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.of(entity));

        ExperimentPlan result = service.getById(id, tenantId);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }
}
