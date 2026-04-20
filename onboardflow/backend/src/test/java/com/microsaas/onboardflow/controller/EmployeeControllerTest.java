package com.microsaas.onboardflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsaas.onboardflow.dto.EmployeeRequest;
import com.microsaas.onboardflow.model.Employee;
import com.microsaas.onboardflow.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService service;

    @Test
    @WithMockUser
    public void testGetAll() throws Exception {
        UUID tenantId = UUID.randomUUID();
        when(service.findAll(tenantId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/onboardflow/employees")
                .header("X-Tenant-ID", tenantId.toString()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser
    public void testCreate() throws Exception {
        UUID tenantId = UUID.randomUUID();
        EmployeeRequest request = new EmployeeRequest();
        request.setName("Test");

        Employee response = new Employee();
        response.setName("Test");

        when(service.create(eq(tenantId), any(EmployeeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/onboardflow/employees")
                .header("X-Tenant-ID", tenantId.toString()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"));
    }
}
