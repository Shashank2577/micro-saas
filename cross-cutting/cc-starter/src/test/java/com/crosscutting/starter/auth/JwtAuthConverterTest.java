package com.crosscutting.starter.auth;

import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAuthConverterTest {

    private JwtAuthConverter converter;

    @BeforeEach
    void setUp() {
        converter = new JwtAuthConverter();
        MDC.clear();
        TenantContext.clear();
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
        TenantContext.clear();
    }

    @Test
    void convert_extractsRolesFromRealmAccess() {
        String userId = UUID.randomUUID().toString();
        Jwt jwt = buildJwt(userId, "test@example.com",
                UUID.randomUUID().toString(),
                List.of("admin", "user"));

        AbstractAuthenticationToken token = converter.convert(jwt);

        assertThat(token).isNotNull();
        Set<String> authorities = token.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        assertThat(authorities).containsExactlyInAnyOrder("ROLE_admin", "ROLE_user");
    }

    @Test
    void convert_setsUserIdAsName() {
        String userId = UUID.randomUUID().toString();
        Jwt jwt = buildJwt(userId, "test@example.com", null, List.of("user"));

        AbstractAuthenticationToken token = converter.convert(jwt);

        assertThat(token).isNotNull();
        assertThat(token.getName()).isEqualTo(userId);
    }

    @Test
    void convert_setsTenantContext() {
        String tenantId = UUID.randomUUID().toString();
        Jwt jwt = buildJwt(UUID.randomUUID().toString(), "test@example.com",
                tenantId, List.of());

        converter.convert(jwt);

        assertThat(TenantContext.get()).isEqualTo(UUID.fromString(tenantId));
    }

    @Test
    void convert_putsUserIdInMdc() {
        String userId = UUID.randomUUID().toString();
        Jwt jwt = buildJwt(userId, "test@example.com", null, List.of());

        converter.convert(jwt);

        assertThat(MDC.get("userId")).isEqualTo(userId);
    }

    @Test
    void convert_handlesNoRealmAccess() {
        String userId = UUID.randomUUID().toString();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(userId)
                .claim("email", "test@example.com")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        AbstractAuthenticationToken token = converter.convert(jwt);

        assertThat(token).isNotNull();
        assertThat(token.getAuthorities()).isEmpty();
    }

    @Test
    void convert_handlesNullTenantId() {
        Jwt jwt = buildJwt(UUID.randomUUID().toString(), "test@example.com",
                null, List.of("user"));

        converter.convert(jwt);

        assertThat(TenantContext.get()).isNull();
    }

    private Jwt buildJwt(String subject, String email, String tenantId, List<String> roles) {
        var builder = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(subject)
                .claim("email", email)
                .claim("realm_access", Map.of("roles", roles))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600));

        if (tenantId != null) {
            builder.claim("tenant_id", tenantId);
        }

        return builder.build();
    }
}
