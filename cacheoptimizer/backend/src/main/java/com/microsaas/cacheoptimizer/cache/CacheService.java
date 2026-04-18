package com.microsaas.cacheoptimizer.cache;

import com.crosscutting.starter.tenancy.TenantContext;
import com.microsaas.cacheoptimizer.analytics.AnalyticsService;
import com.microsaas.cacheoptimizer.policy.CachePolicy;
import com.microsaas.cacheoptimizer.policy.CachePolicyService;
import com.microsaas.cacheoptimizer.policy.CacheStrategy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Service
public class CacheService {

    private final StringRedisTemplate redisTemplate;
    private final CachePolicyService policyService;
    private final AnalyticsService analyticsService;
    private final RedisLockRegistry lockRegistry;

    private final Cache<String, String> localCache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public CacheService(StringRedisTemplate redisTemplate, CachePolicyService policyService, AnalyticsService analyticsService) {
        this.redisTemplate = redisTemplate;
        this.policyService = policyService;
        this.analyticsService = analyticsService;
        this.lockRegistry = new RedisLockRegistry(redisTemplate.getConnectionFactory(), "cache-lock");
    }

    public String get(String namespace, String key) {
        String tenantId = TenantContext.require().toString();
        String fullKey = buildKey(tenantId, namespace, key);
        CachePolicy policy = policyService.getPolicyEntity(namespace);

        // L0 Local Cache
        String localValue = localCache.getIfPresent(fullKey);
        if (localValue != null) {
            analyticsService.recordHit(tenantId, namespace, localValue.length());
            return localValue;
        }

        // L1 Redis Cache
        String redisValue = redisTemplate.opsForValue().get(fullKey);

        if (redisValue != null) {
            analyticsService.recordHit(tenantId, namespace, redisValue.length());
            
            String decompressed = redisValue;
            if (policy != null && Boolean.TRUE.equals(policy.getCompressionEnabled())) {
                decompressed = decompress(redisValue);
            }
            localCache.put(fullKey, decompressed);
            return decompressed;
        }

        // Cache Miss
        analyticsService.recordMiss(tenantId, namespace);

        // Thundering Herd Protection
        Lock lock = lockRegistry.obtain(fullKey);
        boolean acquired = false;
        try {
            acquired = lock.tryLock(5, TimeUnit.SECONDS);
            if (acquired) {
                // Double check if another thread populated the cache while we waited
                redisValue = redisTemplate.opsForValue().get(fullKey);
                if (redisValue != null) {
                    analyticsService.recordHit(tenantId, namespace, redisValue.length());
                    String decompressed = redisValue;
                    if (policy != null && Boolean.TRUE.equals(policy.getCompressionEnabled())) {
                        decompressed = decompress(redisValue);
                    }
                    localCache.put(fullKey, decompressed);
                    return decompressed;
                }

                // Stale-While-Revalidate and Database Fetch logic placeholder
                // (Would normally fetch from origin here and update cache)
                return null;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (acquired) {
                lock.unlock();
            }
        }

        return null;
    }

    public void put(String namespace, String key, String value) {
        String tenantId = TenantContext.require().toString();
        String fullKey = buildKey(tenantId, namespace, key);
        CachePolicy policy = policyService.getPolicyEntity(namespace);

        localCache.put(fullKey, value);

        String finalValue = value;
        if (policy != null && Boolean.TRUE.equals(policy.getCompressionEnabled())) {
            finalValue = compress(value);
        }

        long ttl = (policy != null && policy.getTtlSeconds() != null) ? policy.getTtlSeconds() : 3600;
        redisTemplate.opsForValue().set(fullKey, finalValue, ttl, TimeUnit.SECONDS);

        if (policy != null && policy.getStrategy() == CacheStrategy.WRITE_THROUGH) {
            // Write-through logic placeholder: Write to DB synchronously
        } else if (policy != null && policy.getStrategy() == CacheStrategy.WRITE_BEHIND) {
            // Write-behind logic placeholder: Queue for async DB write
        }
    }

    public void invalidate(String namespace, String key) {
        String tenantId = TenantContext.require().toString();
        String fullKey = buildKey(tenantId, namespace, key);
        localCache.invalidate(fullKey);
        redisTemplate.delete(fullKey);
    }

    public void warm(String namespace, List<String> keys) {
        for (String key : keys) {
            put(namespace, key, "WARMED_VALUE"); // Simple placeholder
        }
    }

    private String buildKey(String tenantId, String namespace, String key) {
        return tenantId + ":" + namespace + ":" + key;
    }

    private String compress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(str.getBytes());
            gzip.close();
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            return str;
        }
    }

    private String decompress(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(str)));
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toString();
        } catch (IOException e) {
            return str;
        }
    }
}
