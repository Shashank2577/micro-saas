package com.crosscutting.starter.security;

import com.crosscutting.starter.CcProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

public class RateLimitFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    private static final String RATE_LIMIT_PREFIX = "ratelimit:";
    private static final Duration WINDOW = Duration.ofMinutes(1);

    private final StringRedisTemplate redisTemplate;
    private final int defaultRateLimit;

    public RateLimitFilter(StringRedisTemplate redisTemplate, CcProperties ccProperties) {
        this.redisTemplate = redisTemplate;
        this.defaultRateLimit = ccProperties.getSecurity().getDefaultRateLimit();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String clientKey = resolveClientKey(request);
        String redisKey = RATE_LIMIT_PREFIX + clientKey;

        Long currentCount = redisTemplate.opsForValue().increment(redisKey);
        if (currentCount == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (currentCount == 1L) {
            redisTemplate.expire(redisKey, WINDOW);
        }

        long remaining = Math.max(0, defaultRateLimit - currentCount);
        long resetSeconds = WINDOW.getSeconds();

        response.setHeader("X-RateLimit-Limit", String.valueOf(defaultRateLimit));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(remaining));
        response.setHeader("X-RateLimit-Reset", String.valueOf(resetSeconds));

        if (currentCount > defaultRateLimit) {
            log.warn("Rate limit exceeded for client: {}", clientKey);
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"RATE_LIMITED\",\"message\":\"Too many requests\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String resolveClientKey(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
