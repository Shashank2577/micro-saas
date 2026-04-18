package com.microsaas.retirementplus.controller;

import com.microsaas.retirementplus.dto.ProjectionResponseDto;
import com.microsaas.retirementplus.service.ProjectionService;
import com.crosscutting.starter.tenancy.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProjectionController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ProjectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectionService projectionService;

    private UUID tenantId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        tenantId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    void generateProjection() throws Exception {
        ProjectionResponseDto response = new ProjectionResponseDto();
        response.setLifeExpectancy(89);

        try (MockedStatic<TenantContext> mockedTenantContext = Mockito.mockStatic(TenantContext.class)) {
            mockedTenantContext.when(TenantContext::require).thenReturn(tenantId);
            when(projectionService.generateProjection(any(UUID.class), any(UUID.class))).thenReturn(response);

            mockMvc.perform(post("/api/projections/analyze/" + userId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.lifeExpectancy").value(89));
        }
    }
}
