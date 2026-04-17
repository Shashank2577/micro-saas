package com.crosscutting.starter.tenancy;

import com.crosscutting.starter.error.CcErrorCodes;
import com.crosscutting.starter.error.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TenantController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class TenantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TenantService tenantService;

    @MockBean
    private TenantOnboardingService tenantOnboardingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Tenant sampleTenant;
    private UUID tenantId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        sampleTenant = new Tenant();
        sampleTenant.setName("Acme");
        sampleTenant.setSlug("acme");
        try {
            var idField = Tenant.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(sampleTenant, tenantId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getTenantReturnsOk() throws Exception {
        when(tenantService.getTenant(tenantId)).thenReturn(sampleTenant);

        mockMvc.perform(get("/cc/tenants/{id}", tenantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Acme"))
                .andExpect(jsonPath("$.slug").value("acme"));
    }

    @Test
    void getTenantReturns404WhenNotFound() throws Exception {
        UUID missingId = UUID.randomUUID();
        when(tenantService.getTenant(missingId))
                .thenThrow(CcErrorCodes.tenantNotFound("Tenant not found: " + missingId));

        mockMvc.perform(get("/cc/tenants/{id}", missingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("TENANT_NOT_FOUND"));
    }

    @Test
    void getTenantBySlugReturnsOk() throws Exception {
        when(tenantService.getTenantBySlug("acme")).thenReturn(sampleTenant);

        mockMvc.perform(get("/cc/tenants/slug/{slug}", "acme"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Acme"))
                .andExpect(jsonPath("$.slug").value("acme"));
    }

    @Test
    void getTenantBySlugReturns404WhenNotFound() throws Exception {
        when(tenantService.getTenantBySlug("missing"))
                .thenThrow(CcErrorCodes.tenantNotFound("Tenant not found with slug: missing"));

        mockMvc.perform(get("/cc/tenants/slug/{slug}", "missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("TENANT_NOT_FOUND"));
    }

    @Test
    void createTenantReturns201() throws Exception {
        UUID currentUserId = UUID.randomUUID();
        // Set up JWT auth context so extractUserId() works
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(currentUserId.toString())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));

        when(tenantOnboardingService.onboardTenant("Acme", "acme", currentUserId)).thenReturn(sampleTenant);

        String body = objectMapper.writeValueAsString(Map.of("name", "Acme", "slug", "acme"));

        mockMvc.perform(post("/cc/tenants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Acme"))
                .andExpect(jsonPath("$.slug").value("acme"));

        SecurityContextHolder.clearContext();
    }

    @Test
    void updateTenantReturnsOk() throws Exception {
        Tenant updated = new Tenant();
        updated.setName("New Name");
        updated.setSlug("acme");
        Map<String, Object> settings = Map.of("theme", "dark");
        updated.setSettings(settings);

        when(tenantService.updateTenant(eq(tenantId), eq("New Name"), any())).thenReturn(updated);

        String body = objectMapper.writeValueAsString(Map.of("name", "New Name", "settings", settings));

        mockMvc.perform(put("/cc/tenants/{id}", tenantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    void onboardTenantReturnsOk() throws Exception {
        UUID adminUserId = UUID.randomUUID();
        when(tenantOnboardingService.onboardExistingTenant(tenantId, adminUserId)).thenReturn(sampleTenant);

        String body = objectMapper.writeValueAsString(
                Map.of("name", "Acme", "slug", "acme", "adminUserId", adminUserId.toString()));

        mockMvc.perform(post("/cc/tenants/{id}/onboard", tenantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Acme"));
    }
}
