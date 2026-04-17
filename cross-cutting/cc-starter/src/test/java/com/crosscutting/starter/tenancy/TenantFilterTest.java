package com.crosscutting.starter.tenancy;

import com.crosscutting.starter.CcProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TenantFilterTest {

    private CcProperties properties;
    private TenantRepository tenantRepository;
    private TenantMembershipRepository tenantMembershipRepository;
    private TenantFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        properties = new CcProperties();
        tenantRepository = mock(TenantRepository.class);
        tenantMembershipRepository = mock(TenantMembershipRepository.class);
        when(tenantRepository.existsById(any(UUID.class))).thenReturn(true);
        when(tenantMembershipRepository.existsByUserIdAndTenantId(any(UUID.class), any(UUID.class))).thenReturn(true);
        filter = new TenantFilter(properties, tenantRepository, tenantMembershipRepository);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
    }

    @AfterEach
    void cleanup() {
        TenantContext.clear();
        MDC.clear();
        SecurityContextHolder.clearContext();
    }

    private void setUpJwtAuth(UUID userId) {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(userId.toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));
    }

    @Test
    void extractsTenantFromHeader() throws ServletException, IOException {
        UUID tenantId = UUID.randomUUID();
        request.addHeader("X-Tenant-ID", tenantId.toString());
        request.setRequestURI("/cc/tenants");

        filter.doFilter(request, response, (req, res) -> {
            assertEquals(tenantId, TenantContext.get());
            assertEquals(tenantId.toString(), MDC.get("tenantId"));
        });

        // Context should be cleared after filter
        assertNull(TenantContext.get());
        assertNull(MDC.get("tenantId"));
    }

    @Test
    void skipsHealthPath() throws ServletException, IOException {
        request.setRequestURI("/cc/health");
        request.addHeader("X-Tenant-ID", UUID.randomUUID().toString());

        filter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void skipsActuatorPaths() throws ServletException, IOException {
        request.setRequestURI("/actuator/health");
        request.addHeader("X-Tenant-ID", UUID.randomUUID().toString());

        filter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void singleModeUsesDefaultTenantId() throws ServletException, IOException {
        UUID defaultId = UUID.randomUUID();
        properties.getTenancy().setMode("single");
        properties.getTenancy().setDefaultTenantId(defaultId);
        request.setRequestURI("/cc/tenants");

        filter.doFilter(request, response, (req, res) -> {
            assertEquals(defaultId, TenantContext.get());
        });
    }

    @Test
    void clearsContextOnException() throws ServletException, IOException {
        UUID tenantId = UUID.randomUUID();
        request.addHeader("X-Tenant-ID", tenantId.toString());
        request.setRequestURI("/cc/tenants");

        try {
            filter.doFilter(request, response, (req, res) -> {
                throw new ServletException("test error");
            });
        } catch (ServletException ignored) {
            // expected
        }

        assertNull(TenantContext.get());
        assertNull(MDC.get("tenantId"));
    }

    @Test
    void handlesNoTenantHeader() throws ServletException, IOException {
        request.setRequestURI("/cc/tenants");

        filter.doFilter(request, response, (req, res) -> {
            assertNull(TenantContext.get());
        });
    }

    @Test
    void rejectsTenantWhenUserNotMember() throws ServletException, IOException {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        setUpJwtAuth(userId);
        request.addHeader("X-Tenant-ID", tenantId.toString());
        request.setRequestURI("/api/data");

        when(tenantMembershipRepository.existsByUserIdAndTenantId(eq(userId), eq(tenantId))).thenReturn(false);

        filter.doFilter(request, response, filterChain);

        assertEquals(403, response.getStatus());
    }

    @Test
    void allowsTenantWhenUserIsMember() throws ServletException, IOException {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        setUpJwtAuth(userId);
        request.addHeader("X-Tenant-ID", tenantId.toString());
        request.setRequestURI("/api/data");

        when(tenantMembershipRepository.existsByUserIdAndTenantId(eq(userId), eq(tenantId))).thenReturn(true);

        filter.doFilter(request, response, (req, res) -> {
            assertEquals(tenantId, TenantContext.get());
        });
    }

    @Test
    void skipsMembershipCheckForTenantCreation() throws ServletException, IOException {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        setUpJwtAuth(userId);
        request.addHeader("X-Tenant-ID", tenantId.toString());
        request.setRequestURI("/cc/tenants");
        request.setMethod("POST");

        // Membership returns false, but should be skipped for POST /cc/tenants
        when(tenantMembershipRepository.existsByUserIdAndTenantId(eq(userId), eq(tenantId))).thenReturn(false);

        filter.doFilter(request, response, (req, res) -> {
            assertEquals(tenantId, TenantContext.get());
        });
    }

    @Test
    void skipsMembershipCheckForOnboardEndpoint() throws ServletException, IOException {
        UUID tenantId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        setUpJwtAuth(userId);
        request.addHeader("X-Tenant-ID", tenantId.toString());
        request.setRequestURI("/cc/tenants/" + tenantId + "/onboard");
        request.setMethod("POST");

        // Membership returns false, but should be skipped for POST /cc/tenants/{id}/onboard
        when(tenantMembershipRepository.existsByUserIdAndTenantId(eq(userId), eq(tenantId))).thenReturn(false);

        filter.doFilter(request, response, (req, res) -> {
            assertEquals(tenantId, TenantContext.get());
        });
    }
}
