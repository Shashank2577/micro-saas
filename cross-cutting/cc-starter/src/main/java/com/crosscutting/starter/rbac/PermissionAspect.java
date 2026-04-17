package com.crosscutting.starter.rbac;

import com.crosscutting.starter.error.CcErrorCodes;
import com.crosscutting.starter.tenancy.TenantContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * AOP aspect that intercepts methods annotated with {@link RequirePermission}
 * and enforces RBAC permission checks.
 */
@Aspect
@Component
public class PermissionAspect {

    private static final Logger log = LoggerFactory.getLogger(PermissionAspect.class);

    private final RbacService rbacService;

    public PermissionAspect(RbacService rbacService) {
        this.rbacService = rbacService;
    }

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        UUID userId = extractUserId();
        UUID tenantId = TenantContext.require();

        String resource = requirePermission.resource();
        String action = requirePermission.action();

        if (!rbacService.hasPermission(userId, tenantId, resource, action)) {
            log.warn("Permission denied: user={} tenant={} resource={} action={}",
                    userId, tenantId, resource, action);
            throw CcErrorCodes.permissionDenied(
                    "User does not have permission: " + resource + ":" + action);
        }

        return joinPoint.proceed();
    }

    private UUID extractUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw CcErrorCodes.unauthorized("No authentication found");
        }

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            return UUID.fromString(jwt.getSubject());
        }

        // Fallback: try to parse the principal name as UUID
        String name = authentication.getName();
        try {
            return UUID.fromString(name);
        } catch (IllegalArgumentException e) {
            throw CcErrorCodes.unauthorized("Cannot determine user identity");
        }
    }
}
