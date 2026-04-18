package com.tenantmanager.controller;

import com.crosscutting.starter.tenancy.TenantContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenantmanager.domain.CustomerTenant;
import com.tenantmanager.dto.CreateTenantRequest;
import com.tenantmanager.service.TenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TenantController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class TenantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TenantService tenantService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        TenantContext.set(UUID.randomUUID());
    }

    @Test
    void testGetTenants() throws Exception {
        CustomerTenant tenant = new CustomerTenant();
        tenant.setId(UUID.randomUUID());
        tenant.setName("Acme");

        when(tenantService.getTenants()).thenReturn(List.of(tenant));

        mockMvc.perform(get("/api/tenants")
                .header("X-Tenant-ID", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Acme"));
    }

    @Test
    void testCreateTenant() throws Exception {
        CreateTenantRequest request = new CreateTenantRequest();
        request.setName("New Corp");

        CustomerTenant tenant = new CustomerTenant();
        tenant.setId(UUID.randomUUID());
        tenant.setName("New Corp");

        when(tenantService.createTenant(any(CreateTenantRequest.class))).thenReturn(tenant);

        mockMvc.perform(post("/api/tenants")
                .header("X-Tenant-ID", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Corp"));
    }
}
