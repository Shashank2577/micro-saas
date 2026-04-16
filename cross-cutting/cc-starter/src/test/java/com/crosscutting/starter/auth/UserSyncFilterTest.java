package com.crosscutting.starter.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSyncFilterTest {

    @Mock
    private UserSyncService userSyncService;

    @Mock
    private FilterChain filterChain;

    private UserSyncFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        filter = new UserSyncFilter(userSyncService);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilter_callsSyncUserWhenAuthenticated() throws ServletException, IOException {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(UUID.randomUUID().toString())
                .claim("email", "test@example.com")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(jwtAuth);

        filter.doFilterInternal(request, response, filterChain);

        verify(userSyncService).syncUser(jwt);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilter_skipsWhenNoAuthentication() throws ServletException, IOException {
        // SecurityContext has no authentication set

        filter.doFilterInternal(request, response, filterChain);

        verify(userSyncService, never()).syncUser(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilter_continuesChainEvenIfSyncFails() throws ServletException, IOException {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(UUID.randomUUID().toString())
                .claim("email", "test@example.com")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(jwt, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(jwtAuth);

        doThrow(new RuntimeException("DB connection failed")).when(userSyncService).syncUser(any());

        filter.doFilterInternal(request, response, filterChain);

        // Chain must still be called despite the exception
        verify(filterChain).doFilter(request, response);
    }
}
