package com.crosscutting.starter.audit;

import com.crosscutting.starter.logging.CorrelationIdFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.UUID;

@Service
public class BusinessAuditService {

    private static final Logger log = LoggerFactory.getLogger(BusinessAuditService.class);

    private final BusinessAuditLogRepository repository;
    private final ObjectMapper objectMapper;

    public BusinessAuditService(BusinessAuditLogRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public void record(UUID tenantId, UUID userId, String action, String resourceType,
                       UUID resourceId, Object beforeState, Object afterState) {
        try {
            BusinessAuditLog auditLog = new BusinessAuditLog();
            auditLog.setTenantId(tenantId);
            auditLog.setUserId(userId);
            auditLog.setAction(action);
            auditLog.setResourceType(resourceType);
            auditLog.setResourceId(resourceId);
            auditLog.setBeforeState(convertToMap(beforeState));
            auditLog.setAfterState(convertToMap(afterState));

            // Correlation ID from MDC
            String correlationIdStr = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
            if (correlationIdStr != null) {
                try {
                    auditLog.setCorrelationId(UUID.fromString(correlationIdStr));
                } catch (IllegalArgumentException e) {
                    // non-UUID correlation ID, skip
                }
            }

            // IP address from current request
            auditLog.setIpAddress(extractClientIp());

            repository.save(auditLog);
        } catch (Exception e) {
            log.warn("Failed to record business audit log", e);
        }
    }

    public Page<BusinessAuditLog> findByTenant(UUID tenantId, Pageable pageable) {
        if (tenantId == null) {
            return repository.findAll(pageable);
        }
        return repository.findByTenantIdIncludingNull(tenantId, pageable);
    }

    public Page<BusinessAuditLog> findByResource(String resourceType, UUID resourceId, Pageable pageable) {
        return repository.findByResourceTypeAndResourceId(resourceType, resourceId, pageable);
    }

    public Page<BusinessAuditLog> findByUser(UUID userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> convertToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return objectMapper.convertValue(obj, Map.class);
    }

    private String extractClientIp() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String xForwardedFor = request.getHeader("X-Forwarded-For");
                if (xForwardedFor != null && !xForwardedFor.isBlank()) {
                    return xForwardedFor.split(",")[0].trim();
                }
                return request.getRemoteAddr();
            }
        } catch (Exception e) {
            // Not in a request context
        }
        return null;
    }
}
