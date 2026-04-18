package com.microsaas.apievolver.service;

import com.microsaas.apievolver.model.ApiSpec;
import com.microsaas.apievolver.repository.ApiSpecRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiSpecServiceTest {

    @Mock
    private ApiSpecRepository repository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private ApiSpecService service;

    @Test
    void testCreate() {
        UUID tenantId = UUID.randomUUID();
        ApiSpec spec = new ApiSpec();
        spec.setName("Test Spec");

        ApiSpec savedSpec = new ApiSpec();
        savedSpec.setId(UUID.randomUUID());
        savedSpec.setName("Test Spec");
        savedSpec.setTenantId(tenantId);

        when(repository.save(any(ApiSpec.class))).thenReturn(savedSpec);

        ApiSpec result = service.create(spec, tenantId);

        assertNotNull(result);
        assertEquals("Test Spec", result.getName());
        verify(repository).save(any(ApiSpec.class));
        verify(eventPublisher).publish(eq("apievolver.spec.uploaded"), eq(tenantId), anyString());
    }

    @Test
    void testFindById() {
        UUID tenantId = UUID.randomUUID();
        UUID specId = UUID.randomUUID();

        ApiSpec spec = new ApiSpec();
        spec.setId(specId);
        spec.setTenantId(tenantId);

        when(repository.findByIdAndTenantId(specId, tenantId)).thenReturn(Optional.of(spec));

        ApiSpec result = service.findById(specId, tenantId);

        assertNotNull(result);
        assertEquals(specId, result.getId());
    }
}
