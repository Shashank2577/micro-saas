package com.crosscutting.starter.security;

import com.crosscutting.starter.CcProperties;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateLimitFilterTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private FilterChain filterChain;

    private RateLimitFilter rateLimitFilter;

    @BeforeEach
    void setUp() {
        CcProperties ccProperties = new CcProperties();
        ccProperties.getSecurity().setDefaultRateLimit(10);
        rateLimitFilter = new RateLimitFilter(redisTemplate, ccProperties);
    }

    @Test
    void allowsRequestUnderLimit() throws Exception {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("ratelimit:127.0.0.1")).thenReturn(5L);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        rateLimitFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(response.getHeader("X-RateLimit-Limit")).isEqualTo("10");
        assertThat(response.getHeader("X-RateLimit-Remaining")).isEqualTo("5");
        assertThat(response.getHeader("X-RateLimit-Reset")).isEqualTo("60");
    }

    @Test
    void blocksRequestOverLimit() throws Exception {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("ratelimit:127.0.0.1")).thenReturn(11L);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        rateLimitFilter.doFilterInternal(request, response, filterChain);

        assertThat(response.getStatus()).isEqualTo(429);
        assertThat(response.getContentAsString()).contains("RATE_LIMITED");
        assertThat(response.getHeader("X-RateLimit-Remaining")).isEqualTo("0");
    }

    @Test
    void setsExpireOnFirstRequest() throws Exception {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("ratelimit:127.0.0.1")).thenReturn(1L);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        rateLimitFilter.doFilterInternal(request, response, filterChain);

        verify(redisTemplate).expire(eq("ratelimit:127.0.0.1"), eq(Duration.ofMinutes(1)));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void returnsCorrectHeaders() throws Exception {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment("ratelimit:127.0.0.1")).thenReturn(3L);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");
        MockHttpServletResponse response = new MockHttpServletResponse();

        rateLimitFilter.doFilterInternal(request, response, filterChain);

        assertThat(response.getHeader("X-RateLimit-Limit")).isEqualTo("10");
        assertThat(response.getHeader("X-RateLimit-Remaining")).isEqualTo("7");
        assertThat(response.getHeader("X-RateLimit-Reset")).isEqualTo("60");
    }
}
