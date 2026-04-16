package com.crosscutting.starter.auth;

import com.crosscutting.starter.tenancy.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class UserSyncFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(UserSyncFilter.class);

    private final UserSyncService userSyncService;

    public UserSyncFilter(UserSyncService userSyncService) {
        this.userSyncService = userSyncService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof JwtAuthenticationToken jwtAuth) {
                Jwt jwt = jwtAuth.getToken();
                UUID userId = UUID.fromString(jwt.getSubject());

                // Set userId in MDC for structured logging
                MDC.put("userId", userId.toString());

                userSyncService.syncUser(jwt);

                // If no tenant context was set by TenantFilter, fall back to default tenant
                if (TenantContext.get() == null) {
                    UUID defaultTenantId = userSyncService.getDefaultTenantIdForUser(userId);
                    if (defaultTenantId != null) {
                        TenantContext.set(defaultTenantId);
                        MDC.put("tenantId", defaultTenantId.toString());
                        log.debug("Auto-set tenant context to default tenant for user {}", userId);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to sync user from JWT: {}", e.getMessage());
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("userId");
        }
    }
}
