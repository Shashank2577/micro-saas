package com.crosscutting.starter.audit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuditController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SystemAuditService systemAuditService;

    @MockBean
    private BusinessAuditService businessAuditService;

    @MockBean
    private AuthEventRepository authEventRepository;

    @Test
    void getSystemAuditLogsReturnsPage() throws Exception {
        Page<SystemAuditLog> page = new PageImpl<>(List.of());
        when(systemAuditService.findByTenant(isNull(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/cc/audit/system"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getSystemAuditLogsFiltersByTenantId() throws Exception {
        UUID tenantId = UUID.randomUUID();
        SystemAuditLog log = new SystemAuditLog();
        log.setTenantId(tenantId);
        Page<SystemAuditLog> page = new PageImpl<>(List.of(log));
        when(systemAuditService.findByTenant(eq(tenantId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/cc/audit/system").param("tenantId", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getSystemAuditLogsFiltersByEventType() throws Exception {
        Page<SystemAuditLog> page = new PageImpl<>(List.of());
        when(systemAuditService.findByEventType(eq("AUTH"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/cc/audit/system").param("eventType", "AUTH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getSystemAuditLogsFiltersByUserId() throws Exception {
        UUID userId = UUID.randomUUID();
        Page<SystemAuditLog> page = new PageImpl<>(List.of());
        when(systemAuditService.findByUser(eq(userId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/cc/audit/system").param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getBusinessAuditLogsReturnsPage() throws Exception {
        Page<BusinessAuditLog> page = new PageImpl<>(List.of());
        when(businessAuditService.findByTenant(isNull(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/cc/audit/business"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getBusinessAuditLogsFiltersByTenantId() throws Exception {
        UUID tenantId = UUID.randomUUID();
        Page<BusinessAuditLog> page = new PageImpl<>(List.of());
        when(businessAuditService.findByTenant(eq(tenantId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/cc/audit/business").param("tenantId", tenantId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getAuthEventsReturnsPage() throws Exception {
        Page<AuthEvent> page = new PageImpl<>(List.of());
        when(authEventRepository.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/cc/audit/auth"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getAuthEventsFiltersByUserId() throws Exception {
        UUID userId = UUID.randomUUID();
        Page<AuthEvent> page = new PageImpl<>(List.of());
        when(authEventRepository.findByUserId(eq(userId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/cc/audit/auth").param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getResourceAuditReturnsSystemAndBusinessLogs() throws Exception {
        UUID resourceId = UUID.randomUUID();
        Page<SystemAuditLog> systemPage = new PageImpl<>(List.of());
        Page<BusinessAuditLog> businessPage = new PageImpl<>(List.of());

        when(systemAuditService.findByResource(eq("Order"), eq(resourceId.toString()), any(Pageable.class)))
                .thenReturn(systemPage);
        when(businessAuditService.findByResource(eq("Order"), eq(resourceId), any(Pageable.class)))
                .thenReturn(businessPage);

        mockMvc.perform(get("/cc/audit/resource/Order/" + resourceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.systemLogs").exists())
                .andExpect(jsonPath("$.businessLogs").exists());
    }

    @Test
    void getResourceAuditWithNonUuidIdSkipsBusinessLogs() throws Exception {
        Page<SystemAuditLog> systemPage = new PageImpl<>(List.of());

        when(systemAuditService.findByResource(eq("Order"), eq("not-a-uuid"), any(Pageable.class)))
                .thenReturn(systemPage);

        mockMvc.perform(get("/cc/audit/resource/Order/not-a-uuid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.systemLogs").exists())
                .andExpect(jsonPath("$.businessLogs").exists());
    }

    @Test
    void getUserAuditReturnsAllAuditTypes() throws Exception {
        UUID userId = UUID.randomUUID();
        Page<SystemAuditLog> systemPage = new PageImpl<>(List.of());
        Page<BusinessAuditLog> businessPage = new PageImpl<>(List.of());
        Page<AuthEvent> authPage = new PageImpl<>(List.of());

        when(systemAuditService.findByUser(eq(userId), any(Pageable.class))).thenReturn(systemPage);
        when(businessAuditService.findByUser(eq(userId), any(Pageable.class))).thenReturn(businessPage);
        when(authEventRepository.findByUserId(eq(userId), any(Pageable.class))).thenReturn(authPage);

        mockMvc.perform(get("/cc/audit/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.systemLogs").exists())
                .andExpect(jsonPath("$.businessLogs").exists())
                .andExpect(jsonPath("$.authEvents").exists());
    }
}
