package com.crosscutting.starter.auth;

import com.crosscutting.starter.error.GlobalExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserSyncService userSyncService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        AuthController controller = new AuthController(userSyncService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void me_returnsPrincipalFromSecurityContext() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID tenantId = UUID.randomUUID();

        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(userId.toString())
                .claim("email", "me@example.com")
                .claim("tenant_id", tenantId.toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        JwtAuthenticationToken jwtAuth = new JwtAuthenticationToken(
                jwt, List.of(new SimpleGrantedAuthority("ROLE_admin")));
        SecurityContextHolder.getContext().setAuthentication(jwtAuth);

        mockMvc.perform(get("/cc/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("me@example.com"))
                .andExpect(jsonPath("$.tenantId").value(tenantId.toString()))
                .andExpect(jsonPath("$.roles[0]").value("admin"));
    }

    @Test
    void me_returns401WhenNoAuthentication() throws Exception {
        // No authentication in security context — CcPrincipal.current() throws unauthorized
        mockMvc.perform(get("/cc/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getUser_returnsUserWhenFound() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("found@example.com");
        user.setDisplayName("Found User");
        user.setStatus("active");

        when(userSyncService.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/cc/auth/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.email").value("found@example.com"))
                .andExpect(jsonPath("$.displayName").value("Found User"));
    }

    @Test
    void getUser_returns404WhenNotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        when(userSyncService.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/cc/auth/user/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("RESOURCE_NOT_FOUND"));
    }
}
