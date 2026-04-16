package com.crosscutting.starter.tenancy;

import com.crosscutting.starter.CcProperties;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class TenantFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TenantFilter.class);

    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String TENANT_MDC_KEY = "tenantId";

    private final CcProperties properties;
    private final TenantRepository tenantRepository;
    private final TenantMembershipRepository tenantMembershipRepository;

    public TenantFilter(CcProperties properties, TenantRepository tenantRepository,
                        TenantMembershipRepository tenantMembershipRepository) {
        this.properties = properties;
        this.tenantRepository = tenantRepository;
        this.tenantMembershipRepository = tenantMembershipRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/cc/health") || path.startsWith("/actuator");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            UUID tenantId = resolveTenantId(request);
            if (tenantId != null) {
                // Validate tenant exists
                if (!tenantRepository.existsById(tenantId)) {
                    log.warn("Requested tenant {} does not exist", tenantId);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tenant not found");
                    return;
                }
                // Verify authenticated user is a member of this tenant (skip for tenant creation/onboard endpoints)
                if (!isTenantSetupEndpoint(request)) {
                    UUID userId = extractUserId();
                    if (userId != null && !tenantMembershipRepository.existsByUserIdAndTenantId(userId, tenantId)) {
                        log.warn("User {} is not a member of tenant {}", userId, tenantId);
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Not a member of this tenant");
                        return;
                    }
                }

                TenantContext.set(tenantId);
                MDC.put(TENANT_MDC_KEY, tenantId.toString());
                log.debug("Tenant context set: {}", tenantId);
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
            MDC.remove(TENANT_MDC_KEY);
        }
    }

    /**
     * Returns true for endpoints that should skip the membership check:
     * - POST /cc/tenants (tenant creation - user becomes admin after)
     * - POST /cc/tenants/{id}/onboard (tenant onboarding setup)
     */
    private boolean isTenantSetupEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (!"POST".equalsIgnoreCase(method)) {
            return false;
        }

        // POST /cc/tenants — create tenant
        if (path.equals("/cc/tenants")) {
            return true;
        }

        // POST /cc/tenants/{id}/onboard — onboard existing tenant
        return path.matches("/cc/tenants/[^/]+/onboard");
    }

    private UUID extractUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            try {
                return UUID.fromString(jwt.getSubject());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid user ID in JWT subject: {}", jwt.getSubject());
            }
        }
        return null;
    }

    private UUID resolveTenantId(HttpServletRequest request) {
        CcProperties.TenancyProperties tenancy = properties.getTenancy();

        // In single-tenant mode, always use the configured default tenant
        if ("single".equalsIgnoreCase(tenancy.getMode())) {
            return tenancy.getDefaultTenantId();
        }

        // 1. Try X-Tenant-ID header
        String headerValue = request.getHeader(TENANT_HEADER);
        if (headerValue != null && !headerValue.isBlank()) {
            try {
                return UUID.fromString(headerValue.trim());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid X-Tenant-ID header value: {}", headerValue);
            }
        }

        // 2. Try JWT claim "tenant_id"
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            String tenantClaim = jwt.getClaimAsString("tenant_id");
            if (tenantClaim != null && !tenantClaim.isBlank()) {
                try {
                    return UUID.fromString(tenantClaim.trim());
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid tenant_id JWT claim: {}", tenantClaim);
                }
            }
        }

        return null;
    }
}
