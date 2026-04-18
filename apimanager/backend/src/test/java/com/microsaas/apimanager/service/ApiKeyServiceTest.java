package com.microsaas.apimanager.service;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.apimanager.model.ApiKey;
import com.microsaas.apimanager.repository.ApiKeyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
class ApiKeyServiceTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @InjectMocks
    private ApiKeyService apiKeyService;

    @BeforeEach
    void setUp() {
        TenantContext.set(java.util.UUID.randomUUID());
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGenerateKey() {
        ApiKey req = new ApiKey();
        req.setProjectId(UUID.randomUUID());

        when(apiKeyRepository.save(any())).thenReturn(req);

        String rawKey = apiKeyService.generateKey(req);
        assertNotNull(rawKey);
        assertTrue(rawKey.length() > 20);
        assertNotNull(req.getTenantId());
        assertNotNull(req.getKeyHash());
        assertEquals("ACTIVE", req.getStatus());
    }

    @Test
    void testRevokeKey() {
        UUID id = UUID.randomUUID();
        ApiKey k = new ApiKey();
        k.setStatus("ACTIVE");
        
        when(apiKeyRepository.findByIdAndTenantId(eq(id), anyString())).thenReturn(Optional.of(k));

        boolean res = apiKeyService.revokeKey(id);
        assertTrue(res);
        assertEquals("REVOKED", k.getStatus());
        verify(apiKeyRepository).save(k);
    }
}
