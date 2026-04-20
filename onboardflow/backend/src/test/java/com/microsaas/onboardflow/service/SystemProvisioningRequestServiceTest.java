package com.microsaas.onboardflow.service;

import com.microsaas.onboardflow.dto.SystemProvisioningRequestRequest;
import com.microsaas.onboardflow.model.SystemProvisioningRequest;
import com.microsaas.onboardflow.repository.SystemProvisioningRequestRepository;
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
public class SystemProvisioningRequestServiceTest {

    @Mock
    private SystemProvisioningRequestRepository repository;

    @InjectMocks
    private SystemProvisioningRequestService service;

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
        SystemProvisioningRequestRequest request = new SystemProvisioningRequestRequest();
        request.setName("Test");

        SystemProvisioningRequest savedEntity = new SystemProvisioningRequest();
        savedEntity.setName("Test");

        when(repository.save(any(SystemProvisioningRequest.class))).thenReturn(savedEntity);

        SystemProvisioningRequest result = service.create(tenantId, request);
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
