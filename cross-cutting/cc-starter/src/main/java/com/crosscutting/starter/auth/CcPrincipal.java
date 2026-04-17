package com.crosscutting.starter.auth;

import com.crosscutting.starter.error.CcErrorCodes;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class CcPrincipal {

    private final UUID userId;
    private final String email;
    private final UUID tenantId;
    private final Set<String> roles;

    public CcPrincipal(UUID userId, String email, UUID tenantId, Set<String> roles) {
        this.userId = userId;
        this.email = email;
        this.tenantId = tenantId;
        this.roles = roles;
    }

    public static CcPrincipal from(Authentication auth) {
        if (!(auth instanceof JwtAuthenticationToken jwtAuth)) {
            throw CcErrorCodes.unauthorized("Authentication is not a JWT token");
        }

        Jwt jwt = jwtAuth.getToken();
        UUID userId = UUID.fromString(jwt.getSubject());
        String email = jwt.getClaimAsString("email");

        String tenantIdClaim = jwt.getClaimAsString("tenant_id");
        UUID tenantId = tenantIdClaim != null && !tenantIdClaim.isBlank()
                ? UUID.fromString(tenantIdClaim)
                : null;

        Set<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring(5))
                .collect(Collectors.toSet());

        return new CcPrincipal(userId, email, tenantId, roles);
    }

    public static CcPrincipal current() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw CcErrorCodes.unauthorized("No authentication in security context");
        }
        return from(auth);
    }
}
