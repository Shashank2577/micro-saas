package com.microsaas.cacheoptimizer.cache;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cacheoptimizer.analytics.AnalyticsService;
import com.microsaas.cacheoptimizer.policy.CachePolicy;
import com.microsaas.cacheoptimizer.policy.CachePolicyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CacheServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private CachePolicyService policyService;

    @Mock
    private AnalyticsService analyticsService;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private CacheService service;

    @BeforeEach
    void setUp() {
        TenantContext.set(java.util.UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGet_Hit() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("00000000-0000-0000-0000-000000000000:test-ns:test-key")).thenReturn("test-value");
        when(policyService.getPolicyEntity("test-ns")).thenReturn(null);

        String result = service.get("test-ns", "test-key");

        assertEquals("test-value", result);
        verify(analyticsService, times(1)).recordHit(eq("00000000-0000-0000-0000-000000000000"), eq("test-ns"), eq(10L));
    }

    @Test
    void testGet_Miss() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("00000000-0000-0000-0000-000000000000:test-ns:test-key")).thenReturn(null);
        when(policyService.getPolicyEntity("test-ns")).thenReturn(null);

        String result = service.get("test-ns", "test-key");

        assertNull(result);
        verify(analyticsService, times(1)).recordMiss("00000000-0000-0000-0000-000000000000", "test-ns");
    }

    @Test
    void testPut() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        
        CachePolicy policy = new CachePolicy();
        policy.setTtlSeconds(3600L);
        when(policyService.getPolicyEntity("test-ns")).thenReturn(policy);

        service.put("test-ns", "test-key", "test-value");

        verify(valueOperations, times(1)).set("00000000-0000-0000-0000-000000000000:test-ns:test-key", "test-value", 3600L, TimeUnit.SECONDS);
    }
}
