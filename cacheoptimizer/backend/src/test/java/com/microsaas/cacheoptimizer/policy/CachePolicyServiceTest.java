package com.microsaas.cacheoptimizer.policy;

import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CachePolicyServiceTest {

    @Mock
    private CachePolicyRepository repository;

    @InjectMocks
    private CachePolicyService service;

    @BeforeEach
    void setUp() {
        TenantContext.set(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void createPolicy() {
        CachePolicyDto dto = new CachePolicyDto();
        dto.setAppName("test-app");
        dto.setNamespace("test-namespace");
        dto.setTtlSeconds(3600L);
        dto.setStrategy(CacheStrategy.CACHE_ASIDE);

        CachePolicy policy = new CachePolicy();
        policy.setId(UUID.randomUUID());
        policy.setTenantId("00000000-0000-0000-0000-000000000000");
        policy.setAppName("test-app");

        when(repository.save(any(CachePolicy.class))).thenReturn(policy);

        CachePolicyDto result = service.createPolicy(dto);

        assertNotNull(result);
        assertEquals("test-app", result.getAppName());
    }

    @Test
    void getPolicies() {
        CachePolicy policy = new CachePolicy();
        policy.setId(UUID.randomUUID());
        when(repository.findByTenantId("00000000-0000-0000-0000-000000000000")).thenReturn(List.of(policy));

        List<CachePolicyDto> result = service.getPolicies();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
