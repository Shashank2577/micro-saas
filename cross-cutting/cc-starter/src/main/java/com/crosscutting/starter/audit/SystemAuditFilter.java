package com.crosscutting.starter.audit;

import com.crosscutting.starter.CcProperties;
import com.crosscutting.starter.logging.CorrelationIdFilter;
import com.crosscutting.starter.tenancy.TenantContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class SystemAuditFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(SystemAuditFilter.class);

    private final SystemAuditLogRepository repository;
    private final RequestBodySanitizer sanitizer;
    private final CcProperties properties;
    private final ObjectMapper objectMapper;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public SystemAuditFilter(SystemAuditLogRepository repository,
                             RequestBodySanitizer sanitizer,
                             CcProperties properties,
                             ObjectMapper objectMapper) {
        this.repository = repository;
        this.sanitizer = sanitizer;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        List<String> excludePaths = properties.getAudit().getExcludePaths();
        return excludePaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            try {
                long durationMs = System.currentTimeMillis() - startTime;
                recordAuditLog(wrappedRequest, wrappedResponse, durationMs);
            } catch (Exception e) {
                log.warn("Failed to record system audit log", e);
            }
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void recordAuditLog(ContentCachingRequestWrapper request,
                                ContentCachingResponseWrapper response,
                                long durationMs) {
        SystemAuditLog auditLog = new SystemAuditLog();

        // Tenant ID from context
        UUID tenantId = TenantContext.get();
        auditLog.setTenantId(tenantId);

        // User ID from security context
        UUID userId = extractUserId();
        auditLog.setUserId(userId);

        // Request info
        String path = request.getRequestURI();
        auditLog.setRequestMethod(request.getMethod());
        auditLog.setRequestPath(path);

        // Event type classification
        auditLog.setEventType(classifyEventType(path));
        auditLog.setAction(request.getMethod() + " " + path);

        // Resource type/id extraction from path
        extractResource(path, auditLog);

        // Request body (sanitized)
        if (properties.getAudit().isLogRequestBody()) {
            String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
            if (!body.isBlank()) {
                String sanitized = sanitizer.sanitize(body);
                auditLog.setRequestBody(parseJsonToMap(sanitized));
            }
        }

        // Response
        auditLog.setResponseStatus(response.getStatus());

        // Client info
        auditLog.setIpAddress(extractClientIp(request));
        auditLog.setUserAgent(request.getHeader("User-Agent"));

        // Correlation ID from MDC
        String correlationIdStr = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        if (correlationIdStr != null) {
            try {
                auditLog.setCorrelationId(UUID.fromString(correlationIdStr));
            } catch (IllegalArgumentException e) {
                // non-UUID correlation ID, skip
            }
        }

        auditLog.setDurationMs((int) durationMs);

        repository.save(auditLog);
    }

    private String classifyEventType(String path) {
        if (path.startsWith("/cc/auth")) {
            return "AUTH";
        } else if (path.startsWith("/cc/rbac")) {
            return "RBAC";
        } else {
            return "API";
        }
    }

    private void extractResource(String path, SystemAuditLog auditLog) {
        // Extract resource type and ID from REST-style paths like /cc/resource-type/resource-id
        String[] segments = path.split("/");
        if (segments.length >= 3) {
            auditLog.setResourceType(segments[2]);
            if (segments.length >= 4) {
                auditLog.setResourceId(segments[3]);
            }
        }
    }

    private UUID extractUserId() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
                return UUID.fromString(jwt.getSubject());
            }
        } catch (Exception e) {
            // No authenticated user
        }
        return null;
    }

    private String extractClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private Map<String, Object> parseJsonToMap(String json) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return Map.of("_raw", json);
        }
    }
}
