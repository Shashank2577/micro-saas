package com.crosscutting.starter.rbac;

import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RbacController.class)
@AutoConfigureMockMvc(addFilters = false)
class RbacControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RbacService rbacService;

    @MockBean
    private RoleRepository roleRepository;

    private final UUID userId = UUID.randomUUID();
    private final UUID tenantId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        Jwt jwt = new Jwt(
                "test-token",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "RS256"),
                Map.of("sub", userId.toString())
        );
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(jwt, Collections.emptyList(), userId.toString());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        TenantContext.set(tenantId);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        TenantContext.clear();
    }

    @Test
    void getPermissions_returnsUserPermissions() throws Exception {
        when(rbacService.getPermissions(userId, tenantId))
                .thenReturn(Set.of("orders:read", "orders:write"));

        mockMvc.perform(get("/cc/rbac/permissions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void listRoles_returnsRolesForTenant() throws Exception {
        UUID roleId = UUID.randomUUID();
        Role role = new Role();
        role.setId(roleId);
        role.setName("admin");
        role.setDescription("Administrator");
        when(roleRepository.findByTenantIdOrTenantIdIsNull(tenantId))
                .thenReturn(List.of(role));

        mockMvc.perform(get("/cc/rbac/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("admin"))
                .andExpect(jsonPath("$[0].description").value("Administrator"));
    }

    @Test
    void createRole_createsAndReturnsRole() throws Exception {
        UUID createdRoleId = UUID.randomUUID();
        UUID permId1 = UUID.randomUUID();
        UUID permId2 = UUID.randomUUID();
        Role created = new Role();
        created.setId(createdRoleId);
        created.setName("editor");
        created.setDescription("Can edit");
        when(rbacService.createRole(tenantId, "editor", "Can edit", List.of(permId1, permId2)))
                .thenReturn(created);

        mockMvc.perform(post("/cc/rbac/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"editor","description":"Can edit","permissionIds":["%s","%s"]}
                                """.formatted(permId1, permId2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("editor"))
                .andExpect(jsonPath("$.id").value(createdRoleId.toString()));
    }

    @Test
    void assignRole_returnsAssignedStatus() throws Exception {
        UUID targetUserId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        mockMvc.perform(post("/cc/rbac/roles/" + roleId + "/assign/" + targetUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("assigned"));

        verify(rbacService).assignRole(targetUserId, roleId, tenantId);
    }

    @Test
    void revokeRole_returnsRevokedStatus() throws Exception {
        UUID targetUserId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();

        mockMvc.perform(delete("/cc/rbac/roles/" + roleId + "/revoke/" + targetUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("revoked"));

        verify(rbacService).removeRole(targetUserId, roleId, tenantId);
    }

    @Test
    void checkPermission_returnsAllowedTrue() throws Exception {
        when(rbacService.hasPermission(userId, tenantId, "orders", "read"))
                .thenReturn(true);

        mockMvc.perform(get("/cc/rbac/check")
                        .param("resource", "orders")
                        .param("action", "read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed").value(true));
    }

    @Test
    void checkPermission_returnsAllowedFalse() throws Exception {
        when(rbacService.hasPermission(userId, tenantId, "orders", "delete"))
                .thenReturn(false);

        mockMvc.perform(get("/cc/rbac/check")
                        .param("resource", "orders")
                        .param("action", "delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allowed").value(false));
    }
}
