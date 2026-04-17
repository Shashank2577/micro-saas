package com.crosscutting.starter.rbac;

import com.crosscutting.starter.error.CcException;
import com.crosscutting.starter.tenancy.TenantContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PermissionAspectTest {

    @Mock
    private RbacService rbacService;

    @InjectMocks
    private PermissionAspect permissionAspect;

    private final UUID userId = UUID.randomUUID();
    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Set up JWT authentication in security context
        Jwt jwt = new Jwt(
                "test-token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "RS256"),
                Map.of("sub", userId.toString())
        );
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(jwt, Collections.emptyList(), userId.toString());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Set tenant context
        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        TenantContext.clear();
    }

    @Test
    void checkPermission_allowed_proceedsWithMethod() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        RequirePermission annotation = mock(RequirePermission.class);
        when(annotation.resource()).thenReturn("orders");
        when(annotation.action()).thenReturn("read");
        when(rbacService.hasPermission(userId, tenantId, "orders", "read")).thenReturn(true);
        when(joinPoint.proceed()).thenReturn("result");

        Object result = permissionAspect.checkPermission(joinPoint, annotation);

        assertThat(result).isEqualTo("result");
        verify(joinPoint).proceed();
    }

    @Test
    void checkPermission_denied_throwsCcException() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        RequirePermission annotation = mock(RequirePermission.class);
        when(annotation.resource()).thenReturn("orders");
        when(annotation.action()).thenReturn("delete");
        when(rbacService.hasPermission(userId, tenantId, "orders", "delete")).thenReturn(false);

        assertThatThrownBy(() -> permissionAspect.checkPermission(joinPoint, annotation))
                .isInstanceOf(CcException.class)
                .hasFieldOrPropertyWithValue("errorCode", "PERMISSION_DENIED")
                .hasFieldOrPropertyWithValue("httpStatus", 403);

        verify(joinPoint, never()).proceed();
    }

    @Test
    void checkPermission_noAuthentication_throwsUnauthorized() {
        SecurityContextHolder.clearContext();

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        RequirePermission annotation = mock(RequirePermission.class);

        assertThatThrownBy(() -> permissionAspect.checkPermission(joinPoint, annotation))
                .isInstanceOf(CcException.class)
                .hasFieldOrPropertyWithValue("errorCode", "UNAUTHORIZED");
    }

    @Test
    void checkPermission_noTenant_throwsError() {
        TenantContext.clear();

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        RequirePermission annotation = mock(RequirePermission.class);

        assertThatThrownBy(() -> permissionAspect.checkPermission(joinPoint, annotation))
                .isInstanceOf(CcException.class)
                .hasFieldOrPropertyWithValue("errorCode", "TENANT_NOT_FOUND");
    }
}
