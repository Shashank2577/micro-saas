package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.dto.OnboardingWorkflowRequest;
import com.microsaas.onboardflow.model.OnboardingWorkflow;
import com.microsaas.onboardflow.repository.OnboardingWorkflowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OnboardingWorkflowServiceTest {

    @Mock
    private OnboardingWorkflowRepository repository;

    @InjectMocks
    private OnboardingWorkflowService service;

    @Test
    public void testFindAll() {
        UUID tenantId = UUID.randomUUID();
        when(repository.findByTenantId(tenantId)).thenReturn(Collections.emptyList());

        assertTrue(service.findAll(tenantId).isEmpty());
        verify(repository, times(1)).findByTenantId(tenantId);
    }

    @Test
    public void testCreate() {
        UUID tenantId = UUID.randomUUID();
        OnboardingWorkflowRequest request = new OnboardingWorkflowRequest();
        request.setName("Test");

        OnboardingWorkflow savedEntity = new OnboardingWorkflow();
        savedEntity.setName("Test");

        when(repository.save(any(OnboardingWorkflow.class))).thenReturn(savedEntity);

        OnboardingWorkflow result = service.create(tenantId, request);
        assertEquals("Test", result.getName());
    }

    @Test
    public void testFindById_NotFound() {
        UUID tenantId = UUID.randomUUID();
        UUID id = UUID.randomUUID();

        when(repository.findByIdAndTenantId(id, tenantId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.findById(id, tenantId));
    }
}
