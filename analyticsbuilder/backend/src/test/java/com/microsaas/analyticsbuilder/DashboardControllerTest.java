package com.microsaas.analyticsbuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.analyticsbuilder.dto.CreateDashboardRequest;
import com.microsaas.analyticsbuilder.dto.DashboardDto;
import com.microsaas.analyticsbuilder.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.microsaas.analyticsbuilder.controller.DashboardController;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DashboardService dashboardService;

    @Test
    public void createDashboard_ShouldReturnCreatedDashboard() throws Exception {
        CreateDashboardRequest request = new CreateDashboardRequest();
        request.setName("Sales Dashboard");
        request.setDescription("Monthly sales metrics");

        DashboardDto mockDto = new DashboardDto();
        mockDto.setId(UUID.randomUUID());
        mockDto.setName("Sales Dashboard");
        mockDto.setDescription("Monthly sales metrics");

        when(dashboardService.createDashboard(any(CreateDashboardRequest.class), eq("test-tenant")))
                .thenReturn(mockDto);

        mockMvc.perform(post("/api/v1/dashboards")
                        .header("X-Tenant-ID", "test-tenant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sales Dashboard"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    public void getAllDashboards_ShouldReturnListOfDashboards() throws Exception {
        DashboardDto mockDto = new DashboardDto();
        mockDto.setId(UUID.randomUUID());
        mockDto.setName("Sales Dashboard");

        when(dashboardService.getAllDashboards("test-tenant"))
                .thenReturn(Collections.singletonList(mockDto));

        mockMvc.perform(get("/api/v1/dashboards")
                        .header("X-Tenant-ID", "test-tenant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Sales Dashboard"));
    }
}
