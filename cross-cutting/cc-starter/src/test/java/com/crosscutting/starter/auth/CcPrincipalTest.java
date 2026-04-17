package com.crosscutting.starter.auth;

import com.crosscutting.starter.error.CcException;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CcPrincipalTest {

    @Test
    void from_extractsAllFieldsFromJwtAuth() {
        UUID userId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();
        String email = "user@example.com";

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(userId.toString())
                .claim("email", email)
                .claim("tenant_id", tenantId.toString())
                .claim("realm_access", Map.of("roles", List.of("admin", "user")))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt, List.of(
                new SimpleGrantedAuthority("ROLE_admin"),
                new SimpleGrantedAuthority("ROLE_user")
        ), userId.toString());

        CcPrincipal principal = CcPrincipal.from(auth);

        assertThat(principal.getUserId()).isEqualTo(userId);
        assertThat(principal.getEmail()).isEqualTo(email);
        assertThat(principal.getTenantId()).isEqualTo(tenantId);
        assertThat(principal.getRoles()).containsExactlyInAnyOrder("admin", "user");
    }

    @Test
    void from_handlesNullTenantId() {
        UUID userId = UUID.randomUUID();

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(userId.toString())
                .claim("email", "user@example.com")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt, List.of(
                new SimpleGrantedAuthority("ROLE_user")
        ), userId.toString());

        CcPrincipal principal = CcPrincipal.from(auth);

        assertThat(principal.getTenantId()).isNull();
        assertThat(principal.getRoles()).containsExactly("user");
    }

    @Test
    void from_throwsWhenNotJwtAuthentication() {
        assertThatThrownBy(() -> CcPrincipal.from(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("user", "pass")
        ))
                .isInstanceOf(CcException.class)
                .hasMessageContaining("not a JWT token");
    }

    @Test
    void from_filtersOnlyRolePrefixedAuthorities() {
        UUID userId = UUID.randomUUID();

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(userId.toString())
                .claim("email", "user@example.com")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt, List.of(
                new SimpleGrantedAuthority("ROLE_admin"),
                new SimpleGrantedAuthority("SCOPE_read")
        ), userId.toString());

        CcPrincipal principal = CcPrincipal.from(auth);

        assertThat(principal.getRoles()).containsExactly("admin");
    }
}
