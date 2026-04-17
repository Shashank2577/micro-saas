package com.crosscutting.starter.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import jakarta.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CorrelationIdFilterTest {

    private CorrelationIdFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new CorrelationIdFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
    }

    @Test
    void generatesCorrelationId_whenHeaderMissing() throws ServletException, IOException {
        filter.doFilter(request, response, filterChain);

        String correlationId = response.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER);
        assertNotNull(correlationId, "Correlation ID should be generated when header is missing");
        assertFalse(correlationId.isBlank(), "Generated correlation ID should not be blank");
    }

    @Test
    void usesExistingCorrelationId_whenHeaderPresent() throws ServletException, IOException {
        String existingId = "test-correlation-id-123";
        request.addHeader(CorrelationIdFilter.CORRELATION_ID_HEADER, existingId);

        filter.doFilter(request, response, filterChain);

        String correlationId = response.getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER);
        assertEquals(existingId, correlationId,
                "Filter should use the existing correlation ID from the request header");
    }

    @Test
    void mdcIsCleanedUp_afterFilter() throws ServletException, IOException {
        request.addHeader(CorrelationIdFilter.CORRELATION_ID_HEADER, "test-id");

        filter.doFilter(request, response, filterChain);

        assertNull(MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY),
                "correlationId should be removed from MDC after filter completes");
        assertNull(MDC.get(CorrelationIdFilter.TENANT_ID_MDC_KEY),
                "tenantId should be removed from MDC after filter completes");
        assertNull(MDC.get(CorrelationIdFilter.USER_ID_MDC_KEY),
                "userId should be removed from MDC after filter completes");
    }
}
