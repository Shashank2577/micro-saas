package com.crosscutting.starter.auth;

import com.crosscutting.starter.tenancy.TenantContext;
import org.slf4j.MDC;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Converts a JWT into a Spring Security authentication token.
 *
 * <p><strong>Keycloak role mapping note:</strong> This converter reads roles from the standard
 * {@code realm_access.roles} claim path. The Keycloak realm export (realm-export.json) includes
 * a custom protocol mapper named "realm roles" that maps roles to a {@code realm_roles} claim.
 * These are two DIFFERENT claim paths:</p>
 * <ul>
 *   <li>{@code realm_access.roles} — Keycloak's built-in default (what this converter reads)</li>
 *   <li>{@code realm_roles} — custom mapper in realm-export.json (not used by this converter)</li>
 * </ul>
 * <p>Both will be present in the token. This converter uses the standard path. If you need to
 * read from the custom mapper instead, modify {@link #extractAuthorities(Jwt)}.</p>
 */
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);

        String userId = jwt.getSubject();
        MDC.put("userId", userId);

        String tenantIdClaim = jwt.getClaimAsString("tenant_id");
        if (tenantIdClaim != null && !tenantIdClaim.isBlank()) {
            UUID tenantId = UUID.fromString(tenantIdClaim);
            TenantContext.set(tenantId);
            MDC.put("tenantId", tenantIdClaim);
        }

        JwtAuthenticationToken authToken = new JwtAuthenticationToken(jwt, authorities, userId);
        return authToken;
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        if (realmAccess == null) {
            return Collections.emptySet();
        }

        Object rolesObj = realmAccess.get("roles");
        if (!(rolesObj instanceof Collection<?>)) {
            return Collections.emptySet();
        }

        Collection<String> roles = (Collection<String>) rolesObj;
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
