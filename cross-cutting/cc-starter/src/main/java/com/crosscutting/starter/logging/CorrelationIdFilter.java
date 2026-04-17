package com.crosscutting.starter.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(CorrelationIdFilter.class);

    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String CORRELATION_ID_MDC_KEY = "correlationId";
    public static final String TENANT_ID_MDC_KEY = "tenantId";
    public static final String USER_ID_MDC_KEY = "userId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String correlationId = request.getHeader(CORRELATION_ID_HEADER);
            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }

            MDC.put(CORRELATION_ID_MDC_KEY, correlationId);

            // Preserve tenantId and userId if already set by other modules
            String tenantId = MDC.get(TENANT_ID_MDC_KEY);
            if (tenantId != null) {
                MDC.put(TENANT_ID_MDC_KEY, tenantId);
            }
            String userId = MDC.get(USER_ID_MDC_KEY);
            if (userId != null) {
                MDC.put(USER_ID_MDC_KEY, userId);
            }

            response.setHeader(CORRELATION_ID_HEADER, correlationId);

            log.debug("Correlation ID set: {}", correlationId);

            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(CORRELATION_ID_MDC_KEY);
            MDC.remove(TENANT_ID_MDC_KEY);
            MDC.remove(USER_ID_MDC_KEY);
        }
    }
}
