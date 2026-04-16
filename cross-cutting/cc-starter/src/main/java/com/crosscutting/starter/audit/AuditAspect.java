package com.crosscutting.starter.audit;

import com.crosscutting.starter.tenancy.TenantContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    private final BusinessAuditService businessAuditService;

    public AuditAspect(BusinessAuditService businessAuditService) {
        this.businessAuditService = businessAuditService;
    }

    @Around("@annotation(audited)")
    public Object auditMethod(ProceedingJoinPoint joinPoint, Audited audited) throws Throwable {
        // Capture "before" state from the first argument if available
        Object beforeState = null;
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            beforeState = args[0];
        }

        // Execute the method
        Object result = joinPoint.proceed();

        // Capture "after" state from the return value
        Object afterState = result;

        // Extract context
        UUID tenantId = TenantContext.get();
        UUID userId = extractUserId();
        UUID resourceId = extractResourceId(args);

        try {
            businessAuditService.record(
                    tenantId,
                    userId,
                    audited.action(),
                    audited.resource(),
                    resourceId,
                    beforeState,
                    afterState
            );
        } catch (Exception e) {
            log.warn("Failed to record audit for @Audited method: {}", joinPoint.getSignature(), e);
        }

        return result;
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

    private UUID extractResourceId(Object[] args) {
        // Look for a UUID argument (typically the resource ID parameter)
        for (Object arg : args) {
            if (arg instanceof UUID uuid) {
                return uuid;
            }
        }
        return null;
    }
}
